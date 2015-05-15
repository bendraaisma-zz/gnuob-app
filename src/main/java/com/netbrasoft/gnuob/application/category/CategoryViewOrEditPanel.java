package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

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

   class CategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 5133082553128798473L;

      public CategoryEditFragement() {
         super("categoryViewOrEditFragement", "categoryEditFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Category>) getDefaultModel());
         SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Category>) getDefaultModel());

         Form<Category> categoryEditForm = new Form<Category>("categoryEditForm");

         categoryEditForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
         categoryEditForm.add(new NumberTextField<Integer>("position"));
         categoryEditForm.add(new TextField<String>("name"));
         categoryEditForm.add(new TextArea<String>("description"));

         add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentEditFragement()).setOutputMarkupId(true));
         add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
         add(categoryEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         add(new SaveAjaxButton(categoryEditForm).setOutputMarkupId(true));

         super.onInitialize();
      }
   }

   class CategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 5863708936560086113L;

      public CategoryViewFragement() {
         super("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewOrEditPanel.this, CategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Category>) getDefaultModel());
         SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Category>) getDefaultModel());
         Form<Category> categoryViewForm = new Form<Category>("categoryViewForm");

         categoryViewForm.setModel(new CompoundPropertyModel<Category>((IModel<Category>) getDefaultModel()));
         categoryViewForm.add(new NumberTextField<Integer>("position"));
         categoryViewForm.add(new TextField<String>("name"));
         categoryViewForm.add(new Label("description"));

         add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentViewFragement()).setOutputMarkupId(true));
         add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
         add(new EditAjaxLink().setOutputMarkupId(true));
         add(categoryViewForm.setOutputMarkupId(true));

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
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Category category = (Category) form.getDefaultModelObject();

            if (category.getId() == 0) {
               category.setActive(true);
               categoryDataProvider.persist(category);
            } else {
               categoryDataProvider.merge(category);
            }

            CategoryViewOrEditPanel.this.removeAll();
            CategoryViewOrEditPanel.this.add(new CategoryViewFragement().setOutputMarkupId(true));
         } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);

            String[] messages = e.getMessage().split(": ");
            String message = messages[messages.length - 1];

            warn(message.substring(0, 1).toUpperCase() + message.substring(1));
         } finally {
            target.add(target.getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(CategoryViewOrEditPanel.class);

   private static final long serialVersionUID = 3968615764565588442L;

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;

   public CategoryViewOrEditPanel(String id, IModel<Category> model) {
      super(id, model);
   }
}
