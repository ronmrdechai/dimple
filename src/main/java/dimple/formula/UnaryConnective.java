package dimple.formula;

public abstract class UnaryConnective implements Formula {
  final Formula argument;

  UnaryConnective(Formula argument) {
    this.argument = argument;
  }

  public static class Not extends UnaryConnective {
    public Not(Formula argument) {
      super(argument);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }
}
