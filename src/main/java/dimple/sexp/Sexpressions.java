package dimple.sexp;

import dimple.formula.Formula;

public class Sexpressions {
  public static Formula compile(String program) throws ParseException {
    return SexpressionCompiler.compile(SexpressionParser.parse(program));
  }
}
