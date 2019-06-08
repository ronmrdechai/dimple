package dimple.formula;

public abstract class BinaryConnective implements Formula {
  final Formula left;
  final Formula right;

  BinaryConnective(Formula left, Formula right) {
    this.left = left;
    this.right = right;
  }

  public static class And extends BinaryConnective {
    public And(Formula left, Formula right) {
      super(left, right);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Or extends BinaryConnective {
    public Or(Formula left, Formula right) {
      super(left, right);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }

  public static class If extends BinaryConnective {
    public If(Formula left, Formula right) {
      super(left, right);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }

  public static class Iff extends BinaryConnective {
    public Iff(Formula left, Formula right) {
      super(left, right);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }
}
