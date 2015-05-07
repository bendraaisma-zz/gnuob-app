package com.netbrasoft.gnuob.application.offer;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.OfferRecord;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class OfferRecordViewOrEditPanel extends Panel {

   class OfferRecordDataview extends DataView<OfferRecord> {

      private static final long serialVersionUID = 8996562822101409998L;

      protected OfferRecordDataview() {
         super("offerRecordDataview", new OfferRecordListDataProvider(), ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<OfferRecord> item) {
         IModel<OfferRecord> compound = new CompoundPropertyModel<OfferRecord>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("description"));
         item.add(new RemoveAjaxLink());
      }
   }

   class OfferRecordEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OfferRecordEditFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordEditFragement", OfferRecordViewOrEditPanel.this, OfferRecordViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(offerRecordPagingNavigator);
         add(offerRecordDataviewContainer.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class OfferRecordListDataProvider extends ListDataProvider<OfferRecord> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<OfferRecord> getData() {
         return ((Offer) OfferRecordViewOrEditPanel.this.getDefaultModelObject()).getRecords();
      }
   }

   class OfferRecordViewFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OfferRecordViewFragement() {
         super("offerRecordViewOrEditFragement", "offerRecordViewFragement", OfferRecordViewOrEditPanel.this, OfferRecordViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(offerRecordPagingNavigator);
         add(offerRecordDataviewContainer.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink() {
         super("remove");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = -4119480413347414297L;

   private static final long ITEMS_PER_PAGE = 5;

   private WebMarkupContainer offerRecordDataviewContainer = new WebMarkupContainer("offerRecordDataviewContainer") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
         add(offerRecordDataview);
         super.onInitialize();
      }
   };

   private OfferRecordDataview offerRecordDataview = new OfferRecordDataview();

   private BootstrapPagingNavigator offerRecordPagingNavigator = new BootstrapPagingNavigator("offerRecordPagingNavigator", offerRecordDataview);

   public OfferRecordViewOrEditPanel(final String id, final IModel<Offer> model) {
      super(id, model);
   }
}
