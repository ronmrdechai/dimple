package dimple.sexp;

import dimple.formula.Atom;
import dimple.formula.BinaryConnective;
import dimple.formula.Formula;
import dimple.formula.UnaryConnective;

public class SexpressionCompiler {

  private static String firstAtom(final Either<String, Sexpression> first) throws ParseException {
    try {
      return (String)first.match((v) -> v, (v) -> v);
    } catch (ClassCastException ignored) {
      throw new ParseException("Expected atom, found list");
    }
  }

  private static Formula compileRecursive(final Either<String, Sexpression> element)
      throws ParseException {
    return element.match((String s) -> new Atom.Var(s), (Sexpression sexp) -> compile(sexp));
  }

  private static Formula compileNot(final Sexpression sexp) throws ParseException {
    if (sexp.size() != 1)
      throw new ParseException("To many arguments to `not' function");
    return new UnaryConnective.Not(compileRecursive(sexp.getFirst()));
  }

  private static Formula compileAnd(final Sexpression sexp) throws ParseException {
    if (sexp.size() < 2)
      throw new ParseException("Not enough arguments to `and' function'");
    if (sexp.size() == 2)
      return new BinaryConnective.And(
          compileRecursive(sexp.getFirst()), compileRecursive(sexp.getLast()));

    Formula result = compileRecursive(sexp.removeFirst());
    for (var element : sexp)
      result = new BinaryConnective.And(result, compileRecursive(element));
    return result;
  }

  private static Formula compileOr(final Sexpression sexp) throws ParseException {
    if (sexp.size() < 2)
      throw new ParseException("Not enough arguments to `or' function'");
    if (sexp.size() == 2)
      return new BinaryConnective.Or(
          compileRecursive(sexp.getFirst()), compileRecursive(sexp.getLast()));

    Formula result = compileRecursive(sexp.removeFirst());
    for (var element : sexp)
      result = new BinaryConnective.Or(result, compileRecursive(element));
    return result;
  }

  private static Formula compileIf(final Sexpression sexp) throws ParseException {
    if (sexp.size() != 2)
      throw new ParseException("Invalid amount of arguments to `if' function'");
    return new BinaryConnective.If(
        compileRecursive(sexp.getFirst()), compileRecursive(sexp.getLast()));
  }

  private static Formula compileIff(final Sexpression sexp) throws ParseException {
    if (sexp.size() != 2)
      throw new ParseException("Invalid amount of arguments to `iff' function'");
    return new BinaryConnective.Iff(
        compileRecursive(sexp.getFirst()), compileRecursive(sexp.getLast()));
  }

  public static Formula compile(final Sexpression sexp) throws ParseException {
    var first = firstAtom(sexp.removeFirst());

    if (first.equals("not"))
      return compileNot(sexp);
    else if (first.equals("and"))
      return compileAnd(sexp);
    else if (first.equals("or"))
      return compileOr(sexp);
    else if (first.equals("if"))
      return compileIf(sexp);
    else if (first.equals("iff"))
      return compileIff(sexp);
    else
      throw new ParseException("Invalid function: " + first);
  }
}
