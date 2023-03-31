package at.chitas.builder;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public abstract class Pizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER}
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping(Topping topping){
            toppings.add(requireNonNull(topping));
            return self();
        }
        abstract Pizza build();
        protected abstract T self();
    }
    Pizza(Builder<?> builder){
        toppings = builder.toppings.clone();
    }
}
