package dimple.reduce;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import dimple.formula.Formula;
import dimple.sexp.Sexpressions;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SudokuReduction implements Reduction<int[][]> {
  @Override
  public Formula reduce(int[][] sudoku) {
    Preconditions.checkArgument(isSudoku(sudoku), "Sudoku is invalid");
    var builder = new SudokuSexpressionBuilder(sudoku);

    builder.writePrefix();
    builder.writeValueForEachSquare();
    builder.writeDedupedRows();
    builder.writeDedupedCols();
    builder.writeDedupedSqrts();
    builder.writePuzzle();
    builder.writeSuffix();

    return Sexpressions.compile(builder.get());
  }

  public static int[][] decodeSolution(Map<String, Boolean> solution) {
    var map = solution.keySet().stream()
        .filter((k) -> solution.get(k))
        .map((k) -> k.substring(1))
        .collect(Collectors.toMap(
            (k) -> k.split("=")[0],
            (k) -> Integer.valueOf(k.split("=")[1])));

    int dimention = IntMath.sqrt(map.size(), RoundingMode.FLOOR);
    int[][] result = new int[dimention][dimention];

    for (var element : map.entrySet()) {
      int i = Integer.valueOf(element.getKey().split(",")[0]) - 1;
      int j = Integer.valueOf(element.getKey().split(",")[1]) - 1;
      result[i][j] = element.getValue();
    }

    return result;
  }

  boolean isSudoku(int[][] sudoku) {
    for (int i = 0; i < sudoku.length; i++)
      if (sudoku[i].length != sudoku.length)
        return false;

    int sqrt = IntMath.sqrt(sudoku.length, RoundingMode.FLOOR);
    return sqrt * sqrt == sudoku.length;
  }

  private class SudokuSexpressionBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();
    private final int[][] sudoku;
    private final int size;
    private final int sqrt;

    SudokuSexpressionBuilder(int[][] sudoku) {
      this.sudoku = sudoku;
      this.size = sudoku.length;
      this.sqrt = IntMath.sqrt(sudoku.length, RoundingMode.UNNECESSARY);
    }

    public void writePrefix() {
      writeLine("(and");
    }

    public void writeValueForEachSquare() {
      IntStream.rangeClosed(1, size).forEach(
          i -> IntStream.rangeClosed(1, size).forEach(
            j -> exactlyOneOf(IntStream.rangeClosed(1, size)
              .boxed()
              .map((value) -> toVariable(i, j, value))
              .collect(Collectors.toList()))));
    }

    public void writeDedupedRows() {
      writeLine("  ; Rows cannot contain duplicate values");
      IntStream.rangeClosed(1, size).forEach(
          i -> IntStream.rangeClosed(1, size).forEach(
            value -> exactlyOneOf(IntStream.rangeClosed(1, size)
              .boxed()
              .map((j) -> toVariable(i, j, value))
              .collect(Collectors.toList()))));
    }

    public void writeDedupedCols() {
      writeLine("  ; Columns cannot contain duplicate values");
      IntStream.rangeClosed(1, size).forEach(
          j -> IntStream.rangeClosed(1, size).forEach(
            value -> exactlyOneOf(IntStream.rangeClosed(1, size)
              .boxed()
              .map((i) -> toVariable(i, j, value))
              .collect(Collectors.toList()))));
    }

    public void writeDedupedSqrts() {
      writeLine("  ; Squares cannot contain duplicate values");
      IntStream.iterate(1, i -> i + sqrt).limit(sqrt).forEach(
          i -> IntStream.iterate(1, j -> j + sqrt).limit(sqrt).forEach(
            j -> IntStream.rangeClosed(1, size).forEach(
              value -> exactlyOneOf(IntStream.range(0, sqrt)
                .boxed()
                .map((subRow) -> IntStream.range(0, sqrt)
                    .boxed()
                    .map((subCol) -> toVariable(i + subRow, j + subCol, value)))
                .flatMap(Function.identity())
                .collect(Collectors.toList())))));
    }

    public void writePuzzle() {
      writeLine("  ; The following must be true");
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          int value = sudoku[i][j];
          if (value != 0)
            writeLine("  " + toVariable(i + 1, j + 1, value));
        }
      }
    }

    public void writeSuffix() {
      stringBuilder.append(")");
    }

    public String get() {
      return stringBuilder.toString();
    }

    private String toVariable(int i, int j, int value) {
      return String.format("X%d,%d=%d", i, j, value);
    }

    private void exactlyOneOf(List<String> literals) {
      writeLine("  ; One of '(%s)", Joiner.on(' ').join(literals));
      writeLine("  (or %s)", Joiner.on(' ').join(literals));
      for (int i = 0; i < literals.size(); i++)
        for (int j = i + 1; j < literals.size(); j++)
          writeLine("  (or (not %s) (not %s))", literals.get(i), literals.get(j));
    }

    private void writeLine(String string) {
      stringBuilder.append(string + "\n");
    }

    private void writeLine(String format, Object... args) {
      stringBuilder.append(String.format(format, args) + "\n");
    }
  }
}
