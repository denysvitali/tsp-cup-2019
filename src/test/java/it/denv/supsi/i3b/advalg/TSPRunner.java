package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPSolution;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidate.NearestNeighborCandidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.ThreeOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TSPRunner {

	@Test
	public void ch130() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		double v = r.compareTo(data.getBestKnown());

		String path = tsp.writeRoute(r);

		TSPSolution sol = new TSPSolution(data, r);
		File sol_f = File.createTempFile("sol_", ".opt.tour");
		sol.write(sol_f);

		System.out.println("Length: " + r.getLength() + ", V: " + v);
		Utils.checkTour(filePath, sol_f, r.getLength());
		GnuPlotUtils.plot(path);
	}

	@Test
	public void c1() throws IOException {
		String filePath = Utils.getTestFile("/problems/c1.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
				.add(new TwoOpt(data))
				//.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		TSPSolution sol = new TSPSolution(data, r);

		GnuPlotUtils.plot(path);
	}

	@Test
	public void ch130TwoOpt() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
			(new CompositeRoutingAlgorithm())
					.startWith(new RandomNearestNeighbour())
					.add(new TwoOpt(data))
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
	public void ch130SimulatedAnnealing() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		System.out.println("Route length: " + r.getLength());

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void pcb442SimAn() throws IOException {
		String filePath = Utils.getTestFile("/problems/pcb442.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		System.out.println("Route length: " + r.getLength() + "\t"
				+ (1 - r.getLength() * 1.0 / data.getBestKnown()));

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void pcb442_3opt() throws IOException {
		String filePath = Utils.getTestFile("/problems/pcb442.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new ThreeOpt(data)
								.addCandidator(
										new NearestNeighborCandidator(10, data)
								)
								.addCandidator(
										new NearestNeighborCandidator(10, data)
								)
						)
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		System.out.println("Route length: " + r.getLength() + "\t"
				+ (1 - r.getLength() * 1.0 / data.getBestKnown()));

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76_1() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour())
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		Utils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void d198() throws IOException {
		String filePath = Utils.getTestFile("/problems/d198.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//Route r = tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void eil76() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//tsp.run(data, new GeneticAlgorithm());
	}

	@Test
	public void lin318() throws IOException {
		String filePath = Utils.getTestFile("/problems/lin318.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		//tsp.run(data, new GeneticAlgorithm());
	}


}
