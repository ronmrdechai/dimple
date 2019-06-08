package dimple.formula;

interface FormulaVisitor<T> {
  default T visit(Atom.Var atom) {
    return null;
  }

  default T visit(UnaryConnective.Not connective) {
    return null;
  }

  default T visit(BinaryConnective.And connective) {
    return null;
  }

  default T visit(BinaryConnective.Or connective) {
    return null;
  }

  default T visit(BinaryConnective.If connective) {
    return null;
  }

  default T visit(BinaryConnective.Iff connective) {
    return null;
  }
}
