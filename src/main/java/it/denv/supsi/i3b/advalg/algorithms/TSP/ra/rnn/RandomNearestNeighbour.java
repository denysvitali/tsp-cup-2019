package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.rnn;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RandomNearestNeighbour extends RoutingAlgorithm {
	private TSPData data;
	private LinkedList<Edge> tour = new LinkedList<>();

	@Override
	public Route route(int startNode, TSPData data) {
		this.data = data;

		Edge candidate = getCandidate();
		do {
			tour.add(candidate);
			candidate = getCandidate();
		}
		while(candidate != null);

		int last = tour.getLast().getSecond();

		tour.add(new Edge(last, data.getStartNode(),
				data.getDistances()[last][data.getStartNode()]));

		ArrayList<Integer> arr = tour.stream()
				.map(Edge::getFirst)
				.collect(Collectors.toCollection(ArrayList::new));

		arr.add(data.getStartNode()-1);


		int length = tour.stream().map(Edge::getWeight).reduce(Integer::sum)
				.orElse(0);

		LinkedList<Coordinate> coords =
				arr.stream().map(e->
						data.getCoordinates().get(e)
				).collect(Collectors.toCollection(LinkedList::new));

		return new Route(startNode, coords, length);
	}

	private Edge getCandidate(){
		int a;
		if(tour.size() == 0){
			a = data.getStartNode()-1;
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
		return candidates.get((int) ((Math.random() * 3) % candidates.size()));
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
