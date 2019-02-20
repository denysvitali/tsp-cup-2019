package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.util.ArrayList;

public class GeneticAlgorithm extends RoutingAlgorithm {

	private int genes_size = -1;
	private int[] initial_genes;
	private Population population;
	private double sigma = 100.0;
	private int startNode = -1;

	@Override
	public Route route(int startNode, TSPData data) {
		/*
			Path:  [ SN, A, B, C, D, SN ]
			Genes: [ A, B, C, D ]
		*/

		this.startNode = startNode;
		genes_size = data.getDimension() - 1;

		assert(genes_size > 0);

		initial_genes = data.getCoordinates()
				.stream()
				.mapToInt(Coordinate::getNumber)
				.filter(i -> i != startNode)
				.toArray();
		population = new Population(genes_size, initial_genes, data);

		while(population.getFittest().getFitness() >= sigma){
			population = new Population(population);
			System.out.println(population);
		}

		System.out.println("End. " + population);

		return null;
	}
}
