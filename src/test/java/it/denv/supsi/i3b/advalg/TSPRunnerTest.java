package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPSolution;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.NNCandidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.ThreeOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class TSPRunnerTest {

	@Test
	public void ch130() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
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
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
				//.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
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
						.startWith(new RandomNearestNeighbour(data))
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
	public void eil76NN() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76NN2Opt() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
		);

		assertTrue(r.isValid());

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76RNN() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
		);

		assertTrue(r.isValid());

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76RNN2OPT() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
		);

		assertTrue(r.isValid());

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76RNN2OPTGA() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		assertTrue(r.isValid());

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void pr439NNTwoOpt() throws IOException {
		String filePath = Utils.getTestFile("/problems/pr439.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data)
						)
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void pr439NNTwoOptGA() throws IOException {
		String filePath = Utils.getTestFile("/problems/pr439.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76_SA() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void pr439NN_SA() throws IOException {
		String filePath = Utils.getTestFile("/problems/pr439.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void fl1577_NN_SA() throws IOException {
		String filePath = Utils.getTestFile("/problems/fl1577.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}


	@Test
	public void u1060NNTwoOpt() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm()
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))));

		String path = tsp.writeRoute(r);

		GnuPlotUtils.plot(path);

		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76ACO() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		tsp.init(data);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(
								new AntColonySystem(
										data.getDimension(), data)
										.setSolutionImprover(new TwoOpt(data)))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76GA() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060GA() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060SA() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new SimulatedAnnealing().setMode(SimulatedAnnealing.Mode.TwoOpt))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
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
						.startWith(new RandomNearestNeighbour(data))
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
	public void ch130SimulatedAnnealing3Opt() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing()
								.setMode(SimulatedAnnealing.Mode.ThreeOpt))
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
						.startWith(new NearestNeighbour(data))
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
						.startWith(new RandomNearestNeighbour(data))
						.add(new ThreeOpt(data)
								.addCandidator(
										new NNCandidator(10, data)
								)
								.addCandidator(
										new NNCandidator(10, data)
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
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060_NN() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060_NN_SA() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP();
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing())
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		RouteUtils.computePerformance(r, data);
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
