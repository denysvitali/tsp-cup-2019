package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Population {
	private int size;
	private int generation = 0;
	private int permutations = 2;
	private PermutationPolicy firstPermutation = PermutationPolicy.PERMUTE_ALL;

	private int bestSize = 10;
	private ArrayList<Individual> population = new ArrayList<>();
	private TSPData tspData;

	public Population(int size, int[] initial_genes, TSPData tspData) {
		this.size = size;
		this.tspData = tspData;
		this.generation = 1;
		assert (size >= 1);

		for (int i = 0; i < size; i++) {
			Individual individual = new Individual(tspData);
			int genes[] = performPermutations(initial_genes, firstPermutation);
			individual.setGenes(genes);
		}
	}

	public Population(Population pop) {
		this.size = pop.size;
		ArrayList<Individual> bestIndividuals = pop.getOffspring(bestSize);
		this.generation = pop.generation + 1;

		for (int i = 0; i < size; i++) {
			Individual individual =
					bestIndividuals.get(i % bestIndividuals.size());

			int[] genes = individual.getGenes();
			for (int j = 0; j < permutations; j++) {
				int a = (int) (Math.random() * size);
				int b = (int) (Math.random() * size);

				while (a == b) {
					b = (int) (Math.random() * size);
				}

				int tmp_g = genes[a];
				genes[a] = genes[b];
				genes[b] = tmp_g;
			}

			individual.setGenes(genes);
			population.add(individual);
		}
	}

	private int[] performPermutations(int[] ig, PermutationPolicy pp) {
		int nperms;
		int[] newgenes = new int[ig.length];
		System.arraycopy(ig, 0, newgenes, 0, ig.length);

		switch (pp) {
			case PERMUTE_ALL:
				nperms = ig.length;
				break;
			case PERMUTE_FIXED:
				nperms = permutations;
				break;
			default:
				nperms = 0;
		}

		assert(ig.length >= 2);

		for(int i=0; i<nperms; i++){
			int a = (int) (Math.random() * ig.length);
			int b = (int) (Math.random() * ig.length);

			while(a == b){
				b = (int) (Math.random() * ig.length);
			}

			int v = newgenes[a];
			newgenes[a] = newgenes[b];
			newgenes[b] = v;
		}

		return newgenes;
	}

	private ArrayList<Individual> getOffspring(int bestSize) {
		return population.stream()
				.sorted(Comparator.comparing(Individual::getFitness))
				.limit(bestSize)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public int getGeneration() {
		return generation;
	}

	public Individual getFittest() {
		return population.stream()
				.min(Comparator.comparing(Individual::getFitness))
				.get();
	}

	@Override
	public String toString() {
		return String.format(
				"Population (S: %d, G: %d, Fittest: %.2f)",
				size, generation, getFittest().getFitness()
		);
	}
}
