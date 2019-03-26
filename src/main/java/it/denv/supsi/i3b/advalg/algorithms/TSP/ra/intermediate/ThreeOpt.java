package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ThreeOpt implements IntermediateRoutingAlgorithm  {

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

	public ThreeOpt addCandidator(Candidator<Integer> c){
		candidators.add(c);
		return this;
	}

	private ArrayList<Route> generateCombinations(Route r, int i, int k, int m){
		ArrayList<Route> routes = new ArrayList<>();
		int cities = r.getLength();
		int[] op = r.getPath();

		int[][] paths = new int[8][cities];

		int j = i+1;
		int l = k+1;
		int n = m+1;

		/*paths[0] = new SwappablePath(op)
				.twoOptSwap(i,k)
				.twoOptSwap()
				.getPathArr();

		 */
		for(int z=0; z<8; z++){
			routes.add(new Route(paths[z], r.getData()));
		}

		return routes;
	}

	public Route ThreeOptMove(Route r, int i, int j, int k){
		/*
			i-j: Edge 1
			k-l: Edge 2
			m-n: Edge 3

			First Move:
			i-j => i-k
			k-l => l-n
			m-n => m-j

			Second Move:
			i-j => i-l
			k-l => k-n
			m-n => m-j
		 */

		int edgeI = i+1;
		int edgeJ = j+1;
		int edgeK = k+1;

		int[] path = r.getPath();

		return null;
	}

	private Route improve(Route r){

		/*

			In a 3-opt algorithm, when removing 3 edges, there are
			2^3 - 1 alternative feasible solutions.

			(3 of these solutions are 2-opt moves.)
		 */

		if(candidators.size() == 0){
			throw new RuntimeException("Unable to start 3-Opt without" +
					" any candidator.");
		}

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

		boolean improvement = true;
		int[] initialPath = r.getPath();

		while(improvement){
			improvement = false;



		}

		throw new NotImplementedException();
	}
}
