package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABCycle;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.EAX;

import java.util.Arrays;
import java.util.Random;

public class Population {
	private TSPData data;
	private GeneticAlgorithm ga;
	private Random r;

	private Individual[] individuals;
	private boolean[] initialGenes;
	private int population_size = -1;
	private int generation = 1;

	private SwappablePath fittest;
	private SwappablePath best_fit_generation;


	public Population(GeneticAlgorithm ga, Route r, TSPData data){
		this.ga = ga;
		this.r = ga.getRandom();
		this.data = data;
		initialGenes = getGenes(r);
		this.population_size = ga.getPopulationSize();

		// Initial Population
		this.individuals = new Individual[this.population_size];

		RandomNearestNeighbour rnn = new RandomNearestNeighbour(this.r, data);
		TwoOpt twoOpt = new TwoOpt(data);
		for(int i=0; i<this.population_size; i++){
			// Generate random individuals:
			Route route = rnn.route(this.r.nextInt(data.getDimension()), data);
			route = twoOpt.improve(route);
			this.individuals[i] = new Individual(getGenes(route), this);
		}
	}

	public boolean[] mutateGenes(boolean[] genes){
		// Mutation isn't implemented

		return genes;
	}

	/*
		Do a crossover of A and B using the EAX algorithm

		@returns An Individual generated from parent A & parent B
	 */
	public Individual crossOver(Individual A, Individual B){
		// EAX Crossover
		return EAX.crossover(A, B, this);
	}

	public static boolean isValid(boolean[] mutatedGenes) {
		int y = mutatedGenes.length;
		int n = (int) (1.0/2  * (Math.sqrt(8 * y + 1)) + 1);

		// (n^2 - n)/2 = genes.length
		// n = 1/2 * (sqrt(8y + 1) + 1)

		int index = 0;
		int sum = 0;
		for(int i=0; i<n; i++){
			int rowSum = 0;
			for(int j = i + 1; j < n; j++){
				if(mutatedGenes[index]){
					rowSum++;
					sum++;
				}
				index++;
			}

			if(rowSum > 2){
				return false;
			}
		}

		return sum == n;
	}

	public static boolean[] getGenes(Route r) {
		/*
		 	Given a path (e.g: 1, 2, 6, 3, 5, 4, 1),
		 	the incidence matrix is the following:

		 	0 1 0 1 0 0
		 	X 0 0 0 0 1
		 	X X 0 0 1 1
		 	X X X 0 1 0
		 	X X X X 0 0
		 	X X X X X 0

		 	We'll consider only the upper part of the incidence
		 	matrix since the TSP problem is asymmetric.

		 	We can then discard the i = j column, since
		 	a city can't be visited twice.

		 	This gives us the following matrix:

		 	X 1 0 1 0 0
		 	X X 0 0 0 1
		 	X X X 0 1 1
		 	X X X X 1 0
		 	X X X X X 0
		 	X X X X X X

		 	Which gives us the following genes:
		 	[ 1 0 1 0 0 0 0 0 1 0 1 1 1 0 0 ]

		 	A solution is feasible iff each rows has a maximum of
		 	2 "ones", and the sum of all the "ones" is the number
		 	of nodes.
		 */

		int[] path = r.getPath();
		boolean[][] incidMat = new boolean[path.length - 1][path.length - 1];
		int n = path.length - 1;


		for(int i=0; i<path.length-1; i++){
			incidMat[path[i]][path[i+1]] = true;
			incidMat[path[i+1]][path[i]] = true;
		}

		int dim = (n * n - n)/2;

		boolean[] genes = new boolean[dim];
		int p = 0;
		for(int i=0; i<path.length-1; i++){
			for(int j=i + 1; j<path.length-1; j++){
				if(i==j){
					continue;
				}
				genes[p] = incidMat[i][j];
				p++;
			}
		}

		return genes;
	}

	Population newGeneration(){
		/*
			Given the current population, calculate the fittness of each
			chromosome X in the population.
		 */

		double totalFit = 0.0;
		Individual[] current_generation = new Individual[population_size];

		double[] probK = new double[population_size];

		for(Individual i : individuals){
			totalFit += i.getFit();
		}

		for(int i=0; i<individuals.length; i++){
			probK[i] = individuals[i].getFit() / totalFit;
		}

		Individual[] offsprings = new Individual[population_size];

		for(int i=0; i<population_size; i++){
			Individual A = getRandomIndividual(individuals, probK);
			Individual B = getRandomIndividual(individuals, probK);

			while(A == B){
				B = getRandomIndividual(individuals, probK);
			}

			// Given Two Parents, generate an offspring
			offsprings[i] = crossOver(A, B);
		}

		this.generation += 1;
		this.individuals = current_generation;

		// TODO: Set the best_fit for this generation
		best_fit_generation = null;

		if(fittest == null || fittest.getLength() > best_fit_generation.getLength()){
			fittest = best_fit_generation;
		}

		printGenerationStats();
		return this;
	}

	private Individual getRandomIndividual(Individual[] individuals, double[] probK) {
		double rand = r.nextDouble();

		for(int i=0; i<individuals.length; i++){
			if(rand <= 0){
				return individuals[i];
			}

			rand -= probK[i];
		}

		return individuals[individuals.length-1];
	}

	private void printGenerationStats(){
		System.out.println(
				String.format("G: %d\tP: %d\t F: %d",
						generation, this.individuals.length,
						this.best_fit_generation.getLength())
		);
	}

	/*
		Returns the problem dimension.
		E.g: 130 for ch130
	 */
	public int getDimension() {
		return data.getDimension();
	}

	/*
		Returns the incidence matrix
	 */
	public int[][] getIncidenceMatrix() {
		return data.getDistances();
	}

	public Random getRandom() {
		return r;
	}

	public TSPData getData(){
		return data;
	}

	public GeneticAlgorithm getGA() {
		return this.ga;
	}
}
