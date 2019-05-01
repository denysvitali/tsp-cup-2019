package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.ACOParams;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColony;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.ACSParams;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class SeedFinderTest {

	private static String GIT_COMMIT;

	static {
		try {
			InputStream is = Runtime.getRuntime().exec(new String[]{
					"git",
					"rev-parse",
					"--short",
					"HEAD"
			}).getInputStream();

			GIT_COMMIT = new String(is.readAllBytes(), StandardCharsets.UTF_8)
					.replace("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private interface Thunk {
		void apply(int seed);
	}

	private interface SAThunk {
		void apply(int seed, double alpha, int r, SimulatedAnnealing.Mode mode);
	}

	private interface ACSThunk {
		void apply(int seed, int nr_ants, ACSParams params);
	}

	private static TSPData loadProblem(String pName) throws IOException {
		String filePath = Utils.getTestFile("/problems/" + pName + ".tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData d = loader.load();
		return d;
	}

	private static void runThreaded(Thunk f) {
		ExecutorService exec =
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
			exec.submit(() -> f.apply(r.nextInt()));
		}

		try {
			exec.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void runSAThreaded(SAThunk f) {
		ExecutorService exec =
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			for(int iter = 100; iter < 500; iter += 100) {
				for (double alpha = 0.984; alpha <= 0.999; alpha += 0.010) {
					for (SimulatedAnnealing.Mode mode : SimulatedAnnealing.Mode.values()) {
						double finalAlpha = alpha;
						int finalI = i;
						int finalIter = iter;

						exec.submit(() -> f.apply(finalI, finalAlpha, finalIter, mode));
					}
				}
			}
		}

		try {
			exec.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void runACSThreaded(ACSThunk f) {
		ExecutorService exec =
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		Random r = new Random();
		for (int i = 0; i < 30; i++) {
			for (double q0 = 0.99; q0 > 0.2; q0 -= 0.1) {
				for (double beta = 2; beta < 10; beta += 0.1) {
					ACSParams a = new ACSParams();
					a.setBeta(beta);
					a.setQ0(q0);

					exec.submit(() -> f.apply(r.nextInt(), 3, a));
				}
			}
		}

		exec.shutdown();

		try {
			exec.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void runProblem(TSP tsp, TSPData data, OutputStreamWriter os, CompositeRoutingAlgorithm alg) throws IOException {
		long time_before = System.nanoTime();
		Route r = tsp.run(data, alg);
		long time_after = System.nanoTime();


		os.write("{\"problem\": \"" + data.getName() + "\"," +
				"\"time_elapsed\": " + (time_after - time_before) + "," +
				"\"algorithms\": ["
		);

		printAlgorithm(os, alg.getSa());
		for (ILS ILS : alg.getIas()) {
			os.write(",");
			printAlgorithm(os, ILS);
		}
		os.write("]");

		double perf = 1 - data.getBestKnown() * 1.0 / r.getLength();
		perf *= 100;

		os.write(",\"rl\": " + r.getLength() + ", \"bk\": " + data.getBestKnown()
				+ ", \"perf\": " + perf + "}");
		assertTrue(r.getLength() >= data.getBestKnown());
	}

	private static void printAlgorithm(OutputStreamWriter os, RoutingAlgorithm sa) throws IOException {
		if (sa instanceof AntColonySystem) {
			AntColonySystem acs = (AntColonySystem) sa;
			int m = acs.getAnts();
			int s = acs.getSeed();
			ACOParams p = acs.getParams();

			os.write("{\"name\": \"" + sa.getClass().getName() + "\"," +
					"\"seed\": " + s + ", \"ants\": " + m + ", \"params\":{" +
					"\"alpha\": " + p.getALPHA() + "," +
					"\"beta\": " + p.getBeta() + "," +
					"\"q0\": " + p.getQ0() + "," +
					"\"PD\": " + p.getPD() + "," +
					"\"PE\": " + p.getPE() + "}}");
		} else {
			os.write("{\"name\": \"" + sa.getClass().getName() + "\"," +
					"\"seed\": " + sa.getSeed() + "}");
		}
	}

	private static void printAlgorithm(OutputStreamWriter os, ILS ra) throws IOException {
		if(ra instanceof SimulatedAnnealing){
			SimulatedAnnealing sa = (SimulatedAnnealing) ra;

			os.write("{\"name\": \"" + sa.getClass().getName() + "\"," +
					"\"seed\": " + sa.getSeed() + ", " +
					"\"mode\": \"" + sa.getMode() + "\"," +
					"\"params\":{" +
					"\"alpha\": " + sa.getAlpha() + "," +
					"\"r\": " + sa.getR() + "," +
					"\"start_temperature\": " + sa.getStartTemp() + "}}");


		} else {
			os.write("{\"name\": \"" + ra.getClass().getName() + "\"," +
					"\"seed\": " + ra.getSeed() + "}");
		}
	}

	private static ACSThunk runACS(String problem) {
		return (seed, ants, acsParams) -> {
			try {
				TSPData data = loadProblem(problem);
				TSP tsp = new TSP();
				tsp.init(data);
				File f = new File("/tmp/tsp-" + problem + "-acs_" + GIT_COMMIT + ".json");

				OutputStreamWriter ob = new OutputStreamWriter(
						new BufferedOutputStream(new FileOutputStream(f, true)));
				CompositeRoutingAlgorithm cra =
						(new CompositeRoutingAlgorithm());


				System.out.println(String.format("Run ACS w/ %d ants, " +
								"ACSParams = %s, Seed = %d",
						ants, acsParams, seed));

				cra.startWith(new AntColonySystem(acsParams, seed, ants, data)
						.setSolutionImprover(new TwoOpt(data)));
				cra.add(new TwoOpt(data));

				runProblem(tsp, data, ob, cra);
				ob.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		};
	}


	private SAThunk runSA(String problem) {
		return (seed, alpha, r, mode) -> {
			try {
				TSP tsp = new TSP();
				TSPData data = loadProblem(problem);
				tsp.init(data);

				File f = new File("/tmp/tsp-" + problem + "-sa_" + GIT_COMMIT + ".json");

				OutputStreamWriter ob = new OutputStreamWriter(
						new BufferedOutputStream(new FileOutputStream(f, true)));
				CompositeRoutingAlgorithm cra = (new CompositeRoutingAlgorithm())
						.startWith(new RandomNearestNeighbour(seed, data))
						.add(new SimulatedAnnealing(seed).setMode(mode))
						.add(new TwoOpt(data));

				runProblem(tsp, data, ob, cra);
				ob.flush();
			} catch (IOException ex) {

			}
		};
	}

	// ch130

	@Test
	public void ch130ACS_SF() {
		runACSThreaded(runACS("ch130"));
	}

	@Test
	public void ch130SA_SF() {
		runSAThreaded(runSA("ch130"));
	}

	// d198

	@Test
	public void d198ACS_SF() {
		runACSThreaded(runACS("d198"));
	}

	@Test
	public void d198SA_SF() {
		runSAThreaded(runSA("d198"));
	}

	// eil76

	@Test
	public void eil76ACS_SF() {
		runACSThreaded(runACS("eil76"));
	}

	@Test
	public void eil76SA_SF() {
		runSAThreaded(runSA("eil76"));
	}

	// fl1577

	@Test
	public void fl1577ACS_SF() {
		runACSThreaded(runACS("fl1577"));
	}

	@Test
	public void fl1577SA_SF() {
		runSAThreaded(runSA("fl1577"));
	}

	// kroA100

	@Test
	public void kroA100ACS_SF() {
		runACSThreaded(runACS("kroA100"));
	}

	@Test
	public void kroA100SA_SF() {
		runSAThreaded(runSA("kroA100"));
	}

	// lin318

	@Test
	public void lin318ACS_SF() {
		runACSThreaded(runACS("lin318"));
	}

	@Test
	public void lin318SA_SF() {
		runSAThreaded(runSA("lin318"));
	}

	// pcb442

	@Test
	public void pcb442ACS_SF() {
		runACSThreaded(runACS("pcb442"));
	}

	@Test
	public void pcb442SA_SF() {
		runSAThreaded(runSA("pcb442"));
	}

	// pr439

	@Test
	public void pr439ACS_SF() {
		runACSThreaded(runACS("pr439"));
	}

	@Test
	public void pr439SA_SF() {
		runSAThreaded(runSA("pr439"));
	}

	// rat783

	@Test
	public void rat783ACS_SF() {
		runACSThreaded(runACS("rat783"));
	}

	@Test
	public void rat783SA_SF() {
		runSAThreaded(runSA("rat783"));
	}

	// u1060

	@Test
	public void u1060ACS_SF() {
		runACSThreaded(runACS("u1060"));
	}

	@Test
	public void u1060SA_SF() {
		runSAThreaded(runSA("u1060"));
	}


}
