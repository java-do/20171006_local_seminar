package com.example.value;

public class Tweet extends SingleValue<String> {

	// 送信できる文字は140字だが、受信できる文字は140字とは限らないので、2倍に仮定
	private static final int MAX_LENGTH = 280;

	public Tweet(String value) {
		super(value);
	}

	@Override
	protected void validate(String value) {
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("value は " + MAX_LENGTH + "字以内:" + value);
		}
	}
}
