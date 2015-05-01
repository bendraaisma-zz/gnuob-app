package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.application.security.AppRoles;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CategoryViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         CategoryViewOrEditPanel.this.removeAll();
         CategoryViewOrEditPanel.this.add(new CategoryViewFragement()).setOutputMarkupId(true);
         paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
      }
   }

   class CategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 5863708936560086113L;

      public CategoryViewFragement() {
         super("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         boolean enableOperations = false;
         Form<Category> categoryViewForm = new Form<Category>("categoryViewForm");

         categoryViewForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
         categoryViewForm.add(new Label("position"));
         categoryViewForm.add(new Label("name"));
         categoryViewForm.add(new Label("description"));

         add(new ContentPanel("contentPanel", (IModel<Category>) getDefaultModel(), enableOperations).setOutputMarkupId(true));
         add(new SubCategoryPanel("subCategoriesPanel", (IModel<Category>) getDefaultModel(), enableOperations).setOutputMarkupId(true));
         add(new EditAjaxLink().setOutputMarkupId(true));
         add(categoryViewForm.setOutputMarkupId(true));

         super.onInitialize();
      }
   }

   class CategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 5133082553128798473L;

      public CategoryEditFragement() {
         super("categoryViewOrEditFragement", "categoryEditFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         boolean enableOperations = true;
         Form<Category> categoryEditForm = new Form<Category>("categoryEditForm");

         categoryEditForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
         categoryEditForm.add(new NumberTextField<Integer>("position"));
         categoryEditForm.add(new TextField<String>("name"));
         categoryEditForm.add(new TextArea<String>("description"));

         add(new ContentPanel("contentPanel", (IModel<Category>) getDefaultModel(), enableOperations).setOutputMarkupId(true));
         add(new SubCategoryPanel("subCategoriesPanel", (IModel<Category>) getDefaultModel(), enableOperations).setOutputMarkupId(true));
         add(categoryEditForm.setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         add(new SaveAjaxLink().setOutputMarkupId(true));

         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         CategoryViewOrEditPanel.this.removeAll();
         CategoryViewOrEditPanel.this.add(new CategoryEditFragement().setOutputMarkupId(true));
         paramAjaxRequestTarget.add(CategoryViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxLink() {
         super("save");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = 3968615764565588442L;

   public CategoryViewOrEditPanel(String id, IModel<Category> model) {
      super(id, model);
      add(new CategoryViewFragement().setOutputMarkupId(true));
   }
}
