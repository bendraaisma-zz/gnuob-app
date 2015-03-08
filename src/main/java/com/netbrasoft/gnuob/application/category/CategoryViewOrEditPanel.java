package com.netbrasoft.gnuob.application.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.image.ImageUploadPanel;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;
import com.netbrasoft.gnuob.wicket.bootstrap.ajax.markup.html.BootstrapConfirmationAjaxLink;
import com.netbrasoft.gnuob.wicket.bootstrap.extensions.markup.html.repeater.tree.BootstrapTableTree;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapNumberTextField;
import com.netbrasoft.gnuob.wicket.bootstrap.markup.html.form.BootstrapTextField;

public class CategoryViewOrEditPanel extends Panel {

   private static final long serialVersionUID = 3968615764565588442L;
   private static final int ITEMS_PER_PAGE = 5;

   public static int getItemsPerPage() {
      return ITEMS_PER_PAGE;
   }

   public static long getSerialversionuid() {
      return serialVersionUID;
   }

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
      return new Fragment("categoryViewOrEditFragement", "categoryEditFragement", CategoryViewOrEditPanel.this,
            CategoryViewOrEditPanel.this.getDefaultModel()) {

         private static final long serialVersionUID = 5133082553128798473L;

         @SuppressWarnings("unchecked")
         @Override
         protected void onInitialize() {
            super.onInitialize();

            Form<Category> categoryEditForm = new Form<Category>("categoryEditForm");
            WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer");
            DataView<Content> contentDataview = createContentDataView(createContentListDataProvider(), true);
            BootstrapTableTree<SubCategory, String> subCategoriesBootstrapTableTree = createSubCategoriesBootstrapTableTree(
                  createSubCategoryTreeProvider(), true);

            subCategoriesBootstrapTableTree.getTable().addTopToolbar(
                  new AddSubCategoryToolBar<String>(subCategoriesBootstrapTableTree.getTable()));

            categoryEditForm.setModel(new CompoundPropertyModel<Category>(
                  (IModel<Category>) CategoryViewOrEditPanel.this.getDefaultModel()));
            categoryEditForm.add(new BootstrapNumberTextField<Integer>("position"));
            categoryEditForm.add(new BootstrapTextField<String>("name"));
            categoryEditForm.add(new Label("description"));
            categoryEditForm.add(contentDataviewContainer.add(contentDataview));
            categoryEditForm.add(new ItemsPerPagePagingNavigator("contentPagingNavigator", contentDataview));
            categoryEditForm.add(subCategoriesBootstrapTableTree);

            add(categoryEditForm.setOutputMarkupId(true));
            add(createImageUploadPanel());
            add(createCancelAjaxLink());
            add(createSaveAjaxLink());
         }
      };
   }

   private Fragment createCategoryViewFragement() {
      return new Fragment("categoryViewOrEditFragement", "categoryViewFragement", CategoryViewOrEditPanel.this,
            CategoryViewOrEditPanel.this.getDefaultModel()) {

         private static final long serialVersionUID = 5863708936560086113L;

         @SuppressWarnings("unchecked")
         @Override
         protected void onInitialize() {
            super.onInitialize();

            Form<Category> categoryViewForm = new Form<Category>("categoryViewForm");
            WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer");
            DataView<Content> contentDataview = createContentDataView(createContentListDataProvider(), false);
            BootstrapTableTree<SubCategory, String> subCategoriesBootstrapTableTree = createSubCategoriesBootstrapTableTree(
                  createSubCategoryTreeProvider(), false);

            categoryViewForm.setModel(new CompoundPropertyModel<Category>(
                  (IModel<Category>) CategoryViewOrEditPanel.this.getDefaultModel()));
            categoryViewForm.add(new Label("position"));
            categoryViewForm.add(new Label("name"));
            categoryViewForm.add(new Label("description"));
            categoryViewForm.add(contentDataviewContainer.add(contentDataview));
            categoryViewForm.add(new ItemsPerPagePagingNavigator("contentPagingNavigator", contentDataview));
            categoryViewForm.add(subCategoriesBootstrapTableTree);

            add(createEditAjaxLink());
            add(categoryViewForm.setOutputMarkupId(true));
         }
      };
   }

   private List<IColumn<SubCategory, String>> createColumns(boolean enableOperations) {
      List<IColumn<SubCategory, String>> columns = new ArrayList<IColumn<SubCategory, String>>();

      columns.add(new TreeColumn<SubCategory, String>(Model.of("Name")) {
         private static final long serialVersionUID = -8544017108974205690L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });
      columns.add(new PropertyColumn<SubCategory, String>(Model.of("Description"), "description") {
         private static final long serialVersionUID = -1013188144051609487L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });

      if (enableOperations) {
         columns.add(new AbstractColumn<SubCategory, String>(Model.of("Operation")) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getCssClass() {
               return "small";
            }

            @Override
            public void populateItem(Item<ICellPopulator<SubCategory>> cellItem, String componentId,
                  IModel<SubCategory> rowModel) {
               cellItem.add(new Button(componentId));
            }
         });
      }

      return columns;
   }

   private DataView<Content> createContentDataView(ListDataProvider<Content> contentListDataProvider,
         boolean enableOperations) {
      DataView<Content> contentDataview = new DataView<Content>("contentDataview", contentListDataProvider,
            ITEMS_PER_PAGE) {

         private static final long serialVersionUID = -7353992345622657728L;

         private BootstrapConfirmationAjaxLink<Void> createRemoveBootstrapConfirmationAjaxLink(Item<Content> paramItem) {
            return new BootstrapConfirmationAjaxLink<Void>("remove") {

               private static final long serialVersionUID = -6950515027229520882L;

               @Override
               public void onCancel(AjaxRequestTarget paramAjaxRequestTarget) {
                  return;
               }

               @Override
               public void onConfirm(AjaxRequestTarget paramAjaxRequestTarget) {
                  ((Category) CategoryViewOrEditPanel.this.getDefaultModelObject()).getContents().remove(
                        paramItem.getModelObject());
                  paramAjaxRequestTarget.add(CategoryViewOrEditPanel.this);
               }
            };
         }

         @Override
         protected void populateItem(Item<Content> paramItem) {
            IModel<Content> compound = new CompoundPropertyModel<Content>(paramItem.getModelObject());
            paramItem.setModel(compound);
            paramItem.add(new Label("format"));
            paramItem.add(new Label("name"));

            if (enableOperations) {
               paramItem.add(createRemoveBootstrapConfirmationAjaxLink(paramItem));
            }
         }
      };
      return contentDataview;
   }

   private ListDataProvider<Content> createContentListDataProvider() {
      ListDataProvider<Content> contentListDataProvider = new ListDataProvider<Content>(new ArrayList<Content>()) {

         private static final long serialVersionUID = 7155280713102473155L;

         @Override
         protected List<Content> getData() {
            return ((Category) CategoryViewOrEditPanel.this.getDefaultModel().getObject()).getContents();
         }
      };
      return contentListDataProvider;
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

   private ImageUploadPanel createImageUploadPanel() {
      return new ImageUploadPanel("imageUploadPanel") {

         private static final long serialVersionUID = -7913720234767091477L;

         @Override
         public void uploadedImage(FileUpload uploadedImage, AjaxRequestTarget paramAjaxRequestTarget) {
            Content content = new Content();

            content.setFormat(uploadedImage.getContentType());
            content.setName(uploadedImage.getClientFileName());
            content.setContent(uploadedImage.getBytes());

            ((Category) CategoryViewOrEditPanel.this.getDefaultModelObject()).getContents().add(content);

            paramAjaxRequestTarget.add(CategoryViewOrEditPanel.this);
         }
      };
   }

   private AjaxLink<Void> createSaveAjaxLink() {
      return new AjaxLink<Void>("save") {

         private static final long serialVersionUID = 2695394292963384938L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            return;
         }
      };
   }

   private BootstrapTableTree<SubCategory, String> createSubCategoriesBootstrapTableTree(
         ITreeProvider<SubCategory> subCategoryTreeProvider, boolean enableOperations) {
      BootstrapTableTree<SubCategory, String> subCategoriesBootstrapTableTree = new BootstrapTableTree<SubCategory, String>(
            "subCategoriesBootstrapTableTree", createColumns(enableOperations), subCategoryTreeProvider,
            Integer.MAX_VALUE, new SubCategoryExpansionModel()) {

         private static final long serialVersionUID = 6447602986770067893L;

         @Override
         protected Component newContentComponent(String id, IModel<SubCategory> model) {
            return new Label(id, model.getObject().getName());
         }

      };
      return subCategoriesBootstrapTableTree;
   }

   private ITreeProvider<SubCategory> createSubCategoryTreeProvider() {
      ITreeProvider<SubCategory> subCategoryTreeProvider = new ITreeProvider<SubCategory>() {

         private static final long serialVersionUID = -3115478003988567518L;

         @Override
         public void detach() {

         }

         @Override
         public Iterator<? extends SubCategory> getChildren(SubCategory node) {
            return node.getSubCategories().iterator();
         }

         @Override
         public Iterator<? extends SubCategory> getRoots() {
            return ((Category) CategoryViewOrEditPanel.this.getDefaultModel().getObject()).getSubCategories()
                  .iterator();
         }

         @Override
         public boolean hasChildren(SubCategory node) {
            return node.getSubCategories().isEmpty();
         }

         @Override
         public IModel<SubCategory> model(SubCategory object) {
            return new Model<SubCategory>(object);
         }
      };
      return subCategoryTreeProvider;
   }

   @Override
   protected void onInitialize() {
      setOutputMarkupId(true);
      super.onInitialize();
   }
}
