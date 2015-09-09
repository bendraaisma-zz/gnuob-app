package com.netbrasoft.gnuob.application.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.category.table.SubCategoryTableTree;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.util.Attributes;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductSubCategoryViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class SubCategoryDataview extends SubCategoryTableTree {

      private static final long serialVersionUID = 890348942507232169L;

      private long selectedObjectId;

      public SubCategoryDataview(List<? extends IColumn<SubCategory, String>> columns) {
         super("productSubCategoriesDataView", columns, subCategoryTreeProvider, Integer.MAX_VALUE);
      }

      @Override
      public Item<SubCategory> newItem(Item<SubCategory> item, IModel<SubCategory> model) {
         for(final SubCategory subCategory : ((Product)markupProvider.getDefaultModelObject()).getSubCategories()) {
            if(subCategory.getId() == model.getObject().getId()) {
               item.add(new BootstrapBaseBehavior() {

                  private static final long serialVersionUID = -4903722864597601489L;

                  @Override
                  public void onComponentTag(Component component, ComponentTag tag) {
                     Attributes.addClass(tag, "success");
                  }
               });
               return item;
            }
         }

         return item;
      }

      @Override
      protected void onConfigure() {
         if (selectedObjectId != ((Category) ProductSubCategoryViewOrEditPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Category) ProductSubCategoryViewOrEditPanel.this.getDefaultModelObject()).getId();
         }
         super.onConfigure();
      }

      @Override
      public void populateItem(Item<SubCategory> item) {
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

               target.add(productSubCategoriesDataViewContainer.setOutputMarkupId(true));
            }
         });
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   public class SubCategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 1492400872373227225L;

      public SubCategoryEditFragement() {
         super("productSubCategoryViewOrEditFragement", "productSubCategoryEditFragement", ProductSubCategoryViewOrEditPanel.this, ProductSubCategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(productSubCategoryEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class SubCategoryTreeProvider implements ITreeProvider<SubCategory> {
      private static final long serialVersionUID = -592161727647897932L;

      public SubCategoryTreeProvider() {
      }

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
         return ((Category) ProductSubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories().iterator();
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

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   public class SubCategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 6624858821589938712L;

      public SubCategoryViewFragement() {
         super("productSubCategoryViewOrEditFragement", "productSubCategoryViewFragement", ProductSubCategoryViewOrEditPanel.this, ProductSubCategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(productSubCategoryViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final long serialVersionUID = 35319895698886122L;

   private final WebMarkupContainer productSubCategoryEditTable;

   private final WebMarkupContainer productSubCategoryViewTable;

   private final WebMarkupContainer productSubCategoriesDataViewContainer;

   private final SubCategoryTreeProvider subCategoryTreeProvider;

   private final SubCategoryDataview productSubCategoriesDataView;

   private final MarkupContainer markupProvider;

   public ProductSubCategoryViewOrEditPanel(final String id, final IModel<Category> model, final MarkupContainer markupProvider) {
      super(id, model);
      this.markupProvider = markupProvider;

      subCategoryTreeProvider = new SubCategoryTreeProvider();
      productSubCategoriesDataView = new SubCategoryDataview(createColumns());
      productSubCategoryEditTable = new WebMarkupContainer("productSubCategoryEditTable", getDefaultModel()) {

         private static final long serialVersionUID = -7906213103462054641L;

         @Override
         protected void onInitialize() {
            add(productSubCategoriesDataViewContainer.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      productSubCategoryViewTable = new WebMarkupContainer("productSubCategoryViewTable", getDefaultModel()) {

         private static final long serialVersionUID = 5103788574709914026L;

         @Override
         protected void onInitialize() {
            add(productSubCategoriesDataViewContainer.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      productSubCategoriesDataViewContainer = new WebMarkupContainer("productSubCategoriesDataViewContainer", getDefaultModel()) {

         private static final long serialVersionUID = 586368973894377938L;

         @Override
         protected void onInitialize() {
            add(productSubCategoriesDataView.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private List<IColumn<SubCategory, String>> createColumns() {
      final List<IColumn<SubCategory, String>> columns = new ArrayList<IColumn<SubCategory, String>>();

      columns.add(new TreeColumn<SubCategory, String>(Model.of(getString("nameMessage"))) {
         private static final long serialVersionUID = -8544017108974205690L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });

      columns.add(new PropertyColumn<SubCategory, String>(Model.of(getString("descriptionMessage")), "description") {
         private static final long serialVersionUID = -1013188144051609487L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });

      return columns;
   }
}
