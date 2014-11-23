package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;
import com.netbrasoft.gnuob.wicket.bootstrap.ajax.markup.html.BootstrapAjaxLink;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapForm;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapNumberTextField;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapTextArea;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapTextField;

public class CategoryViewPanel extends Panel {

   class CategoryViewOrEditForm extends BootstrapForm<Category> {

      private static final long serialVersionUID = 964236234970913381L;

      public CategoryViewOrEditForm(String id, IModel<Category> model) {
         super(id, model);
      }

      @Override
      protected void onInitialize() {
         super.onInitialize();
         setOutputMarkupId(true);
      }
   };

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 5;

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;
   private Category model = new Category();
   private AjaxLink<Void> addButton = new AjaxLink<Void>("addButton") {

      private static final long serialVersionUID = 9191172039973638020L;

      @Override
      public void onClick(AjaxRequestTarget arg0) {
         // TODO Auto-generated method stub

      }
   };
   private AjaxLink<Void> removeButton = new AjaxLink<Void>("removeButton") {

      private static final long serialVersionUID = 9191172039973638021L;

      @Override
      public void onClick(AjaxRequestTarget arg0) {
         // TODO Auto-generated method stub

      }
   };
   private OrderByBorder<String> positionOrderByBorder = new OrderByBorder<String>("orderByPosition", "position", categoryDataProvider);
   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", categoryDataProvider);
   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", categoryDataProvider);
   private DataView<Category> categoryDataview = new DataView<Category>("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Category> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Category>(paramItem.getModelObject()));
         paramItem.add(new Label("position"));
         paramItem.add(new Label("name"));
         paramItem.add(new Label("description"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               categoryViewOrEditForm.setModel(new CompoundPropertyModel<Category>(paramItem.getModelObject()));
               target.add(categoryViewOrEditForm);
            }
         });
      }
   };
   private ItemsPerPagePagingNavigator categoryPagingNavigator = new ItemsPerPagePagingNavigator("categoryPagingNavigator", categoryDataview);
   private CategoryViewOrEditForm categoryViewOrEditForm = new CategoryViewOrEditForm("categoryViewOrEditForm", new CompoundPropertyModel<Category>(new Category()));
   private AjaxLink<Void> editButton = new AjaxLink<Void>("editButton") {

      private static final long serialVersionUID = 9191172039973638022L;

      @Override
      public void onClick(AjaxRequestTarget target) {
         categoryViewOrEditForm.removeAll();
         categoryViewOrEditForm.add(new Fragment("categoryViewOrEditFragement", "categoryEditFragement", CategoryViewPanel.this) {

            private static final long serialVersionUID = 1543659855459551650L;

            @Override
            protected void onInitialize() {
               super.onInitialize();
               add(editButton);
               add(cancelButton);
               add(saveButton);
               add(new BootstrapNumberTextField<Integer>("position"));
               add(new BootstrapTextField<String>("name"));
               add(new BootstrapTextArea<String>("description"));
            }
         });

         cancelButton.setVisible(true);
         saveButton.setVisible(true);
         editButton.setEnabled(false);
         target.add(categoryViewOrEditForm);
      }
   };
   private AjaxLink<Void> cancelButton = new AjaxLink<Void>("cancelButton") {

      private static final long serialVersionUID = 9191172039973638022L;

      @Override
      public void onClick(AjaxRequestTarget target) {
         categoryViewOrEditForm.removeAll();
         categoryViewOrEditForm.add(new Fragment("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewPanel.this) {

            private static final long serialVersionUID = 5133082553128798473L;

            @Override
            protected void onInitialize() {
               super.onInitialize();
               add(editButton);
               add(cancelButton);
               add(saveButton);
               add(new Label("position"));
               add(new Label("name"));
               add(new Label("description"));
            }
         });

         cancelButton.setVisible(false);
         saveButton.setVisible(false);
         editButton.setEnabled(true);
         target.add(categoryViewOrEditForm);
      }

      @Override
      protected void onInitialize() {
         super.onInitialize();
         setVisible(false);
      };
   };
   private BootstrapAjaxLink<Void> saveButton = new BootstrapAjaxLink<Void>("saveButton", true) {

      private static final long serialVersionUID = 9191172039973638022L;

      @Override
      public void onCancel(AjaxRequestTarget target) {
         return;
      }

      @Override
      public void onConfirm(AjaxRequestTarget target) {
         Category category = categoryViewOrEditForm.getModelObject();

         categoryDataProvider.setType(category);
         categoryDataProvider.merge();

         categoryViewOrEditForm.setModel(new CompoundPropertyModel<Category>(categoryDataProvider.findById()));
         categoryDataProvider.setType(model);
         
         cancelButton.onClick(target);

         setResponsePage(getPage());
      }

      @Override
      protected void onInitialize() {
         super.onInitialize();
         setVisible(false);
      }
   };

   public CategoryViewPanel(String id) {
      super(id);
      model.setActive(true);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      categoryDataProvider.setUser(roleSession.getUsername());
      categoryDataProvider.setPassword(roleSession.getPassword());
      categoryDataProvider.setSite(roleSession.getSite());
      categoryDataProvider.setType(model);

      categoryViewOrEditForm.add(new Fragment("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewPanel.this) {

         private static final long serialVersionUID = 5133082553128798473L;

         @Override
         protected void onInitialize() {
            super.onInitialize();
            add(editButton);
            add(cancelButton);
            add(saveButton);
            add(new Label("position"));
            add(new Label("name"));
            add(new Label("description"));
         }
      });

      add(addButton);
      add(removeButton);
      add(positionOrderByBorder);
      add(orderByName);
      add(orderByDescription);
      add(categoryDataview);
      add(categoryPagingNavigator);
      add(categoryViewOrEditForm);
   }
}
