package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class ProductViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   private OrderByBorder<String> orderByNumber = new OrderByBorder<String>("orderByNumber", "number", productDataProvider);
   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", productDataProvider);
   private DataView<Product> productDataview = new DataView<Product>("productDataview", productDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Product> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Product>(paramItem.getModelObject()));
         paramItem.add(new Label("number"));
         paramItem.add(new Label("name"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator productPagingNavigator = new ItemsPerPagePagingNavigator("productPagingNavigator", productDataview);

   public ProductViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      productDataProvider.setUser(roleSession.getUsername());
      productDataProvider.setPassword(roleSession.getPassword());
      productDataProvider.setSite(roleSession.getSite());

      add(orderByNumber);
      add(orderByName);
      add(productDataview);
      add(productPagingNavigator);
   }
}
