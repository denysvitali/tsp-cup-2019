package it.denv.supsi.i3b.advalg.utils;

import java.io.IOException;

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
}
