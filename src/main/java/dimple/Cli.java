package dimple;

import com.google.common.io.CharStreams;
import dimple.formula.Formulas;
import dimple.sexp.ParseException;
import dimple.sexp.Sexpressions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(description = "A simiple SAT solver",
         name = "dimple", mixinStandardHelpOptions = true, version = "dimple 0.1")
public class Cli implements Callable<Integer> {
  @Parameters(index = "0", paramLabel = "FILE", description = "File to solve.")
  private File file;

  @Option(names = { "-a", "--all" }, description = "Print all solutions.")
  private boolean all = false;

  private static void printSolution(Map<String, Boolean> solution) {
    for (Map.Entry<String, Boolean> entry : solution.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
  }

  private Optional<Map<String, Boolean>> solveFor(InputStream input) throws Exception {
    String program = CharStreams.toString(new InputStreamReader(input));
    var solver = Formulas.createSolver(Sexpressions.compile(program));
    return solver.solve();
  }

  private Optional<List<Map<String, Boolean>>> solveAllFor(InputStream input) throws Exception {
    String program = CharStreams.toString(new InputStreamReader(input));
    var solver = Formulas.createSolver(Sexpressions.compile(program));
    return solver.solveAll();
  }

  private int solve() throws Exception {
    var solution = solveFor(new FileInputStream(file));

    if (solution.isPresent()) {
      System.out.println("SAT");
      printSolution(solution.get());
      return 0;
    } else {
      System.out.println("NO SAT");
      return 1;
    }
  }

  private int solveAll() throws Exception {
    var solutions = solveAllFor(new FileInputStream(file));

    if (solutions.isPresent()) {
      System.out.println("SAT");
      for (var solution : solutions.get()) {
        printSolution(solution);
        System.out.println("---");
      }
      return 0;
    } else {
      System.out.println("NO SAT");
      return 1;
    }
  }

  @Override
  public Integer call() throws Exception {
    try {
      if (all)
        return solveAll();
      else
        return solve();
    } catch (IOException | ParseException exception) {
      System.out.println("Error reading input: " + exception.getMessage());
      return 127;
    }
  }

  public static void main(String[] args) {
    System.exit(CommandLine.call(new Cli(), args));
  }
}
