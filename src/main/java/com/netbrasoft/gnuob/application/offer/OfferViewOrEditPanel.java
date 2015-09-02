package com.netbrasoft.gnuob.application.offer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(OfferViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(OfferViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(new OfferViewFragement()).setOutputMarkupId(true);

         if (((Offer) OfferViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            OfferViewOrEditPanel.this.setDefaultModelObject(offerDataProvider.findById((Offer) OfferViewOrEditPanel.this.getDefaultModelObject()));
         }

         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(OfferViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(OfferViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OfferViewOrEditPanel.this.removeAll();
         OfferViewOrEditPanel.this.add(new OfferEditFragement().setOutputMarkupId(true));
         target.add(OfferViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OfferEditFragement extends Fragment {

      private static final long serialVersionUID = -5645656866901827543L;

      private final WebMarkupContainer offerEditTable;

      private final OfferRecordPanel offerRecordPanel;

      public OfferEditFragement() {
         super("offerViewOrEditFragement", "offerEditFragement", OfferViewOrEditPanel.this, OfferViewOrEditPanel.this.getDefaultModel());

         offerEditTable = new WebMarkupContainer("offerEditTable", getDefaultModel()) {

            private static final long serialVersionUID = 1724130778352490686L;

            @Override
            protected void onInitialize() {
               final Form<Offer> offerEditForm = new Form<Offer>("offerEditForm");
               offerEditForm.setModel(new CompoundPropertyModel<Offer>((IModel<Offer>) getDefaultModel()));
               offerEditForm.add(new RequiredTextField<String>("offerId").add(StringValidator.maximumLength(64)));
               offerEditForm.add(new RequiredTextField<String>("contract.contractId").add(StringValidator.maximumLength(127)));
               offerEditForm.add(new NumberTextField<Integer>("itemTotal"));
               offerEditForm.add(new TextArea<String>("offerDescription").add(StringValidator.maximumLength(127)));
               offerEditForm.add(new NumberTextField<Integer>("extraAmount").setRequired(true));
               offerEditForm.add(new NumberTextField<Integer>("discountTotal"));
               offerEditForm.add(new NumberTextField<Integer>("handlingTotal").setRequired(true));
               offerEditForm.add(new NumberTextField<Integer>("shippingTotal"));
               offerEditForm.add(new NumberTextField<Integer>("insuranceTotal").setRequired(true));
               offerEditForm.add(new NumberTextField<Integer>("shippingDiscount").setRequired(true));
               offerEditForm.add(new NumberTextField<Integer>("taxTotal"));
               offerEditForm.add(new NumberTextField<Integer>("offerTotal"));
               offerEditForm.add(offerRecordPanel.add(offerRecordPanel.new OfferRecordEditFragement()).setOutputMarkupId(true));
               add(offerEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new SaveAjaxButton(offerEditForm).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }

         };
         offerRecordPanel = new OfferRecordPanel("offerRecordPanel", (IModel<Offer>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(offerEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OfferViewFragement extends Fragment {

      private static final long serialVersionUID = 2134263849806147209L;

      private final WebMarkupContainer offerViewTable;

      private final OfferRecordPanel offerRecordPanel;

      public OfferViewFragement() {
         super("offerViewOrEditFragement", "offerViewFragement", OfferViewOrEditPanel.this, OfferViewOrEditPanel.this.getDefaultModel());
         offerViewTable = new WebMarkupContainer("offerViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 3049291652892184867L;

            @Override
            protected void onInitialize() {
               final Form<Offer> offerViewForm = new Form<Offer>("offerViewForm");
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
               offerViewForm.add(offerRecordPanel.add(offerRecordPanel.new OfferRecordViewFragement()).setOutputMarkupId(true));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(offerViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
         offerRecordPanel = new OfferRecordPanel("offerRecordPanel", (IModel<Offer>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(offerViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(OfferViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(OfferViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OfferViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Offer offer = (Offer) form.getDefaultModelObject();
            offer.setActive(true);

            if (offer.getId() == 0) {
               OfferViewOrEditPanel.this.setDefaultModelObject(offerDataProvider.findById(offerDataProvider.persist(offer)));
            } else {
               OfferViewOrEditPanel.this.setDefaultModelObject(offerDataProvider.findById(offerDataProvider.merge(offer)));
            }

            OfferViewOrEditPanel.this.removeAll();
            OfferViewOrEditPanel.this.add(new OfferViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
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
   }

   @Override
   protected void onInitialize() {
      offerDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      offerDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      offerDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      offerDataProvider.setType(new Offer());
      offerDataProvider.getType().setActive(true);
      super.onInitialize();
   }
}
