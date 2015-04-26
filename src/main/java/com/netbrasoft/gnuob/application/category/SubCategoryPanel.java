package com.netbrasoft.gnuob.application.category;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.SubCategory;

@SuppressWarnings("unchecked")
public class SubCategoryPanel extends Panel {

   private static final String CSS_SMALL = "small";
   private static final long serialVersionUID = 4492979061717676247L;
   private boolean enableOperations = false;

   public SubCategoryPanel(final String id, final IModel<Category> model, final boolean enableOperations) {
      super(id, model);
      this.enableOperations = enableOperations;
   }

   private List<IColumn<SubCategory, String>> createColumns() {
      List<IColumn<SubCategory, String>> columns = new ArrayList<IColumn<SubCategory, String>>();

      columns.add(new TreeColumn<SubCategory, String>(Model.of("Name")) {
         private static final long serialVersionUID = -8544017108974205690L;

         @Override
         public String getCssClass() {
            return CSS_SMALL;
         }
      });

      columns.add(new PropertyColumn<SubCategory, String>(Model.of("Description"), "description") {
         private static final long serialVersionUID = -1013188144051609487L;

         @Override
         public String getCssClass() {
            return CSS_SMALL;
         }
      });

      columns.add(new AbstractColumn<SubCategory, String>(Model.of("Operation")) {
         private static final long serialVersionUID = 1L;

         @Override
         public String getCssClass() {
            return CSS_SMALL;
         }

         @Override
         public void populateItem(Item<ICellPopulator<SubCategory>> cellItem, String componentId, IModel<SubCategory> rowModel) {
            SubCategoryOperationPanel operationSubCategoryPanel = new SubCategoryOperationPanel(componentId, rowModel);
            cellItem.add(operationSubCategoryPanel.setVisible(enableOperations));
         }
      });

      return columns;
   }

   private SubCategoryTableTree<SubCategory> createSubCategoriesBootstrapTableTree(SubCategoryTreeProvider<SubCategory> subCategoryTreeProvider) {
      SubCategoryExpansionModel subCategoryExpansionModel = new SubCategoryExpansionModel();
      List<IColumn<SubCategory, String>> columns = createColumns();
      SubCategoryTableTree<SubCategory> subCategoriesBootstrapTableTree = new SubCategoryTableTree<SubCategory>("subCategoriesTableTree", columns, subCategoryTreeProvider, Integer.MAX_VALUE, subCategoryExpansionModel);
      return subCategoriesBootstrapTableTree;
   }

   private SubCategoryTreeProvider<SubCategory> createSubCategoryTreeProvider() {
      IModel<Category> model = (IModel<Category>) getDefaultModel();
      SubCategoryTreeProvider<SubCategory> subCategoryTreeProvider = new SubCategoryTreeProvider<SubCategory>(model);
      return subCategoryTreeProvider;
   }

   @Override
   protected void onInitialize() {
      SubCategoryTableTree<SubCategory> subCategoriesBootstrapTableTree = createSubCategoriesBootstrapTableTree(createSubCategoryTreeProvider());
      SubCategoryToolBar addSubCategoryToolBar = new SubCategoryToolBar(subCategoriesBootstrapTableTree.getTable());

      subCategoriesBootstrapTableTree.getTable().addTopToolbar((AbstractToolbar) addSubCategoryToolBar.setVisible(enableOperations));
      subCategoriesBootstrapTableTree.getTable().addTopToolbar(new HeadersToolbar<String>(subCategoriesBootstrapTableTree.getTable(), null));
      add(subCategoriesBootstrapTableTree.setOutputMarkupId(true));

      setOutputMarkupId(true);
      super.onInitialize();
   }
}
