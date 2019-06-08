package dimple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Joiner;
import dimple.formula.CnfConverter;
import dimple.formula.Formulas;
import dimple.sexp.Sexpressions;
import org.junit.Test;

public class SolverTest {
  static final String FILE_CONTENTS = Joiner.on("\n").join(
      "(and",
      " (or A B (not C))",
      " (or B C)",
      " (not B)",
      " (or (not A) C))");

  static final String NO_SOLUTION = Joiner.on("\n").join(
      "(and",
      " (iff A B)",
      " A",
      " (not B))");

  @Test
  public void solveCorrectness() throws Exception {
    var solver = Formulas.createSolver(Sexpressions.compile(FILE_CONTENTS));
    var solution = solver.solve();

    assertTrue(solution.isPresent());

    var solutionMap = solution.get();
    assertTrue(solutionMap.get("A"));
    assertTrue(solutionMap.get("C"));
    assertFalse(solutionMap.get("B"));
  }

  @Test
  public void solveAllcorrectness() throws Exception {
    var solver = Formulas.createSolver(Sexpressions.compile(FILE_CONTENTS));
    var solutions = solver.solveAll().get();

    assertEquals(1, solutions.size());

    var solution = solutions.get(0);
    assertTrue(solution.get("A"));
    assertTrue(solution.get("C"));
    assertFalse(solution.get("B"));
  }

  @Test
  public void noSolution() throws Exception {
    var formula = Sexpressions.compile(NO_SOLUTION);
    var cnf = CnfConverter.convert(formula);
    var solver = Formulas.createSolver(cnf);
    var solution = solver.solve();

    assertFalse(solution.isPresent());
  }
}
