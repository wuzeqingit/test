package class_scheduling2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

/**
 * Lots of comments in the source that are omitted here!
 */
public class GeneticAlgorithm {
	private int populationSize;
	private double mutationRate; // 基因突变的概率
	private double crossoverRate; // 染色体交叉的概率
	private int elitismCount; // 精英主义：将最优解保留到下一代

	/**
	 * 在交叉中锦选择标赛的种群的大小。
	 */
	protected int tournamentSize;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
			int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	/**
	 * 初始化种群
	 * 
	 */
	public Population initPopulation(Timetable timetable) {
		// Initialize population
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	// Create fitness hashtable
	private Map<Individual, Double> fitnessHash = Collections.synchronizedMap(new LinkedHashMap<Individual, Double>() {
		@Override
		protected boolean removeEldestEntry(Entry<Individual, Double> eldest) {
			// Store a maximum of 1000 fitness values
			return this.size() > 1000;
		}
	});

	/**
	 * Calculate individual's fitness value
	 *
	 * @param individual
	 * @param timetable
	 * @return fitness
	 */
	public double calcFitness(Individual individual, Timetable timetable) {
		Double storedFitness = this.fitnessHash.get(individual);
		if (storedFitness != null) {
			return storedFitness;
		}
		// Create new timetable object for thread
		Timetable threadTimetable = new Timetable(timetable);
		threadTimetable.createClasses(individual);
		// Calculate fitness
		int clashes = threadTimetable.calcClashes();
		double fitness = 1 / (double) (clashes + 1);
		individual.setFitness(fitness);
		// Store fitness in hashtable
		this.fitnessHash.put(individual, fitness);
		return fitness;
	}

	/**
	 * 计算种群中每条路线的适应度，及其种群的适应度
	 */
	public void evalPopulation(Population population, Timetable timetable) {

		IntStream.range(0, population.size()).parallel()
				.forEach(i -> this.calcFitness(population.getIndividual(i), timetable));

		double populationFitness = 0;
		// Loop over population evaluating individuals and summing population
		// fitness
		for (Individual individual : population.getIndividuals()) {
			populationFitness += this.calcFitness(individual, timetable);
		}
		population.setPopulationFitness(populationFitness);
	}

	/**
	 * 设置终止条件:当种群迭代达到了最大限制，则终止
	 * 
	 * @param generationsCount
	 * @param maxGenerations
	 * @return
	 */
	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	public boolean isTerminationConditionMet(Population population) {
		return population.getFittest(0).getFitness() == 1.0;
	}

	/**
	 * 锦标赛选择法
	 * 
	 * @param population
	 * @return
	 */
	public Individual selectParent(Population population) {
		// Create tournament
		Population tournament = new Population(this.tournamentSize);
		// 打乱顺序
		population.shuffle();
		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}
		// Return the best
		return tournament.getFittest(0);
	}

	// 均匀交叉法
	public Population crossoverPopulation(Population population) {
		// 创建一个新的种群
		Population newPopulation = new Population(population.size());

		// Get best fitness
		double bestFitness = population.getFittest(0).getFitness();

		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual parent1 = population.getFittest(populationIndex);

			// 重新计算交叉概率
			double adaptiveCrossoverRate = this.crossoverRate;
			if (parent1.getFitness() > population.getAvgFitness()) {
				double fitnessDelta1 = bestFitness - parent1.getFitness();
				double fitnessDelta2 = bestFitness - population.getAvgFitness();
				adaptiveCrossoverRate = (fitnessDelta1 / fitnessDelta2) * this.crossoverRate;
			}

			if (adaptiveCrossoverRate > Math.random() && populationIndex > this.elitismCount) {
				// 子代个体
				Individual offspring = new Individual(parent1.getChromosomeLength());

				Individual parent2 = selectParent(population);

				// 产生子代
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					// 均匀选择
					if (0.5 > Math.random()) {
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
					} else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}

				newPopulation.setIndividual(populationIndex, offspring);
			} else {
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}
		return newPopulation;
	}

	/**
	 * 基因变异:互换变异
	 */
	/**
	 * Apply mutation to population
	 *
	 * @param population
	 * @param timetable
	 * @return The mutated population
	 */
	public Population mutatePopulation(Population population, Timetable timetable) {
		// Initialize new population
		Population newPopulation = new Population(this.populationSize);
		// Get best fitness
		double bestFitness = population.getFittest(0).getFitness();
		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);
			// Create random individual to swap genes with
			Individual randomIndividual = new Individual(timetable);
			// Calculate adaptive mutation rate
			double adaptiveMutationRate = this.mutationRate;
			if (individual.getFitness() > population.getAvgFitness()) {
				double fitnessDelta1 = bestFitness - individual.getFitness();
				double fitnessDelta2 = bestFitness - population.getAvgFitness();
				adaptiveMutationRate = (fitnessDelta1 / fitnessDelta2) * this.mutationRate;
			}
			// Loop over individual's genes
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				// Skip mutation if this is an elite individual
				if (populationIndex > this.elitismCount) {
					// Does this gene need mutating?
					if (adaptiveMutationRate > Math.random()) {
						// Swap for new gene
						individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
					}
				}
			}
			// Add individual to population
			newPopulation.setIndividual(populationIndex, individual);
		}
		// Return mutated population
		return newPopulation;
	}
}
