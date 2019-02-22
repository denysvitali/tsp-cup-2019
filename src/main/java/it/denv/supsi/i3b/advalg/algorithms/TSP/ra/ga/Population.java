package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Population {
	private int size;
	private int generation = 0;
	private double mutation_prob = 0.1;
	private double crossover_prob = 0.9;
	private double fittest_perc = 0.2;
	private int[] initial_genes;
	private int pop_size;

	private TSPData data;

	private PermutationPolicy firstPermutation = PermutationPolicy.PERMUTE_FIXED;

	private ArrayList<Individual> population = new ArrayList<>();

	public Population(int size, int[] initial_genes, TSPData tspData) {
		this.pop_size = size;
		this.initial_genes = initial_genes;
		this.size = initial_genes.length;
		this.generation = 1;
		this.data = tspData;
		assert (size >= 1);

		Individual ind1 = new Individual(tspData);
		ind1.setGenes(initial_genes);
		population.add(ind1);

		for (int i = 1; i < size; i++) {
			population.add(generateRandomIndividual());
		}
	}

	private Individual generateRandomIndividual() {
		Individual individual = new Individual(data);
		int[] genes = performPermutations(initial_genes, firstPermutation);
		individual.setGenes(genes);

		return individual;
	}

	public Population(Population pop) {
		this.size = pop.size;
		this.generation = pop.generation + 1;


		int bestSize = (int) (pop.size * fittest_perc);


		ArrayList<Individual> bestIndividuals = pop.getFittest(bestSize);

		// Select Crossover Population
		ArrayList<Individual> crossOverItems = new ArrayList<>();

		for(Individual bi : bestIndividuals){
			if(Math.random() < crossover_prob){
				crossOverItems.add(bi);
			}
		}


		// Crossover / Offspring
		for(int i=0; i<crossOverItems.size() - 1; i+=2){
			Individual individual = getOffspring(
					bestIndividuals.get(i),
					bestIndividuals.get(i+1)
			);
			population.add(individual);
		}

		for(int i=0; i<crossOverItems.size() - 1; i+=2){
			Individual individual = getOffspring(
					bestIndividuals.get(i),
					bestIndividuals.get(i+1)
			);
			population.add(individual);
		}


		// Mutation

		for (int i = 0; i < population.size(); i++) {

			double rand = Math.random();
			if(rand < mutation_prob){
				Individual individual = population.get(i);

				int[] genes = individual.getGenes();

				double perm_perc = Math.random() * crossover_prob;

				for (int j = 0; j < perm_perc * genes.length; j++) {
					int a = (int) (Math.random() * size);
					int b = (int) (Math.random() * size);

					int tmp_g = genes[a];
					genes[a] = genes[b];
					genes[b] = tmp_g;
				}

				individual.setGenes(genes);
				population.add(individual);
			}
		}

		this.initial_genes = pop.initial_genes;
		this.data = pop.data;
		this.pop_size = pop.pop_size;

		population.addAll(bestIndividuals);

		int missing = (pop_size - population.size());

		for(int i=0; i<missing; i++) {
			population.add(generateRandomIndividual());
		}
	}

	public static Individual getOffspring(Individual p1, Individual p2){
		int p1_genes[] = p1.getGenes();

		// OffSpring Start
		int o_s = (int) (Math.random() * p1_genes.length);
		int o_e = (int) (o_s + Math.random() * (p1_genes.length - o_s)) % p1_genes.length;

		if(o_e < o_s){
			o_e = p1_genes.length;
		}

		assert(o_s <= o_e);
		assert(o_e < p1_genes.length+1);
		assert(o_s >= 0);

		return getOffspring(p1, p2, o_s, o_e);
	}

	public static Individual getOffspring(Individual p1, Individual p2,
										  int o_s,
										  int o_e) {
		int p1_genes[] = p1.getGenes();
		int p2_genes[] = p2.getGenes();

		int p3_genes[] = new int[p1_genes.length];

		// Merge the genes
		int p2_offset = 0;

		for(int i=0; i < p1_genes.length; i++){
			if(i >= o_s && i < o_e) {
				// Add the genes from P1
				p3_genes[i] = p1_genes[i];
				p2_offset--;
			} else {
				while(in_subarray(p2_genes[i + p2_offset], p1_genes, o_s, o_e)){
					p2_offset++;
				}

				p3_genes[i] = p2_genes[i + p2_offset];
			}
		}

		Individual i = new Individual(p1);
		i.setGenes(p3_genes);

		return i;
	}

	private static boolean in_subarray(int element, int[] p1_genes, int o_s, int o_e) {
		for(int i=o_s; i<o_e; i++){
			if(element == p1_genes[i]){
				return true;
			}
		}
		return false;
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
				double perm_perc = Math.random() * crossover_prob;
				nperms = (int) (Math.random() * ig.length);
				break;
			default:
				nperms = 0;
		}

		assert(ig.length >= 2);

		for(int i=0; i<nperms; i++){
			int a = (int) (Math.random() * ig.length);
			int b = (int) (Math.random() * ig.length);

			int v = newgenes[a];
			newgenes[a] = newgenes[b];
			newgenes[b] = v;
		}

		return newgenes;
	}

	private ArrayList<Individual> getFittest(int bestSize) {
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

	public double getRate(int bestKnown){
		return 1-(bestKnown * 1.0 /getFittest().getFitness());
	}

	@Override
	public String toString() {
		return String.format(
				"Population (S: %d, Genes Size: %d, G: %d, Fittest: %d)",
				population.size(), size, generation, getFittest().getFitness()
		);
	}
}
