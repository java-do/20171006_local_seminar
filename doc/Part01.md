# Part1 Twitterの情報を取得する

## Twitterのユーザー名（ScreenName)の役割の部品プログラムを作る

main/javaフォルダの `com.example.value.ScreenName.java` を下のようにプログラミングしましょう。

```java
package com.example.value;

import static java.util.Objects.requireNonNull;

public class ScreenName {

  // Twitterのユーザ名（ScreenName）は15文字まで
  private static final int MAX_LENGTH = 15;

  // 管理するデータ
  private String value;

  // データの初期化
  public ScreenName(String arg0) {
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

  // データの取り出し
  public String getValue() {
    return value;
  }
}

```

## Twitterのツイート文章（Tweet）の役割の部品プログラムを作る

main/javaフォルダの `com.example.value.Tweet.java` を下のようにプログラミングしましょう。

```java
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

  // データの取り出し
  public String getValue() {
    return value;
  }
}

```

## ツイッターのタイムラインの部品プログラムを作る

上で作ったScreenName, Tweetの部品プログラム、Javaの標準の部品プログラムを組み合わせて作ります。

![fig02.jpg](./fig02.jpg)

main/javaフォルダの `com.example.data.TimeLineBlock.java` を下のようにプログラミングしましょう。

```java
package com.example.data;

import com.example.value.ScreenName;
import com.example.value.Tweet;
import twitter4j.Status;
import twitter4j.User;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class TimeLineBlock {

  private final Tweet tweet;
  private final ScreenName screenName;
  private final URI iconURI;
  private final ZonedDateTime tweetAt;

  public TimeLineBlock(Status status) {
    requireNonNull(status);
    tweet = new Tweet(status.getText());

    User user = status.getUser();
    screenName = new ScreenName(user.getScreenName());
    iconURI = URI.create(user.getBiggerProfileImageURL());

    Instant instant = status.getCreatedAt().toInstant();
    tweetAt = instant.atZone(ZoneId.systemDefault());
  }

  public String getIconUrI() {
    return iconURI.toString();
  }

  public String getExtention() {
    String uri = iconURI.toString();
    int index = uri.lastIndexOf(".") + 1;
    String extension = uri.substring(index);
    return Objects.equals(extension, "jpeg") ? "jpg" : extension;
  }

  public String getBlockMessage() {
    return String.format("%s\t%s\n%s",
      screenName.getValue(), tweetAt.toString(), tweet.getValue());
  }

}

```

## Twitterに接続する役割の部品プログラムをつくる

Twitterにアクセスして、上のTimeLineBlockの部品プログラムに情報をいれるプログラムを作りましょう。

main/javaフォルダの `com.example.repository.TwitterRepository.java` を下のようにプログラミングしましょう。

`****` の部分は、自分でTwitterのサイトから取得したものに変えて下さい。 

```java
package com.example.repository;

import com.example.data.TimeLineBlock;
import twitter4j.Query;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class TwitterRepository {

  private static final String CONSUMER_KEY = "****";
  	private static final String CONSUMER_SECRET = "****";
  	private static final String ACCESS_TOKEN = "****";
  	private static final String ACCESS_TOKEN_SECRET = "****";

  private Twitter twitter;

  public TwitterRepository() {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(CONSUMER_KEY)
      .setOAuthConsumerSecret(CONSUMER_SECRET)
      .setOAuthAccessToken(ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
    twitter = new TwitterFactory(cb.build()).getInstance();
  }

  public List<TimeLineBlock> getTimeLineBlocks(int limit, String tag) throws TwitterException {
    requireNonNull(tag);
    if (tag.isEmpty()) {
      return emptyList();
    }
    System.out.println("get status...");
    return twitter.search(new Query(tag))
      .getTweets()
      .stream()
      .filter(s -> !s.getUser().isProtected())
      .limit(limit)
      .map(TimeLineBlock::new)
      .collect(toList());
  }
}
```

## 動作確認

test/java フォルダの `com.example.Test` を実行しましょう。

コンソールにツイートらしきものが表示されれば成功（のはず）です。