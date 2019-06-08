package dimple.formula;

class IsCnfVisitor implements FormulaVisitor<Boolean> {
  final boolean encounteredOr;

  IsCnfVisitor() {
    this(false);
  }

  private IsCnfVisitor(boolean encounteredOr) {
    this.encounteredOr = encounteredOr;
  }

  public Boolean visit(Atom.Var atom) {
    return true;
  }

  public Boolean visit(UnaryConnective.Not connective) {
    return connective.argument.accept(this);
  }

  public Boolean visit(BinaryConnective.And connective) {
    if (encounteredOr)
      return false;
    return connective.left.accept(this) && connective.right.accept(this);
  }

  public Boolean visit(BinaryConnective.Or connective) {
    return connective.left.accept(new IsCnfVisitor(true))
      && connective.right.accept(new IsCnfVisitor(true));
  }

  public Boolean visit(BinaryConnective.If connective) {
    return false; // Prepositional logic is not part of CNF
  }

  public Boolean visit(BinaryConnective.Iff connective) {
    return false; // Prepositional logic is not part of CNF
  }
}
