package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.Utils;
import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.ra.TestShuffler;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

class TwoOptTest {

	@Test
	public void testTwoOptRoute() throws IOException {
		TSPLoader loader = new TSPLoader(
				Utils.getTestFile("/problems/eil76.tsp"));
		TSPData data = loader.load();
		TSP tsp = new TSP(20);

		Route r2 = tsp.run(data, new CompositeRoutingAlgorithm()
				.startWith(
					new TestShuffler(5)
				)
				.add(
					new TwoOpt(data)
				)
		);
		assertNotEquals(-1, r2.getLength());
		assertEquals(565, r2.getLength());

		assertEquals(data.getDim()+1, r2.getPath().length);
		assertFalse(r2.getLength() < data.getBestKnown());


		RouteUtils.computePerformance(r2, data);

	}
}