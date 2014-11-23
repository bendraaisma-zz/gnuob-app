package com.netbrasoft.gnuob.application.contract;

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

import com.netbrasoft.gnuob.api.Contract;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class ContractViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ContractDataProvider", required = true)
   private GenericTypeDataProvider<Contract> contractDataProvider;

   private OrderByBorder<String> orderByContractId = new OrderByBorder<String>("orderByContractId", "contractId", contractDataProvider);
   private OrderByBorder<String> orderByPayerId = new OrderByBorder<String>("orderByPayerId", "payerId", contractDataProvider);
   private DataView<Contract> contractDataview = new DataView<Contract>("contractDataview", contractDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Contract> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Contract>(paramItem.getModelObject()));
         paramItem.add(new Label("contractId"));
         paramItem.add(new Label("customer.payerId"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator contractPagingNavigator = new ItemsPerPagePagingNavigator("contractPagingNavigator", contractDataview);

   public ContractViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      contractDataProvider.setUser(roleSession.getUsername());
      contractDataProvider.setPassword(roleSession.getPassword());
      contractDataProvider.setSite(roleSession.getSite());

      add(orderByContractId);
      add(orderByPayerId);
      add(contractDataview);
      add(contractPagingNavigator);
   }
}
