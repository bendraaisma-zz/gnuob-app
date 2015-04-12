package com.netbrasoft.gnuob.application.category;

import java.util.List;

import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;

public class ContentListDataProvider extends ListDataProvider<Content> {

   private static final long serialVersionUID = 5259243752700177690L;
   private IModel<Category> model;

   public ContentListDataProvider(final IModel<Category> model) {
      this.model = model;
   }

   @Override
   protected List<Content> getData() {
      return model.getObject().getContents();
   }
}
