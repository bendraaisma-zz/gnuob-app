package com.netbrasoft.gnuob.application.offer;

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

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class OfferViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "OfferDataProvider", required = true)
   private GenericTypeDataProvider<Offer> offerDataProvider;

   private OrderByBorder<String> orderByOfferId = new OrderByBorder<String>("orderByOfferId", "offerId",
         offerDataProvider);
   private OrderByBorder<String> orderByContractId = new OrderByBorder<String>("orderByContractId", "contractId",
         offerDataProvider);
   private OrderByBorder<String> orderByPayerId = new OrderByBorder<String>("orderByPayerId", "payerId",
         offerDataProvider);
   private DataView<Offer> offerDataview = new DataView<Offer>("offerDataview", offerDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Offer> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Offer>(paramItem.getModelObject()));
         paramItem.add(new Label("offerId"));
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
   private ItemsPerPagePagingNavigator offerPagingNavigator = new ItemsPerPagePagingNavigator("offerPagingNavigator",
         offerDataview);

   public OfferViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      offerDataProvider.setUser(roleSession.getUsername());
      offerDataProvider.setPassword(roleSession.getPassword());
      offerDataProvider.setSite(roleSession.getSite());

      add(orderByOfferId);
      add(orderByContractId);
      add(orderByPayerId);
      add(offerDataview);
      add(offerPagingNavigator);
   }
}
