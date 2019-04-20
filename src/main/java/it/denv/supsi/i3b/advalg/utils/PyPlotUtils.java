package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABCycle;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABEdge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.EAXGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PyPlotUtils {
	public static void plotABCycles(List<ABCycle> cycles, TSPData data) {
		try {
			ArrayList<Coordinate> coords = new ArrayList<>(data.getCoordinates()
					.values());
			ArrayList<String> plotFiles = new ArrayList<>();

			for (int i = 0; i < cycles.size(); i++) {
				String f = File
						.createTempFile("tsp-plot-abc", ".dat")
						.getPath();

				OutputStreamWriter os = new OutputStreamWriter(
						new FileOutputStream(f));

				for(ABEdge e : cycles.get(i).getPath()) {
					Coordinate coord = coords.get(e.getU());
					Coordinate coord2 = coords.get(e.getV());
					os.write(String.format("%f %f\n", coord.getX(), coord.getY()));
					os.write(String.format("%f %f\n\n", coord2.getX(), coord2.getY()));
				}
				os.flush();

				plotFiles.add(f);
			}

			for(String pf : plotFiles){
				String plotPy = PyPlotUtils.class.getResource("/scripts/plot.py").getPath();
				Process p = Runtime.getRuntime().exec(
						new String[]{
								"python",
								plotPy,
								pf
						}
				);

				InputStream err = p.getErrorStream();

				p.onExit().join();

				String errStr = new String(err.readAllBytes());
				System.out.println(errStr);
			}

		} catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public static void plotABCycle(ABCycle intermediateSol, TSPData data) {
		ArrayList<ABCycle> cycles = new ArrayList<>();
		cycles.add(intermediateSol);
		plotABCycles(cycles, data);
	}
}
