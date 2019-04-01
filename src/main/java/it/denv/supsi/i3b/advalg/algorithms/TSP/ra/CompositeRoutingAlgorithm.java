package it.denv.supsi.i3b.advalg.algorithms.TSP.ra;

import it.denv.supsi.i3b.advalg.Route;
import it.denv.supsi.i3b.advalg.algorithms.TSP.io.TSPData;

import java.util.ArrayList;

public class CompositeRoutingAlgorithm extends RoutingAlgorithm {
	private RoutingAlgorithm sa = null;
	private ArrayList<ILS> ias = new ArrayList<>();

	public Route route(int startNode, TSPData data){
		if(sa == null){
			throw new RoutingException("Unable to route. A starting " +
					"algorithm must be selected first.");
		}

		System.out.println("First routing w/ " + sa);
		long s1 = System.currentTimeMillis();
		Route r = sa.route(startNode, data);
		long s2 = System.currentTimeMillis();

		System.out.println("Spent " + (s2-s1) + " ms");

		for(ILS ia : ias){
			s1 = System.currentTimeMillis();
			System.out.println("Intermediate Routing w/ " + ia);
			r = ia.route(r, data);
			s2 = System.currentTimeMillis();
			System.out.println("Spent " + (s2-s1) + " ms");
		}

		return r;
	}

	@Override
	public int getSeed() {
		return 0;
	}

	public CompositeRoutingAlgorithm startWith(RoutingAlgorithm ra){
		this.sa = ra;
		return this;
	}

	public RoutingAlgorithm getSa() {
		return sa;
	}

	public ArrayList<ILS> getIas() {
		return ias;
	}

	public CompositeRoutingAlgorithm add(ILS ia){
		this.ias.add(ia);
		return this;
	}
}
