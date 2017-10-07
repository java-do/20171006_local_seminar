package com.example.value;

import static java.util.Objects.requireNonNull;

public class Tweet {

  // 送信できる文字は140字だが、受信できる文字は140字とは限らないので、2倍に仮定
  private static final int MAX_LENGTH = 280;

  // 管理するデータ
  private String value;

  // データの初期化
  public Tweet(String arg0) {
    requireNonNull(arg0);
    validate(arg0);
    value = arg0;
  }

  // データの整合性チェック
  private void validate(String arg0) {
    if (arg0.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("value は " + MAX_LENGTH + "字以内:" + arg0);
    }
  }

  // データの呼び出し
  public String getValue() {
    return value;
  }
}
