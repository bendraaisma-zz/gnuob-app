package com.netbrasoft.gnuob.application.category;

import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
//import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.SubCategory;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import wicketdnd.DragSource;
import wicketdnd.Operation;

public class SubCategoryTableTree<S extends SubCategory> extends TableTree<S, String> {

   private static final long serialVersionUID = 1500770348691601351L;

   public SubCategoryTableTree(String id, List<? extends IColumn<S, String>> columns, SubCategoryTreeProvider<S> provider, long rowsPerPage, IModel<? extends Set<S>> state) {
      super(id, columns, provider, rowsPerPage, state);
      getTable().add(new HumanTheme());
      getTable().add(new wicketdnd.theme.HumanTheme());
      getTable().add(new TableBehavior());

      setItemReuseStrategy(new ReuseIfModelsEqualStrategy());

      add(new DragSource(Operation.MOVE).drag("tr").initiate("span.tree-content").clone("span.tree-content"));
      add(new SubCategoryDropTarget<S>(provider, this).dropTopAndBottom("tbody tr").dropCenter("tbody tr"));
   }

   @Override
   protected Component newContentComponent(String id, IModel<S> model) {
      return new Label(id, model.getObject().getName());
   }
}
