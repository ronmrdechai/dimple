package dimple.reduce;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.util.Set;
import org.junit.Test;

public class CliqueReductionTest {
  @Test
  public void findClique() {
    var graph = graphWith4Clique();

    var solver = ReductionSolver.of(new CliqueReduction<Integer>());
    var solution = solver.solve(new CliqueReduction.Clique<Integer>(4, graph)).get();
    var clique = CliqueReduction.decodeSolution(solution);

    assertClique(clique, graph);
  }

  @Test
  public void noClique() {
    var graph = graphWith4Clique();
    var solver = ReductionSolver.of(new CliqueReduction<Integer>());
    var solution = solver.solve(new CliqueReduction.Clique<Integer>(5, graph));

    assertFalse(solution.isPresent());
  }

  private <T> void assertClique(Set<T> clique, Graph<T> graph) {
    for (var v : clique)
      for (var u : clique)
        if (v == u)
          continue;
        else
          assertTrue(graph.adjacentNodes(v).contains(u));
  }

  // This graph has a 4 clique at { 3, 4, 5, 6 }
  Graph<Integer> graphWith4Clique() {
    int[][] adjacencyMatrix = new int[][] {
      { 0, 1, 1, 1, 0, 0, 1 },
      { 1, 0, 1, 0, 1, 1, 0 },
      { 1, 1, 0, 1, 1, 0, 0 },
      { 1, 0, 1, 0, 1, 1, 1 },
      { 0, 1, 1, 1, 0, 1, 1 },
      { 0, 1, 0, 1, 1, 0, 1 },
      { 1, 0, 0, 1, 1, 1, 0 }
    };

    MutableGraph<Integer> graph = GraphBuilder.undirected().build();
    for (int i = 0; i < adjacencyMatrix.length; i++)
      for (int j = 0; j < adjacencyMatrix[i].length; j++)
        if (adjacencyMatrix[i][j] == 1)
          graph.putEdge(i, j);
    return graph;
  }
}
