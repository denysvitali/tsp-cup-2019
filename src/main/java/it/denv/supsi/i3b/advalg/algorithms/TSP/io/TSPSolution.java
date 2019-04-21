package it.denv.supsi.i3b.advalg.algorithms.TSP.io;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TSPSolution {
	private String outName;
	private String comment;
	private int dimension;
	private Route r;

	public TSPSolution(TSPData data, Route route){
		outName = data.getName() + ".tour";
		comment = data.getComment() + " (" + route.getLength() + ")";
		dimension = data.getDim();
		r = route;
	}

	public void write(File f) throws IOException{
		FileWriter fw = new FileWriter(f);

		fw.write("NAME : " + outName + "\n");
		fw.write("COMMENT : " + comment + "\n");
		fw.write("TYPE : TOUR\n");
		fw.write("DIMENSION : " + dimension + "\n");
		fw.write("TOUR_SECTION\n");
		for(Coordinate c : r.getCoords()){
			fw.write(c.getNumber() + "\n");
		}
		fw.write("EOF\n");
		fw.close();
	}
}
