package DistSystems.Election;

import DistSystems.Echo.EchoAlgorithm;
import DistSystems.Utilities.RandomGraphBuilder;

/**
 * Created by Hendrik Mahrt on 23.09.17.
 */
public class ElectionAlgorithm extends EchoAlgorithm {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            ElectionAlgorithm electionAlgorithm = new ElectionAlgorithm();
            electionAlgorithm.start();
        }
    }

    public ElectionAlgorithm() {
        RandomGraphBuilder graphBuilder = new RandomGraphBuilder(10, 2, 0.2);
        nodes = graphBuilder.build();
    }
}
