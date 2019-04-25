package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
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
		sp = improveSP(sp);
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
		int[][] d = data.getDistances();

		boolean improvement = true;
		while(improvement){
			int best_i = -1;
			int best_j = -1;
			int best_improvement = Integer.MAX_VALUE;

			improvement = false;
			for(int i=0; i < n - 3; i++){
				for(int k = i + 2; k <= n-1; k++){
					int delta = -d[path[i]][path[i+1]] - d[path[k]][path[k+1]]
							+ d[path[i]][path[k]] + d[path[i+1]][path[k+1]];
					if(delta < 0 && delta < best_improvement){
						best_improvement = delta;
						best_i = i+1;
						best_j = k;
						improvement = true;
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

		int d1 = d[c[p-1]][c[q]] + d[c[p]][c[q+1]]; // before swap
		int d2 = d[c[p-1]][c[p]] + d[c[q]][c[q+1]]; // after swap

		return d1 - d2;
	}
}
