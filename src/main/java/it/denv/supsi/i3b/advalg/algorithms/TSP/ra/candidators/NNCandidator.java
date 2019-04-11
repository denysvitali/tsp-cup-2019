package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Candidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class NNCandidator implements Candidator<Integer> {

	private int size;
	private int startNode;
	private TSPData data;
	private TreeSet<Integer> tabuList = new TreeSet<>();
	private TreeSet<Integer> unvisitedNodes = new TreeSet<>();

	public NNCandidator(int size, TSPData data){
		this.size = size;
		this.data = data;
		for(int i = 0; i < data.getDimension(); i++){
			if(data.getStartNode() != i){
				unvisitedNodes.add(i);
			}
		}
	}

	@Override
	public void computeCandidates() {
		// NO-OP
	}

	@Override
	public ArrayList<Edge<Integer>> getCandidates(int startNode) {
		return unvisitedNodes.stream()
				.filter(e->e!=startNode)
				.map(e -> new Edge<>(startNode, e, data.getDistances()[startNode][e]))
				.sorted()
				.limit(size)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void addVisited(Edge<Integer> candidate) {
		tabuList.add(candidate.getU());
		unvisitedNodes.remove(candidate.getU());
	}

	@Override
	public void setStartNode(int startNode) {
		this.startNode = startNode;
		this.unvisitedNodes.remove(startNode);
	}
}
