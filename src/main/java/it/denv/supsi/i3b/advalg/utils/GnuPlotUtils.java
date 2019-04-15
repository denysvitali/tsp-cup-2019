package it.denv.supsi.i3b.advalg.utils;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABCycle;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.ABEdge;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.intermediate.genetic.eax.EAXGraph;

import java.io.*;
import java.util.*;
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

	private static void gnuplotExecute(String cmd) throws IOException {
		Process p = Runtime.getRuntime().exec(
				new String[]{
						"gnuplot",
						"-p",
						"-e",
						cmd
				}
		);
		p.onExit().join();
	}

	private static void eaxPlot(String aPath, String bPath) throws IOException {
		gnuplotExecute(getEAXPElotCommand(aPath, bPath));
	}

	private static String getEAXPElotCommand(String aPath, String bPath) {
		return String.format("plot \"%1$s\" using 1:2 " +
				"with points pointtype 5, " +
				"\"%1$s\" using 1:2 with l t \"A\" lt rgb \"red\", " +
				"\"%2$s\" using 1:2 with l t \"B\" lt rgb \"yellow\"", aPath
		, bPath);
	}

	public static void plotABCycles(ABCycle[] cycles, TSPData data) {
		try {
			String[] color = new String[]{
				"f44336",
				"E91E63",
				"9C27B0",
				"673AB7",
				"3F51B5",
				"2196F3",
				"4CAF50",
				"CDDC39",
				"FF9800",
				"795548"
			};

			ArrayList<Coordinate> coords = new ArrayList<>(data.getCoordinates()
					.values());

			StringBuilder sb = new StringBuilder();
			sb.append("plot ");

			for (int i = 0; i < cycles.length; i++) {
				if(i != 0){
					sb.append(", ");
				}
				ABCycle currCycle = cycles[i];
				String f = File
						.createTempFile("tsp-plot-abc", ".dat")
						.getPath();

				Set<Integer> cities = new HashSet<>();

				for(ABEdge e : currCycle.getPath()) {
					cities.add(e.getU());
					cities.add(e.getV());
				}

				OutputStreamWriter os = new OutputStreamWriter(
						new FileOutputStream(f));

				Coordinate first = null;
				for(Integer c : cities) {
					Coordinate coord = coords.get(c);
					if(first == null){
						first = coord;
					}
					os.write(String.format("%f %f\n", coord.getX(), coord.getY()));
				}

				if(first != null) {
					os.write(String.format("%f %f\n", first.getX(), first.getY()));
				}
				os.flush();
				sb.append("\"")
						.append(f)
						.append("\" using 1:2 with l t \"")
						.append(i)
						.append("\" lt rgb \"#")
						.append(color[i]).append("\"");
			}

			gnuplotExecute(sb.toString());
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
