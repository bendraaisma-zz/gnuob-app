package com.netbrasoft.gnuob.application.order;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OrderViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(new OrderViewFragement()).setOutputMarkupId(true);
         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(new OrderEditFragment().setOutputMarkupId(true));
         target.add(OrderViewOrEditPanel.this);
      }
   }

   class OrderEditFragment extends Fragment {

      private static final long serialVersionUID = -5645656866901827543L;

      public OrderEditFragment() {
         super("orderViewOrEditFragement", "orderEditFragement", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Order> orderEditForm = new Form<Order>("orderEditForm");
         orderEditForm.setModel(new CompoundPropertyModel<Order>((IModel<Order>) getDefaultModel()));

         orderEditForm.add(new TextField<String>("orderId"));
         orderEditForm.add(new TextField<String>("contract.contractId"));
         orderEditForm.add(new TextField<String>("token"));
         orderEditForm.add(new TextField<String>("transactionId"));
         orderEditForm.add(new TextField<String>("billingAgreementId"));
         orderEditForm.add(new TextField<String>("checkoutStatus"));
         orderEditForm.add(new NumberTextField<Integer>("itemTotal"));
         orderEditForm.add(new TextArea<String>("orderDescription"));
         orderEditForm.add(new TextField<String>("custom"));
         orderEditForm.add(new TextField<String>("note"));
         orderEditForm.add(new TextArea<String>("noteText"));
         orderEditForm.add(new BootstrapCheckbox("insuranceOptionOffered"));
         orderEditForm.add(new NumberTextField<Integer>("giftWrapAmount"));
         orderEditForm.add(new NumberTextField<Integer>("extraAmount"));
         orderEditForm.add(new NumberTextField<Integer>("discountTotal"));
         orderEditForm.add(new NumberTextField<Integer>("handlingTotal"));
         orderEditForm.add(new NumberTextField<Integer>("shippingTotal"));
         orderEditForm.add(new NumberTextField<Integer>("insuranceTotal"));
         orderEditForm.add(new NumberTextField<Integer>("shippingDiscount"));
         orderEditForm.add(new NumberTextField<Integer>("taxTotal"));
         orderEditForm.add(new NumberTextField<Integer>("orderTotal"));
         orderEditForm.add(new BootstrapCheckbox("giftMessageEnable"));
         orderEditForm.add(new BootstrapCheckbox("giftReceiptEnable"));
         orderEditForm.add(new BootstrapCheckbox("giftWrapEnable"));
         orderEditForm.add(new TextField<String>("giftWrapName"));
         orderEditForm.add(new TextArea<String>("giftMessage"));
         orderEditForm.add(new TextField<String>("shipment.shipmentType"));
         orderEditForm.add(new TextField<String>("shipment.address.postalCode"));
         orderEditForm.add(new TextField<String>("shipment.address.number"));
         orderEditForm.add(new TextField<String>("shipment.address.country"));
         orderEditForm.add(new TextField<String>("shipment.address.street1"));
         orderEditForm.add(new TextField<String>("shipment.address.street2"));
         orderEditForm.add(new TextField<String>("shipment.address.complement"));
         orderEditForm.add(new TextField<String>("shipment.address.district"));
         orderEditForm.add(new TextField<String>("shipment.address.cityName"));
         orderEditForm.add(new TextField<String>("shipment.address.stateOrProvince"));
         orderEditForm.add(new TextField<String>("shipment.address.countryName"));
         orderEditForm.add(new TextField<String>("shipment.address.internationalStreet"));
         orderEditForm.add(new TextField<String>("shipment.address.internationalStateAndCity"));
         orderEditForm.add(new TextField<String>("shipment.address.phone"));

         add(orderEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         add(new SaveAjaxButton(orderEditForm).setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class OrderViewFragement extends Fragment {

      private static final long serialVersionUID = 2134263849806147209L;

      public OrderViewFragement() {
         super("orderViewOrEditFragement", "orderViewFragement", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Order> orderViewForm = new Form<Order>("orderViewForm");
         orderViewForm.setModel(new CompoundPropertyModel<Order>((IModel<Order>) getDefaultModel()));

         orderViewForm.add(new Label("orderId"));
         orderViewForm.add(new Label("contract.contractId"));
         orderViewForm.add(new Label("token"));
         orderViewForm.add(new Label("transactionId"));
         orderViewForm.add(new Label("billingAgreementId"));
         orderViewForm.add(new Label("checkoutStatus"));
         orderViewForm.add(new Label("itemTotal"));
         orderViewForm.add(new Label("orderDescription"));
         orderViewForm.add(new Label("custom"));
         orderViewForm.add(new Label("note"));
         orderViewForm.add(new Label("noteText"));
         orderViewForm.add(new Label("insuranceOptionOffered"));
         orderViewForm.add(new Label("giftWrapAmount"));
         orderViewForm.add(new Label("extraAmount"));
         orderViewForm.add(new Label("discountTotal"));
         orderViewForm.add(new Label("handlingTotal"));
         orderViewForm.add(new Label("shippingTotal"));
         orderViewForm.add(new Label("insuranceTotal"));
         orderViewForm.add(new Label("shippingDiscount"));
         orderViewForm.add(new Label("taxTotal"));
         orderViewForm.add(new Label("orderTotal"));
         orderViewForm.add(new Label("giftMessageEnable"));
         orderViewForm.add(new Label("giftReceiptEnable"));
         orderViewForm.add(new Label("giftWrapEnable"));
         orderViewForm.add(new Label("giftWrapName"));
         orderViewForm.add(new Label("giftMessage"));
         orderViewForm.add(new Label("shipment.shipmentType"));
         orderViewForm.add(new Label("shipment.address.postalCode"));
         orderViewForm.add(new Label("shipment.address.number"));
         orderViewForm.add(new Label("shipment.address.country"));
         orderViewForm.add(new Label("shipment.address.street1"));
         orderViewForm.add(new Label("shipment.address.street2"));
         orderViewForm.add(new Label("shipment.address.complement"));
         orderViewForm.add(new Label("shipment.address.district"));
         orderViewForm.add(new Label("shipment.address.cityName"));
         orderViewForm.add(new Label("shipment.address.stateOrProvince"));
         orderViewForm.add(new Label("shipment.address.countryName"));
         orderViewForm.add(new Label("shipment.address.internationalStreet"));
         orderViewForm.add(new Label("shipment.address.internationalStateAndCity"));
         orderViewForm.add(new Label("shipment.address.phone"));

         add(new EditAjaxLink().setOutputMarkupId(true));
         add(orderViewForm.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Order order = (Order) form.getDefaultModelObject();

            if (order.getId() == 0) {
               order.setActive(true);

               orderDataProvider.persist(order);
            } else {
               orderDataProvider.merge(order);
            }

            OrderViewOrEditPanel.this.removeAll();
            OrderViewOrEditPanel.this.add(new OrderViewFragement().setOutputMarkupId(true));
         } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);

            String[] messages = e.getMessage().split(": ");
            String message = messages[messages.length - 1];

            warn(message.substring(0, 1).toUpperCase() + message.substring(1));
         } finally {
            target.add(target.getPage());
         }
      }
   }

   private static final long serialVersionUID = 4702200954395165271L;

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderViewOrEditPanel.class);

   @SpringBean(name = "OrderDataProvider", required = true)
   private GenericTypeDataProvider<Order> orderDataProvider;

   public OrderViewOrEditPanel(final String id, final IModel<Order> model) {
      super(id, model);
      add(new OrderViewFragement().setOutputMarkupId(true));
   }

}
