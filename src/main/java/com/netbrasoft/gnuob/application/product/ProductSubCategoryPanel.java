package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ProductSubCategoryPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductSubCategoryEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class CategoryEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CategoryDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
        class CategoryDataview extends DataView<Category> {

          private static final long serialVersionUID = -5039874949058607907L;

          private int index = 0;

          protected CategoryDataview(final String id, final IDataProvider<Category> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Category> newItem(String id, int index, IModel<Category> model) {
            final Item<Category> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            if (categoryDataProvider.size() > 0) {
              productSubCategoryViewOrEditPanel.setEnabled(true);
              productSubCategoryViewOrEditPanel.removeAll();
              productSubCategoryViewOrEditPanel.setSelectedModel(Model.of(categoryDataProvider.iterator(index, index + (long) 1).next()));
              productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true);
            } else {
              productSubCategoryViewOrEditPanel.setEnabled(false);
              productSubCategoryViewOrEditPanel.removeAll();
              productSubCategoryViewOrEditPanel.setSelectedModel(Model.of(new Category()));
              productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Category> item) {
            item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
            item.add(new Label("name").setOutputMarkupId(true));
            item.add(new Label("position").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productSubCategoryViewOrEditPanel.removeAll();
                productSubCategoryViewOrEditPanel.setSelectedModel(item.getModel());
                target.add(categoryDataviewContainer.setOutputMarkupId(true));
                target.add(productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
              }
            });
          }
        }

        private static final long serialVersionUID = 4047707639075180742L;

        private static final int ITEMS_PER_PAGE = 10;

        private final CategoryDataview categoryDataview;

        public CategoryDataviewContainer(String id, IModel<Product> model) {
          super(id, model);
          categoryDataview = new CategoryDataview("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(categoryDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -3198761923054137704L;

      private final CategoryDataviewContainer categoryDataviewContainer;

      private final BootstrapPagingNavigator categoryPagingNavigator;

      private final ProductSubCategoryViewOrEditPanel productSubCategoryViewOrEditPanel;

      public CategoryEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        categoryDataviewContainer = new CategoryDataviewContainer("categoryDataviewContainer", (IModel<Product>) CategoryEditTable.this.getDefaultModel());
        categoryPagingNavigator = new BootstrapPagingNavigator("categoryPagingNavigator", categoryDataviewContainer.categoryDataview);
        productSubCategoryViewOrEditPanel = new ProductSubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) CategoryEditTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(categoryDataviewContainer.setOutputMarkupId(true));
        add(categoryPagingNavigator.setOutputMarkupId(true));
        add(productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -3118710638006841019L;

    private final CategoryEditTable categoryEditTable;

    public ProductSubCategoryEditFragement() {
      super("productSubCategoryViewOrEditFragement", "productSubCategoryEditFragement", ProductSubCategoryPanel.this, ProductSubCategoryPanel.this.getDefaultModel());
      categoryEditTable = new CategoryEditTable("categoryEditTable", (IModel<Product>) ProductSubCategoryEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(categoryEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ProductSubCategoryViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategoryViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class CategoryDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class CategoryDataview extends DataView<Category> {

          private static final long serialVersionUID = -5039874949058607907L;

          private int index = 0;

          protected CategoryDataview(final String id, final IDataProvider<Category> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Category> newItem(String id, int index, IModel<Category> model) {
            final Item<Category> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            if (categoryDataProvider.size() > 0) {
              productSubCategoryViewOrEditPanel.removeAll();
              productSubCategoryViewOrEditPanel.setSelectedModel(Model.of(categoryDataProvider.iterator(index, index + (long) 1).next()));
              productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true);
            } else {
              productSubCategoryViewOrEditPanel.removeAll();
              productSubCategoryViewOrEditPanel.setSelectedModel(Model.of(new Category()));
              productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Category> item) {
            item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
            item.add(new Label("name").setOutputMarkupId(true));
            item.add(new Label("position").setOutputMarkupId(true));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                productSubCategoryViewOrEditPanel.removeAll();
                productSubCategoryViewOrEditPanel.setSelectedModel(item.getModel());
                target.add(categoryDataviewContainer.setOutputMarkupId(true));
                target.add(productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
              }
            });
          }
        }

        private static final long serialVersionUID = 4047707639075180742L;

        private static final int ITEMS_PER_PAGE = 10;

        private final CategoryDataview categoryDataview;

        public CategoryDataviewContainer(final String id, final IModel<Product> model) {
          super(id, model);
          categoryDataview = new CategoryDataview("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(categoryDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = 1254364120794610309L;

      private final CategoryDataviewContainer categoryDataviewContainer;

      private final BootstrapPagingNavigator categoryPagingNavigator;

      private final ProductSubCategoryViewOrEditPanel productSubCategoryViewOrEditPanel;

      public CategoryViewTable(final String id, final IModel<Product> model) {
        super(id, model);
        categoryDataviewContainer = new CategoryDataviewContainer("categoryDataviewContainer", (IModel<Product>) CategoryViewTable.this.getDefaultModel());
        categoryPagingNavigator = new BootstrapPagingNavigator("categoryPagingNavigator", categoryDataviewContainer.categoryDataview);
        productSubCategoryViewOrEditPanel = new ProductSubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) CategoryViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(categoryDataviewContainer.setOutputMarkupId(true));
        add(categoryPagingNavigator.setOutputMarkupId(true));
        add(productSubCategoryViewOrEditPanel.add(productSubCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 2822717929419097201L;

    private final CategoryViewTable categoryViewTable;

    public ProductSubCategoryViewFragement() {
      super("productSubCategoryViewOrEditFragement", "productSubCategoryViewFragement", ProductSubCategoryPanel.this, ProductSubCategoryPanel.this.getDefaultModel());
      categoryViewTable = new CategoryViewTable("categoryViewTable", (IModel<Product>) ProductSubCategoryViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(categoryViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 3703226064705246155L;

  @SpringBean(name = "CategoryDataProvider", required = true)
  private GenericTypeDataProvider<Category> categoryDataProvider;

  public ProductSubCategoryPanel(final String id, final IModel<Product> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    categoryDataProvider.setOrderBy(OrderBy.POSITION_A_Z);
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
