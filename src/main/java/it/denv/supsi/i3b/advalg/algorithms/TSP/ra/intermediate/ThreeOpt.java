package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.NotImplementedException;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
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

	public ThreeOpt addCandidator(Candidator<Integer> c){
		candidators.add(c);
		return this;
	}

	private Route improve(Route r){

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

		while(improvement){
			improvement = false;

			for(Integer i: r.getPath()){
				// Get Candidates
				ArrayList<Edge<Integer>> candidates = new ArrayList<>();
				for(Candidator<Integer> c : candidators){
					candidates.addAll(c.getCandidates(i)
							.stream()
							.limit(50)
							.collect(Collectors.toCollection(ArrayList::new))
					);
				}

				candidates.sort(Edge::compareTo);
			}
		}

		throw new NotImplementedException();
	}
}
