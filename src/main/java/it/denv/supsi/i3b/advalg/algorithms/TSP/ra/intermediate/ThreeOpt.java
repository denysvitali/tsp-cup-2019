package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

import java.util.ArrayList;

public class ThreeOpt implements ILS {

	private TSPData data = null;
	private ArrayList<Candidator<Integer>> candidators = new ArrayList<>();

	public ThreeOpt(TSPData data){
		this.data = data;
	}

	@Override
	public Route route(Route route, TSPData data) {
		// Given a Route, calculate a better route
		this.data = data;
		return improve(route);
	}

	@Override
	public int getSeed() {
		return 0;
	}

	public Route improve(Route r){

		/*

			In a 3-opt algorithm, when removing 3 edges, there are
			2^3 - 1 alternative feasible solutions.

			(3 of these solutions are 2-opt moves.)
		 */

		/*
		procedure 3-opt
		(1) Let T be the current tour.
		(2) For every node i ∈ V compute a set of sizeNodes N (i)
			(possible choices are discussed below).
		(3) Perform the following until failure is obtained.
			(3.1) For every node i = 1, 2, . . . , n:
			Examine all possibilities to perform a 3-opt move which eliminates
			three edges having each at least one endnode in N (i).
			If it is possible to decrease the tour length this way,
			then choose the best such 3-opt move and update T.

			(3.2) If no improving move could be found, then declare failure.
		end of 3-opt
		 */

		int[] path = r.getPath();

		SwappablePath sp = new SwappablePath(path);
		return new Route(improveSP(sp), data);
	}

	public SwappablePath improveSP(SwappablePath sp){
		int bestLength = sp.calulateDistance(data);
		int sp_size = sp.getPathArr().length;

		/*
		procedure 3-opt
		(1) Let T be the current tour.
		(2) For every node i ∈ V compute a set of sizeNodes N (i)
			(possible choices are discussed below).
		(3) Perform the following until failure is obtained.
			(3.1) For every node i = 1, 2, . . . , n:
			Examine all possibilities to perform a 3-opt move which eliminates
			three edges having each at least one endnode in N (i).
			If it is possible to decrease the tour length this way,
			then choose the best such 3-opt move and update T.

			(3.2) If no improving move could be found, then declare failure.
		end of 3-opt
		 */

		SwappablePath best = sp;

		boolean swapped;
		do {
			swapped = false;
			for(int i=0; i < sp_size - 5; i++) {
				for(int j = i + 2; j < sp_size - 3; j++){
					for(int k= j + 2; k<sp_size - 1; k++){
						SwappablePath[] sps = sp.threeOptSwap(i, j, k);

						int minTourLength = Integer.MAX_VALUE;
						SwappablePath bestSP = null;

						for(int a=0; a<sps.length; a++){
							int length = sps[a].calulateDistance(data);
							if(length < minTourLength){
								minTourLength = length;
								bestSP = sps[a];
							}
						}

						assert bestSP != null;

						swapped = true;

						if(minTourLength < bestLength){
							bestLength = minTourLength;
							best = sp;
						} else {
							swapped = false;
						}
					}
				}
			}

		} while(swapped);

		return best;
	}
}
