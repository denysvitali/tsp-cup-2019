package it.denv.supsi.i3b.advalg;

import it.denv.supsi.i3b.advalg.algorithms.EdgeWeightType;
import it.denv.supsi.i3b.advalg.algorithms.ProblemType;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

		assertEquals("ch130", data.getName());
		assertNotNull(data.getCoordinates());
		assertEquals(130, data.getDimension());
		assertEquals(6110, data.getBestKnown());
		assertEquals(ProblemType.TSP, data.getType());
		assertEquals(EdgeWeightType.EUC_2D, data.getEwt());
		assertEquals("130 city problem (Churritz)", data.getComment());
		assertEquals(data.getDimension(), data.getCoordinates().size());

		assertEquals(209.1887938487, data.getCoordinates().get(8).getX());
		assertEquals(691.0262291948, data.getCoordinates().get(8).getY());
	}

	@Test
	public void parseTest2() throws IOException {
		String filePath = FileParsing.getTestFile("/problems/d198.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		assertEquals("d198", data.getName());
		assertNotNull(data.getCoordinates());
		assertEquals(198, data.getDimension());
		assertEquals(15780, data.getBestKnown());
		assertEquals(ProblemType.TSP, data.getType());
		assertEquals("Drilling problem (Reinelt)", data.getComment());
		assertEquals(data.getDimension(), data.getCoordinates().size());

		assertEquals(551.2, data.getCoordinates().get(1).getX());
		assertEquals(996.4, data.getCoordinates().get(1).getY());
	}

	@Test
	public void parseTest3() throws IOException {
		String filePath = FileParsing.getTestFile("/problems/eil76.tsp");
		assertNotNull(filePath);

		TSPLoader loader = new TSPLoader(filePath);
		TSPData data = loader.load();

		assertEquals("eil76", data.getName());
		assertNotNull(data.getCoordinates());
		assertEquals(76, data.getDimension());
		assertEquals(538, data.getBestKnown());
		assertEquals(ProblemType.TSP, data.getType());
		assertEquals("76-city problem (Christofides/Eilon)", data.getComment());
		assertEquals(data.getDimension(), data.getCoordinates().size());

		assertEquals(36, data.getCoordinates().get(1).getX());
		assertEquals(26, data.getCoordinates().get(1).getY());
	}
}
