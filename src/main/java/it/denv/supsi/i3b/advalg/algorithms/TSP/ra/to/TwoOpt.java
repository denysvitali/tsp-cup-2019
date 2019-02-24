package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.rnn.RandomNearestNeighbour;

import java.util.ArrayList;
import java.util.LinkedList;

public class TwoOpt implements IntermediateRoutingAlgorithm  {

	@Override
	public Route route(Route route, TSPData data) {
		// Given a Route, calculate a better route

		int bestLength = route.getLength();
		boolean improvement = true;

		while(improvement){

			int length = route.getLength();
			if(length < bestLength){
				improvement = false;
			}
		}

		return null;
	}
}
