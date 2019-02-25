package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPSolution;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ga.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.nn.rnn.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.to.TwoOpt;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

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
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour())
						.add(new TwoOpt())
						//.add(new GeneticAlgorithm())
		);

		double v = r.compareTo(data.getBestKnown());

		String path = tsp.writeRoute(r);

		TSPSolution sol = new TSPSolution(data, r);
		File sol_f = File.createTempFile("sol_", ".opt.tour");
		sol.write(sol_f);

		System.out.println("Length: " + r.getLength() + ", V: " + v);

		checkTour(filePath, sol_f, r.getLength());

		GnuPlotUtils.plot(path);
	}

	@Test
	public void c1() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/c1.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
				.add(new TwoOpt())
				//.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		TSPSolution sol = new TSPSolution(data, r);
		/*File sol_f = File.createTempFile("sol_", ".opt.tour");
		sol.write(sol_f);*/

		//checkTour(filePath, sol_f, r.getLength());

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);
	}

	private void checkTour(String filePath, File sol_f, int length) throws IOException {
		Process p = Runtime.getRuntime().exec(new String[]{
				"python",
				getTestFile("/tourCheckv3.py"),
				filePath,
				sol_f.getPath(),
				String.valueOf(length)
		});

		p.onExit().thenRun(()->{
			Scanner sc = new Scanner(
					p.getInputStream()
			);

			String result = sc.nextLine();
			assertEquals("Tour is correct", result);
		}).join();
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
					.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		System.out.println("Route length: " + r.getLength());

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76_1() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new TwoOpt())
						//.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		System.out.println("Route length: " + r.getLength());

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void d198() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/d198.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//Route r = tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void eil76() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void lin318() throws IOException {
		String filePath = TSPRunner.getTestFile("/problems/lin318.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//tsp.run(data, new GeneticAlgorithm());
	}


}
