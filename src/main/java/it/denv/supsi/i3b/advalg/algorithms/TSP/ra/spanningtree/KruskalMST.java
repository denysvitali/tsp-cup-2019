package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;

public class KruskalMST implements SpanningTreeSearch {

	public SpanningTree compute(TSPData data){
		/*
			1. 	Sort all the edges in non-decreasing order of their weight.
			2. 	Pick the smallest edge.
				Check if it forms a cycle with the spanning tree formed so far.
				If cycle is not formed, include this edge. Else, discard it.
			3. 	Repeat step#2 until there are (V-1) edges in the spanning tree.
		 */

		ArrayList<Edge<Integer>> edges = data.getEdges();

		return new SpanningTree();
	}
}
