package dimple.reduce;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import dimple.formula.Formula;
import dimple.sexp.Sexpressions;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FourColorReduction<T> implements Reduction<Graph<T>> {
  @Override
  public Formula reduce(Graph<T> graph) {
    var builder = new FourColorSexpressionBuilder(graph);

    builder.writePrefix();
    builder.writeAllRegions();
    builder.writeAllEdges();
    builder.writeSuffix();

    return Sexpressions.compile(builder.get());
  }

  public static Map<String, String> decodeSolution(Map<String, Boolean> solution) {
    return solution.keySet().stream()
        .filter((k) -> solution.get(k))
        .collect(Collectors.toMap((k) -> k.split("-")[0], (k) -> k.split("-")[1]));
  }

  private class FourColorSexpressionBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final Graph<T> graph;

    FourColorSexpressionBuilder(Graph<T> graph) {
      this.graph = graph;
    }

    public void writePrefix() {
      writeLine("(and");
    }

    public void writeAllRegions() {
      for (T node : graph.nodes())
        writeRegion(node);
    }

    public void writeAllEdges() {
      writeLine("  ; adjacent");

      Set<EndpointPair<T>> writtenEdges = new HashSet<>();
      for (var edge : graph.edges()) {
        if (!writtenEdges.contains(edge)) {
          writeEdge(edge);
          writtenEdges.add(edge);
        }
      }
    }

    public void writeSuffix() {
      stringBuilder.append(")");
    }

    public String get() {
      return stringBuilder.toString();
    }

    private void writeRegion(T region) {
      writeLine("  ; region %s", region);
      writeLine("  (or %s-R %s-G %s-B %s-Y)", region, region, region, region);
      writeLine("  (or (not %s-R) (not %s-B))", region, region);
      writeLine("  (or (not %s-R) (not %s-G))", region, region);
      writeLine("  (or (not %s-R) (not %s-Y))", region, region);
      writeLine("  (or (not %s-B) (not %s-G))", region, region);
      writeLine("  (or (not %s-B) (not %s-Y))", region, region);
      writeLine("  (or (not %s-G) (not %s-Y))", region, region);
    }

    private void writeEdge(EndpointPair<T> edge) {
      writeLine("  (or (not %s-R) (not %s-R))", edge.nodeU(), edge.nodeV());
      writeLine("  (or (not %s-G) (not %s-G))", edge.nodeU(), edge.nodeV());
      writeLine("  (or (not %s-B) (not %s-B))", edge.nodeU(), edge.nodeV());
      writeLine("  (or (not %s-Y) (not %s-Y))", edge.nodeU(), edge.nodeV());
    }

    private void writeLine(String string) {
      stringBuilder.append(string + "\n");
    }

    private void writeLine(String format, Object... args) {
      stringBuilder.append(String.format(format, args) + "\n");
    }
  }
}
