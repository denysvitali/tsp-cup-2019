package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Utils;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AntTest {

	@Test
	void visit() throws IOException {
		TSPLoader tspLoader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = tspLoader.load();
		Ant a = new Ant(new AntColony(1, data));
		assertEquals(0, a.getTabuList().size());

		a.visit(1);
		assertEquals(1, a.getTabuList().size());
		assertEquals(1, a.getTabuList().get(0));
	}

	@Test
	void hasArc() throws IOException {
		TSPLoader tspLoader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = tspLoader.load();
		Ant a = new Ant(new AntColony(1, data));

		assertFalse(a.hasArc(1,2));

		a.visit(1);
		a.visit(2);

		assertTrue(a.hasArc(1,2));
	}

	@Test
	void getProbabilisticRandom() {
	}

	@Test
	void doStep() throws IOException {
		TSPLoader tspLoader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = tspLoader.load();
		Ant a = new Ant(new AntColony(1, data));

		assertFalse(a.hasArc(1,2));

		a.visit(1);
		a.doStep();
	}

	@Test
	void getVisitableNodes() {
	}

	@Test
	void getTabuList() {
	}

	@Test
	void getProb() {
	}

	@Test
	void calculateProb() {
	}

	@Test
	void getAntDecisionTable() {
	}

	@Test
	void pathLength() {
	}
}