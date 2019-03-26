package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.TSP;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPSolution;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.CompositeRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.candidators.NNCandidator;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.NearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.RandomNearestNeighbour;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco.AntColonyOptimization;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.SimulatedAnnealing;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.ThreeOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.TwoOpt;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.GeneticAlgorithm;
import it.denv.supsi.i3b.advalg.utils.GnuPlotUtils;
import it.denv.supsi.i3b.advalg.utils.RouteUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeedFinder {

	private static TSPData loadProblem(String pName) throws IOException {
		String filePath = Utils.getTestFile("/problems/" + pName + ".tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		return loader.load();
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
		for(IntermediateRoutingAlgorithm ira : alg.getIas()){
			os.write(",");
			printAlgorithm(os, ira);
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

	private static void printAlgorithm(OutputStreamWriter os, IntermediateRoutingAlgorithm ra) throws IOException {
		os.write("{\"name\": \"" + ra.getClass().getName() + "\"," +
				"\"seed\": " + ra.getSeed() + "}");
	}

	@Test
	public void ch130SA(int seed) throws IOException {
		TSPData data = loadProblem("ch130");

		File f = new File("/tmp/tsp-130-sa.json");

		OutputStreamWriter ob = new OutputStreamWriter(
				new BufferedOutputStream(new FileOutputStream(f, true)));
		runProblem(data,
					ob,
					(new CompositeRoutingAlgorithm())
					.startWith(new RandomNearestNeighbour(seed, data))
					.add(new TwoOpt(data))
					.add(new SimulatedAnnealing(seed))
		);
		ob.flush();
	}

	@Test
	public void ch130SA_SF() throws IOException, InterruptedException {
		ExecutorService exec =
				Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		for(int i=0; i<10000; i++) {
			int finalI = i;
			exec.submit(() -> {
				try {
					ch130SA(finalI);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		exec.awaitTermination(10, TimeUnit.HOURS);
	}


}
