package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.LinkedList;

public class SpanningTree {
	private ArrayList<Edge> edges = new ArrayList<>();

	public SpanningTree(){

	}

	public void addEdge(Edge e){
		edges.add(e);
	}
}
