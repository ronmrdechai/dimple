package dimple.reduce;

import com.google.common.graph.Graph;
import dimple.formula.Formula;
import dimple.sexp.Sexpressions;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CliqueReduction<T> implements Reduction<CliqueReduction.Clique<T>> {
  @Override
  public Formula reduce(CliqueReduction.Clique<T> clique) {
    var builder = new CliqueSexpressionBuilder(clique);

    builder.writePrefix();
    builder.writeRthVertex();
    builder.writeMutualExclusion();
    builder.writeEdges();
    builder.writeSuffix();

    return Sexpressions.compile(builder.get());
  }

  public static Set<Integer> decodeSolution(Map<String, Boolean> solution) {
    return solution.keySet().stream()
        .filter((k) -> solution.get(k))
        .map((k) -> k.split(",")[0].substring(1))
        .map(Integer::valueOf)
        .collect(Collectors.toSet());
  }

  public static class Clique<U> {
    public final int size;
    public final Graph<U> graph;

    Clique(int size, Graph<U> graph) {
      this.size = size;
      this.graph = graph;
    }
  }

  private class CliqueSexpressionBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final Clique<T> clique;

    CliqueSexpressionBuilder(Clique<T> clique) {
      this.clique = clique;
    }

    public void writePrefix() {
      writeLine("(and");
    }

    public void writeRthVertex() {
      writeLine("  ; For each r, 0 <= r < size: Vi,r is the Rth vertex in the clique");
      for (int n = 0; n < clique.size; n++) {
        final int r = n;
        writeLine("  (or%s)", clique.graph.nodes().stream()
            .reduce("", (s, i) -> s + String.format(" V%s,%d", i, r), (s, t) -> s + " " + t));
      }
    }

    public void writeMutualExclusion() {
      writeLine("  ; No vertex is both the Rth and Sth vertex in the clique");
      for (var i : clique.graph.nodes())
        for (int s = 0; s < clique.size; s++)
          for (int r = 0; r < s; r++)
            writeLine("  (or (not V%s,%d) (not V%s,%d))", i, r, i, s);
    }

    public void writeEdges() {
      writeLine("  ; If there is not edge from Vi to Vj,");
      writeLine("  ; then Vi and Vj cannot both be in the clique");
      for (int r = 0; r < clique.size; r++)
        for (int s = 0; s < clique.size; s++)
          if (r == s)
            continue;
          else
            for (var i : clique.graph.nodes())
              for (var j : clique.graph.nodes())
                if (!clique.graph.adjacentNodes(i).contains(j))
                  writeLine("  (or (not V%s,%d) (not V%s,%d))", i, r, j, s);
    }

    public void writeSuffix() {
      stringBuilder.append(")");
    }

    public String get() {
      return stringBuilder.toString();
    }

    private void writeLine(String string) {
      stringBuilder.append(string + "\n");
    }

    private void writeLine(String format, Object... args) {
      stringBuilder.append(String.format(format, args) + "\n");
    }
  }
}
