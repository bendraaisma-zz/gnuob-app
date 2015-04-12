package com.netbrasoft.gnuob.application.category;

import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.wicket.bootstrap.extensions.markup.html.repeater.tree.BootstrapTableTree;

public class SubCategoryBootstrapTableTree<SC extends SubCategory> extends BootstrapTableTree<SC, String> {

   private static final long serialVersionUID = 1500770348691601351L;

   public SubCategoryBootstrapTableTree(String id, List<? extends IColumn<SC, String>> columns, ITreeProvider<SC> provider, long rowsPerPage, IModel<? extends Set<SC>> state) {
      super(id, columns, provider, rowsPerPage, state);
   }

   @Override
   protected Component newContentComponent(String id, IModel<SC> model) {
      return new Label(id, model.getObject().getName());
   }
}
