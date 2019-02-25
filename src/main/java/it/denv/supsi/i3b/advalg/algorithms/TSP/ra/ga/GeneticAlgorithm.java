package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GeneticAlgorithm implements IntermediateRoutingAlgorithm {

	private int genes_size = -1;
	private int[] initial_genes;
	private Population population;
	private int population_initial_size = 500;
	private int startNode = -1;

	@Override
	public Route route(Route route, TSPData data) {
		this.startNode = route.getStartNode();

		initial_genes = route.getCoords().stream()
				.filter(e -> e.getNumber() != route.getStartNode())
				.mapToInt(Coordinate::getNumber).toArray();

		this.genes_size = initial_genes.length;

		data.setStartNode(route.getStartNode());

		population = new Population(population_initial_size, initial_genes, data);
		System.out.println(population + ", " + population.getRate(data.getBestKnown()));

		int i = 0;
		while(population.getRate(data.getBestKnown()) > 0.02){
			population = new Population(population);

			if(i%10 == 0){
				System.out.println(population + ", " + population.getRate(data.getBestKnown()));
			}
			i++;
		}

		Individual fittest = population.getFittest();
		int[] finalPath = new int[genes_size+2];
		System.arraycopy(fittest.getGenes(), 0, finalPath, 1,
				genes_size);

		finalPath[0] = startNode;
		finalPath[genes_size+1] = startNode;


		String arrayOutput = Arrays.stream(finalPath)
				.mapToObj(Integer::toString)
				.collect(Collectors.joining(", "));
		System.out.println("Final Path: " + arrayOutput);

		System.out.println("End. " + population);

		return route;
	}
}
