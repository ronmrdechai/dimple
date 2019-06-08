package dimple.formula;

import java.util.Map;

class FormulaEvalVisitor implements FormulaVisitor<Boolean> {
  final Map<String, Boolean> assignment;

  FormulaEvalVisitor(Map<String, Boolean> assignment) {
    this.assignment = assignment;
  }

  public Boolean visit(Atom.Var atom) {
    return assignment.get(atom.value);
  }

  public Boolean visit(UnaryConnective.Not connective) {
    return !connective.argument.accept(this);
  }

  public Boolean visit(BinaryConnective.And connective) {
    return connective.left.accept(this) && connective.right.accept(this);
  }

  public Boolean visit(BinaryConnective.Or connective) {
    return connective.left.accept(this) || connective.right.accept(this);
  }

  public Boolean visit(BinaryConnective.If connective) {
    return !connective.left.accept(this) || connective.right.accept(this);
  }

  public Boolean visit(BinaryConnective.Iff connective) {
    return (connective.left.accept(this) || !connective.right.accept(this))
      && (!connective.left.accept(this) || connective.right.accept(this));
  }
}
