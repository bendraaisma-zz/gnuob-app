package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapNumberTextField;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapTextField;

@SuppressWarnings("unchecked")
public class CategoryViewOrEditPanel extends Panel {

   private static final long serialVersionUID = 3968615764565588442L;

   public CategoryViewOrEditPanel(String id, IModel<Category> model) {
      super(id, model);
      add(createCategoryViewFragement());
   }

   private AjaxLink<Void> createCancelAjaxLink() {
      return new AjaxLink<Void>("cancel") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            CategoryViewOrEditPanel.this.removeAll();
            CategoryViewOrEditPanel.this.add(createCategoryViewFragement());
            paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
         }
      };
   }

   private Fragment createCategoryEditFragement() {
      return new Fragment("categoryViewOrEditFragement", "categoryEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 5133082553128798473L;

         @Override
         protected void onInitialize() {
            boolean enableOperations = true;
            Form<Category> categoryEditForm = new Form<Category>("categoryEditForm");

            categoryEditForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
            categoryEditForm.add(new BootstrapNumberTextField<Integer>("position"));
            categoryEditForm.add(new BootstrapTextField<String>("name"));
            categoryEditForm.add(new Label("description"));
            categoryEditForm.add(new ContentPanel("contentPanel", (IModel<Category>) getDefaultModel(), enableOperations));
            categoryEditForm.add(new SubCategoryPanel("subCategoriesPanel", (IModel<Category>) getDefaultModel(), enableOperations));

            add(categoryEditForm.setOutputMarkupId(enableOperations));
            add(createCancelAjaxLink().setOutputMarkupId(enableOperations));
            add(createSaveAjaxLink().setOutputMarkupId(enableOperations));

            setOutputMarkupId(enableOperations);
            super.onInitialize();
         }
      };
   }

   private Fragment createCategoryViewFragement() {
      return new Fragment("categoryViewOrEditFragement", "categoryViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 5863708936560086113L;

         @Override
         protected void onInitialize() {
            boolean enableOperations = false;
            Form<Category> categoryViewForm = new Form<Category>("categoryViewForm");

            categoryViewForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
            categoryViewForm.add(new Label("position"));
            categoryViewForm.add(new Label("name"));
            categoryViewForm.add(new Label("description"));
            categoryViewForm.add(new ContentPanel("contentPanel", (IModel<Category>) getDefaultModel(), enableOperations));
            categoryViewForm.add(new SubCategoryPanel("subCategoriesPanel", (IModel<Category>) getDefaultModel(), enableOperations));

            add(createEditAjaxLink().setOutputMarkupId(true));
            add(categoryViewForm.setOutputMarkupId(true));

            setOutputMarkupId(true);
            super.onInitialize();
         }
      };
   }

   private AjaxLink<Void> createEditAjaxLink() {
      return new AjaxLink<Void>("edit") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            CategoryViewOrEditPanel.this.removeAll();
            CategoryViewOrEditPanel.this.add(createCategoryEditFragement());
            paramAjaxRequestTarget.add(CategoryViewOrEditPanel.this);
         }
      };
   }

   private AjaxLink<Void> createSaveAjaxLink() {
      return new AjaxLink<Void>("save") {

         private static final long serialVersionUID = 2695394292963384938L;

         @Override
         public void onClick(AjaxRequestTarget target) {
            // TODO Auto-generated method stub
         }
      };
   }

   @Override
   protected void onInitialize() {
      setOutputMarkupId(true);
      super.onInitialize();
   }
}
