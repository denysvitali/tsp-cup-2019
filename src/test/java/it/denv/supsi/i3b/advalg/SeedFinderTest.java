package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.acs.AntColonySystem;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
					.replace("\n","");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private interface Thunk { void apply(int seed); }
	private interface ACSThunk { void apply(int seed,
											int nr_ants,
											double alpha,
											double beta,
											double pd, double pe,
											double q0); }

	private static TSPData loadProblem(String pName) throws IOException {
		String filePath = Utils.getTestFile("/problems/" + pName + ".tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData d =	loader.load();
		return d;
	}

	private static void runThreaded(Thunk f) {
		ExecutorService exec =
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		for(int i=0; i<10000; i++) {
			int finalI = i;
			exec.submit(() -> {
				f.apply(finalI);
			});
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

		for(int i=0; i<5; i++) {
			int seed = i;
			for(int nr_ants = 1; nr_ants<10; nr_ants++){
				// Ants

				for(double beta = 0.5; beta < 5.0; beta += 0.5){
					for(double pd = 0.0; pd < 1.0; pd+=0.1){
						for(double pe = 0.05; pe < 1; pe+= 0.1){
							for(double q0=0.1; q0<1; q0 += 0.1){
								int finalNr_ants = nr_ants;
								double finalAlpha = 1.0;
								double finalBeta = beta;
								double finalPd = pd;
								double finalPe = pe;
								double finalQ = q0;

								exec.submit(()->{
									f.apply(seed, finalNr_ants, finalAlpha,
											finalBeta,
											finalPd, finalPe, finalQ);
								});
							}
						}
					}
				}
			}
		}

		System.out.println("Starting ACS...");

		try {
			exec.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void runProblem(TSPData data, OutputStreamWriter os, CompositeRoutingAlgorithm alg) throws IOException {
		TSP tsp = new TSP();
		long time_before = System.nanoTime();
		Route r = tsp.run(data, alg);
		long time_after = System.nanoTime();

		os.write("{\"problem\": \"" + data.getName() + "\"," +
				"\"time_elapsed\": " + (time_after - time_before) + "," +
				"\"algorithms\": ["
		);

		printAlgorithm(os, alg.getSa());
		for(ILS ILS : alg.getIas()){
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
		os.write("{\"name\": \"" + sa.getClass().getName() + "\"," +
				"\"seed\": " + sa.getSeed() + "}");
	}

	private static void printAlgorithm(OutputStreamWriter os, ILS ra) throws IOException {
		os.write("{\"name\": \"" + ra.getClass().getName() + "\"," +
				"\"seed\": " + ra.getSeed() + "}");
	}

	@Test
	public static void ch130SA(int seed) {
		try {
			TSPData data = loadProblem("ch130");

			File f = new File("/tmp/tsp-130-sa_" + GIT_COMMIT + ".json");

			OutputStreamWriter ob = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(f, true)));
			runProblem(data,
					ob,
					(new CompositeRoutingAlgorithm())
							.startWith(new RandomNearestNeighbour(seed, data))
							.add(new TwoOpt(data))
							.add(new SimulatedAnnealing(seed)
									.setMode(SimulatedAnnealing.Mode.TwoOpt))
			);
			ob.flush();
		} catch(IOException ex){

		}
	}

	@Test
	public static void ch130SA3O(int seed) {
		try {
			TSPData data = loadProblem("ch130");

			File f = new File("/tmp/tsp-130-sa-3o_" + GIT_COMMIT + ".json");

			OutputStreamWriter ob = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(f, true)));
			runProblem(data,
					ob,
					(new CompositeRoutingAlgorithm())
							.startWith(new RandomNearestNeighbour(seed, data))
							.add(new TwoOpt(data))
							.add(new SimulatedAnnealing(seed)
									.setMode(SimulatedAnnealing.Mode.ThreeOpt))
			);
			ob.flush();
		} catch(IOException ex){

		}
	}

	@Test
	public static void u1060SA(int seed) {
		try {
			TSPData data = loadProblem("u1060");

			File f = new File("/tmp/tsp-u1060-sa_" + GIT_COMMIT + ".json");

			OutputStreamWriter ob = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(f, true)));
			runProblem(data,
					ob,
					(new CompositeRoutingAlgorithm())
							.startWith(new RandomNearestNeighbour(seed, data))
							.add(new TwoOpt(data))
							.add(new SimulatedAnnealing(seed)
									.setMode(SimulatedAnnealing.Mode.TwoOpt))
			);
			ob.flush();
		} catch(IOException ex){

		}
	}

	@Test
	public static void u1060_ACS(int seed,
								 int amount_ants,
								 double alpha, double beta,
								 double pd, double pe, double q0) {
		try {
			TSPData data = loadProblem("u1060");

			File f = new File("/tmp/tsp-u1060-acs_" + GIT_COMMIT + ".json");

			AntColonySystem acs =
					new AntColonySystem(seed, amount_ants, data,
							alpha, beta, pd, pe, q0);

			OutputStreamWriter ob = new OutputStreamWriter(
					new BufferedOutputStream(new FileOutputStream(f, true)));
			runProblem(data,
					ob,
					(new CompositeRoutingAlgorithm())
							.startWith(acs)
			);
			ob.flush();
		} catch(IOException ex){

		}
	}

	@Test
	public void ch130SA_SF() {
		runThreaded(SeedFinderTest::ch130SA);
	}

	@Test
	public void u1060SA_SF() {
		runThreaded(SeedFinderTest::u1060SA);
	}

	@Test
	public void ch130SA3O_SF() {
		runThreaded(SeedFinderTest::ch130SA3O);
	}

	@Test
	public void u1060_ACS_SF() {
		runACSThreaded(SeedFinderTest::u1060_ACS);
	}

}
