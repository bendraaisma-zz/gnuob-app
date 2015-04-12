package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.SubCategory;

public class SubCategoryOperationPanel extends Panel {

   private static final long serialVersionUID = 7557158822058584007L;

   public SubCategoryOperationPanel(final String id, final IModel<SubCategory> model) {
      super(id, model);
   }
}
