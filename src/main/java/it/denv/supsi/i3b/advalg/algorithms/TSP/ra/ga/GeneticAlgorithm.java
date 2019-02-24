package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.NearestNeighbour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GeneticAlgorithm extends RoutingAlgorithm {

	private int genes_size = -1;
	private int[] initial_genes;
	private Population population;
	private int population_initial_size = 500;
	private int startNode = -1;

	@Override
	public Route route(int startNode, TSPData data) {
		/*
			Path:  [ SN, A, B, C, D, SN ]
			Genes: [ A, B, C, D ]
		*/

		this.startNode = startNode;
		data.setStartNode(startNode);

		genes_size = data.getDimension() - 1;

		population_initial_size = data.getDimension() * 5;

		assert(genes_size > 0);

		NearestNeighbour rnn = new NearestNeighbour();

		ArrayList<Route> routes = new ArrayList<>();

		for(int i=0; i<data.getDimension(); i++){
			Route route = rnn.route(i+1, data);
			routes.add(route);
		}

		Route route = routes
						.stream()
						.sorted(Comparator.comparing(Route::getLength))
						.findFirst()
						.get();

		//Route route = rnn.route(startNode, data);

		System.out.println("RNN Length: " + route.getLength());

		initial_genes = route.getCoords().stream()
				.filter(e -> e.getNumber() != route.getStartNode())
				.mapToInt(Coordinate::getNumber).toArray();

		data.setStartNode(route.getStartNode());

		population = new Population(population_initial_size, initial_genes, data);
		System.out.println(population + ", " + population.getRate(data.getBestKnown()));

		int i = 0;
		while(population.getRate(data.getBestKnown()) > 0.1){
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

		return null;
	}
}
