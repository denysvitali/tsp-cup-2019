package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.rnn.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to.TwoOpt;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TSPRunner {
	private static String getTestFile(String fileName){
		return TSPRunner.class.getResource(fileName).getFile();
	}

	@Test
	public void ch130() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data, new GeneticAlgorithm());
		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);
	}

	@Test
	public void ch130TwoOpt() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
			(new CompositeRoutingAlgorithm())
					.startWith(new RandomNearestNeighbour())
					.add(new TwoOpt())
			);
		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);
	}

	@Test
	public void d198() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/d198.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void eil76() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void lin318() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/lin318.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		tsp.run(data, new GeneticAlgorithm());
	}


}
