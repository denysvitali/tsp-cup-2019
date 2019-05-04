package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax;

import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EAXTest {
	private ArrayList<ABEdge> getProblemEdges(){
		ArrayList<ABEdge> abEdgeArrayList = new ArrayList<>();

		// A Edges
		abEdgeArrayList.add(new ABEdge(1,2, true));
		abEdgeArrayList.add(new ABEdge(2,3, true));
		abEdgeArrayList.add(new ABEdge(3,4, true));
		abEdgeArrayList.add(new ABEdge(4,5, true));
		abEdgeArrayList.add(new ABEdge(5,6, true));
		abEdgeArrayList.add(new ABEdge(6,1, true));

		// B Edges
		abEdgeArrayList.add(new ABEdge(1,2, false));
		abEdgeArrayList.add(new ABEdge(2,5, false));
		abEdgeArrayList.add(new ABEdge(5,4, false));
		abEdgeArrayList.add(new ABEdge(4,6, false));
		abEdgeArrayList.add(new ABEdge(6,3, false));
		abEdgeArrayList.add(new ABEdge(3,1, false));

		return abEdgeArrayList;
	}

	private EAXGraph getProblemGraph(){
		EAXGraph graph = new EAXGraph();
		for(ABEdge e : getProblemEdges()){
			graph.addEdge(e);
		}
		return graph;
	}

	@Test
	void generateABCycle() {
		GeneticAlgorithm mockedGA = mock(GeneticAlgorithm.class);
		when(mockedGA.getRandom()).thenReturn(new Random(2));

		ABCycle c = EAX.generateABCycle(getProblemGraph(), mockedGA);
		assertNotNull(c);

		int prev = -1;
		int first = -1;
		boolean wasA = false;
		for(ABEdge e : c.getPath()){
			if(first == -1){
				first = e.getU();
				prev = first;
			} else {
				assertNotEquals(wasA, e.isA());
			}
			assertEquals(prev, e.getU());
			prev = e.getV();
			wasA = e.isA();
		}

		assertEquals(first, c.getPath().get(c.getPath().size()-1).getV());
		System.out.println(c.getPath());
	}

	@Test
	void generateABCycles() {
		GeneticAlgorithm mockedGA = mock(GeneticAlgorithm.class);
		when(mockedGA.getRandom()).thenReturn(new Random(3));

		ArrayList<ABCycle> cycles =
				EAX.generateABCycles(getProblemGraph(), mockedGA);

		assertNotNull(cycles);
	}
}