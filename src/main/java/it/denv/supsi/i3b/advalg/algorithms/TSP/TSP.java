package it.denv.supsi.i3b.advalg.algorithms.TSP;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.Coordinate;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TSP {
	public TSP(){

	}

	public Route run(TSPData data, RoutingAlgorithm ra){
		Route r = new Route(data.getCoordinates(), 10);
		double[][] distances = calculateDistances(data);
		data.setDistances(distances);

		return ra.route(1, data);
	}

	public String writeRoute(Route r) throws IOException {
		File outputfile = File.createTempFile("tsp-cup_", ".dat");
		FileOutputStream fos = new FileOutputStream(outputfile);
		for(Coordinate c : r.getCoords()){
			fos.write(String.format("%f %f\n", c.getX(), c.getY()).getBytes());
		}
		fos.close();
		return outputfile.getPath();

	}

	private double[][] calculateDistances(TSPData data) {
		int size = data.getCoordinates().size();
		double[][] distances = new double[size][size];
		for(int i=0; i<size/2; i++){
			for(int j=0; j<size/2; j++){
				if(i == j){
					distances[i][j] = 0;
					distances[j][i] = 0;
				} else {
					Coordinate a = data.getCoordinates().get(i);
					Coordinate b = data.getCoordinates().get(j);

					double distance = Math.sqrt(
							Math.pow(a.getX() - b.getX(), 2) +
							Math.pow(a.getY() - b.getY(), 2)
					);
					distances[i][j] = distance;
					distances[j][i] = distance;
				}
			}
		}
		return distances;
	}
}
