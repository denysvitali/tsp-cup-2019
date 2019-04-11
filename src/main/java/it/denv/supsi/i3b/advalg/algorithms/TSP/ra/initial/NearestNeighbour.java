package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.NNCandidator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class NearestNeighbour extends RoutingAlgorithm {
	protected TSPData data;
	private LinkedList<Edge<Integer>> tour = new LinkedList<>();
	protected Candidator<Integer> candidator;

	public NearestNeighbour(TSPData data) {
		this.data = data;
		this.candidator = new NNCandidator(3, data);
	}

	@Override
	public Route route(int startNode, TSPData data) {
		this.data = data;
		this.candidator.setStartNode(startNode);

		ArrayList<Edge<Integer>> candidates = candidator.getCandidates(startNode);
		do {
			Edge<Integer> candidate = candidates.get(0);
			tour.add(candidate);
			candidator.addVisited(candidate);
			candidates = candidator.getCandidates(candidate.getV());
		}
		while (candidates.size() != 0);

		int last = tour.getLast().getV();

		tour.add(new Edge<>(last, startNode,
				data.getDistances()[last][startNode - 1]));

		ArrayList<Integer> arr = tour.stream()
				.map(Edge::getU)
				.collect(Collectors.toCollection(ArrayList::new));

		arr.add(startNode);


		int length = tour
				.stream()
				.map(Edge::getWeight)
				.reduce(Integer::sum)
				.orElse(0);

		// Cleanup
		tour = new LinkedList<>();

		Route r = new Route(arr
				.stream()
				.mapToInt(Integer::intValue)
				.toArray(),
				data);
		r.setLength(length);
		return r;
	}

	@Override
	public int getSeed() {
		return 0;
	}

	private boolean visited(int a) {
		if (tour.size() == 0) {
			return false;
		}

		if (tour.stream().map(Edge::getU).anyMatch(e -> e == a)) {
			return true;
		}

		return tour.getLast().getV() == a;
	}
}
