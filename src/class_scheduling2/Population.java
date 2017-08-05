package class_scheduling2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population {
	private Individual population[];
	private double populationFitness = -1; // 平均适应度

	public Population(int populationSize) {
		this.population = new Individual[populationSize];
	}

	public Population(int populationSize, int chromosomeLength) {
		this.population = new Individual[populationSize];

		for (int i = 0; i < populationSize; i++) {
			Individual individual = new Individual(chromosomeLength);
			this.population[i] = individual;
		}
	}

	public Population(int populationSize, Timetable timetable) {
		// Initial population
		this.population = new Individual[populationSize];
		// Loop over population size
		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			// Create individual
			Individual individual = new Individual(timetable);
			// Add individual to population
			this.population[individualCount] = individual;
		}
	}

	/**
	 * Get average fitness
	 *
	 * @return The average individual fitness
	 */
	public double getAvgFitness() {
		if (this.populationFitness == -1) {
			double totalFitness = 0;
			for (Individual individual : population) {
				totalFitness += individual.getFitness();
			}
			this.populationFitness = totalFitness;
		}
		return populationFitness / this.size();
	}

	public Individual[] getIndividuals() {
		return this.population;
	}

	/**
	 * 获取最适合的个体
	 */
	public Individual getFittest(int offset) {
		// 根据适应度对个体进行从大到小排列
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});

		return this.population[offset];
	}

	/**
	 * @param fitness
	 *            种群的总适应度
	 */
	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}

	public double getPopulationFitness() {
		return this.populationFitness;
	}

	public int size() {
		return this.population.length;
	}

	/**
	 * 更新种群中的个体
	 */
	public Individual setIndividual(int offset, Individual individual) {
		return population[offset] = individual;
	}

	public Individual getIndividual(int offset) {
		return population[offset];
	}

	/**
	 * 打乱种群里的个体顺序
	 */
	public void shuffle() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			// 在[0,i+1)之间随机取出一个整数
			int index = rnd.nextInt(i + 1);
			Individual temp = population[index];
			population[index] = population[i];
			population[i] = temp;
		}
	}
}