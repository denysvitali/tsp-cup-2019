package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPSolution;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.ACSParams;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.ThreeOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class TSPRunnerTest {

	private static TSPData getProblemData(String problem) throws IOException {
		String filePath = Utils
				.getTestFile("/problems/" + problem + ".tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		return loader.load();
	}

	private static void validateResult(TSP tsp, Route r, TSPData data) throws IOException {
		int[] path = r.getPath();
		assertEquals(path[0], path[path.length - 1]);

		double v = r.compareTo(data.getBestKnown());
		String plotPath = tsp.writeRoute(r);

		TSPSolution sol = new TSPSolution(data, r);
		File sol_f = File.createTempFile("sol_", ".opt.tour");
		sol.write(sol_f);

		System.out.println("Length: " + r.getLength() + ", V: " + v);
		GnuPlotUtils.plot(plotPath);
		Utils.checkTour(data.getFilePath(), sol_f, r.getLength());
	}


	private static void ch130_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("ch130");
		TSP tsp = new TSP(20);
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs.setSolutionImprover(new TwoOpt(data)))
		);

		System.out.println("Seed is " + acs.getSeed());

		validateResult(tsp, r, data);
	}

	@Test
	public void d198_OLD() throws IOException {
		d198_ACS(-1, 4);
		//d198_SA(-1);
	}

	private static void d198_SA(int seed) throws IOException {
		TSPData data = getProblemData("d198");
		TSP tsp = new TSP(50);
		tsp.init(data);

		SimulatedAnnealing sa;

		if(seed == -1){
			sa = new SimulatedAnnealing();
		} else {
			sa = new SimulatedAnnealing(seed);
		}

		sa.setMode(SimulatedAnnealing.Mode.DoubleBridge);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
				.add(sa)
		);

		System.out.println("Seed is " + sa.getSeed());

		validateResult(tsp, r, data);
	}

	private static void d198_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("d198");
		TSP tsp = new TSP(50);
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs
								.setSolutionImprover(new TwoOpt(data)))
		);

		System.out.println("Seed is " + acs.getSeed());

		validateResult(tsp, r, data);
	}

	@Test
	public void eil76_OLD() throws IOException {
		eil76_ACS(1, 8);
	}

	private static void eil76_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("eil76");
		TSP tsp = new TSP(20);
		tsp.init(data);

		TwoOpt opt = new TwoOpt(data);
		opt.setCandidate(true);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new AntColonySystem(seed, amount, data)
								.setSolutionImprover(opt))
		);

		validateResult(tsp, r, data);
	}

	@Test
	public void fl1577_OLD() throws IOException {
		//fl1577_ACS(-1, 3);
		fl1577_SA(-1);
	}

	private static void fl1577_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("fl1577");
		TSP tsp = new TSP((int) (0.15 * data.getDim()));
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs.setSolutionImprover(new TwoOpt(data)))
		);

		System.out.println("Seed is " + acs.getSeed());

		validateResult(tsp, r, data);
	}

	private static void fl1577_SA(int seed) throws IOException {
		TSPData data = getProblemData("fl1577");
		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);

		if(seed == -1){
			seed = new Random().nextInt();
		}


		System.out.println("Seed is " + seed);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
						.setMode(SimulatedAnnealing.Mode.TwoOpt))
		);

		System.out.println("Seed is " + seed);


		validateResult(tsp, r, data);
	}

	@Test
	public void kroA100_OLD() throws IOException {
		//kroA100_SA(1);
		kroA100_ACS(1, 3);
	}

	private static void kroA100_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("kroA100");
		TSP tsp = new TSP(50);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new AntColonySystem(seed, amount, data)
								.setSolutionImprover(new TwoOpt(data)))
		);

		validateResult(tsp, r, data);
	}

	private static void kroA100_SA(int seed) throws IOException {
		TSPData data = getProblemData("kroA100");
		TSP tsp = new TSP(50);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
						.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		validateResult(tsp, r, data);
	}

	@Test
	public void lin318_OLD() throws IOException {
		lin318_SA(1);
		//lin318_ACS(-1, 3);
	}

	protected Route lin318_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("lin318");
		TSP tsp = new TSP(50);
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs.setSolutionImprover(new TwoOpt(data)))
		);

		System.out.println("Seed is " + acs.getSeed());

		validateResult(tsp, r, data);
		return r;
	}

	private static void lin318_SA(int seed) throws IOException {
		TSPData data = getProblemData("lin318");
		TSP tsp = new TSP(50);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
						.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		validateResult(tsp, r, data);
	}

	@Test
	public void pcb442_OLD() throws IOException {
		//pcb442_ACS(3, 3);
		pcb442_SA(1);
	}

	private static void pcb442_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("pcb442");
		TSP tsp = new TSP(80);
		tsp.init(data);

		TwoOpt si = new TwoOpt(data);
		si.setCandidate(true);

		AntColonySystem acs = new AntColonySystem(seed, amount, data);
		acs.setSolutionImprover(si);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs)
		);

		validateResult(tsp, r, data);
	}

	private static void pcb442_SA(int seed) throws IOException {
		TSPData data = getProblemData("pcb442");
		TSP tsp = new TSP(10);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
						.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		validateResult(tsp, r, data);
	}


	@Test
	public void pr439_OLD() throws IOException {
		pr439_ACS(-1,12);
		//pr439_SA(1);
	}

	private static void pr439_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("pr439");
		TSP tsp = new TSP(100);
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs.setSolutionImprover(new TwoOpt(data)))
		);


		System.out.println("Seed is " + acs.getSeed());
		validateResult(tsp, r, data);
	}

	private static void pr439_SA(int seed) throws IOException {
		TSPData data = getProblemData("pr439");
		TSP tsp = new TSP(100);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
								.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		validateResult(tsp, r, data);
	}

	@Test
	public void rat783_OLD() throws IOException {
		rat783_ACS(-1,3);
		//rat783_SA(1);
	}

	private static void rat783_ACS(int seed, int amount) throws IOException {
		TSPData data = getProblemData("rat783");
		TSP tsp = new TSP(150);
		tsp.init(data);

		AntColonySystem acs;
		if(seed == -1){
			acs = new AntColonySystem(amount, data);
		} else {
			acs = new AntColonySystem(seed, amount, data);
		}

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(acs.setSolutionImprover(new TwoOpt(data)))
		);


		System.out.println("Seed is " + acs.getSeed());
		validateResult(tsp, r, data);
	}

	private static void rat783_SA(int seed) throws IOException {
		TSPData data = getProblemData("rat783");
		TSP tsp = new TSP(150);
		tsp.init(data);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
								.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		validateResult(tsp, r, data);
	}
	//////////////////////////////////////////////////////

	@Test
	public void c1() throws IOException {
		String filePath = Utils.getTestFile("/problems/c1.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP(2);
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

		TSP tsp = new TSP(20);
		TwoOpt twoOpt = new TwoOpt(data);
		twoOpt.setCandidate(true);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(twoOpt)
		);

		String path = tsp.writeRoute(r);

		assertTrue(r.isValid());
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());

		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76NN() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP(20);
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

		TSP tsp = new TSP(20);
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

		TSP tsp = new TSP(20);
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

		TSP tsp = new TSP(80);
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

		TSP tsp = new TSP(20);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing())
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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


	////////////////////////////// ACO /////////////////////////////////////////

	@Test
	public void ch130ACO() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(
								new AntColonySystem(
										9, data)
										.setSolutionImprover(new TwoOpt(data)))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void eil76ACS() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
					.startWith(
						new AntColonySystem(15, 3, data)
								.setSolutionImprover(new TwoOpt(data))
					)
				.add(new TwoOpt(data))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060ACS() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);
		ACSParams params = new ACSParams();

		int seed = new Random().nextInt();

		System.out.println("Seed is " + seed);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(
								new AntColonySystem(
										params,
										seed,
										3, data)
										.setSolutionImprover(new TwoOpt(data)))
				.add(new TwoOpt(data))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Seed is " + seed);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	public static void _fl1577() throws IOException {
		String filePath = Utils.getTestFile("/problems/fl1577.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(
								new AntColonySystem(
										4, data)
										.setSolutionImprover(new TwoOpt(data)))
						.add(new TwoOpt(data))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	@Test
	public void u1060ACS_SI2OPT() throws IOException {
		String filePath = Utils.getTestFile("/problems/u1060.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		tsp.init(data);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
					.startWith(
					new AntColonySystem(
					3, data)
					.setSolutionImprover(new TwoOpt(data)))
		);

		String path = tsp.writeRoute(r);
		GnuPlotUtils.plot(path);
		System.out.println("Route length: " + r.getLength());
		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	///////////////////////////////////////////////////////////////////////////

	@Test
	public void eil76GA() throws IOException {
		String filePath = Utils.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new GeneticAlgorithm(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new GeneticAlgorithm(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new SimulatedAnnealing()
								.setMode(SimulatedAnnealing.Mode.TwoOpt))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing()
						.setMode(SimulatedAnnealing.Mode.DoubleBridge))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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
	public void ch130GA() throws IOException {
		String filePath = Utils.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm(data))
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

		int seed = (new Random()).nextInt();

		System.out.println("Seed is " + seed);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP(10);
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing(seed)
								.setMode(SimulatedAnnealing.Mode.RAND_CHOICE))
		);

		String path = tsp.writeRoute(r);
		TSPSolution sol = new TSPSolution(data, r);
		File sol_f = File.createTempFile("sol_", ".opt.tour");
		sol.write(sol_f);

		System.out.println("Length: " + r.getLength());
		Utils.checkTour(filePath, sol_f, r.getLength());
		GnuPlotUtils.plot(path);


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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new ThreeOpt(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new GeneticAlgorithm(data))
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
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

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new NearestNeighbour(data))
						.add(new TwoOpt(data))
						.add(new SimulatedAnnealing()
						.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}


	@Test
	public void lin318SA_2O() throws IOException {
		String filePath = Utils.getTestFile("/problems/lin318.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		TSP tsp = new TSP((int) (data.getDim() * 0.15));
		TwoOpt candidateLessTwoOpt = new TwoOpt(data);
		candidateLessTwoOpt.setCandidate(false);

		Route r = tsp.run(data,
				(new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(data))
						.add(candidateLessTwoOpt)
						.add(new SimulatedAnnealing()
								.setMode(SimulatedAnnealing.Mode.DoubleBridge))
		);

		String path = tsp.writeRoute(r);

		System.out.println(
				GnuPlotUtils.getPlotCommand(path)
		);

		RouteUtils.computePerformance(r, data);
		assertTrue(r.getLength() >= data.getBestKnown());
	}


	/* Test w/ Seeds */
	private static TSPSolution runProblem(TSP tsp,
										  TSPData data,
										  CompositeRoutingAlgorithm alg) {
		long time_before = System.nanoTime();
		Route r = tsp.run(data, alg);
		long time_after = System.nanoTime();
		assertTrue(r.getLength() >= data.getBestKnown());
		System.out.println(data.getName() + " (" + r.getLength() + " / " +
				data.getBestKnown() + ") - took " + (time_after - time_before) + "ns");

		return new TSPSolution(data, r);
	}


	private static TSPSolution runR2S2(String problem, SimulatedAnnealing.Mode mode,
			  int seed,
			  double alpha,
			  int r, double s_temp) {
			try {
				TSPData data = Utils.loadProblem(problem);
				TSP tsp = new TSP((int) (data.getDim() * 0.15));
				tsp.init(data);

				SimulatedAnnealing sa = new SimulatedAnnealing(seed);
				sa.setAlpha(alpha);
				sa.setStartTemp(s_temp);
				sa.setR(r);
				sa.setMode(mode);

				CompositeRoutingAlgorithm cra = (new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new TwoOpt(data))
						.add(sa)
						.add(new TwoOpt(data));


				return runProblem(tsp, data, cra);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			return null;
	}

	private static void writeTestResult(TSPSolution sol) {

		if(sol == null){
			System.err.println("Got a null result!");
			return;
		}

		try {
			sol.write(new File("results/" + sol.getName() + ".opt.tour"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ch130

	public static void ch130() {
		writeTestResult(runR2S2("ch130",SimulatedAnnealing.Mode.RAND_CHOICE,-2129375319,0.982,400,80.5));
	}

	// d198

	public static void d198() {
		writeTestResult(runR2S2("d198",SimulatedAnnealing.Mode.RAND_CHOICE,-796751895,0.961,100,80.0));
	}

	// eil76

	public static void eil76() {
		writeTestResult(runR2S2("eil76",SimulatedAnnealing.Mode.RAND_CHOICE,-1460045113,0.996,100,134.0));
	}

	// fl1577

	public static void fl1577() {
		writeTestResult(runR2S2("fl1577",SimulatedAnnealing.Mode.RAND_CHOICE,28586255,0.9,100,200.0));
	}

	// kroA100

	public static void kroA100() {
		writeTestResult(runR2S2("kroA100",SimulatedAnnealing.Mode.RAND_CHOICE,916773322,0.999,100,300));
	}

	// lin318

	public static void lin318() {
		writeTestResult(runR2S2("lin318",SimulatedAnnealing.Mode.RAND_CHOICE,-357553184,0.951,100,100));
	}

	// pcb442

	public static void pcb442() {
		writeTestResult(runR2S2("pcb442",SimulatedAnnealing.Mode.RAND_CHOICE,2079608716,0.954,100,80.0));
	}

	// pr439

	public static void pr439() {
		runR2S2("pr439",SimulatedAnnealing.Mode.RAND_CHOICE,-729289858,0.952,100,100);
	}

	// rat783

	public static void rat783() {
		writeTestResult(runR2S2("rat783",SimulatedAnnealing.Mode.RAND_CHOICE,1032714840,0.956,100,80.5));
	}

	// u1060

	public static void u1060() {
		writeTestResult(runR2S2("u1060",SimulatedAnnealing.Mode.RAND_CHOICE,269590158,0.978,100,80.0));
	}



}
