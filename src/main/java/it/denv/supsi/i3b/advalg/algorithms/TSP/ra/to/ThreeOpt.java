package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import jdk.jshell.spi.ExecutionControl;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class ThreeOpt implements IntermediateRoutingAlgorithm  {

	private TSPData data = null;

	@Override
	public Route route(Route route, TSPData data) {
		// Given a Route, calculate a better route
		this.data = data;
		return improve(route);
	}

	private Route improve(Route r){

		/*
		procedure 3-opt
		(1) Let T be the current tour.
		(2) For every node i ∈ V compute a set of nodes N (i) (possible choices are discussed
		below).
		(3) Perform the following until failure is obtained.
		(3.1) For every node i = 1, 2, . . . , n:
		Examine all possibilities to perform a 3-opt move which eliminates three edges
		having each at least one endnode in N (i). If it is possible to decrease the tour
		length this way, then choose the best such 3-opt move and update T .
		(3.2) If no improving move could be found, then declare failure.
		end of 3-opt
		 */

		throw new NotImplementedException();
	}
}
