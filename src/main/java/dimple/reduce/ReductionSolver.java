package dimple.reduce;

import dimple.Solver;
import dimple.formula.Formulas;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReductionSolver<P> {
  private final Reduction<P> reduction;

  public static <Q> ReductionSolver<Q> of(Reduction<Q> reduction) {
    return new ReductionSolver<>(reduction);
  }

  private ReductionSolver(Reduction<P> reduction) {
    this.reduction = reduction;
  }

  private Solver createSolver(P problem) {
    return Formulas.createSolver(reduction.reduce(problem));
  }

  Optional<Map<String, Boolean>> solve(P problem) {
    return createSolver(problem).solve();
  }

  Optional<List<Map<String, Boolean>>> solveAll(P problem) {
    return createSolver(problem).solveAll();
  }
}
