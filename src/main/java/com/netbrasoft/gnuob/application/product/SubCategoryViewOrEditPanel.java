package com.netbrasoft.gnuob.application.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree.State;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableTreeProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import wicketdnd.DragSource;
import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Transfer;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class SubCategoryViewOrEditPanel extends Panel {

   class SubCategoryDropTarget extends DropTarget {

      private static final long serialVersionUID = 5064629267560245959L;

      public SubCategoryDropTarget() {
         super(EnumSet.allOf(Operation.class));
      }

      @Override
      public void onDrag(AjaxRequestTarget target, Location location) {
         SubCategory subCategory = location.getModelObject();
         if (subCategoriesTableTree.getState(subCategory) == State.COLLAPSED) {
            subCategoriesTableTree.expand(subCategory);
         }
      }

      @Override
      public void onDrop(AjaxRequestTarget target, Transfer transfer, Location location) {
         SubCategory subCategorySource = transfer.getData();
         SubCategory subCategorytarget = location.getModelObject();

         switch (location.getAnchor()) {
         case CENTER:
            if (subCategorySource != target) {
               subCategoryTreeProvider.add(subCategorySource, subCategorytarget);
               subCategoriesTableTree.expand(subCategorytarget);
            }
            break;
         case TOP:
            subCategoryTreeProvider.addBefore(subCategorySource, subCategorytarget);
            break;
         case BOTTOM:
            subCategoryTreeProvider.addAfter(subCategorySource, subCategorytarget);
            break;
         default:
            break;
         }
         target.add(subCategoriesTableTree);
      }
   }

   class SubCategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 3162058383568556008L;

      public SubCategoryEditFragement() {
         super("subCategoryViewOrEditFragement", "subCategoryEditFragement", SubCategoryViewOrEditPanel.this, SubCategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(subCategoriesTableTree.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   static class SubCategoryExpansion implements Set<SubCategory>, Serializable {

      private static final long serialVersionUID = -115456451423752276L;

      private static MetaDataKey<SubCategoryExpansion> KEY = new MetaDataKey<SubCategoryExpansion>() {

         private static final long serialVersionUID = 689335047835010940L;
      };

      public static SubCategoryExpansion get() {

         SubCategoryExpansion expansion = Session.get().getMetaData(KEY);
         if (expansion == null) {
            expansion = new SubCategoryExpansion();

            Session.get().setMetaData(KEY, expansion);
         }
         return expansion;
      }

      private transient Set<Long> ids = new HashSet<Long>();

      private boolean inverse;

      @Override
      public boolean add(SubCategory subCategory) {
         if (inverse) {
            return ids.remove(subCategory.getId());
         } else {
            return ids.add(subCategory.getId());
         }
      }

      @Override
      public boolean addAll(Collection<? extends SubCategory> c) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void clear() {
         throw new UnsupportedOperationException();
      }

      public void collapseAll() {
         ids.clear();
         inverse = false;
      }

      @Override
      public boolean contains(Object object) {
         SubCategory subCategory = (SubCategory) object;

         if (inverse) {
            return !ids.contains(subCategory.getId());
         } else {
            return ids.contains(subCategory.getId());
         }
      }

      @Override
      public boolean containsAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public void expandAll() {
         ids.clear();
         inverse = true;
      }

      @Override
      public boolean isEmpty() {
         throw new UnsupportedOperationException();
      }

      @Override
      public Iterator<SubCategory> iterator() {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean remove(Object object) {
         SubCategory subCategory = (SubCategory) object;

         if (inverse) {
            return ids.add(subCategory.getId());
         } else {
            return ids.remove(subCategory.getId());
         }
      }

      @Override
      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      @Override
      public int size() {
         throw new UnsupportedOperationException();
      }

      @Override
      public Object[] toArray() {
         throw new UnsupportedOperationException();
      }

      @Override
      public <T> T[] toArray(T[] a) {
         throw new UnsupportedOperationException();
      }
   }

   class SubCategoryExpansionModel extends AbstractReadOnlyModel<Set<SubCategory>> {

      private static final long serialVersionUID = 950443447495060811L;

      @Override
      public Set<SubCategory> getObject() {
         return SubCategoryExpansion.get();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SubCategoryOperationPanel extends Panel {

      private static final long serialVersionUID = 7557158822058584007L;

      public SubCategoryOperationPanel(final String id, final IModel<SubCategory> model) {
         super(id, model);
      }
   }

   class SubCategoryTableTree extends TableTree<SubCategory, String> {

      private static final long serialVersionUID = 1500770348691601351L;

      public SubCategoryTableTree() {
         super("subCategoriesTableTree", createColumns(), subCategoryTreeProvider, Integer.MAX_VALUE, new SubCategoryExpansionModel());

         getTable().add(new HumanTheme());
         getTable().add(new wicketdnd.theme.HumanTheme());
         getTable().add(new TableBehavior());

         getTable().addTopToolbar(new SubCategoryToolBar(getTable()));
         getTable().addTopToolbar(new HeadersToolbar<String>(getTable(), null));

         setItemReuseStrategy(new ReuseIfModelsEqualStrategy());

         add(new DragSource(Operation.MOVE).drag("tr").initiate("span.tree-content").clone("span.tree-content"));
         add(new SubCategoryDropTarget().dropTopAndBottom("tbody tr").dropCenter("tbody tr"));
      }

      @Override
      protected Component newContentComponent(String id, IModel<SubCategory> model) {
         return new Label(id, model.getObject().getName());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SubCategoryToolBar extends AbstractToolbar {

      private static final long serialVersionUID = 297616411999549064L;

      public <T> SubCategoryToolBar(final DataTable<T, String> table) {
         super(table);
      }

      @Override
      protected void onInitialize() {
         super.onInitialize();

         add(new Link<Void>("expandAll") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
               SubCategoryExpansion.get().expandAll();
            }
         });

         add(new Link<Void>("collapseAll") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
               SubCategoryExpansion.get().collapseAll();
            }
         });
      }
   }

   class SubCategoryTreeProvider extends SortableTreeProvider<SubCategory, String> {
      private static final long serialVersionUID = -592161727647897932L;

      public SubCategoryTreeProvider() {
      }

      public void add(SubCategory source, SubCategory target) {
         List<SubCategory> subCategorySourceParent = getParent(source, ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories());
         target.getSubCategories().add(source);
         subCategorySourceParent.remove(source);
      }

      public void addAfter(SubCategory source, SubCategory target) {
         List<SubCategory> subCategorySourceParent = getParent(source, ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories());
         List<SubCategory> subCategoryParent = getParent(target, ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories());

         if (subCategoryParent != null) {
            int index = subCategoryParent.indexOf(target);
            subCategoryParent.add(index + 1, source);

            subCategorySourceParent.remove(source);
         }
      }

      public void addBefore(SubCategory source, SubCategory target) {
         List<SubCategory> subCategorySourceParent = getParent(source, ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories());
         List<SubCategory> subCategoryParent = getParent(target, ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories());

         if (subCategoryParent != null) {
            int index = subCategoryParent.indexOf(target);
            subCategoryParent.add(index, source);
            subCategorySourceParent.remove(source);
         }
      }

      @Override
      public Iterator<? extends SubCategory> getChildren(SubCategory node) {
         return node.getSubCategories().iterator();
      }

      private List<SubCategory> getParent(SubCategory subCategory, List<SubCategory> subCategories) {

         if (subCategories.contains(subCategory)) {
            return subCategories;
         }

         List<SubCategory> results = new ArrayList<SubCategory>();

         for (SubCategory sub : subCategories) {
            results = getParent(subCategory, sub.getSubCategories());

            if (results != null && results.contains(subCategory)) {
               return results;
            }
         }

         return results;
      }

      @Override
      public Iterator<? extends SubCategory> getRoots() {
         return ((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories().iterator();
      }

      @Override
      public boolean hasChildren(SubCategory node) {
         return !((Product) SubCategoryViewOrEditPanel.this.getDefaultModelObject()).getSubCategories().isEmpty();
      }

      @Override
      public IModel<SubCategory> model(SubCategory object) {
         return Model.of(object);
      }
   }

   class SubCategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 3162058383568556008L;

      public SubCategoryViewFragement() {
         super("subCategoryViewOrEditFragement", "subCategoryViewFragement", SubCategoryViewOrEditPanel.this, SubCategoryViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(subCategoriesTableTree.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final String CSS_SMALL = "small";

   private static final long serialVersionUID = 4492979061717676247L;

   private SubCategoryTreeProvider subCategoryTreeProvider = new SubCategoryTreeProvider();

   private SubCategoryTableTree subCategoriesTableTree = new SubCategoryTableTree();

   public SubCategoryViewOrEditPanel(final String id, final IModel<Product> model) {
      super(id, model);
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
            cellItem.add(operationSubCategoryPanel);
         }
      });

      return columns;
   }
}
