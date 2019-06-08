package dimple.reduce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.util.Map;
import org.junit.Test;

public class FourColorReductionTest {
  @Test
  public void solveComplete3NodeGraph() {
    var solver = ReductionSolver.of(new FourColorReduction<Integer>());
    var solutions = solver.solveAll(complete3NodeGraph()).get();

    assertEquals(24, solutions.size());
  }

  @Test
  public void solveMapOfUsa() {
    var solver = ReductionSolver.of(new FourColorReduction<String>());
    var graph = mapOfUsa();
    var solution = solver.solve(graph).get();
    var colorMap = FourColorReduction.decodeSolution(solution);

    assertSolution(colorMap, graph);
  }

  private void assertSolution(Map<String, String> colorMap, Graph<String> graph) {
    for (var node : colorMap.keySet())
      for (var adjacentNode : graph.incidentEdges(node))
        assertNotEquals(colorMap.get(node), colorMap.get(adjacentNode));
  }

  private Graph<Integer> complete3NodeGraph() {
    MutableGraph<Integer> graph = GraphBuilder.undirected().build();
    graph.putEdge(1, 2);
    graph.putEdge(2, 3);
    graph.putEdge(3, 1);
    return graph;
  }

  private Graph<String> mapOfUsa() {
    MutableGraph<String> graph = GraphBuilder.undirected().build();
    graph.putEdge("CA", "OR");
    graph.putEdge("CA", "AZ");
    graph.putEdge("CA", "NV");
    graph.putEdge("CO", "NM");
    graph.putEdge("CO", "OK");
    graph.putEdge("CO", "UT");
    graph.putEdge("CO", "WY");
    graph.putEdge("CO", "AZ");
    graph.putEdge("CO", "KS");
    graph.putEdge("CO", "NE");
    graph.putEdge("CT", "NY");
    graph.putEdge("CT", "RI");
    graph.putEdge("CT", "MA");
    graph.putEdge("DE", "NJ");
    graph.putEdge("DE", "PA");
    graph.putEdge("DE", "MD");
    graph.putEdge("FL", "GA");
    graph.putEdge("FL", "AL");
    graph.putEdge("GA", "NC");
    graph.putEdge("GA", "SC");
    graph.putEdge("GA", "TN");
    graph.putEdge("GA", "AL");
    graph.putEdge("GA", "FL");
    graph.putEdge("ID", "UT");
    graph.putEdge("ID", "WA");
    graph.putEdge("ID", "WY");
    graph.putEdge("ID", "MT");
    graph.putEdge("ID", "NV");
    graph.putEdge("ID", "OR");
    graph.putEdge("IL", "KY");
    graph.putEdge("IL", "MO");
    graph.putEdge("IL", "WI");
    graph.putEdge("IL", "IN");
    graph.putEdge("IL", "IA");
    graph.putEdge("IL", "MI");
    graph.putEdge("IN", "MI");
    graph.putEdge("IN", "OH");
    graph.putEdge("IN", "IL");
    graph.putEdge("IN", "KY");
    graph.putEdge("IA", "NE");
    graph.putEdge("IA", "SD");
    graph.putEdge("IA", "WI");
    graph.putEdge("IA", "IL");
    graph.putEdge("IA", "MN");
    graph.putEdge("IA", "MO");
    graph.putEdge("KS", "NE");
    graph.putEdge("KS", "OK");
    graph.putEdge("KS", "CO");
    graph.putEdge("KS", "MO");
    graph.putEdge("KY", "TN");
    graph.putEdge("KY", "VA");
    graph.putEdge("KY", "WV");
    graph.putEdge("KY", "IL");
    graph.putEdge("KY", "IN");
    graph.putEdge("KY", "MO");
    graph.putEdge("KY", "OH");
    graph.putEdge("LA", "TX");
    graph.putEdge("LA", "AR");
    graph.putEdge("LA", "MS");
    graph.putEdge("ME", "NH");
    graph.putEdge("MD", "VA");
    graph.putEdge("MD", "WV");
    graph.putEdge("MD", "DE");
    graph.putEdge("MD", "PA");
    graph.putEdge("MA", "NY");
    graph.putEdge("MA", "RI");
    graph.putEdge("MA", "VT");
    graph.putEdge("MA", "CT");
    graph.putEdge("MA", "NH");
    graph.putEdge("MI", "OH");
    graph.putEdge("MI", "WI");
    graph.putEdge("MI", "IL");
    graph.putEdge("MI", "IN");
    graph.putEdge("MI", "MN");
    graph.putEdge("MN", "ND");
    graph.putEdge("MN", "SD");
    graph.putEdge("MN", "WI");
    graph.putEdge("MN", "IA");
    graph.putEdge("MN", "MI");
    graph.putEdge("MS", "LA");
    graph.putEdge("MS", "TN");
    graph.putEdge("MS", "AL");
    graph.putEdge("MS", "AR");
    graph.putEdge("MO", "NE");
    graph.putEdge("MO", "OK");
    graph.putEdge("MO", "TN");
    graph.putEdge("MO", "AR");
    graph.putEdge("MO", "IL");
    graph.putEdge("MO", "IA");
    graph.putEdge("MO", "KS");
    graph.putEdge("MO", "KY");
    graph.putEdge("MT", "SD");
    graph.putEdge("MT", "WY");
    graph.putEdge("MT", "ID");
    graph.putEdge("MT", "ND");
    graph.putEdge("NE", "MO");
    graph.putEdge("NE", "SD");
    graph.putEdge("NE", "WY");
    graph.putEdge("NE", "CO");
    graph.putEdge("NE", "IA");
    graph.putEdge("NE", "KS");
    graph.putEdge("NV", "ID");
    graph.putEdge("NV", "OR");
    graph.putEdge("NV", "UT");
    graph.putEdge("NV", "AZ");
    graph.putEdge("NV", "CA");
    graph.putEdge("NH", "VT");
    graph.putEdge("NH", "ME");
    graph.putEdge("NH", "MA");
    graph.putEdge("NJ", "PA");
    graph.putEdge("NJ", "DE");
    graph.putEdge("NJ", "NY");
    graph.putEdge("NM", "OK");
    graph.putEdge("NM", "TX");
    graph.putEdge("NM", "UT");
    graph.putEdge("NM", "AZ");
    graph.putEdge("NM", "CO");
    graph.putEdge("NY", "PA");
    graph.putEdge("NY", "RI");
    graph.putEdge("NY", "VT");
    graph.putEdge("NY", "CT");
    graph.putEdge("NY", "MA");
    graph.putEdge("NY", "NJ");
    graph.putEdge("NC", "TN");
    graph.putEdge("NC", "VA");
    graph.putEdge("NC", "GA");
    graph.putEdge("NC", "SC");
    graph.putEdge("ND", "SD");
    graph.putEdge("ND", "MN");
    graph.putEdge("ND", "MT");
    graph.putEdge("OH", "MI");
    graph.putEdge("OH", "PA");
    graph.putEdge("OH", "WV");
    graph.putEdge("OH", "IN");
    graph.putEdge("OH", "KY");
    graph.putEdge("OK", "MO");
    graph.putEdge("OK", "NM");
    graph.putEdge("OK", "TX");
    graph.putEdge("OK", "AR");
    graph.putEdge("OK", "CO");
    graph.putEdge("OK", "KS");
    graph.putEdge("OR", "NV");
    graph.putEdge("OR", "WA");
    graph.putEdge("OR", "CA");
    graph.putEdge("OR", "ID");
    graph.putEdge("PA", "NY");
    graph.putEdge("PA", "OH");
    graph.putEdge("PA", "WV");
    graph.putEdge("PA", "DE");
    graph.putEdge("PA", "MD");
    graph.putEdge("PA", "NJ");
    graph.putEdge("RI", "MA");
    graph.putEdge("RI", "NY");
    graph.putEdge("RI", "CT");
    graph.putEdge("SC", "NC");
    graph.putEdge("SC", "GA");
    graph.putEdge("SD", "NE");
    graph.putEdge("SD", "ND");
    graph.putEdge("SD", "WY");
    graph.putEdge("SD", "IA");
    graph.putEdge("SD", "MN");
    graph.putEdge("SD", "MT");
    graph.putEdge("TN", "MS");
    graph.putEdge("TN", "MO");
    graph.putEdge("TN", "NC");
    graph.putEdge("TN", "VA");
    graph.putEdge("TN", "AL");
    graph.putEdge("TN", "AR");
    graph.putEdge("TN", "GA");
    graph.putEdge("TN", "KY");
    graph.putEdge("TX", "NM");
    graph.putEdge("TX", "OK");
    graph.putEdge("TX", "AR");
    graph.putEdge("TX", "LA");
    graph.putEdge("UT", "NV");
    graph.putEdge("UT", "NM");
    graph.putEdge("UT", "WY");
    graph.putEdge("UT", "AZ");
    graph.putEdge("UT", "CO");
    graph.putEdge("UT", "ID");
    graph.putEdge("VT", "NH");
    graph.putEdge("VT", "NY");
    graph.putEdge("VT", "MA");
    graph.putEdge("VA", "NC");
    graph.putEdge("VA", "TN");
    graph.putEdge("VA", "WV");
    graph.putEdge("VA", "KY");
    graph.putEdge("VA", "MD");
    graph.putEdge("WA", "OR");
    graph.putEdge("WA", "ID");
    graph.putEdge("WV", "PA");
    graph.putEdge("WV", "VA");
    graph.putEdge("WV", "KY");
    graph.putEdge("WV", "MD");
    graph.putEdge("WV", "OH");
    graph.putEdge("WI", "MI");
    graph.putEdge("WI", "MN");
    graph.putEdge("WI", "IL");
    graph.putEdge("WI", "IA");
    graph.putEdge("WY", "NE");
    graph.putEdge("WY", "SD");
    graph.putEdge("WY", "UT");
    graph.putEdge("WY", "CO");
    graph.putEdge("WY", "ID");
    graph.putEdge("WY", "MT");
    return graph;
  }
}
