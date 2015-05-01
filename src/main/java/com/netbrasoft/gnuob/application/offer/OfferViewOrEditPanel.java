package com.netbrasoft.gnuob.application.offer;

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

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(new OfferViewFragement()).setOutputMarkupId(true);
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
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(new OfferEditFragement().setOutputMarkupId(true));
         target.add(OfferViewOrEditPanel.this);
      }
   }

   class OfferEditFragement extends Fragment {

      private static final long serialVersionUID = -5645656866901827543L;

      public OfferEditFragement() {
         super("offerViewOrEditFragement", "offerEditFragement", OfferViewOrEditPanel.this, OfferViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Offer> offerEditForm = new Form<Offer>("offerEditForm");
         offerEditForm.setModel(new CompoundPropertyModel<Offer>((IModel<Offer>) getDefaultModel()));

         offerEditForm.add(new TextField<String>("offerId"));
         offerEditForm.add(new TextField<String>("contract.contractId"));
         offerEditForm.add(new NumberTextField<Integer>("itemTotal"));
         offerEditForm.add(new TextArea<String>("offerDescription"));
         offerEditForm.add(new NumberTextField<Integer>("extraAmount"));
         offerEditForm.add(new NumberTextField<Integer>("discountTotal"));
         offerEditForm.add(new NumberTextField<Integer>("handlingTotal"));
         offerEditForm.add(new NumberTextField<Integer>("shippingTotal"));
         offerEditForm.add(new NumberTextField<Integer>("insuranceTotal"));
         offerEditForm.add(new NumberTextField<Integer>("shippingDiscount"));
         offerEditForm.add(new NumberTextField<Integer>("taxTotal"));
         offerEditForm.add(new NumberTextField<Integer>("offerTotal"));

         add(offerEditForm.setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         add(new SaveAjaxButton(offerEditForm).setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class OfferViewFragement extends Fragment {

      private static final long serialVersionUID = 2134263849806147209L;

      public OfferViewFragement() {
         super("offerViewOrEditFragement", "offerViewFragement", OfferViewOrEditPanel.this, OfferViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Offer> offerViewForm = new Form<Offer>("offerViewForm");
         offerViewForm.setModel(new CompoundPropertyModel<Offer>((IModel<Offer>) getDefaultModel()));

         offerViewForm.add(new Label("offerId"));
         offerViewForm.add(new Label("contract.contractId"));
         offerViewForm.add(new Label("itemTotal"));
         offerViewForm.add(new Label("offerDescription"));
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
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
         form.add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Offer offer = (Offer) form.getDefaultModelObject();

            if (offer.getId() == 0) {
               offerDataProvider.persist(offer);
            } else {
               offerDataProvider.merge(offer);
            }

            OfferViewOrEditPanel.this.removeAll();
            OfferViewOrEditPanel.this.add(new OfferViewFragement().setOutputMarkupId(true));
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

   private static final Logger LOGGER = LoggerFactory.getLogger(OfferViewOrEditPanel.class);

   private static final long serialVersionUID = 4702200954395165271L;

   @SpringBean(name = "OfferDataProvider", required = true)
   private GenericTypeDataProvider<Offer> offerDataProvider;

   public OfferViewOrEditPanel(final String id, final IModel<Offer> model) {
      super(id, model);
      add(new OfferViewFragement().setOutputMarkupId(true));
   }
}
