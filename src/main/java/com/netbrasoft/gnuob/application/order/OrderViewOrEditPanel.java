package com.netbrasoft.gnuob.application.order;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.application.security.Roles;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER, Roles.EMPLOYEE })
public class OrderViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(createOrderViewFragement()).setOutputMarkupId(true);
         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(createOrderEditFragement().setOutputMarkupId(true));
         target.add(OrderViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class SaveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = -7111613947066504408L;

      public SaveAjaxLink() {
         super("save");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = 4702200954395165271L;

   public OrderViewOrEditPanel(final String id, final IModel<Order> model) {
      super(id, model);
      add(createOrderViewFragement().setOutputMarkupId(true));
   }

   private Fragment createOrderEditFragement() {
      return new Fragment("orderViewOrEditFragement", "orderEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -5645656866901827543L;

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
            orderEditForm.add(new TextField<String>("itemTotal"));
            orderEditForm.add(new TextArea<String>("orderDescription"));
            orderEditForm.add(new TextField<String>("custom"));
            orderEditForm.add(new TextField<String>("note"));
            orderEditForm.add(new TextArea<String>("noteText"));
            orderEditForm.add(new BootstrapCheckbox("insuranceOptionOffered"));
            orderEditForm.add(new TextField<String>("giftWrapAmount"));
            orderEditForm.add(new TextField<String>("extraAmount"));
            orderEditForm.add(new TextField<String>("discountTotal"));
            orderEditForm.add(new TextField<String>("handlingTotal"));
            orderEditForm.add(new TextField<String>("shippingTotal"));
            orderEditForm.add(new TextField<String>("insuranceTotal"));
            orderEditForm.add(new TextField<String>("shippingDiscount"));
            orderEditForm.add(new TextField<String>("taxTotal"));
            orderEditForm.add(new TextField<String>("orderTotal"));
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
            add(new CancelAjaxLink().setOutputMarkupId(true));
            add(new SaveAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private Fragment createOrderViewFragement() {
      return new Fragment("orderViewOrEditFragement", "orderViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 2134263849806147209L;

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
      };
   }

}
