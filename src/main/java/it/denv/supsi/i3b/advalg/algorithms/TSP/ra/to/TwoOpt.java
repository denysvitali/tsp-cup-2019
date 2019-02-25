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
		/*
			procedure 2-opt
				(1) Let T be the current tour.
				(2) Perform the following until failure is obtained.
				(2.1) For every node i = 1, 2, . . . , n:
				Examine all 2-opt moves involving the edge between i and its successor in the
				tour. If it is possible to decrease the tour length this way, then choose the
				best such 2-opt move and update T .
				(2.2) If no improving move could be found, then declare failure.
			end of 2-opt
		 */

		int[] path = r.getPath();
		int bestLength = r.getLength();

		SwappablePath sp = new SwappablePath(path);
		SwappablePath newsp;
		int swappableNodes = path.length - 1;

		boolean improvement = true;
		while(improvement) {
			improvement = false;
			for (int i = 1; i < swappableNodes - 1; i++) {
				for (int k = i + 1; k < swappableNodes; k++) {
					newsp = sp.swap(i, k);
					int distance = newsp.calulateDistance(data);
					if (distance < bestLength) {
						improvement = true;
						bestLength = distance;
						sp = newsp;
					}
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
