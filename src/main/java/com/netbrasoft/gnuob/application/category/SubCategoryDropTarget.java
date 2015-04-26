package com.netbrasoft.gnuob.application.category;

import java.util.EnumSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree.State;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;

import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Reject;
import wicketdnd.Transfer;

import com.netbrasoft.gnuob.api.SubCategory;

public class SubCategoryDropTarget<S extends SubCategory> extends DropTarget {

   private static final long serialVersionUID = 5064629267560245959L;
   private TableTree<S, String> tableTree;
   private SubCategoryTreeProvider<S> provider;

   public SubCategoryDropTarget() {
      super(EnumSet.allOf(Operation.class));
   }

   public SubCategoryDropTarget(SubCategoryTreeProvider<S> provider, TableTree<S, String> subCategoryBootstrapTableTree) {
      super(EnumSet.of(Operation.MOVE));
      this.tableTree = subCategoryBootstrapTableTree;
      this.provider = provider;
   }

   @Override
   public void onDrag(AjaxRequestTarget target, Location location) {
      S subCategory = location.getModelObject();
      if (tableTree.getState(subCategory) == State.COLLAPSED) {
         tableTree.expand(subCategory);
      }
   }

   @Override
   public void onDrop(AjaxRequestTarget target, Transfer transfer, Location location) throws Reject {
      S subCategorySource = transfer.getData();
      S subCategorytarget = location.getModelObject();

      switch (location.getAnchor()) {
      case CENTER:
         if (subCategorySource != target) {
            provider.add(subCategorySource, subCategorytarget);
            tableTree.expand(subCategorytarget);
         }
         break;
      case TOP:
         provider.addBefore(subCategorySource, subCategorytarget);
         break;
      case BOTTOM:
         provider.addAfter(subCategorySource, subCategorytarget);
      default:
         break;
      }
      target.add(tableTree);
   }
}
