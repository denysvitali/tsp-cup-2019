package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.EdgeWeightType;
import it.denv.supsi.i3b.advalg.algorithms.ProblemType;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TSPLoader {
	private String path;
	private boolean loaded = false;
	private TSPData tspData = null;
	private BufferedInputStream fis = null;

	public TSPLoader(String filePath){
		this.path = filePath;
	}

	public TSPData load() throws IOException {
		if(!loaded){
			fis = new BufferedInputStream(new FileInputStream(path));
			tspData = parseFile();
			loaded = true;
		}

		return tspData;
	}

	private void parseTag(String tag) throws IOException {
		char[] type_tag = tag.toCharArray();
		fis.mark(1);
		int c = fis.read();
		for (char value : type_tag) {
			assert (c == value);
			fis.mark(1);
			c = fis.read();
		}

		fis.reset();
	}

	private TSPData parseFile() throws IOException {
		TSPData data = new TSPData();

		int c = -2;

		// NAME Parsing
		parseTag("NAME: ");
		data.name = getString(fis);
		assert(fis.read() == '\n');

		// TYPE Parsing
		parseTag("TYPE: ");
		data.type = ProblemType.valueOf(getString(fis));
		assert(fis.read() == '\n');

		// COMMENT parsing
		parseTag("COMMENT: ");
		data.comment = getString(fis);
		assert(fis.read() == '\n');

		parseTag("DIMENSION: ");
		data.dimension = getInt(fis);
		assert(fis.read() == '\n');

		parseTag("EDGE_WEIGHT_TYPE: ");
		data.ewt = EdgeWeightType.valueOf(
				getString(fis)
		);
		assert(fis.read() == '\n');

		parseTag("BEST_KNOWN :");



		return data;
	}

	private String getString(BufferedInputStream fis) throws IOException {
		StringBuilder sb = new StringBuilder();
		fis.mark(1);
		int c = fis.read();
		while(c != '\n' && c != -1){
			sb.append((char) c);
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
		return sb.toString();
	}

	private static boolean isNumeric(int c){
		return c >= '0' && c <= '9';
	}

	private int getInt(BufferedInputStream fis) throws IOException {
		StringBuilder sb = new StringBuilder();
		fis.mark(1);
		int c = fis.read();
		while(isNumeric(c)) {
			sb.append((char) c);
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
		return Integer.valueOf(sb.toString());
	}
}
