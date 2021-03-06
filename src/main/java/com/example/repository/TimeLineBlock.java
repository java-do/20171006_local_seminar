package com.example.repository;

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

  // タイムラインを構成する部品プログラム
  private final Tweet tweet;
  private final ScreenName screenName;
  private final URI iconURI;
  private final ZonedDateTime tweetAt;

  // データの初期化
  public TimeLineBlock(Status status) {
    requireNonNull(status);
    tweet = new Tweet(status.getText());

    User user = status.getUser();
    screenName = new ScreenName(user.getScreenName());
    iconURI = URI.create(user.getBiggerProfileImageURL());

    Instant instant = status.getCreatedAt().toInstant();
    tweetAt = instant.atZone(ZoneId.systemDefault());
  }

  // URIデータを文字列で呼び出し
  public String getIconUrI() {
    return iconURI.toString();
  }

  // URIデータの拡張子を呼び出し
  public String getExtention() {
    String uri = iconURI.toString();
    int index = uri.lastIndexOf(".") + 1;
    String extension = uri.substring(index);
    return Objects.equals(extension, "jpeg") ? "jpg" : extension;
  }

  // タイムラインの文章部分を文字列で呼び出し
  public String getBlockMessage() {
    return String.format("%s\t%s\n%s",
      screenName.getValue(), tweetAt.toString(), tweet.getValue());
  }

}
