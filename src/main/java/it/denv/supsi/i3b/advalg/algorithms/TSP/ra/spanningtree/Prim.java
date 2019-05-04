package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Prim {

	private class Node {

	}

	private int start = 0; // Arbitrary
	private TSPData data;
	private ArrayList<Node> n = new ArrayList<>();
	private Set<Edge> mstSet = new HashSet<>();

	public Prim() {
		super();
	}

	public SpanningTree compute() {
		boolean[] reached = new boolean[data.getDim()];

		reached[start] = true;

		return null;
	}
}
