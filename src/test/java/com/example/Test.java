package com.example;

import com.example.data.TimeLineBlock;
import com.example.repository.TwitterRepository;
import twitter4j.TwitterException;

public class Test {

  public static void main(String[] args) throws TwitterException {
    TwitterRepository sut = new TwitterRepository();
    sut.getTimeLineBlocks(3, "旭川")
      .stream()
      .map(TimeLineBlock::getBlockMessage)
      .forEach(System.out::println);
  }

}