package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;

public class CategoryTab extends AbstractTab {

   private static final long serialVersionUID = 4835579949680085443L;

   public CategoryTab(IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(String panelId) {
      Category category = new Category();
      category.setActive(true);
      return new CategoryPanel(panelId, new Model<Category>(category));
   }
}