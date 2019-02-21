package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class NearestNeighbour extends RoutingAlgorithm {
	private TSPData data;
	private LinkedList<Edge> tour = new LinkedList<>();
	private int startNode = -1;

	@Override
	public Route route(int startNode, TSPData data) {
		this.data = data;
		this.startNode = startNode;

		Edge candidate = getCandidate();
		do {
			tour.add(candidate);
			candidate = getCandidate();
		}
		while(candidate != null);

		int last = tour.getLast().getSecond();

		tour.add(new Edge(last, startNode,
				data.getDistances()[last][startNode-1]));

		ArrayList<Integer> arr = tour.stream()
				.map(Edge::getFirst)
				.collect(Collectors.toCollection(ArrayList::new));

		arr.add(startNode-1);


		int length = tour.stream().map(Edge::getWeight).reduce(Integer::sum)
				.orElse(0);

		LinkedList<Coordinate> coords =
				arr.stream().map(e->
						data.getCoordinates().get(e)
				).collect(Collectors.toCollection(LinkedList::new));

		// Cleanup
		tour = new LinkedList<>();
		this.startNode = -1;
		return new Route(startNode, coords, length);
	}

	private Edge getCandidate(){
		int a;
		if(tour.size() == 0){
			a = startNode-1;
		} else {
			a = tour.getLast().getSecond();
		}

		int[][] dimensions = data.getDistances();
		ArrayList<Edge> candidates = new ArrayList<>();


		for(int i=0; i<dimensions.length; i++){
			if(i == a){
				continue;
			}

			if(!visited(i)){
				candidates.add(new Edge(a, i, dimensions[a][i]));
			}
		}

		if(candidates.size() == 0){
			return null;
		}

		candidates.sort(Edge::compare);
		return candidates.get(0);
	}

	private boolean visited(int a) {

		if(tour.size() == 0){
			return false;
		}

		if(tour.stream().map(Edge::getFirst).anyMatch(e->e == a)) {
			return true;
		}

		if(tour.getLast().getSecond() == a){
			return true;
		}

		return false;

	}
}
