package com.netbrasoft.gnuob.application.customer;

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

import com.netbrasoft.gnuob.api.Customer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class CustomerViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "CustomerDataProvider", required = true)
   private GenericTypeDataProvider<Customer> customerDataProvider;

   private OrderByBorder<String> orderByPayerId = new OrderByBorder<String>("orderByPayerId", "payerId",
         customerDataProvider);
   private OrderByBorder<String> orderByFirstName = new OrderByBorder<String>("orderByFirstName", "firstName",
         customerDataProvider);
   private OrderByBorder<String> orderByLastName = new OrderByBorder<String>("orderByLastName", "lastName",
         customerDataProvider);
   private OrderByBorder<String> orderByBuyerEmail = new OrderByBorder<String>("orderByBuyerEmail", "buyerEmail",
         customerDataProvider);

   private DataView<Customer> customerDataview = new DataView<Customer>("customerDataview", customerDataProvider,
         ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Customer> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Customer>(paramItem.getModelObject()));
         paramItem.add(new Label("payerId"));
         paramItem.add(new Label("firstName"));
         paramItem.add(new Label("lastName"));
         paramItem.add(new Label("buyerEmail"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator customerPagingNavigator = new ItemsPerPagePagingNavigator(
         "customerPagingNavigator", customerDataview);

   public CustomerViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      customerDataProvider.setUser(roleSession.getUsername());
      customerDataProvider.setPassword(roleSession.getPassword());
      customerDataProvider.setSite(roleSession.getSite());

      add(orderByPayerId);
      add(orderByFirstName);
      add(orderByLastName);
      add(orderByBuyerEmail);
      add(customerDataview);
      add(customerPagingNavigator);
   }
}
