package dimple.reduce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.HashSet;
import org.junit.Ignore;
import org.junit.Test;

public class SudokuReductionTest {
  @Test
  public void solve4x4() {
    int[][] sudoku = {
      {0, 1, 3, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {3, 4, 2, 1},
    };

    var solver = ReductionSolver.of(new SudokuReduction());
    var solution = solver.solve(sudoku).get();
    var solvedSudoku = SudokuReduction.decodeSolution(solution);

    assertSolved(solvedSudoku, sudoku);
  }

  @Ignore("Causes stack overflow")
  @Test
  public void solve9x9() {
    int[][] sudoku = {
        {0, 0, 0, 0, 0, 6, 0, 0, 0},
        {0, 5, 9, 0, 0, 0, 0, 0, 8},
        {2, 0, 0, 0, 0, 8, 0, 0, 0},
        {0, 4, 5, 0, 0, 0, 0, 0, 0},
        {0, 0, 3, 0, 0, 0, 0, 0, 0},
        {0, 0, 6, 0, 0, 3, 0, 5, 4},
        {0, 0, 0, 3, 2, 5, 0, 0, 6},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    var solver = ReductionSolver.of(new SudokuReduction());
    var solution = solver.solve(sudoku).get();
    var solvedSudoku = SudokuReduction.decodeSolution(solution);

    assertSolved(solvedSudoku, sudoku);
  }

  @Test
  public void noSolution() {
    int[][] sudoku = {
      {0, 1, 3, 0},
      {0, 2, 0, 0},
      {0, 0, 0, 0},
      {3, 4, 2, 1},
    };

    var solver = ReductionSolver.of(new SudokuReduction());
    var solution = solver.solve(sudoku);

    assertFalse(solution.isPresent());
  }

  private void assertSolved(int[][] solvedSudoku, int[][] sudoku) {
    int dimention = sudoku.length;

    assertDimention(sudoku, dimention);
    assertDimention(solvedSudoku, dimention);
    assertSolutionOf(solvedSudoku, sudoku, dimention);
    assertNoDuplicatesInRow(solvedSudoku, dimention);
    assertNoDuplicatesInCol(solvedSudoku, dimention);
    assertNoDuplicatesInSquares(solvedSudoku, dimention);
  }

  private void assertDimention(int[][] sudoku, int dimention) {
    assertEquals(sudoku.length, dimention);
    for (int i = 0; i < dimention; i++)
      assertEquals(sudoku[i].length, dimention);
  }

  private void assertSolutionOf(int[][] solvedSudoku, int[][] sudoku, int dimention) {
    for (int i = 0; i < dimention; i++)
      for (int j = 0; j < dimention; j++)
        assertTrue(sudoku[i][j] == 0 || solvedSudoku[i][j] == sudoku[i][j]);
  }

  private void assertNoDuplicatesInRow(int[][] sudoku, int dimention) {
    for (int i = 0; i < dimention; i++) {
      var set = new HashSet<Integer>();
      for (int j = 0; j < dimention; j++)
        set.add(sudoku[i][j]);
      assertEquals(dimention, set.size());
    }
  }

  private void assertNoDuplicatesInCol(int[][] sudoku, int dimention) {
    for (int i = 0; i < dimention; i++) {
      var set = new HashSet<Integer>();
      for (int j = 0; j < dimention; j++)
        set.add(sudoku[j][i]);
      assertEquals(dimention, set.size());
    }
  }

  private void assertNoDuplicatesInSquares(int[][] sudoku, int dimention) {
    int sqrt = IntMath.sqrt(dimention, RoundingMode.UNNECESSARY);
    for (int i = 0; i < dimention; i += sqrt)
      for (int j = 0; j < dimention; j += sqrt) {
        var set = new HashSet<Integer>();
        for (int subRow = 0; subRow < sqrt; subRow++)
          for (int subCol = 0; subCol < sqrt; subCol++)
            set.add(sudoku[i + subRow][j + subCol]);
        assertEquals(dimention, set.size());
      }
  }
}
