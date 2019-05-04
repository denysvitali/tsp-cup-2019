package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class KruskalMST implements SpanningTreeSearch {

	/*
		Returns a minimum spanning tree using Kruskal's algorithm.

		@param edges	TreeSet containing the edges of a complete graph.
	 */

	public static SpanningTree compute(int nodes, ArrayList<Edge> edges){
		/*
			1. 	Sort all the edges in non-decreasing order of their weight.
			2. 	Pick the smallest edge.
				Check if it forms a cycle with the spanning tree formed so far.
				If cycle is not formed, include this edge. Else, discard it.
			3. 	Repeat step#2 until there are (V-1) edges in the spanning tree.
		 */

		HashMap<Integer, Integer> cpmapper = new HashMap<>();
		ArrayList<Edge> mst = new ArrayList<>();

		int[] parent = new int[nodes];
		for(int i=0; i<nodes; i++){
			parent[i] = -1;
		}

		for(Edge e : edges){
			int st_u = findSubtree(parent, e.getU());
			int st_v = findSubtree(parent, e.getV());

			if(st_u != st_v){
				mst.add(e);
				union(parent, st_u, st_v);
			}
		}

		return new SpanningTree(mst);
	}

	private static void union(int[] parent, int st_u, int st_v) {
		parent[st_v] = st_u;
	}

	private static int findSubtree(int[] nodes, int e) {
		if(nodes[e] == -1){
			return e;
		}

		return findSubtree(nodes, nodes[e]);
	}


}
