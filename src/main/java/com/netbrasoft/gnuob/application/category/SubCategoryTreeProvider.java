package com.netbrasoft.gnuob.application.category;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableTreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.SubCategory;

@SuppressWarnings("unchecked")
public class SubCategoryTreeProvider<SC extends SubCategory> extends SortableTreeProvider<SC, String> {
   private static final long serialVersionUID = -592161727647897932L;
   private IModel<Category> model;

   public SubCategoryTreeProvider(final IModel<Category> model) {
      this.model = model;
   }

   @Override
   public Iterator<? extends SC> getChildren(SC node) {
      return (Iterator<? extends SC>) node.getSubCategories().iterator();
   }

   @Override
   public Iterator<? extends SC> getRoots() {
      return (Iterator<SC>) model.getObject().getSubCategories().iterator();
   }

   @Override
   public boolean hasChildren(SC node) {
      return !model.getObject().getSubCategories().isEmpty();
   }

   @Override
   public IModel<SC> model(SC object) {
      return new Model<SC>(object);
   }
}
