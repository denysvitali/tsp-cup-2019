package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.spanningtree;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.Edge;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;


class KruskalMSTTest {

	@Test
	void compute() {
		ArrayList<Edge> mstEdges = new ArrayList<>();

		mstEdges.add(new Edge(1,0,4)); // X
		mstEdges.add(new Edge(0,7,8)); // X
		mstEdges.add(new Edge(7,6,1));
		mstEdges.add(new Edge(6,5,2));
		mstEdges.add(new Edge(5,2,4));
		mstEdges.add(new Edge(2,8,2));
		mstEdges.add(new Edge(2,3,7));
		mstEdges.add(new Edge(3,4,9));

		ArrayList<Edge> edges = new ArrayList<>(mstEdges);

		edges.add(new Edge(1,7,11));  // X
		edges.add(new Edge(7,8,8));
		edges.add(new Edge(1,2,8));
		edges.add(new Edge(6,8,6));
		edges.add(new Edge(3,5,14));
		edges.add(new Edge(4,5,10));

		assertEquals(14, edges.size());
		SpanningTree t = KruskalMST.compute(9, edges);

		ArrayList<Edge> mst = t.getEdges();

		mst.sort(Comparator.comparing(Edge::getWeight));
		mstEdges.sort(Comparator.comparing(Edge::getWeight));

		assertEquals(mstEdges, mst);
		assertEquals(8, mstEdges.size());
	}
}