package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.City;

import java.util.List;

public class TwoOpt implements ILS {

	private TSPData data = null;
	private boolean cl = false;

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

	public void setCandidate(boolean cl){
		this.cl = cl;
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
		SwappablePath sp = improveSP(r.getSP());
		return new Route(sp, data);
	}

	// http://www.cs.colostate.edu/~cs314/yr2018sp/more_progress/slides/CS314-S4-Sprint4.pdf
	public SwappablePath improveSP(SwappablePath sp){
		int[] path = sp.getPathArr();
		/*
			Source:
			- https://on-demand.gputechconf.com/gtc/2014/presentations/S4534-high-speed-2-opt-tsp-solver.pdf
			- http://olab.is.s.u-tokyo.ac.jp/~kamil.rocki/logo.pdf
			- Advanced search algorithms, Gambardella, 70
		 */

		int n = sp.getPathArr().length - 1;
		assert(n > 4);

		boolean improvement = true;
		while(improvement){
			int best_i = -1;
			int best_j = -1;

			improvement = false;

			if(cl){
				for(int i=1; i < path.length; i++){
					int[] c = data.getCL(path[i]);
					int best_delta = Integer.MAX_VALUE;

					for(int a : c){
						int k = -1;
						for(int j = 0; j<path.length; j++){
							if(a == path[j]){
								k=j;
							}
						}

						int p = i;
						int q = k;

						if(p>=q){
							continue;
						}


						int delta = cg(path, p, q);
						if (delta < 0 && delta < best_delta) {
							best_i = p;
							best_j = q;
							best_delta = delta;
							improvement = true;
						}
					}

					if(improvement){
						break;
					}
				}
			} else {
				for(int i=1; i < path.length; i++){
					int best_delta = Integer.MAX_VALUE;
					for(int j=i; j<path.length; j++){
						int delta = cg(path, i, j);
						if (delta < 0 && delta < best_delta) {
							best_i = i;
							best_j = j;
							best_delta = delta;
							improvement = true;
						}
					}

					if(improvement){
						break;
					}
				}
			}

			if(improvement){
				sp.twoOptSwap(best_i,best_j);
			}
		}

		return sp;
	}

	private int cg(int[] c, int p, int q){
		int[][] d = data.getDistances();

		/*
			From "The Traveling Salesman Problem: A Computational Study",
			Chapter 15, page 425 - 426.

			c_{i_{p-1}i}_{p} + c_{i_qi}
		 */

		int qn = q+1;
		if(qn == c.length){
			qn = 0;
		}

		int d1 = d[c[p-1]][c[q]] + d[c[p]][c[qn]];
		int d2 = d[c[p-1]][c[p]] + d[c[q]][c[qn]];

		return d1 - d2;
	}
}
