package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;

import java.util.Random;

public class GeneticAlgorithm implements ILS {
	private int seed;
	private TSPData data;
	private Random random;

	/*
		GA Parameters:
	 */

	private double CROSSOVER_RATE = 0.8;
	private double MUTATION_RATE = 0.5;
	private int POPULATION_SIZE = 50;

	public GeneticAlgorithm(int seed, TSPData data){
		this.seed = seed;
		this.data = data;
		this.random = new Random(seed);
	}

	public GeneticAlgorithm(TSPData data){
		this.seed = (int) (Math.random() * 1000);
		this.random = new Random(seed);
		this.data = data;
	}

	@Override
	public Route route(Route route, TSPData data) {
		Population p = new Population(this, route, data);

		for(int i=0; i<10; i++){
			p = p.newGeneration();
		}

		return null;
	}

	@Override
	public int getSeed() {
		return 0;
	}

	public double getCrossoverRate() {
		return CROSSOVER_RATE;
	}

	public void setCrossoverRate(double CROSSOVER_RATE) {
		this.CROSSOVER_RATE = CROSSOVER_RATE;
	}

	public double getMutationRate() {
		return MUTATION_RATE;
	}

	public void setMutationRate(double MUTATION_RATE) {
		this.MUTATION_RATE = MUTATION_RATE;
	}

	public int getPopulationSize() {
		return POPULATION_SIZE;
	}

	public void setPopulationSize(int POPULATION_SIZE) {
		this.POPULATION_SIZE = POPULATION_SIZE;
	}

	public Random getRandom() {
		return this.random;
	}
}
