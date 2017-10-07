package com.example.repository;

import com.example.Data.TimeLineBlock;
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