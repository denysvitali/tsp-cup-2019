package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;

import java.util.*;
import java.util.stream.Collectors;

public class Population {
	private int size;
	private int generation = 0;

	/*
	private int individuals = 100; // Magic Value by Gambardella
	private double crossover_prob = 0.99; // 1.0 is best, according to Gambardella
	private double generation_gap = 1.0; // Number of children to keep
	private double mutation_prob = 5.0/1000;
	private int elitism = 1; // Number of best indivduals to keep

	*/

	// Genetic Varied
	private int individuals = 100; // Magic Value by Gambardella
	private double crossover_prob = 0.50; // 1.0 is best, according to Gambardella
	private double generation_gap = 0.80; // Number of children to keep vs parent
	private double mutation_prob = 5.0/1000;
	private int elitism = 1; // Number of best indivduals to keep, most imporant


	private int[] initial_genes;

	private int pop_size;
	private Individual best;

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

		best = getFittest();
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
		this.data = pop.data;
		this.best = pop.best;
		this.pop_size = pop.pop_size;

		int bestSize = individuals - elitism;

		ArrayList<Individual> parentsCandidates = new ArrayList<>();

		for(int i=0; i<bestSize; i++){
			parentsCandidates.add(pop.getProbabilisticParent());
		}

		for(Individual i : pop.getPopulation()){
			if(i.getFitness() < best.getFitness()){
				best = i;
			}
		}

		// Improve parents w/ 2-Opt

		TwoOpt to = new TwoOpt(data);

		for(Individual ind : parentsCandidates){
			LinkedList<Integer> optimizedGenes = new LinkedList<>();
			int[] genes = ind.getGenes();
			optimizedGenes.add(data.getStartNode());
			for (int gene : genes) {
				optimizedGenes.add(gene);
			}
			optimizedGenes.add(data.getStartNode());

			SwappablePath sp = new SwappablePath(optimizedGenes
					.stream()
					.mapToInt(Integer::intValue)
					.toArray());

			int[] newGenes = new int[data.getDimension()-1];
			System.arraycopy(to.improveSP(sp),
					1, newGenes, 0, data.getDimension() - 1);
			ind.setGenes(newGenes);
		}



		// Select Crossover Population
		ArrayList<Individual> crossOverItems = new ArrayList<>();
		ArrayList<Individual> nonCrossOverItems = new ArrayList<>();

		for(Individual bi : parentsCandidates){
			if(Math.random() < crossover_prob){
				crossOverItems.add(bi);
			} else {
				nonCrossOverItems.add(bi);
			}
		}

		ArrayList<Individual> mutatedIndividuals = new ArrayList<>();
		ArrayList<Individual> unmutatedIndividuals = new ArrayList<>();
		ArrayList<Individual> crossOverIndividuals = new ArrayList<>();


		// Crossover / Offspring
		for(int i=0; i<crossOverItems.size() - 1; i+=2){
			Individual individual = getOffspring(
					parentsCandidates.get(i),
					parentsCandidates.get(i+1)
			);
			Individual individual2 = getOffspring(
					parentsCandidates.get(i+1),
					parentsCandidates.get(i)
			);
			crossOverIndividuals.add(individual);
			crossOverIndividuals.add(individual2);
		}


		ArrayList<Individual> mutationCandidates = new ArrayList<>();
		mutationCandidates.addAll(
				crossOverIndividuals
						.stream()
						.limit((long) (pop_size * generation_gap))
						.collect(Collectors.toCollection(ArrayList::new))
		);

		mutationCandidates.addAll(
				parentsCandidates
						.stream()
						.limit((long) (pop_size * (1-generation_gap)))
						.collect(Collectors.toCollection(ArrayList::new))
		);

		// Mutation

		for (Individual mutationCandidate : mutationCandidates) {
			double rand = Math.random();
			if (rand < mutation_prob) {
				int[] genes = mutationCandidate.getGenes();
				for (int j = 0; j < Math.random() * genes.length; j++) {
					int a = (int) (Math.random() * genes.length);
					int b = (int) (Math.random() * genes.length);

					while (a == b) {
						b = (int) (Math.random() * genes.length);
					}

					int tmp_g = genes[a];
					genes[a] = genes[b];
					genes[b] = tmp_g;
				}

				mutatedIndividuals.add(mutationCandidate);
			}
			else {
				unmutatedIndividuals.add(mutationCandidate);
			}
		}

		// Add new children
		population.addAll(mutatedIndividuals);
		population.addAll(unmutatedIndividuals);
		population.addAll(getFittest(elitism));

		this.initial_genes = pop.initial_genes;
		this.pop_size = pop.pop_size;
	}

	private Individual getProbabilisticParent() {
		double sum = 0.0;
		for(int i=0; i<population.size(); i++){
			sum += population.get(i).getFitness();
		}

		double sum_tmp = 0.0;
		double random = Math.random();

		for(Individual i : population){
			i.setProb(1 - (i.getFitness() / sum));
		}

		for(Individual i : population){
			sum_tmp += i.getProb();

			if(sum_tmp >= random){
				return i;
			}
		}

		return null;
	}

	private ArrayList<Individual> getPopulation() {
		return population;
	}

	public static Individual getOffspring(Individual p1, Individual p2){
		int[] p1_genes = p1.getGenes();

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
		int[] p1_genes = p1.getGenes();
		int[] p2_genes = p2.getGenes();

		int[] p3_genes = new int[p1_genes.length];

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
		return ((best.getFitness() * 1.0 / bestKnown) - 1) * 100;
	}

	@Override
	public String toString() {
		return String.format(
				"Population (S: %d, Genes Size: %d, G: %d, Fittest: %d, OB: %d, BK: %d)",
				population.size(), size,
				generation,
				getFittest().getFitness(),
				best.getFitness(),
				data.getBestKnown()
		);
	}
}
