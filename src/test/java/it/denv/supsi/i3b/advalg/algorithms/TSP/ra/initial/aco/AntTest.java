package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Utils;
import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.ACSParams;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;


class AntTest {

	@Test
	void visit() throws IOException {
		TSPLoader tspLoader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = tspLoader.load();
		Ant a = new Ant(new AntColony(AcoType.ACS, ACSParams.getInstance(),
				1, data));
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
		TSP tsp = new TSP(20);
		tsp.init(data);
		Ant a = new Ant(new AntColony(AcoType.ACS, ACSParams.getInstance(),
				0, 1, data));
		assertEquals(1, a.getVisited());
		//assertEquals(24, a.getTabuList().get(0));

		a.step();
		//assertEquals(2, a.getTabuList().size());
		//assertEquals(12, a.getTabuList().get(1));
	}

	@Test
	void testProbCity() {
		double[] probList = new double[]{0.0, 1.0};
		Random r = new Random(1);

		int c = Ant.getRandomCityByProb(r, probList);
		assertEquals(1, c);
	}
}