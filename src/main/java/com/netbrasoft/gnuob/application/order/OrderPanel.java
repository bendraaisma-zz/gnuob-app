package com.netbrasoft.gnuob.application.order;

import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OrderPanel extends Panel {

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

   class OrderDataview extends DataView<Order> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected OrderDataview() {
         super("orderDataview", orderDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Order> newItem(String id, int index, IModel<Order> model) {
         final Item<Order> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Order) orderViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Order> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Order>(paramItem.getModelObject()));
         paramItem.add(new Label("orderId"));
         paramItem.add(new Label("contract.contractId"));
         paramItem.add(new Label("contract.customer.firstName"));
         paramItem.add(new Label("contract.customer.lastName"));
         paramItem.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               orderViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "OrderDataProvider", required = true)
   private GenericTypeDataProvider<Order> orderDataProvider;

   private final OrderByBorder<String> orderByFirstName = new OrderByBorder<String>("orderByFirstName", "contract.customer.firstName", orderDataProvider);

   private final OrderByBorder<String> orderByLastName = new OrderByBorder<String>("orderByLastName", "contract.customer.lastName", orderDataProvider);

   private final OrderByBorder<String> orderByOrderId = new OrderByBorder<String>("orderByOrderId", "orderId", orderDataProvider);

   private final OrderByBorder<String> orderByContractId = new OrderByBorder<String>("orderByContractId", "contract.contractId", orderDataProvider);

   private final OrderDataview orderDataview = new OrderDataview();

   private final WebMarkupContainer orderDataviewContainer = new WebMarkupContainer("orderDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(orderDataview);
         super.onInitialize();
      }
   };

   private final BootstrapPagingNavigator orderPagingNavigator = new BootstrapPagingNavigator("orderPagingNavigator", orderDataview);

   private final OrderViewOrEditPanel orderViewOrEditPanel = new OrderViewOrEditPanel("orderViewOrEditPanel", (IModel<Order>) getDefaultModel());

   public OrderPanel(final String id, final IModel<Order> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      orderDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      orderDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      orderDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      orderDataProvider.setType(new Order());
      orderDataProvider.getType().setActive(true);

      add(new AddAjaxLinke());
      add(orderByFirstName);
      add(orderByLastName);
      add(orderByOrderId);
      add(orderByContractId);
      add(orderDataviewContainer.setOutputMarkupId(true));
      add(orderPagingNavigator);
      add(orderViewOrEditPanel.add(orderViewOrEditPanel.new OrderViewFragement()).setOutputMarkupId(true));

      super.onInitialize();
   }
}
