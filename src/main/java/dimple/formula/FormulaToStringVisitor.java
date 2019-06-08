package dimple.formula;

class FormulaToStringVisitor implements FormulaVisitor<String> {
  public String visit(Atom.Var atom) {
    return atom.value;
  }

  public String visit(UnaryConnective.Not connective) {
    return String.format("(not %s)", connective.argument.accept(this));
  }

  public String visit(BinaryConnective.And connective) {
    return String.format("(and %s %s)",
        connective.left.accept(this), connective.right.accept(this));
  }

  public String visit(BinaryConnective.Or connective) {
    return String.format("(or %s %s)", connective.left.accept(this), connective.right.accept(this));
  }

  public String visit(BinaryConnective.If connective) {
    return String.format("(if %s %s)", connective.left.accept(this), connective.right.accept(this));
  }

  public String visit(BinaryConnective.Iff connective) {
    return String.format("(iff %s %s)",
        connective.left.accept(this), connective.right.accept(this));
  }
}
