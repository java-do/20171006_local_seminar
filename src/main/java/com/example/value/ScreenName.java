package com.example.value;

public class ScreenName extends SingleValue<String> {

	private static final int MAX_LENGTH = 15;

	public ScreenName(String value) {
		super(value);
	}

	@Override
	protected void validate(String value) {
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("value は " + MAX_LENGTH + "字以内:" + value);
		}
	}

}
