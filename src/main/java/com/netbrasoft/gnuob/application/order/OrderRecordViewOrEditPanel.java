package com.netbrasoft.gnuob.application.order;

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

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.OrderRecord;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class OrderRecordViewOrEditPanel extends Panel {

   class OrderRecordDataview extends DataView<OrderRecord> {

      private static final long serialVersionUID = 8996562822101409998L;

      protected OrderRecordDataview() {
         super("orderRecordDataview", new OrderRecordListDataProvider(), ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<OrderRecord> item) {
         IModel<OrderRecord> compound = new CompoundPropertyModel<OrderRecord>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("description"));
         item.add(new RemoveAjaxLink());
      }
   }

   class OrderRecordEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OrderRecordEditFragement() {
         super("orderRecordViewOrEditFragement", "orderRecordEditFragement", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(orderRecordPagingNavigator);
         add(orderRecordDataviewContainer.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class OrderRecordListDataProvider extends ListDataProvider<OrderRecord> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<OrderRecord> getData() {
         return ((Order) OrderRecordViewOrEditPanel.this.getDefaultModelObject()).getRecords();
      }
   }

   class OrderRecordViewFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OrderRecordViewFragement() {
         super("orderRecordViewOrEditFragement", "orderRecordViewFragement", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(orderRecordPagingNavigator);
         add(orderRecordDataviewContainer.setOutputMarkupId(true));
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

   private WebMarkupContainer orderRecordDataviewContainer = new WebMarkupContainer("orderRecordDataviewContainer") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
         add(orderRecordDataview);
         super.onInitialize();
      }
   };

   private OrderRecordDataview orderRecordDataview = new OrderRecordDataview();

   private BootstrapPagingNavigator orderRecordPagingNavigator = new BootstrapPagingNavigator("orderRecordPagingNavigator", orderRecordDataview);

   public OrderRecordViewOrEditPanel(final String id, final IModel<Order> model) {
      super(id, model);
   }
}
