package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

public class RoueUtils {
	public static void computePerformance(Route r, TSPData d){
		double perf = 1 - d.getBestKnown() * 1.0 / r.getLength();

		System.out.println(String.format(
				"Our path: %d \t Best: %d \t Perf: %f",
				r.getLength(),
				d.getBestKnown(),
				perf * 100
				)
		);
	}
}
