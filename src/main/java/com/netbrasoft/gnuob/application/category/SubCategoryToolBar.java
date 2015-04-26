package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.link.Link;

public class SubCategoryToolBar extends AbstractToolbar {

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
