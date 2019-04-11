package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class KruskalMST implements SpanningTreeSearch {

	/*
		Returns a minimum spanning tree using Kruskal's algorithm.

		@param edges	TreeSet containing the edges of a complete graph.
	 */

	public static SpanningTree compute(TreeSet<Edge<Integer>> edges){
		/*
			1. 	Sort all the edges in non-decreasing order of their weight.
			2. 	Pick the smallest edge.
				Check if it forms a cycle with the spanning tree formed so far.
				If cycle is not formed, include this edge. Else, discard it.
			3. 	Repeat step#2 until there are (V-1) edges in the spanning tree.
		 */

		HashMap<Integer, Integer> cpmapper = new HashMap<>();
		ArrayList<Edge<Integer>> mst = new ArrayList<>();

		for(Edge<Integer> e : edges){
			int st_u = findSubtree(cpmapper, e.getU());
			int st_v = findSubtree(cpmapper, e.getV());

			if(st_u != st_v){
				mst.add(e);

				if(cpmapper.containsKey(e.getV())){
					cpmapper.put(e.getU(), e.getV());
				} else {
					cpmapper.put(e.getV(), e.getU());
				}
			}
		}

		return new SpanningTree(mst);
	}

	private static int findSubtree(HashMap<Integer, Integer> cpmapper,
								   int e) {
		if(cpmapper.get(e) != null){
			return findSubtree(cpmapper, cpmapper.get(e));
		}

		return e;
	}


}
