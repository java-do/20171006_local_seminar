# Part3 ローカルアプリでTwitterを検索できるようにする

Java でもローカルアプリを作ることができます。

たとえば、Androidアプリの多くが、Javaで作られています。

一方でPC用のデスクトップアプリの分野では、Javaが活躍できている場は少ないようです。

Androidの体験には、やはり機材の準備などが必要なので、ここではJavaでアートやデザインを行うことができるProcessingという開発土台を使って、簡易的なデスクトップアプリをつくってみましょう。

## デスクトップアプリの画面を作る

main/javaフォルダの `com.example.local.View.java` を下のようにプログラミングしましょう。

```java
package com.example.local;

import com.example.repository.TimeLineBlock;
import com.example.repository.TwitterRepository;
import controlP5.ControlP5;
import controlP5.Textfield;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import twitter4j.TwitterException;

public class View extends PApplet {

  //画面上の部品の描画座標
  private int x;
  private int y;

  //テキスト入力欄用の部品プログラム
  private ControlP5 cp5;
  
  //Twitter接続用の部品プログラム
  private TwitterRepository service;

  //アプリの設定（アプリの縦横サイズ）
  @Override
  public void settings() {
    super.settings();
    size(1024, 768);
  }

  //初期設定（フォント、色、入力欄など）
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

  // フレームレートごとの処理
  @Override
  public void draw() {
    // no tasks.
  }

  // 座標と背景色の初期化
  private void reflesh() {
    background(0);
    x = 0;
    y = 80;
  }

  // serachボタンが押された時の処理（検索ワードの取り出し）
  public void search() {
    String word = cp5.get(Textfield.class, "word").getText();
    cp5.get(Textfield.class, "word").clear();
    if (!word.isEmpty()) {
      makeBlocks(word);
    }
  }

  // 検索ワードのデータを使い、Twitterのタイムライン部品を8件取得
  private void makeBlocks(String tag) {
    reflesh();
    try {
      service.getTimeLineBlocks(8, tag)
        .forEach(this::makeBlock);
    } catch (TwitterException e) {
      e.printStackTrace();
    }
  }

  //Twitterのタイムライン部品を、描画座標を変えながら組み込む
  private void makeBlock(TimeLineBlock tlb) {
    line(x, y, 1024, y);
    PImage icon = loadImage(tlb.getIconUrI(), tlb.getExtention());
    image(icon, x, y, 72, 72);
    text(tlb.getBlockMessage(), 80, y, 980, 80);
    y = y + 80;
  }

}

```

## 動作確認

test/java フォルダの `com.example.Local` を実行しましょう。

検索ワードに何か文字（簡易アプリなので、英数字しか入りません）を入れて、Searchボタンを押すと、タイムラインが表示されることを確認しましょう。

