package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileParsing {
	private static String getTestFile(String fileName){
		return FileParsing.class.getResource(fileName).getFile();
	}

	@Test
	public void parseTest1() throws IOException {
		String filePath = FileParsing.getTestFile("/problems/ch130.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();
	}

	@Test
	public void parseTest2() throws IOException {
		String filePath = FileParsing.getTestFile("/problems/d198.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();
	}
}
