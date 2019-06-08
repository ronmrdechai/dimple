package dimple.sexp;

import com.google.common.base.Splitter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public class SexpressionParser {
  private static String removeComments(String string) {
    return string.replaceAll(";.*\n", "\n");
  }

  private static List<String> tokenize(String string) {
    return Splitter.on(Pattern.compile("\\s+"))
      .omitEmptyStrings()
      .splitToList(string.replace("(", " ( ").replace(")", " ) "));
  }

  private static Either<String, Sexpression> parseInner(Queue<String> tokens)
      throws ParseException {
    if (tokens.isEmpty())
      throw new ParseException("Unexpected EOF");
    String token = tokens.remove();
    if (token.equals("(")) {
      var sexp = new Sexpression();
      while (!tokens.peek().equals(")"))
        sexp.add(parseInner(tokens));
      tokens.remove();
      return Either.right(sexp);
    } else if (token.equals(")"))
      throw new ParseException("Unexpected ')'");
    else
      return Either.left(token);
  }

  public static Sexpression parse(String string) throws ParseException {
    var parsed = parseInner(new LinkedList<>(tokenize(removeComments(string))));
    return parsed.match(
        (String atom) -> new Sexpression(Either.left(atom)),
        (Sexpression atom) -> atom
    );
  }
}
