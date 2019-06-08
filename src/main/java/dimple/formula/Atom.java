package dimple.formula;

public abstract class Atom implements Formula {
  final String value;

  Atom(String value) {
    this.value = value;
  }

  public static class Var extends Atom {
    public Var(String value) {
      super(value);
    }

    @Override
    public <T> T accept(FormulaVisitor<T> visitor) {
      return visitor.visit(this);
    }
  }
}
