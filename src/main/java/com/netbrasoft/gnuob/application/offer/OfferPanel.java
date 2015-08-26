package com.netbrasoft.gnuob.application.offer;

import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLinke extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 9191172039973638020L;

      public AddAjaxLinke() {
         super("add", Model.of(OfferPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(OfferPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         offerViewOrEditPanel.setDefaultModelObject(new Offer());
         target.add(offerViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OfferDataview extends DataView<Offer> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected OfferDataview() {
         super("offerDataview", offerDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Offer> newItem(String id, int index, IModel<Offer> model) {
         final Item<Offer> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Offer) offerViewOrEditPanel.getDefaultModelObject()).getId()) {
            // FIXME BD: use wicket bootstrap for this attribute / table.
            item.add(new AttributeModifier("class", "info"));
         } else {
            if (index == 0 && ((Offer) offerViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
               // FIXME BD: use wicket bootstrap for this attribute / table.
               item.add(new AttributeModifier("class", "info"));
            }
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Offer> item) {
         item.setModel(new CompoundPropertyModel<Offer>(item.getModelObject()));
         item.add(new Label("offerId"));
         item.add(new Label("contract.contractId"));
         item.add(new Label("contract.customer.payerId"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               offerViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(offerDataviewContainer.setOutputMarkupId(true));
               target.add(offerViewOrEditPanel.setOutputMarkupId(true));
            }
         });
         item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

            private static final long serialVersionUID = 7744720444161839031L;

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
               response.render($(component)
                     .chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true).withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage")))
                     .asDomReadyScript());
            }
         }));

         if (item.getIndex() == 0 && ((Offer) offerViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            offerViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Offer> {

      private static final long serialVersionUID = -8317730269644885290L;

      public RemoveAjaxLink(final IModel<Offer> model) {
         super("remove", model, Buttons.Type.Default, Model.of(OfferPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            getModelObject().setActive(false);
            offerDataProvider.merge(getModelObject());
            offerViewOrEditPanel.setDefaultModelObject(new Offer());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(OfferPanel.class);

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "OfferDataProvider", required = true)
   private GenericTypeDataProvider<Offer> offerDataProvider;

   private final OrderByBorder<String> orderByOfferId;

   private final OrderByBorder<String> orderByContractId;

   private final OrderByBorder<String> orderByPayerId;

   private final OfferDataview offerDataview;

   private final WebMarkupContainer offerDataviewContainer;

   private final BootstrapPagingNavigator offerPagingNavigator;

   private final OfferViewOrEditPanel offerViewOrEditPanel;

   public OfferPanel(final String id, final IModel<Offer> model) {
      super(id, model);

      orderByOfferId = new OrderByBorder<String>("orderByOfferId", "offerId", offerDataProvider);
      orderByContractId = new OrderByBorder<String>("orderByContractId", "contract.contractId", offerDataProvider);
      orderByPayerId = new OrderByBorder<String>("orderByPayerId", "contract.customer.payerId", offerDataProvider);
      offerDataview = new OfferDataview();
      offerDataviewContainer = new WebMarkupContainer("offerDataviewContainer") {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(offerDataview);
            super.onInitialize();
         }
      };
      offerPagingNavigator = new BootstrapPagingNavigator("offerPagingNavigator", offerDataview);
      offerViewOrEditPanel = new OfferViewOrEditPanel("offerViewOrEditPanel", (IModel<Offer>) getDefaultModel());
   }

   @Override
   protected void onInitialize() {
      offerDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      offerDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      offerDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      offerDataProvider.setType(new Offer());
      offerDataProvider.getType().setActive(true);

      add(new AddAjaxLinke());
      add(orderByOfferId.setOutputMarkupId(true));
      add(orderByContractId.setOutputMarkupId(true));
      add(orderByPayerId.setOutputMarkupId(true));
      add(offerDataviewContainer.setOutputMarkupId(true));
      add(offerPagingNavigator.setOutputMarkupId(true));
      add(offerViewOrEditPanel.add(offerViewOrEditPanel.new OfferViewFragement()).setOutputMarkupId(true));

      add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));

      super.onInitialize();
   }
}
