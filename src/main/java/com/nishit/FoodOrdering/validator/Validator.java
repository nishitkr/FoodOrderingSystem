package com.nishit.FoodOrdering.validator;

public interface Validator<T> {
    void validate(T t);
}
