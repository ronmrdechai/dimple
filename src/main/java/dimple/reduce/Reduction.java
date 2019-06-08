package dimple.reduce;

import dimple.formula.Formula;

@FunctionalInterface
interface Reduction<P> {
  public Formula reduce(P problem);
}
