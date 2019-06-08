package dimple;

import com.google.common.collect.ImmutableBiMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solver {
  private final ImmutableBiMap<String, Integer> variables;
  private final LinkedList<Boolean[]> assignments;
  private final AssignmentIterator assignmentIterator;

  public Solver(
      ImmutableBiMap<String, Integer> variables,
      Collection<Set<Integer>> clauses) {
    this.variables = variables;
    this.assignments = new LinkedList<>();
    this.assignmentIterator = new AssignmentIterator(variables, clauses);
  }

  private Map<String, Boolean> decodeAssignment(Boolean[] assignment) {
    return IntStream.range(0, assignment.length)
      .boxed()
      .collect(Collectors.toMap((i) -> variables.inverse().get(i), (i) -> assignment[i]));
  }

  public Optional<Map<String, Boolean>> solve() {
    if (assignmentIterator.hasNext())
      assignments.add(assignmentIterator.next().clone());
    if (!assignmentIterator.hasNext()) // last value is always bogus
      assignments.removeLast();

    if (!assignments.isEmpty())
      return Optional.of(decodeAssignment(assignments.peek()));
    else
      return Optional.empty();
  }

  public Optional<List<Map<String, Boolean>>> solveAll() {
    if (assignmentIterator.hasNext())
      assignmentIterator.forEachRemaining((assignment) -> assignments.add(assignment.clone()));
    assignments.removeLast();

    if (assignments.isEmpty())
      return Optional.empty();
    return Optional.of(assignments.stream()
        .map((v) -> decodeAssignment(v))
        .collect(Collectors.toList()));
  }
}
