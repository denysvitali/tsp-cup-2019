package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;

public class CompositeRoutingAlgorithm extends RoutingAlgorithm {
	private RoutingAlgorithm sa = null;
	private ArrayList<IntermediateRoutingAlgorithm> ias = new ArrayList<>();

	public Route route(int startNode, TSPData data){
		if(sa == null){
			throw new RoutingException("Unable to route. A starting " +
					"algorithm must be selected first.");
		}

		Route r = sa.route(startNode, data);

		for(IntermediateRoutingAlgorithm ia : ias){
			r = ia.route(r, data);
		}

		return r;
	}

	public CompositeRoutingAlgorithm startWith(RoutingAlgorithm ra){
		this.sa = ra;
		return this;
	}

	public CompositeRoutingAlgorithm add(IntermediateRoutingAlgorithm ia){
		this.ias.add(ia);
		return this;
	}
}
