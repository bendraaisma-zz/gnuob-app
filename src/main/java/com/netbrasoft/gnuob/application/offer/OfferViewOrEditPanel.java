package com.netbrasoft.gnuob.application.offer;

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

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.application.security.Roles;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER, Roles.EMPLOYEE })
public class OfferViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(createOfferViewFragement()).setOutputMarkupId(true);
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
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(createOfferEditFragement().setOutputMarkupId(true));
         target.add(OfferViewOrEditPanel.this);
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

   public OfferViewOrEditPanel(final String id, final IModel<Offer> model) {
      super(id, model);
      add(createOfferViewFragement().setOutputMarkupId(true));
   }

   private Fragment createOfferEditFragement() {
      return new Fragment("offerViewOrEditFragement", "offerEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -5645656866901827543L;

         @Override
         protected void onInitialize() {
            Form<Offer> offerEditForm = new Form<Offer>("offerEditForm");
            offerEditForm.setModel(new CompoundPropertyModel<Offer>((IModel<Offer>) getDefaultModel()));

            offerEditForm.add(new TextField<String>("offerId"));
            offerEditForm.add(new TextField<String>("contract.contractId"));
            offerEditForm.add(new TextField<String>("itemTotal"));
            offerEditForm.add(new TextArea<String>("offerDescription"));
            // offerEditForm.add(new
            // BootstrapCheckbox("insuranceOptionOffered"));
            // offerEditForm.add(new TextField<String>("giftWrapAmount"));
            offerEditForm.add(new TextField<String>("extraAmount"));
            offerEditForm.add(new TextField<String>("discountTotal"));
            offerEditForm.add(new TextField<String>("handlingTotal"));
            offerEditForm.add(new TextField<String>("shippingTotal"));
            offerEditForm.add(new TextField<String>("insuranceTotal"));
            offerEditForm.add(new TextField<String>("shippingDiscount"));
            offerEditForm.add(new TextField<String>("taxTotal"));
            offerEditForm.add(new TextField<String>("offerTotal"));

            add(offerEditForm.setOutputMarkupId(true));
            add(new CancelAjaxLink().setOutputMarkupId(true));
            add(new SaveAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private Fragment createOfferViewFragement() {
      return new Fragment("offerViewOrEditFragement", "offerViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 2134263849806147209L;

         @Override
         protected void onInitialize() {
            Form<Offer> offerViewForm = new Form<Offer>("offerViewForm");
            offerViewForm.setModel(new CompoundPropertyModel<Offer>((IModel<Offer>) getDefaultModel()));

            offerViewForm.add(new Label("offerId"));
            offerViewForm.add(new Label("contract.contractId"));
            offerViewForm.add(new Label("itemTotal"));
            offerViewForm.add(new Label("offerDescription"));
            // offerViewForm.add(new Label("insuranceOptionOffered"));
            // offerViewForm.add(new Label("giftWrapAmount"));
            offerViewForm.add(new Label("extraAmount"));
            offerViewForm.add(new Label("discountTotal"));
            offerViewForm.add(new Label("handlingTotal"));
            offerViewForm.add(new Label("shippingTotal"));
            offerViewForm.add(new Label("insuranceTotal"));
            offerViewForm.add(new Label("shippingDiscount"));
            offerViewForm.add(new Label("taxTotal"));
            offerViewForm.add(new Label("offerTotal"));

            add(new EditAjaxLink().setOutputMarkupId(true));
            add(offerViewForm.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }
}
