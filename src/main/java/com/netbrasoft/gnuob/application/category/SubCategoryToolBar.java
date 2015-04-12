package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;

public class SubCategoryToolBar extends AbstractToolbar {

   private static final long serialVersionUID = 297616411999549064L;

   public <T> SubCategoryToolBar(final DataTable<T, String> table) {
      super(table);
   }
}
