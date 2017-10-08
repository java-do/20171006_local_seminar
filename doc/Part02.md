# Part2 WebアプリでTwitterを検索できるようにする

Java は Webシステムでよく使われます。

特に最近は、スマホのアプリやブラウザ上のプログラムにデータを渡す Web-API と、Web-APIで受け渡しされるデータの管理や分散処理に使われます。（こうした役割をバックエンドと呼びます）

これの作成を短い時間で体験するのは残念ながら大変なので、一昔前の方法ですが、JavaでWebサイト（Webアプリ）を作ってみましょう。

(Apache Wicketという、Webアプリケーション開発用の土台部品：フレームワークを使っています)

## Webアプリの画面（HTML）を作る

main/javaフォルダの `com.example.web.HomePage.html` を下のようにプログラミングしましょう。

```html
<!DOCTYPE html>
<html xmlns:wicket="http://wicket.apache.org">
<head>
  <meta charset="utf-8"/>
  <style>
    form {
      margin-bottom: 10px;
    }

    div.timeline {
      display: flex;
      border-top: thin solid gray;
    }

    div.icon {
      width: 80px;
    }

    p {
      margin: 0;
    }
  </style>
</head>
<body>
<form wicket:id="form">
  検索ワード：<input type="text" wicket:id="word">
  <button type="submit">検索</button>
</form>
<div class="timeline" wicket:id="timeLine">
  <div class="icon">
    <img wicket:id="iconUrI"/>
  </div>
  <div wicket:id="blockMessage">blockMessage</div>
</div>
</body>
</html>

}

```

## HTMLに部品を埋め込むJavaプログラムを作る

main/javaフォルダの `com.example.web.HomePage.java` を下のようにプログラミングしましょう。

```java
package com.example.web;

import com.example.repository.TimeLineBlock;
import com.example.repository.TwitterRepository;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import twitter4j.TwitterException;

import java.util.List;

import static java.util.Collections.emptyList;

public class HomePage extends WebPage {

  // 検索ワードのデータ
  private IModel<String> wordModel;

  // HTMLへの部品の埋め込み
  public HomePage() {

    // 送信フォーム部品を設定して埋め込み
    Form<Void> form = new Form<>("form");
    this.add(form);

    // 送信フォームのテキスト入力欄部品を設定して埋め込み
    wordModel = new Model<>("");
    form.add(new TextField<>("word", wordModel));

    // 検索ワードのデータを使い、Twitterのタイムライン部品を8件取得
    IModel<List<TimeLineBlock>> timeLineBlockModel =
      new LoadableDetachableModel<List<TimeLineBlock>>() {

        @Override
        protected List<TimeLineBlock> load() {
          String word = wordModel.getObject();
          TwitterRepository repo = new TwitterRepository();
          try {
            return repo.getTimeLineBlocks(8, word);
          } catch (TwitterException e) {
            e.printStackTrace();
          }
          return emptyList();
        }
      };

    // タイムライン部品をHTMLに埋め込み
    this.add(new PropertyListView<TimeLineBlock>("timeLine", timeLineBlockModel) {

      @Override
      protected void populateItem(ListItem<TimeLineBlock> listItem) {
        listItem.add(new ExternalImage("iconUrI"));
        listItem.add(new MultiLineLabel("blockMessage"));
      }
    });
  }
}

```

## 動作確認

test/java フォルダの `com.example.Web` を実行しましょう。

ブラウザで、 [http://localhost:8080/](http://localhost:8080/) にアクセスしてみましょう。

検索ワードに何か文字を入れて、検索ボタンを押すと、タイムラインが表示されることを確認しましょう。

