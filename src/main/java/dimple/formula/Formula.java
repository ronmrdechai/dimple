package dimple.formula;

public interface Formula {
  public <T> T accept(FormulaVisitor<T> visitor);
}
