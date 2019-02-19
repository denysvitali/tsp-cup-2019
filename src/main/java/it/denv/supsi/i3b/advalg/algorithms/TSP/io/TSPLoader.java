package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
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

	private void parseKVTag(String tag) throws IOException {
		parseTag(tag);
		skipWhiteSpace(fis);
		assert(fis.read() == ':');
		skipWhiteSpace(fis);
	}

	private boolean parseTag(String tag) throws IOException {
		char[] type_tag = tag.toCharArray();
		fis.mark(1);
		int c = fis.read();
		for (char value : type_tag) {
			if(c != value){
				fis.reset();
				return false;
			}
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
		return true;
	}

	private void skipWhiteSpace(BufferedInputStream fis) throws IOException {
		fis.mark(1);
		int c = fis.read();
		while(c == ' '){
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
	}

	private TSPData parseFile() throws IOException {
		TSPData data = new TSPData();

		int c = -2;

		// KV Section
		

		// NAME Parsing
		parseKVTag("NAME");
		data.name = getString(fis);
		assert(fis.read() == '\n');

		// TYPE Parsing
		parseKVTag("TYPE");
		data.type = ProblemType.valueOf(getString(fis));
		assert(fis.read() == '\n');

		// COMMENT parsing
		parseKVTag("COMMENT");
		data.comment = getString(fis);
		assert(fis.read() == '\n');

		parseKVTag("DIMENSION");
		data.dimension = getInt(fis);
		assert(fis.read() == '\n');

		parseKVTag("EDGE_WEIGHT_TYPE");
		data.ewt = EdgeWeightType.valueOf(
				getString(fis)
		);
		assert(fis.read() == '\n');

		parseKVTag("BEST_KNOWN");
		data.best_known = getInt(fis);
		assert(fis.read() == '\n');

		parseTag("NODE_COORD_SECTION");
		assert(fis.read() == '\n');

		while(!parseTag("EOF")){
			Coordinate coord = parseCoordLine(fis);
			data.coordinates.add(coord);
		}

		return data;
	}

	private Coordinate parseCoordLine(BufferedInputStream fis) throws IOException {
		skipWhiteSpace(fis);
		int name = getInt(fis);
		skipWhiteSpace(fis);
		double x = getDouble(fis);
		skipWhiteSpace(fis);
		double y = getDouble(fis);

		assert(fis.read() == '\n');

		return new Coordinate(name, x, y);
	}

	private double getDouble(BufferedInputStream fis) throws IOException {
		fis.mark(1);
		StringBuilder sb = new StringBuilder();
		int c = fis.read();
		boolean commaAlreadySeen = false;
		while(isNumeric(c) || isComma(c)){
			if(isComma(c)){
				if(commaAlreadySeen) {
					throw new RuntimeException("Invalid Number!");
				}
				commaAlreadySeen = true;
				sb.append((char) c);
			} else {
				sb.append((char) c);
			}
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
		return Double.valueOf(sb.toString());
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

	private static boolean isComma(int c){
		return c == '.';
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
