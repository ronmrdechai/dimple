package dimple.sexp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SexpressionParserTest {
  private Object eitherUnwrap(Either<?, ?> either) {
    return either.match((v) -> v, (v) -> v);
  }

  @SuppressWarnings("unchecked")
  private <T> void assertEitherEquals(T value, Either<?, ?> either) throws ClassCastException {
    T eitherValue = (T)eitherUnwrap(either);
    assertEquals(value, eitherValue);
  }

  @Test
  public void parseFlat() throws Exception {
    var sexp = SexpressionParser.parse("(this is a test)");

    assertEquals(4, sexp.size());
    assertEitherEquals("this", sexp.get(0));
    assertEitherEquals("is", sexp.get(1));
    assertEitherEquals("a", sexp.get(2));
    assertEitherEquals("test", sexp.get(3));
  }

  @Test
  public void parseNested() throws Exception {
    var sexp = SexpressionParser.parse("(this (is (a new)) test)");

    assertEquals(3, sexp.size());
    assertEitherEquals("this", sexp.get(0));
    {
      var nested = (Sexpression)eitherUnwrap(sexp.get(1));

      assertEquals(2, nested.size());
      assertEitherEquals("is", nested.get(0));
      {
        var nestedNested = (Sexpression)eitherUnwrap(nested.get(1));

        assertEquals(2, nestedNested.size());
        assertEitherEquals("a", nestedNested.get(0));
        assertEitherEquals("new", nestedNested.get(1));
      }
    }
    assertEitherEquals("test", sexp.get(2));
  }

  @Test
  public void parseSingleAtom() throws Exception {
    var sexp = SexpressionParser.parse("test");

    assertEquals(1, sexp.size());
    assertEitherEquals("test", sexp.get(0));
  }
}
