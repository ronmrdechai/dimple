package dimple.sexp;

import static org.junit.Assert.assertEquals;

import dimple.formula.Formulas;
import org.junit.Test;

// These tests work by comparing the formula to its string representation. This is not ideal, but it
// gets the job done.
public class SexpressionCompilerTest {

  @Test
  public void compileBinaryConnective() throws Exception {
    String program = "(and a b)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);

    assertEquals(program, Formulas.toString(formula));
  }

  @Test
  public void compileBinaryConnectiveMultipleArgs() throws Exception {
    String program = "(and a b c)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);

    assertEquals("(and (and a b) c)", Formulas.toString(formula));
  }

  @Test(expected = ParseException.class)
  public void compileBinaryConnectiveOneArg() throws Exception {
    String program = "(and a)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);
  }

  @Test
  public void compileUnaryConnective() throws Exception {
    String program = "(not a)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);

    assertEquals(program, Formulas.toString(formula));
  }

  @Test(expected = ParseException.class)
  public void compileUnaryConnectiveMultipleArgs() throws Exception {
    String program = "(not a b)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);
  }

  @Test(expected = ParseException.class)
  public void compileInvalidFunction() throws Exception {
    String program = "(invalid a b)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);
  }

  @Test
  public void compileStrictlyBinaryFunction() throws Exception {
    String program = "(if a b)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);

    assertEquals(program, Formulas.toString(formula));
  }

  @Test(expected = ParseException.class)
  public void compileStrictlyBinaryFunctionMultipleArgs() throws Exception {
    String program = "(if a b a)";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);
  }

  @Test
  public void compileNested() throws Exception {
    String program = "(and (not a) b (or c (not a)))";
    var parsed = SexpressionParser.parse(program);
    var formula = SexpressionCompiler.compile(parsed);

    assertEquals("(and (and (not a) b) (or c (not a)))", Formulas.toString(formula));
  }
}
