package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.LinkedList;

public class SpanningTree {
	private ArrayList<Edge> edges;

	public SpanningTree(ArrayList<Edge> edges){ this.edges = edges; }

	public void addEdge(Edge e){
		edges.add(e);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}
}
