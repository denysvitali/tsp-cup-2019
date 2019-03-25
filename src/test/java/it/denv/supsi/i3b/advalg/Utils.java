package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Utils {
	public static String getTestFile(String fileName){
		return TSPRunner.class.getResource(fileName).getFile();
	}

	public static void checkTour(String filePath, File sol_f, int length) throws IOException {
		Process p = Runtime.getRuntime().exec(new String[]{
				"python",
				Utils.getTestFile("/tourCheckv3.py"),
				filePath,
				sol_f.getPath(),
				String.valueOf(length)
		});

		p.onExit().thenRun(()->{
			Scanner sc = new Scanner(
					p.getInputStream()
			);

			String result = sc.nextLine();
			assertEquals("Tour is correct", result);
		}).join();
	}
}
