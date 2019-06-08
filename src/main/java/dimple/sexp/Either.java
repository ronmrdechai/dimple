package dimple.sexp;

import java.util.function.Function;

public abstract class Either<T, U> {
  private Either() {}

  public abstract <V> V match(Function<? super T, ? extends V> left,
                              Function<? super U, ? extends V> right);

  public static <T, U> Either<T, U> left(T value) {
    return new Either<>() {
      @Override
      public <V> V match(Function<? super T, ? extends V> left,
                         Function<? super U, ? extends V> right) {
        return left.apply(value);
      }
    };
  }

  public static <T, U> Either<T, U> right(U value) {
    return new Either<>() {
      @Override
      public <V> V match(Function<? super T, ? extends V> left,
                         Function<? super U, ? extends V> right) {
        return right.apply(value);
      }
    };
  }

  public String toString() {
    return match((v) -> v.toString(), (v) -> v.toString());
  }
}
