package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.rnn.RandomNearestNeighbour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TwoOpt implements IntermediateRoutingAlgorithm  {

	private TSPData data = null;

	@Override
	public Route route(Route route, TSPData data) {
		// Given a Route, calculate a better route
		this.data = data;
		return improve(route);
	}

	private Route improve(Route r){
		int[] path = r.getPath();


		int bestLength = r.getLength();
		boolean improvement = true;

		SwappablePath sp = new SwappablePath(path);
		SwappablePath newsp;
		int swappableNodes = path.length -2;

		for(int i=1; i< swappableNodes - 1; i++){
			for(int k=i; k < swappableNodes - 1; k++){
				newsp = sp.swap(i, k);
				int distance = newsp.calulateDistance(data);
				if(distance < bestLength){
					bestLength = distance;
					sp = newsp;
				}
			}
		}

		LinkedList<Coordinate> coordinates =
				sp.getPath().stream()
						.map(e-> data.getCoordinates().get(e-1))
						.collect(Collectors.toCollection(LinkedList::new));

		return new Route(
				data.getStartNode(),
				coordinates,
				bestLength);

	}
}
