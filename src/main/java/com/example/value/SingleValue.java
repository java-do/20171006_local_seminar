package com.example.value;

import static java.util.Objects.requireNonNull;

public abstract class SingleValue<T> {

	private final T value;

	public SingleValue(T value) {
		requireNonNull(value, "value が null.");
		validate(value);
		this.value = value;
	}

	protected abstract void validate(T value);

	public T getValue() {
		return value;
	}

}
