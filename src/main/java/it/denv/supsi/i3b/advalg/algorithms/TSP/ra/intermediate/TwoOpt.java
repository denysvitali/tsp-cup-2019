package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

public class TwoOpt implements IntermediateRoutingAlgorithm  {

	private TSPData data = null;

	public TwoOpt(TSPData data){
		this.data = data;
	}

	@Override
	public Route route(Route route, TSPData data) {
		// Given a Route, calculate a better route
		this.data = data;
		return improve(route);
	}

	public void setData(TSPData data){
		this.data = data;
	}

	public Route improve(Route r){

		/*
			In a 2-opt Algorithm, when removing 2 edges, there is only
			2^2 - 1 alternative solution.
		 */

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

		SwappablePath sp = new SwappablePath(path);
		return improveSP(sp);
	}

	public Route improveSP(SwappablePath sp){
		SwappablePath newsp;
		int bestLength = sp.calulateDistance(data);
		int swappableNodes = sp.getPathArr().length - 1;

		boolean improvement = true;
		while(improvement) {
			improvement = false;
			for (int i = 1; i < swappableNodes - 1; i++) {
				for (int k = i + 1; k < swappableNodes; k++) {
					newsp = sp.twoOptSwap(i, k);
					int distance = newsp.calulateDistance(data);
					if (distance < bestLength) {
						improvement = true;
						bestLength = distance;
						sp = newsp;
					}
				}
			}
		}

		return new Route(sp.getPathArr(), bestLength, data);
	}
}
