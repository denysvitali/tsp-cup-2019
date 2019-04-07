package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.ILS;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
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

	private interface Thunk { void apply(int seed); };

	private static TSPData loadProblem(String pName) throws IOException {
		String filePath = Utils.getTestFile("/problems/" + pName + ".tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		return loader.load();
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
									.setMode(SimulatedAnnealing.Mode.DoubleBridge))
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


}
