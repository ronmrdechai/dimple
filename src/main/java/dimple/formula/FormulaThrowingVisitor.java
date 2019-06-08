package dimple.formula;

import com.google.common.base.Preconditions;

public class FormulaThrowingVisitor<T> implements FormulaVisitor<T> {
  String message;

  FormulaThrowingVisitor(String message) {
    this.message = message;
  }

  @Override
  public T visit(Atom.Var atom) {
    Preconditions.checkState(false, message);
    return null;
  }

  @Override
  public T visit(UnaryConnective.Not connective) {
    Preconditions.checkState(false, message);
    return null;
  }

  @Override
  public T visit(BinaryConnective.And connective) {
    Preconditions.checkState(false, message);
    return null;
  }

  @Override
  public T visit(BinaryConnective.Or connective) {
    Preconditions.checkState(false, message);
    return null;
  }

  @Override
  public T visit(BinaryConnective.If connective) {
    Preconditions.checkState(false, message);
    return null;
  }

  @Override
  public T visit(BinaryConnective.Iff connective) {
    Preconditions.checkState(false, message);
    return null;
  }
}
