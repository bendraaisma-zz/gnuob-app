package com.netbrasoft.gnuob.application.category;

import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;

import com.netbrasoft.gnuob.api.SubCategory;

public class SubCategoryExpansionModel extends AbstractReadOnlyModel<Set<SubCategory>> {

   private static final long serialVersionUID = 950443447495060811L;

   @Override
   public Set<SubCategory> getObject() {
      return SubCategoryExpansion.get();
   }
}
