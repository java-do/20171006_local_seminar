package com.example.local;

import com.example.Data.TimeLineBlock;
import com.example.repository.TwitterRepository;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import twitter4j.TwitterException;

public class View extends PApplet {

	private int x;
	private int y;

	private ControlP5 cp5;
	private TwitterRepository service;

	@Override
	public void settings() {
		super.settings();
		size(1024, 768);
	}

	@Override
	public void setup() {
		super.setup();
		reflesh();
		PFont font = createFont("MS Gothic", 14, true);
		textFont(font);
		fill(255);
		stroke(255);
		service = new TwitterRepository();
		cp5 = new ControlP5(this);
		cp5.addTextfield("word")
			.setPosition(30, 10)
			.setSize(200, 40)
			.setColorBackground(0)
			.setCaptionLabel("Search Word")
			.setAutoClear(false)
			.setFont(font)
			.setFocus(true);
		cp5.addBang("search")
			.setPosition(270, 10)
			.setSize(80, 40)
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
	}

	@Override
	public void draw() {
		// no tasks.
	}

	private void reflesh() {
		background(0);
		x = 0;
		y = 80;
	}

	public void search() {
		String word = cp5.get(Textfield.class, "word").getText();
		cp5.get(Textfield.class, "word").clear();
		if (!word.isEmpty()) {
			makeBlocks(word);
		}
	}

	private void makeBlocks(String tag) {
		reflesh();
		try {
			service.getTimeLineBlocks(8, tag)
				.forEach(this::makeBlock);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private void makeBlock(TimeLineBlock tlb) {
		line(x, y, 1024, y);
		PImage icon = loadImage(tlb.getIconUrI(), tlb.getExtention());
		image(icon, x, y, 72, 72);
		text(tlb.getBlockMessage(), 80, y, 980, 80);
		y = y + 80;
	}

}
