package dimple.formula;

import com.google.common.collect.ImmutableBiMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FormulaSolverVisitor extends FormulaThrowingVisitor<Void> {
  private static class ClauseVisitor extends FormulaThrowingVisitor<Set<Integer>> {
    Map<String, Integer> variables;

    ClauseVisitor(Map<String, Integer> variables) {
      super("Formula not in CNF");
      this.variables = variables;
    }

    @Override
    public Set<Integer> visit(Atom.Var formula) {
      String variable = formula.value;
      variables.computeIfAbsent(variable, (k) -> {
        return variables.size();
      });

      return Set.of(variables.get(variable) << 1);
    }

    @Override
    public Set<Integer> visit(UnaryConnective.Not formula) {
      Atom.Var atom = (Atom.Var)formula.argument;

      String variable = atom.value;
      variables.computeIfAbsent(variable, (k) -> {
        return variables.size();
      });

      return Set.of((variables.get(variable) << 1) | 1);
    }

    @Override
    public Set<Integer> visit(BinaryConnective.Or formula) {
      var result = new HashSet<Integer>();
      result.addAll(formula.left.accept(this));
      result.addAll(formula.right.accept(this));
      return result;
    }
  }

  private Map<String, Integer> variables;
  private Collection<Set<Integer>> clauses;

  FormulaSolverVisitor() {
    super("Formula not in CNF");
    this.variables = new HashMap<>();
    this.clauses = new HashSet<>();
  }

  public ImmutableBiMap<String, Integer> variables() {
    return ImmutableBiMap.copyOf(variables);
  }

  public Collection<Set<Integer>> clauses() {
    return clauses;
  }

  @Override
  public Void visit(Atom.Var formula) {
    clauses.add(formula.accept(new ClauseVisitor(this.variables)));
    return null;
  }

  @Override
  public Void visit(UnaryConnective.Not formula) {
    clauses.add(formula.accept(new ClauseVisitor(this.variables)));
    return null;
  }

  @Override
  public Void visit(BinaryConnective.And formula) {
    formula.left.accept(this);
    formula.right.accept(this);
    return null;
  }

  @Override
  public Void visit(BinaryConnective.Or formula) {
    clauses.add(formula.accept(new ClauseVisitor(this.variables)));
    return null;
  }
}
