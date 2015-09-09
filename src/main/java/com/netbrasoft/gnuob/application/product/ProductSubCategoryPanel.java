package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductSubCategoryPanel extends Panel {

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class CategoryDataview extends DataView<Category> {

      private static final long serialVersionUID = -5039874949058607907L;

      private long selectedObjectId;

      protected CategoryDataview() {
         super("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Category> newItem(String id, int index, IModel<Category> model) {
         final Item<Category> item = super.newItem(id, index, model);
         final long modelObjectId = ((Category) subCategoryViewOrEditPanel.getDefaultModelObject()).getId();

         if ((model.getObject().getId() == modelObjectId) || modelObjectId == 0) {
            item.add(new BootstrapBaseBehavior() {

               private static final long serialVersionUID = -4903722864597601489L;

               @Override
               public void onComponentTag(Component component, ComponentTag tag) {
                  Attributes.addClass(tag, "info");
               }
            });
         }
         return item;
      }

      @Override
      protected void onConfigure() {
         if (selectedObjectId != ((Product) ProductSubCategoryPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Product) ProductSubCategoryPanel.this.getDefaultModelObject()).getId();
         }
         super.onConfigure();
      }

      @Override
      protected void populateItem(Item<Category> item) {
         item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
         item.add(new Label("name"));
         item.add(new Label("position"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               subCategoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(categoryDataviewContainer.setOutputMarkupId(true));
               target.add(subCategoryViewOrEditPanel.setOutputMarkupId(true));
            }
         });

         if (item.getIndex() == 0 && ((Category) subCategoryViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            subCategoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class ProductSubCategoryEditFragement extends Fragment {

      private static final long serialVersionUID = -3118710638006841019L;

      public ProductSubCategoryEditFragement() {
         super("productSubCategoryViewOrEditFragement", "productSubCategoryEditFragement", ProductSubCategoryPanel.this, ProductSubCategoryPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(categoryEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ProductSubCategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 2822717929419097201L;

      public ProductSubCategoryViewFragement() {
         super("productSubCategoryViewOrEditFragement", "productSubCategoryViewFragement", ProductSubCategoryPanel.this, ProductSubCategoryPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(categoryViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;

   private final CategoryDataview categoryDataview;

   private final OrderByBorder<String> orderByposition;

   private final OrderByBorder<String> orderByName;

   private final WebMarkupContainer categoryDataviewContainer;

   private final BootstrapPagingNavigator categoryPagingNavigator;

   private final ProductSubCategoryViewOrEditPanel subCategoryViewOrEditPanel;

   private final WebMarkupContainer categoryEditTable;

   private final WebMarkupContainer categoryViewTable;

   public ProductSubCategoryPanel(final String id, final IModel<Product> model) {
      super(id, model);

      orderByposition = new OrderByBorder<String>("orderByPosition", "position", categoryDataProvider);
      orderByName = new OrderByBorder<String>("orderByName", "name", categoryDataProvider);
      categoryDataview = new CategoryDataview();
      categoryPagingNavigator = new BootstrapPagingNavigator("categoryPagingNavigator", categoryDataview);
      categoryDataviewContainer = new WebMarkupContainer("categoryDataviewContainer") {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(categoryDataview);
            super.onInitialize();
         }
      };
      categoryEditTable = new WebMarkupContainer("categoryEditTable", getDefaultModel()) {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(orderByposition);
            add(orderByName);
            add(categoryDataviewContainer.setOutputMarkupId(true));
            add(categoryPagingNavigator);
            add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
            add(new TableBehavior().hover());
            super.onInitialize();
         }
      };
      categoryViewTable = new WebMarkupContainer("categoryViewTable", getDefaultModel()) {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(orderByposition);
            add(orderByName);
            add(categoryDataviewContainer.setOutputMarkupId(true));
            add(categoryPagingNavigator);
            add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
            add(new TableBehavior().hover());
            super.onInitialize();
         }
      };
      subCategoryViewOrEditPanel = new ProductSubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", Model.of(new Category()), ProductSubCategoryPanel.this);
   }

   @Override
   protected void onInitialize() {
      categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      categoryDataProvider.setType(new Category());
      categoryDataProvider.getType().setActive(true);
      super.onInitialize();
   }
}
