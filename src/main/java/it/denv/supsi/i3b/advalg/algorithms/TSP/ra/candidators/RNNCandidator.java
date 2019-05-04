package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.*;
import java.util.stream.Collectors;

public class RNNCandidator implements Candidator {

	private int size;
	private int seed = -1;
	private Random random;
	private TSPData data;
	private TreeSet<Integer> tabuList = new TreeSet<>();
	private TreeSet<Integer> unvisitedNodes = new TreeSet<>();

	public RNNCandidator(int seed, int size, TSPData data){
		this.size = size;
		this.data = data;
		this.seed = seed;
		for(int i = 0; i < data.getDim(); i++){
			unvisitedNodes.add(i);
		}

		this.random = new Random(seed);
	}

	public RNNCandidator(Random r, int size, TSPData data){
		this.random = r;
		this.size = size;
		this.data = data;
		for(int i = 0; i < data.getDim(); i++){
			unvisitedNodes.add(i);
		}
	}

	public RNNCandidator(int size, TSPData data){
		this.size = size;
		this.data = data;
		for(int i = 0; i < data.getDim(); i++){
			unvisitedNodes.add(i);
		}

		this.seed = (int) (Math.random() * 10000);
		this.random = new Random(seed);
	}

	@Override
	public void computeCandidates() {
		// NO-OP
	}

	public int getSeed() {
		return seed;
	}

	@Override
	public ArrayList<Edge> getCandidates(int startNode) {
		ArrayList<Edge> result = unvisitedNodes.stream()
				.filter(e->e!=startNode)
				.map(e -> new Edge(startNode, e, data.getDistances()[startNode][e]))
				.sorted(Comparator.comparing(Edge::getWeight))
				.limit(size)
				.collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(result);
		return result;
	}

	@Override
	public void addVisited(Edge candidate) {
		tabuList.add(candidate.getU());
		unvisitedNodes.remove(candidate.getU());
	}

	@Override
	public void setStartNode(int startNode) {

	}
}
