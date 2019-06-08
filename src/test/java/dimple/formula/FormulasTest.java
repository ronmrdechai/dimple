package dimple.formula;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dimple.sexp.Sexpressions;
import java.util.Map;
import org.junit.Test;

public class FormulasTest {
  @Test
  public void evalAnd() throws Exception {
    var formula = Sexpressions.compile("(and a b)");

    assertTrue(Formulas.eval(formula, Map.of("a", true, "b", true)));
    assertFalse(Formulas.eval(formula, Map.of("a", false, "b", true)));
    assertFalse(Formulas.eval(formula, Map.of("a", true, "b", false)));
    assertFalse(Formulas.eval(formula, Map.of("a", false, "b", false)));
  }

  @Test
  public void evalOr() throws Exception {
    var formula = Sexpressions.compile("(or a b)");

    assertTrue(Formulas.eval(formula, Map.of("a", true, "b", true)));
    assertTrue(Formulas.eval(formula, Map.of("a", false, "b", true)));
    assertTrue(Formulas.eval(formula, Map.of("a", true, "b", false)));
    assertFalse(Formulas.eval(formula, Map.of("a", false, "b", false)));
  }

  @Test
  public void evalNot() throws Exception {
    var formula = Sexpressions.compile("(not a)");

    assertFalse(Formulas.eval(formula, Map.of("a", true)));
    assertTrue(Formulas.eval(formula, Map.of("a", false)));
  }

  @Test
  public void evalIf() throws Exception {
    var formula = Sexpressions.compile("(if a b)");

    assertTrue(Formulas.eval(formula, Map.of("a", true, "b", true)));
    assertTrue(Formulas.eval(formula, Map.of("a", false, "b", true)));
    assertFalse(Formulas.eval(formula, Map.of("a", true, "b", false)));
    assertTrue(Formulas.eval(formula, Map.of("a", false, "b", false)));
  }

  @Test
  public void evalIff() throws Exception {
    var formula = Sexpressions.compile("(iff a b)");

    assertTrue(Formulas.eval(formula, Map.of("a", true, "b", true)));
    assertFalse(Formulas.eval(formula, Map.of("a", false, "b", true)));
    assertFalse(Formulas.eval(formula, Map.of("a", true, "b", false)));
    assertTrue(Formulas.eval(formula, Map.of("a", false, "b", false)));
  }

  @Test
  public void isCnfIdentifiesCnf() throws Exception {
    var formula = Sexpressions.compile("(and (or a b) (or b (not c)))");
    assertTrue(Formulas.isCnf(formula));
  }

  @Test
  public void isCnfIdentifiesNonCnf() throws Exception {
    var formula = Sexpressions.compile("(and (or (and a c) b) (or b (not c)))");
    assertFalse(Formulas.isCnf(formula));
  }
}
