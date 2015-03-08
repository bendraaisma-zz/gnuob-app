package com.netbrasoft.gnuob.application.paging;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

public class ItemsPerPagePagingNavigator extends PagingNavigator {

   private static final long serialVersionUID = 2826128563781254609L;

   public ItemsPerPagePagingNavigator(String id, IPageable pageable) {
      super(id, pageable);

      // Toegevoegd ivm irritante foutmelding dat wicket:id= niet
      // gevonden kan worden in super class.
      new Label("pageLink");
      new Label("first");
      new Label("prev");
      new Label("last");
      new Label("next");
      new Label("navigation");
      new Label("pageNumber");
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
   }

}
