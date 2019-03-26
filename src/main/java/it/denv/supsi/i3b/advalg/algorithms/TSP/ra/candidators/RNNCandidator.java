package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RNNCandidator implements Candidator<Integer> {

	private int size;
	private int startNode;
	private int seed = -1;
	private Random random;
	private TSPData data;
	private TreeSet<Integer> tabuList = new TreeSet<>();
	private TreeSet<Integer> unvisitedNodes = new TreeSet<>();

	public RNNCandidator(int seed, int size, TSPData data){
		this.size = size;
		this.data = data;
		this.seed = seed;
		for(int i = 0; i < data.getDimension(); i++){
			unvisitedNodes.add(i);
		}

		this.random = new Random(seed);
	}
	public RNNCandidator(int size, TSPData data){
		this.size = size;
		this.data = data;
		for(int i = 0; i < data.getDimension(); i++){
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
	public ArrayList<Edge<Integer>> getCandidates(int startNode) {
		ArrayList<Edge<Integer>> result = unvisitedNodes.stream()
				.filter(e->e!=startNode)
				.map(e -> new Edge<>(startNode, e, data.getDistances()[startNode][e]))
				.sorted()
				.limit(size)
				.collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(result);
		return result;
	}

	@Override
	public void addVisited(Edge<Integer> candidate) {
		tabuList.add(candidate.getFirst());
		unvisitedNodes.remove(candidate.getFirst());
	}

	@Override
	public void setStartNode(int startNode) {
		this.startNode = startNode;
	}
}
