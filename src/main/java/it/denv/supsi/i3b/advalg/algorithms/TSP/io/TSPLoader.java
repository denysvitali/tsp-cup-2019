package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.EdgeWeightType;
import it.denv.supsi.i3b.advalg.algorithms.ProblemType;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

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
		HashMap<String, String> hm = new HashMap<>();
		KV k;
		do {
			k = parseKVLine(fis);
			if(k != null) {
				hm.put(k.getKey(), k.getValue());
			}

		} while(k != null);

		data.name = hm.getOrDefault("NAME", "Unnamed");
		data.comment = hm.getOrDefault("COMMENT", "");
		data.type = ProblemType.valueOf(hm.get("TYPE"));
		data.ewt = EdgeWeightType.valueOf(hm.get("EDGE_WEIGHT_TYPE"));
		data.best_known = Integer.valueOf(
				hm.getOrDefault("BEST_KNOWN", "-1")
		);
		data.dimension = Integer.valueOf(hm.getOrDefault(
				"DIMENSION",
				"-1"
		));

		parseTag("NODE_COORD_SECTION");
		assert(fis.read() == '\n');

		while(!parseTag("EOF")){
			Coordinate coord = parseCoordLine(fis);
			data.coordinates.add(coord);
		}

		return data;
	}

	private KV parseKVLine(BufferedInputStream fis) throws IOException {
		skipWhiteSpace(fis);
		String key = getStringTill(fis, ':');
		key = key.trim();
		if(fis.read() != ':'){
			fis.reset();
			return null;
		}
		skipWhiteSpace(fis);
		String value = getStringTill(fis, '\n');

		if(fis.read() != '\n'){
			fis.reset();
			return null;
		}

		if(key.length() != 0 && value.length() == 0){
			return null;
		}

		return new KV(key, value);
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

		if(isSign(c)){
			sb.append((char) c);
			fis.mark(1);
			c = fis.read();
		}

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

		// Scientific notation
		if(c == 'e' || c == 'E'){
			sb.append((char) c);
			fis.mark(1);
			c = fis.read();
			if(c == '+' || c == '-') {
				fis.mark(1);
				c = fis.read();
				while(isNumeric(c)) {
					sb.append((char) c);
					fis.mark(1);
					c = fis.read();
				}
			} else {
				fis.reset();
			}
		}
		fis.reset();
		return Double.valueOf(sb.toString());
	}

	private boolean isSign(int c) {
		return c == '+' || c == '-';
	}

	private String getStringTill(BufferedInputStream fis, char e) throws IOException {
		StringBuilder sb = new StringBuilder();
		fis.mark(1);
		int c = fis.read();
		while(c != e && c!= '\n' && c != -1){
			sb.append((char) c);
			fis.mark(1);
			c = fis.read();
		}
		fis.reset();
		return sb.toString();
	}

	private String getString(BufferedInputStream fis) throws IOException {
		return getStringTill(fis, '\n');
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
