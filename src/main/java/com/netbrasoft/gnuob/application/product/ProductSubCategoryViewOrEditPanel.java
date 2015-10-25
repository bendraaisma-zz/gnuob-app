package com.netbrasoft.gnuob.application.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.category.table.SubCategoryTableTree;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ProductSubCategoryViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  public class SubCategoryEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class ProductSubCategoryEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class ProductSubCategoriesDataViewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
        class SubCategoryDataview extends SubCategoryTableTree {

          private static final long serialVersionUID = 890348942507232169L;

          public SubCategoryDataview(final String id,
              List<? extends IColumn<SubCategory, String>> columns,
              final ITreeProvider<SubCategory> provider, final long rowsPerPage) {
            super(id, columns, provider, rowsPerPage);
          }

          @Override
          public Item<SubCategory> newItem(Item<SubCategory> item, IModel<SubCategory> model) {
            for (final SubCategory subCategory : ((IModel<Product>) ProductSubCategoriesDataViewContainer.this
                .getDefaultModel()).getObject().getSubCategories()) {
              if (subCategory.getId() == model.getObject().getId()) {
                item.add(new BootstrapBaseBehavior() {

                  private static final long serialVersionUID = -4903722864597601489L;

                  @Override
                  public void onComponentTag(Component component, ComponentTag tag) {
                    Attributes.addClass(tag, "info");
                  }
                });
                item.add(new AjaxEventBehavior("click") {

                  private static final long serialVersionUID = 1L;

                  @Override
                  public void onEvent(AjaxRequestTarget target) {
                    ((IModel<Product>) ProductSubCategoriesDataViewContainer.this.getDefaultModel())
                        .getObject().getSubCategories().remove(subCategory);
                    ProductSubCategoryViewOrEditPanel.this.removeAll();
                    target.add(ProductSubCategoryViewOrEditPanel.this
                        .add(ProductSubCategoryViewOrEditPanel.this.new SubCategoryEditFragment())
                        .setOutputMarkupId(true));
                  }
                });
                return item;
              }
            }

            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                ((IModel<Product>) ProductSubCategoriesDataViewContainer.this.getDefaultModel())
                    .getObject().getSubCategories().add(item.getModelObject());
                ProductSubCategoryViewOrEditPanel.this.removeAll();
                target.add(ProductSubCategoryViewOrEditPanel.this
                    .add(ProductSubCategoryViewOrEditPanel.this.new SubCategoryEditFragment())
                    .setOutputMarkupId(true));
              }
            });
            return item;
          }
        }

        private static final long serialVersionUID = 3279611195983210197L;

        private final SubCategoryDataview subCategoryDataview;

        public ProductSubCategoriesDataViewContainer(final String id,
            final IModel<Category> model) {
          super(id, model);
          subCategoryDataview = new SubCategoryDataview("productSubCategoriesDataView",
              createColumns(), subCategoryTreeProvider, Integer.MAX_VALUE);
        }

        @Override
        protected void onInitialize() {
          add(subCategoryDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = 1244529149500926347L;

      private final ProductSubCategoriesDataViewContainer productSubCategoriesDataViewContainer;

      private final BootstrapForm<?> subCategoryEditForm;

      private final NotificationPanel feedbackPanel;

      public ProductSubCategoryEditTable(final String id, final IModel<Category> model) {
        super(id, model);
        productSubCategoriesDataViewContainer =
            new ProductSubCategoriesDataViewContainer("productSubCategoriesDataViewContainer",
                (IModel<Category>) ProductSubCategoryEditTable.this.getDefaultModel());
        subCategoryEditForm = new BootstrapForm<>("subCategoryEditForm");
        feedbackPanel = new NotificationPanel("feedback");
      }

      @Override
      protected void onInitialize() {
        subCategoryEditForm.add(productSubCategoriesDataViewContainer.setOutputMarkupId(true));
        add(subCategoryEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 1492400872373227225L;

    private final ProductSubCategoryEditTable productSubCategoryEditTable;

    public SubCategoryEditFragment() {
      super("productSubCategoryViewOrEditFragment", "productSubCategoryEditFragment",
          ProductSubCategoryViewOrEditPanel.this,
          ProductSubCategoryViewOrEditPanel.this.getDefaultModel());
      productSubCategoryEditTable = new ProductSubCategoryEditTable("productSubCategoryEditTable",
          (IModel<Category>) SubCategoryEditFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(productSubCategoryEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  class SubCategoryTreeProvider implements ITreeProvider<SubCategory> {
    private static final long serialVersionUID = -592161727647897932L;

    @Override
    public void detach() {
      return;
    }

    @Override
    public Iterator<? extends SubCategory> getChildren(SubCategory node) {
      return node.getSubCategories().iterator();
    }

    @Override
    public Iterator<? extends SubCategory> getRoots() {
      return ProductSubCategoryViewOrEditPanel.this.selectedModel.getObject().getSubCategories()
          .iterator();
    }

    @Override
    public boolean hasChildren(SubCategory node) {
      return !node.getSubCategories().isEmpty();
    }

    @Override
    public IModel<SubCategory> model(SubCategory object) {
      return Model.of(object);
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  public class SubCategoryViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ProductSubCategoryViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class ProductSubCategoriesDataViewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class SubCategoryDataview extends SubCategoryTableTree {

          private static final long serialVersionUID = 890348942507232169L;

          public SubCategoryDataview(final String id,
              List<? extends IColumn<SubCategory, String>> columns,
              final ITreeProvider<SubCategory> provider, final long rowsPerPage) {
            super(id, columns, provider, rowsPerPage);
          }

          @Override
          public Item<SubCategory> newItem(Item<SubCategory> item, IModel<SubCategory> model) {
            for (final SubCategory subCategory : ((IModel<Product>) ProductSubCategoriesDataViewContainer.this
                .getDefaultModel()).getObject().getSubCategories()) {
              if (subCategory.getId() == model.getObject().getId()) {
                item.add(new BootstrapBaseBehavior() {

                  private static final long serialVersionUID = -4903722864597601489L;

                  @Override
                  public void onComponentTag(Component component, ComponentTag tag) {
                    Attributes.addClass(tag, "info");
                  }
                });
                return item;
              }
            }
            return item;
          }
        }

        private static final long serialVersionUID = 3279611195983210197L;

        private final SubCategoryDataview subCategoryDataview;

        public ProductSubCategoriesDataViewContainer(final String id,
            final IModel<Category> model) {
          super(id, model);
          subCategoryDataview = new SubCategoryDataview("productSubCategoriesDataView",
              createColumns(), subCategoryTreeProvider, Integer.MAX_VALUE);
        }

        @Override
        protected void onInitialize() {
          add(subCategoryDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -4728519835478043962L;

      private final ProductSubCategoriesDataViewContainer productSubCategoriesDataViewContainer;

      private final BootstrapForm<?> subCategoryViewForm;

      public ProductSubCategoryViewTable(String id, IModel<Category> model) {
        super(id, model);
        productSubCategoriesDataViewContainer =
            new ProductSubCategoriesDataViewContainer("productSubCategoriesDataViewContainer",
                (IModel<Category>) ProductSubCategoryViewTable.this.getDefaultModel());
        subCategoryViewForm = new BootstrapForm<>("subCategoryViewForm");
      }

      @Override
      protected void onInitialize() {
        subCategoryViewForm.add(productSubCategoriesDataViewContainer.setOutputMarkupId(true));
        add(subCategoryViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 6624858821589938712L;

    private final ProductSubCategoryViewTable productSubCategoryViewTable;

    public SubCategoryViewFragment() {
      super("productSubCategoryViewOrEditFragment", "productSubCategoryViewFragment",
          ProductSubCategoryViewOrEditPanel.this,
          ProductSubCategoryViewOrEditPanel.this.getDefaultModel());
      productSubCategoryViewTable = new ProductSubCategoryViewTable("productSubCategoryViewTable",
          (IModel<Category>) SubCategoryViewFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(productSubCategoryViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 35319895698886122L;

  private IModel<Category> selectedModel;

  private final SubCategoryTreeProvider subCategoryTreeProvider;

  public ProductSubCategoryViewOrEditPanel(final String id, final IModel<Product> model) {
    super(id, model);
    subCategoryTreeProvider = new SubCategoryTreeProvider();
  }

  private List<IColumn<SubCategory, String>> createColumns() {
    final List<IColumn<SubCategory, String>> columns =
        new ArrayList<IColumn<SubCategory, String>>();

    columns.add(new TreeColumn<SubCategory, String>(Model.of(getString("nameMessage"))) {
      private static final long serialVersionUID = -8544017108974205690L;

      @Override
      public String getCssClass() {
        return "small";
      }
    });

    columns.add(new PropertyColumn<SubCategory, String>(Model.of(getString("descriptionMessage")),
        "description") {
      private static final long serialVersionUID = -1013188144051609487L;

      @Override
      public String getCssClass() {
        return "small";
      }
    });

    return columns;
  }

  public void setSelectedModel(final IModel<Category> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
