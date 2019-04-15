package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABEdge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.EAXGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class GnuPlotUtils {
	public static String getPlotCommand(String path){
		return String.format("plot \"%1$s\" using 1:2 " +
				"with points pointtype 5, " +
				"\"%1$s\" using 1:2 with l t \"S1\"", path);
	}

	public static void plot(String path) throws IOException {
		Process p = Runtime.getRuntime().exec(
				new String[]{
						"gnuplot",
						"-p",
						"-e",
						getPlotCommand(path)
				}
		);

		p.onExit().join();
	}

	public static void plotEAXGraph(EAXGraph g, TSPData d) {

		try {
			String aPath = File.createTempFile("tsp-eax-", "-a.dat").getPath();
			String bPath = File.createTempFile("tsp-eax-", "-b.dat").getPath();

			OutputStreamWriter aFos = new OutputStreamWriter(new FileOutputStream(aPath));
			OutputStreamWriter bFos = new OutputStreamWriter(new FileOutputStream(bPath));

			ArrayList<Coordinate> coords = new ArrayList<>(d.getCoordinates().values());

			for (ABEdge e : g.getEdges()) {
				Coordinate u = coords.get(e.getU());
				Coordinate v = coords.get(e.getV());

				if (e.isA()) {
					aFos.write(String.format("%f %f\n", u.getX(), u.getY()));
					aFos.write(String.format("%f %f\n", v.getX(), v.getY()));
				} else {
					bFos.write(String.format("%f %f\n", u.getX(), u.getY()));
					bFos.write(String.format("%f %f\n", v.getX(), v.getY()));
				}
			}

			aFos.flush();
			aFos.close();

			bFos.flush();
			bFos.close();

			System.out.println(aPath);
			System.out.println(bPath);

			eaxPlot(aPath, bPath);
		} catch(IOException ex){
			// Ignored.
			ex.printStackTrace();
		}
		throw new RuntimeException("OK");
	}

	private static void eaxPlot(String aPath, String bPath) throws IOException {
		Process p = Runtime.getRuntime().exec(
				new String[]{
						"gnuplot",
						"-p",
						"-e",
						getEAXPElotCommand(aPath, bPath)
				}
		);

		System.out.println(getEAXPElotCommand(aPath, bPath));

		p.onExit().join();
	}

	private static String getEAXPElotCommand(String aPath, String bPath) {
		return String.format("plot \"%1$s\" using 1:2 " +
				"with points pointtype 5, " +
				"\"%1$s\" using 1:2 with l t \"A\" lt rgb \"red\", " +
				"\"%2$s\" using 1:2 with l t \"B\" lt rgb \"yellow\"", aPath
		, bPath);
	}
}
