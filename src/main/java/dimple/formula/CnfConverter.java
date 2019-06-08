package dimple.formula;

public class CnfConverter {
  private static class NopVisitor implements FormulaVisitor<Formula> {
    @Override
    public Formula visit(Atom.Var formula) {
      return formula;
    }

    @Override
    public Formula visit(UnaryConnective.Not formula) {
      return new UnaryConnective.Not(formula.argument.accept(this));
    }

    @Override
    public Formula visit(BinaryConnective.And formula) {
      return new BinaryConnective.And(formula.left.accept(this), formula.right.accept(this));
    }

    @Override
    public Formula visit(BinaryConnective.Or formula) {
      return new BinaryConnective.Or(formula.left.accept(this), formula.right.accept(this));
    }

    @Override
    public Formula visit(BinaryConnective.If formula) {
      return new BinaryConnective.If(formula.left.accept(this), formula.right.accept(this));
    }

    @Override
    public Formula visit(BinaryConnective.Iff formula) {
      return new BinaryConnective.Iff(formula.left.accept(this), formula.right.accept(this));
    }
  }

  private static class BiconditionalEliminationVisitor extends NopVisitor {
    @Override
    public Formula visit(BinaryConnective.Iff formula) {
      return new BinaryConnective.And(
            new BinaryConnective.Or(
                formula.left.accept(this), new UnaryConnective.Not(formula.right.accept(this))),
            new BinaryConnective.Or(
                new UnaryConnective.Not(formula.left.accept(this)), formula.right.accept(this)));
    }
  }

  private static class ImplicationEliminationVisitor extends NopVisitor {
    @Override
    public Formula visit(BinaryConnective.If formula) {
      return new BinaryConnective.Or(
          new UnaryConnective.Not(formula.left.accept(this)), formula.right.accept(this));
    }
  }

  private static class NotPropagationVisitor extends NopVisitor {
    private class NotPropagationVisitorInner extends NopVisitor {
      // (not (and a b)) -> (or (not a) (not b))
      @Override
      public Formula visit(BinaryConnective.And formula) {
        return new BinaryConnective.Or(
            new UnaryConnective.Not(formula.left).accept(NotPropagationVisitor.this),
            new UnaryConnective.Not(formula.right).accept(NotPropagationVisitor.this));
      }

      // (not (or a b)) -> (and (not a) (not b))
      @Override
      public Formula visit(BinaryConnective.Or formula) {
        return new BinaryConnective.And(
            new UnaryConnective.Not(formula.left).accept(NotPropagationVisitor.this),
            new UnaryConnective.Not(formula.right).accept(NotPropagationVisitor.this));
      }

      // (not (not a)) -> a
      @Override
      public Formula visit(UnaryConnective.Not formula) {
        return formula.argument.accept(NotPropagationVisitor.this);
      }

      // (not a) -> (not a)
      @Override
      public Formula visit(Atom.Var formula) {
        return new UnaryConnective.Not(formula.accept(NotPropagationVisitor.this));
      }
    }

    NotPropagationVisitorInner inner = new NotPropagationVisitorInner();

    @Override
    public Formula visit(UnaryConnective.Not formula) {
      return formula.argument.accept(inner);
    }
  }

  private static class OrOverAndDistributionVisitor extends NopVisitor {
    private Formula visitInner(Formula left, BinaryConnective.And right) {
      return new BinaryConnective.And(
          new BinaryConnective.Or(left.accept(this), right.left.accept(this)),
          new BinaryConnective.Or(left.accept(this), right.right.accept(this)));
    }

    @Override
    public Formula visit(BinaryConnective.Or formula) {
      if (formula.right instanceof BinaryConnective.And)
        return visitInner(formula.left, (BinaryConnective.And)formula.right);
      if (formula.left instanceof BinaryConnective.And)
        return visitInner(formula.right, (BinaryConnective.And)formula.left);
      else
        return super.visit(formula);
    }
  }

  public static Formula convert(Formula formula) {
    formula = formula.accept(new BiconditionalEliminationVisitor());
    formula = formula.accept(new ImplicationEliminationVisitor());
    formula = formula.accept(new NotPropagationVisitor());
    formula = formula.accept(new OrOverAndDistributionVisitor());
    return formula;
  }
}
