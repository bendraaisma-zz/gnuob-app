package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class ProductPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   private AjaxLink<Void> add = new AjaxLink<Void>("add") {

      private static final long serialVersionUID = 9191172039973638020L;

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      }
   };

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

   private WebMarkupContainer productDataviewContainer = new WebMarkupContainer("productDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(productDataview);
         super.onInitialize();
      };
   };

   private ItemsPerPagePagingNavigator productPagingNavigator = new ItemsPerPagePagingNavigator("productPagingNavigator", productDataview);

   public ProductPanel(final String id, final IModel<Product> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      productDataProvider.setUser(roleSession.getUsername());
      productDataProvider.setPassword(roleSession.getPassword());
      productDataProvider.setSite(roleSession.getSite());
      productDataProvider.setType((Product) getDefaultModelObject());

      add(add);
      add(orderByNumber);
      add(orderByName);
      add(productDataviewContainer.setOutputMarkupId(true));
      add(productPagingNavigator);
   }
}
