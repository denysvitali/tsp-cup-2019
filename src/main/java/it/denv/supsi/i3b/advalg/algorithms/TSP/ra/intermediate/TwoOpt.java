package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

public class TwoOpt implements ILS {

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

	@Override
	public int getSeed() {
		return 0;
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
		return new Route(sp, data);
	}

	public SwappablePath improveSP(SwappablePath sp){
		int bestLength = sp.calulateDistance(data);
		int[] msp = sp.getPathArr();
		int sp_size = sp.getPathArr().length;
		int[][] d = data.getDistances();


		/*
			Source:
			- https://on-demand.gputechconf.com/gtc/2014/presentations/S4534-high-speed-2-opt-tsp-solver.pdf
			- http://olab.is.s.u-tokyo.ac.jp/~kamil.rocki/logo.pdf
		 */

		SwappablePath best = sp;

		boolean swapped;
		do {
			swapped = false;
			for(int i=1; i < sp_size - 2; i++) {
				for(int j = i + 2; j < sp_size - 1; j++){

					int d1 = d[msp[i]][msp[j+1]] + d[msp[i-1]][msp[j]];
					int d2 = d[msp[i]][msp[i-1]] + d[msp[j+1]][msp[j]];

					if(d1 < d2){
						sp = sp.twoOptSwap(i, j);
						msp = sp.getPathArr();
						swapped = true;

						int mRouteLength = sp.calulateDistance(data);
						if(mRouteLength < bestLength){
							bestLength = mRouteLength;
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
