package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.Arrays;
import java.util.Random;

public class Population {
	private Individual[] individuals;
	private TSPData data;
	private boolean[] initialGenes;
	private GeneticAlgorithm ga;
	private Random r;
	private int genesSize;

	public Population(GeneticAlgorithm ga, Route r, TSPData data){
		this.ga = ga;
		this.r = ga.getRandom();
		this.data = data;
		initialGenes = getGenes(r);
		this.genesSize = initialGenes.length;

		// Initial Population
		this.individuals = new Individual[ga.getPopulationSize()];
		for(int i=0; i<ga.getPopulationSize(); i++){
			this.individuals[i] = new Individual(mutateGenes(initialGenes),
					this);
		}
	}

	public boolean[] mutateGenes(boolean[] genes){
		boolean[] mutatedGenes = new boolean[genes.length];
		System.arraycopy(genes, 0, mutatedGenes, 0,
				genes.length);
		do {
			for(int i=0; i<2; i++){
				int mutationIndex = this.r.nextInt(genes.length);
				mutatedGenes[mutationIndex] = !mutatedGenes[mutationIndex];
			}
		} while(!isValid(mutatedGenes));

		return mutatedGenes;
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


		int prev = path[0];
		for(int i=1; i<path.length; i++){
			incidMat[prev][path[i]] = true;
			incidMat[path[i]][prev] = true;
			prev = path[i];
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
		for(Individual i : individuals){
			totalFit += i.getFit();
		}


		return this;
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
}
