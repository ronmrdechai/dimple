package dimple;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AssignmentIterator implements Iterator<Boolean[]> {
  private final ImmutableBiMap<String, Integer> variables;
  private final Collection<Set<Integer>> clauses;

  private final List<Deque<Set<Integer>>> watchList;
  private final Boolean[] currentAssignment;

  private int currentVariable;
  private Integer[] state;
  private boolean hasNext;

  private List<Deque<Set<Integer>>> createWatchList() {
    List<Deque<Set<Integer>>> watchList = Stream.generate(() -> new ArrayDeque<Set<Integer>>())
        .limit(2 * variables.size())
        .collect(Collectors.toList());

    for (var clause : clauses) {
      var any = clause.iterator().next();
      watchList.get(any).add(clause);
    }

    return watchList;
  }

  AssignmentIterator(ImmutableBiMap<String, Integer> variables, Collection<Set<Integer>> clauses) {
    this.variables = variables;
    this.clauses = clauses;

    this.watchList = createWatchList();
    this.currentAssignment = Stream.generate(() -> null)
        .limit(variables.size())
        .toArray(Boolean[]::new);

    this.currentVariable = 0;
    this.state = Stream.generate(() -> 0).limit(variables.size()).toArray(Integer[]::new);
    this.hasNext = true;
  }

  private boolean findAlterantive(final int falseVariable) {
    boolean foundAlternative = false;

    var clause = watchList.get(falseVariable).getFirst();
    for (var alternative : clause) {
      var variable = alternative >> 1;
      var isFalse = (alternative & 1) > 0;
      if (currentAssignment[variable] == null || currentAssignment[variable] != isFalse) {
        foundAlternative = true;
        watchList.get(falseVariable).removeFirst();
        watchList.get(alternative).add(clause);
        break;
      }
    }

    return foundAlternative;
  }

  private boolean updateWatchList(final int falseVariable) {
    while (!watchList.get(falseVariable).isEmpty())
      if (!findAlterantive(falseVariable))
        return false;
    return true;
  }

  private boolean tryUpdate() {
    boolean triedUpdate = false;
    for (var value : new int[]{0, 1}) {
      if (((state[currentVariable] >> value) & 1) == 0) {

        triedUpdate = true;
        state[currentVariable] |= 1 << value;
        currentAssignment[currentVariable] = value != 0;

        if (!updateWatchList(currentVariable << 1 | value))
          currentAssignment[currentVariable] = null;
        else {
          currentVariable += 1;
          break;
        }
      }
    }
    return triedUpdate;
  }

  private Boolean[] nextAssignment() {
    while (true) {
      if (currentVariable == variables.size()) {
        currentVariable -= 1;
        return currentAssignment;
      }

      if (!tryUpdate()) {
        if (currentVariable == 0) {
          hasNext = false;
          return null;
        } else {
          state[currentVariable] = 0;
          currentAssignment[currentVariable] = null;
          currentVariable -= 1;
        }
      }
    }
  }

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public Boolean[] next() {
    if (hasNext) {
      var assignment = nextAssignment();
      if (assignment == null)
        return new Boolean[variables.size()];
      return assignment;
    } else
      throw new NoSuchElementException();
  }
}
