package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Utils;
import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
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
		Ant a = new Ant(new AntColony(AcoType.ACS , 1, data));
		//assertEquals(0, a.getTabuList().size());

		a.visit(1);
		//assertEquals(1, a.getTabuList().size());
		//assertEquals(1, a.getTabuList().get(0));
	}

	@Test
	void step() throws IOException {
		TSPLoader tspLoader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = tspLoader.load();
		TSP tsp = new TSP();
		tsp.init(data);
		Ant a = new Ant(new AntColony(AcoType.ACS , 0, 1, data));
		assertEquals(1, a.getVisited());
		//assertEquals(24, a.getTabuList().get(0));

		a.step();
		//assertEquals(2, a.getTabuList().size());
		//assertEquals(12, a.getTabuList().get(1));
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