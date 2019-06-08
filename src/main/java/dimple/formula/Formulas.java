package dimple.formula;

import com.google.common.base.Preconditions;
import dimple.Solver;
import java.util.Map;

public class Formulas {
  public static boolean eval(Formula formula, Map<String, Boolean> assignment) {
    return formula.accept(new FormulaEvalVisitor(assignment));
  }

  public static String toString(Formula formula) {
    return formula.accept(new FormulaToStringVisitor());
  }

  public static boolean isCnf(Formula formula) {
    return formula.accept(new IsCnfVisitor());
  }

  public static Solver createSolver(Formula formula) {
    Preconditions.checkArgument(
        isCnf(formula),
        "Provided formula is not in conjunctive normal form.\n  Formula: %s",
        toString(formula));
    var visitor = new FormulaSolverVisitor();
    formula.accept(visitor);
    return new Solver(visitor.variables(), visitor.clauses());
  }
}
