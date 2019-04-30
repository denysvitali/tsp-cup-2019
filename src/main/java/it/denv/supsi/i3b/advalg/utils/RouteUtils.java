package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.SwappablePath;

public class RouteUtils {
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

	public static void computePerformance(SwappablePath sp, TSPData d){
		double length = sp.calculateDistance(d);
		double perf = 1 - d.getBestKnown() * 1.0 / length;

		System.out.println(String.format(
			"Our path: %d \t Best: %d \t Perf: %f",
			(int) length,
			d.getBestKnown(),
			perf * 100
			)
		);
	}
}
