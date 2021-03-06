package com.example.repository;

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

	// Twitter4J（Twitterにアクセスする役割の外部部品プログラム）
  private Twitter twitter;

  // データの初期化
  public TwitterRepository() {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(CONSUMER_KEY)
      .setOAuthConsumerSecret(CONSUMER_SECRET)
      .setOAuthAccessToken(ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
    twitter = new TwitterFactory(cb.build()).getInstance();
  }

  // Twitterから limit 件、word での検索結果を取り出し、TimeLineBlock部品に情報をいれる
  public List<TimeLineBlock> getTimeLineBlocks(int limit, String word) throws TwitterException {
    requireNonNull(word);
    if (word.isEmpty()) {
      return emptyList();
    }
    System.out.println("get status...");
    return twitter.search(new Query(word))
      .getTweets()
      .stream()
      .filter(s -> !s.getUser().isProtected())
      .limit(limit)
      .map(TimeLineBlock::new)
      .collect(toList());
  }

}