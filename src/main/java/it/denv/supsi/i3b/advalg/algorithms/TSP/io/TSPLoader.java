package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.ProblemType;

import java.io.FileInputStream;
import java.io.IOException;

public class TSPLoader {
	private String path;
	private boolean loaded = false;
	private TSPData tspData = null;
	private FileInputStream fis = null;

	public TSPLoader(String filePath){
		this.path = filePath;
	}

	public TSPData load() throws IOException {
		if(!loaded){
			fis = new FileInputStream(path);
			tspData = parseFile();
			loaded = true;
		}

		return tspData;
	}

	private int parseTag(String tag) throws IOException {
		char[] type_tag = tag.toCharArray();
		int c = fis.read();
		for (char value : type_tag) {
			assert (c == value);
			c = fis.read();
		}

		return c;
	}

	private TSPData parseFile() throws IOException {
		TSPData data = new TSPData();
		int c = 0;

		// NAME Parsing

		data.name = getString(fis);
		assert(fis.read() == '\n');

		// TYPE Parsing
		c = parseTag("TYPE: ");
		data.type = ProblemType.valueOf(getString(fis));
		assert(c == '\n');

		// COMMENT parsing
		c = parseTag("COMMENT: ");
		data.comment = getString(fis);


		return data;
	}

	private String getString(FileInputStream fis) throws IOException {
		StringBuilder sb = new StringBuilder();
		int c = fis.read();
		while(c != '\n' && c != -1){
			sb.append((char) c);
			c = fis.read();
		}

		return sb.toString();
	}
}
