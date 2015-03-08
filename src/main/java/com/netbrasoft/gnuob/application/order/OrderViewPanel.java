package com.netbrasoft.gnuob.application.order;

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

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class OrderViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "OrderDataProvider", required = true)
   private GenericTypeDataProvider<Order> orderDataProvider;

   private OrderByBorder<String> orderByOrderId = new OrderByBorder<String>("orderByOrderId", "orderId",
         orderDataProvider);
   private OrderByBorder<String> orderByContractId = new OrderByBorder<String>("orderByContractId", "contractId",
         orderDataProvider);
   private OrderByBorder<String> orderByPayerId = new OrderByBorder<String>("orderByPayerId", "payerId",
         orderDataProvider);
   private DataView<Order> orderDataview = new DataView<Order>("orderDataview", orderDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Order> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Order>(paramItem.getModelObject()));
         paramItem.add(new Label("orderId"));
         paramItem.add(new Label("contract.contractId"));
         paramItem.add(new Label("contract.customer.payerId"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator orderPagingNavigator = new ItemsPerPagePagingNavigator("orderPagingNavigator",
         orderDataview);

   public OrderViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      orderDataProvider.setUser(roleSession.getUsername());
      orderDataProvider.setPassword(roleSession.getPassword());
      orderDataProvider.setSite(roleSession.getSite());

      add(orderByOrderId);
      add(orderByContractId);
      add(orderByPayerId);
      add(orderDataview);
      add(orderPagingNavigator);
   }
}
