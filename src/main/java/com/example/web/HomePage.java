package com.example.web;

import com.example.Data.TimeLineBlock;
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

	private IModel<String> wordModel;

	public HomePage() {
		Form<Void> form = new Form<>("form");
		this.add(form);

		wordModel = new Model<>("");
		form.add(new TextField<>("word", wordModel));

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

		this.add(new PropertyListView<TimeLineBlock>("timeLine", timeLineBlockModel) {

			@Override
			protected void populateItem(ListItem<TimeLineBlock> listItem) {
				listItem.add(new ExternalImage("iconUrI"));
				listItem.add(new MultiLineLabel("blockMessage"));
			}
		});
	}
}
