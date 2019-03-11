package it.denv.supsi.i3b.advalg.algorithms.TSP.ra.initial.aco;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.IntermediateRoutingAlgorithm;
import it.denv.supsi.i3b.advalg.algorithms.TSP.ra.RoutingAlgorithm;

public class AntColonyOptimization extends RoutingAlgorithm {
	private AntColony ac;
	private IntermediateRoutingAlgorithm ira = null;


	public AntColonyOptimization(int amount_ants, TSPData data) {
		this.ac = new AntColony(amount_ants, data);
	}

	public AntColonyOptimization setSolutionImprover(IntermediateRoutingAlgorithm ira){
		this.ira = ira;
		return this;
	}

	@Override
	public Route route(int startNode, TSPData data) {

		Route bestRoute = null;
		int antsCompleted = 0;
		int globalAntsCompleted = 0;

		Route bestIteration = null;

		//long target = System.currentTimeMillis() + 1000 * 60 * 1;
		long target = System.currentTimeMillis() + 1000 * 15;

		while(System.currentTimeMillis() < target){
			bestIteration = null;

			for (Ant a : ac.getAnts()) {
				if (a.doStep() == AntStatus.STOPPED) {
					Route r = new Route(
							a.getTabuList()
									.stream()
									.mapToInt(Integer::intValue)
									.toArray(),
							data);

					if(bestIteration == null){
						bestIteration = r;
					} else {
						if(r.getLength() < bestIteration.getLength()){
							bestIteration = r;
						}
					}

					antsCompleted++;
				}
			}

			if(antsCompleted == 3){
				antsCompleted = 0;
				globalAntsCompleted += 3;

				if(ira != null) {
					bestIteration = ira.route(
							bestIteration, ac.getData());
				}

				if (bestRoute == null) {
					bestRoute = bestIteration;
				} else {
					if (bestIteration.getLength() < bestRoute.getLength()) {
						bestRoute = bestIteration;
						System.out.println(bestIteration.getLength());
					}
				}

				ac.globalUpdate(bestIteration);
				ac.updatePheronome();
				for(Ant a : ac.getAnts()){
					a.reset();
				}
			}
		}

		System.out.println("Ants completed: " + globalAntsCompleted);

		return bestRoute;
	}
}
