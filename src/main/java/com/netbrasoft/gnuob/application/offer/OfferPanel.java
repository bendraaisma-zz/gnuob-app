package com.netbrasoft.gnuob.application.offer;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLinke extends AjaxLink<Void> {

      private static final long serialVersionUID = 9191172039973638020L;

      public AddAjaxLinke() {
         super("add");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   class OfferDataview extends DataView<Offer> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected OfferDataview() {
         super("offerDataview", offerDataProvider, ITEMS_PER_PAGE);
      }

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
               offerViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(offerViewOrEditPanel);
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "OfferDataProvider", required = true)
   private GenericTypeDataProvider<Offer> offerDataProvider;

   private OrderByBorder<String> orderByOfferId = new OrderByBorder<String>("orderByOfferId", "offerId", offerDataProvider);

   private OrderByBorder<String> orderByContractId = new OrderByBorder<String>("orderByContractId", "contractId", offerDataProvider);

   private OrderByBorder<String> orderByPayerId = new OrderByBorder<String>("orderByPayerId", "payerId", offerDataProvider);

   private OfferDataview offerDataview = new OfferDataview();

   private WebMarkupContainer offerDataviewContainer = new WebMarkupContainer("offerDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(offerDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator offerPagingNavigator = new BootstrapPagingNavigator("offerPagingNavigator", offerDataview);

   private OfferViewOrEditPanel offerViewOrEditPanel = new OfferViewOrEditPanel("offerViewOrEditPanel", new Model<Offer>(new Offer()));

   public OfferPanel(final String id, final IModel<Offer> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      offerDataProvider.setUser(roleSession.getUsername());
      offerDataProvider.setPassword(roleSession.getPassword());
      offerDataProvider.setSite(roleSession.getSite());
      offerDataProvider.setType((Offer) getDefaultModelObject());

      add(new AddAjaxLinke());
      add(orderByOfferId);
      add(orderByContractId);
      add(orderByPayerId);
      add(offerDataviewContainer.setOutputMarkupId(true));
      add(offerPagingNavigator);
      add(offerViewOrEditPanel.setOutputMarkupId(true));
   }
}
