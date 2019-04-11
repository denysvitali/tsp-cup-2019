package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.LinkedList;

public class SpanningTree {
	private ArrayList<Edge<Integer>> edges = new ArrayList<>();

	public SpanningTree(ArrayList<Edge<Integer>> edges){
		this.edges = edges;
	}

	public void addEdge(Edge<Integer> e){
		edges.add(e);
	}

	public ArrayList<Edge<Integer>> getEdges() {
		return edges;
	}
}
