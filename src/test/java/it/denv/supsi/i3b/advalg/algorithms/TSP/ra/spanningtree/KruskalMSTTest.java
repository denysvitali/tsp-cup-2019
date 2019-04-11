package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class KruskalMSTTest {

	@Test
	void compute() {
		TreeSet<Edge<Integer>> edges = new TreeSet<>();
		ArrayList<Edge<Integer>> mstEdges = new ArrayList<>();

		mstEdges.add(new Edge<>(0,2,6));
		mstEdges.add(new Edge<>(2,1,3));
		mstEdges.add(new Edge<>(1,3,2));

		edges.addAll(mstEdges);

		edges.add(new Edge<>(2,3,4));
		edges.add(new Edge<>(0,1,7));

		SpanningTree t = KruskalMST.compute(edges);
		ArrayList<Edge<Integer>> mst = t.getEdges();

		mst.sort(Edge::compareTo);
		mstEdges.sort(Edge::compareTo);

		assertEquals(mstEdges, mst);
	}
}