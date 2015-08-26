package com.netbrasoft.gnuob.application.offer;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.OfferRecord;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OfferRecordPanel extends Panel {

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OfferRecordDataview extends DataView<OfferRecord> {

      private static final long serialVersionUID = 8996562822101409998L;

      private boolean removeAjaxLinkVisable;

      protected OfferRecordDataview() {
         super("offerRecordDataview", offerRecordListDataProvider, ITEMS_PER_PAGE);
      }

      public boolean isRemoveAjaxLinkVisable() {
         return removeAjaxLinkVisable;
      }

      @Override
      protected void populateItem(Item<OfferRecord> item) {
         final IModel<OfferRecord> compound = new CompoundPropertyModel<OfferRecord>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("description"));
         item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

            private static final long serialVersionUID = 7744720444161839031L;

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
               response.render($(component)
                     .chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true).withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage")))
                     .asDomReadyScript());
            }
         }).setVisible(isRemoveAjaxLinkVisable()));
      }

      public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
         this.removeAjaxLinkVisable = removeAjaxLinkVisable;
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OfferRecordEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OfferRecordEditFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordEditFragement", OfferRecordPanel.this, OfferRecordPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         offerRecordDataview.setRemoveAjaxLinkVisable(true);
         add(offerRecordPagingNavigator);
         add(offerRecordDataviewContainer.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class OfferRecordListDataProvider extends ListDataProvider<OfferRecord> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<OfferRecord> getData() {
         return ((Offer) OfferRecordPanel.this.getDefaultModelObject()).getRecords();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class OfferRecordViewFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OfferRecordViewFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordViewFragement", OfferRecordPanel.this, OfferRecordPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         offerRecordDataview.setRemoveAjaxLinkVisable(false);
         add(offerRecordPagingNavigator.setOutputMarkupId(true));
         add(offerRecordDataviewContainer.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<OfferRecord> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink(final IModel<OfferRecord> model) {
         super("remove", model, Buttons.Type.Default, Model.of(OfferRecordPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            offerRecordListDataProvider.getData().remove(getDefaultModelObject());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(offerRecordDataviewContainer.setOutputMarkupId(true));
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(OfferRecordPanel.class);

   private static final long serialVersionUID = -4119480413347414297L;

   private static final long ITEMS_PER_PAGE = 5;

   private final WebMarkupContainer offerRecordDataviewContainer;

   private final OfferRecordListDataProvider offerRecordListDataProvider;

   private final OfferRecordDataview offerRecordDataview;

   private final BootstrapPagingNavigator offerRecordPagingNavigator;

   public OfferRecordPanel(final String id, final IModel<Offer> model) {
      super(id, model);
      offerRecordListDataProvider = new OfferRecordListDataProvider();
      offerRecordDataview = new OfferRecordDataview();
      offerRecordPagingNavigator = new BootstrapPagingNavigator("offerRecordPagingNavigator", offerRecordDataview);
      offerRecordDataviewContainer = new WebMarkupContainer("offerRecordDataviewContainer") {

         private static final long serialVersionUID = 1L;

         @Override
         protected void onInitialize() {
            add(offerRecordDataview.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }
}
