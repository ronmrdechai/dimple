package dimple.formula;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dimple.sexp.Sexpressions;
import org.junit.Test;

// These tests work by comparing the formula to its string representation. This is not ideal, but it
// gets the job done.
public class CnfConverterTest {
  @Test
  public void convertIff() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(iff a b)"));
    assertEquals("(and (or a (not b)) (or (not a) b))", Formulas.toString(formula));
  }

  @Test
  public void convertIf() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(if a b)"));
    assertEquals("(or (not a) b)", Formulas.toString(formula));
  }

  @Test
  public void convertNotNot() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(not (not a))"));
    assertEquals("a", Formulas.toString(formula));
  }

  @Test
  public void convertNotNotNot() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(not (not (not a)))"));
    assertEquals("(not a)", Formulas.toString(formula));
  }

  @Test
  public void convertNotNotNotNot() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(not (not (not (not a))))"));
    assertEquals("a", Formulas.toString(formula));
  }

  @Test
  public void convertNotAnd() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(not (and a b))"));
    assertEquals("(or (not a) (not b))", Formulas.toString(formula));
  }

  @Test
  public void convertNotOr() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(not (or a b))"));
    assertEquals("(and (not a) (not b))", Formulas.toString(formula));
  }

  @Test
  public void convertDemorganRecursive() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(and c (not (or a b)))"));
    assertEquals("(and c (and (not a) (not b)))", Formulas.toString(formula));
  }

  @Test
  public void convertDistributeOrOverAnd_andFirst() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(or (and b c) a)"));
    assertEquals("(and (or a b) (or a c))", Formulas.toString(formula));
  }

  @Test
  public void convertDistributeOrOverAnd_andSecond() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(or a (and b c))"));
    assertEquals("(and (or a b) (or a c))", Formulas.toString(formula));
  }

  @Test
  public void correctness() throws Exception {
    var formula = CnfConverter.convert(Sexpressions.compile("(or a (and b (if a c)))"));
    assertTrue(Formulas.isCnf(formula));
  }
}
