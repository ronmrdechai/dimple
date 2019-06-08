package dimple.sexp;

import java.util.LinkedList;

public class Sexpression extends LinkedList<Either<String, Sexpression>> {
  Sexpression() {
    super();
  }

  Sexpression(Either<String, Sexpression> atom) {
    super();
    add(atom);
  }
}
