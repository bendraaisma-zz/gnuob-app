package com.netbrasoft.gnuob.application.category;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableTreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.SubCategory;

@SuppressWarnings("unchecked")
public class SubCategoryTreeProvider<S extends SubCategory> extends SortableTreeProvider<S, String> {
   private static final long serialVersionUID = -592161727647897932L;
   private IModel<Category> model;

   public SubCategoryTreeProvider(final IModel<Category> model) {
      this.model = model;
   }

   public void add(S source, S target) {
      List<S> subCategorySourceParent = getParent(source, (List<S>) model.getObject().getSubCategories());
      target.getSubCategories().add(source);
      subCategorySourceParent.remove(source);
   }

   public void addAfter(S source, S target) {
      List<S> subCategorySourceParent = getParent(source, (List<S>) model.getObject().getSubCategories());
      List<S> subCategoryParent = getParent(target, (List<S>) model.getObject().getSubCategories());

      if (subCategoryParent != null) {
         int index = subCategoryParent.indexOf(target);
         subCategoryParent.add(index + 1, source);

         subCategorySourceParent.remove(source);
      }

   }

   public void addBefore(S source, S target) {
      List<S> subCategorySourceParent = getParent(source, (List<S>) model.getObject().getSubCategories());
      List<S> subCategoryParent = getParent(target, (List<S>) model.getObject().getSubCategories());

      if (subCategoryParent != null) {
         int index = subCategoryParent.indexOf(target);
         subCategoryParent.add(index, source);
         subCategorySourceParent.remove(source);
      }
   }

   @Override
   public Iterator<? extends S> getChildren(S node) {
      return (Iterator<? extends S>) node.getSubCategories().iterator();
   }

   private List<S> getParent(S subCategory, List<S> subCategories) {

      if (subCategories.contains(subCategory)) {
         return subCategories;
      }

      for (S sub : subCategories) {
         List<S> results = getParent(subCategory, (List<S>) sub.getSubCategories());

         if (results != null && results.contains(subCategory)) {
            return results;
         }
      }

      return null;
   }

   @Override
   public Iterator<? extends S> getRoots() {
      return (Iterator<S>) model.getObject().getSubCategories().iterator();
   }

   @Override
   public boolean hasChildren(S node) {
      return !model.getObject().getSubCategories().isEmpty();
   }

   @Override
   public IModel<S> model(S object) {
      return new Model<S>(object);
   }
}
