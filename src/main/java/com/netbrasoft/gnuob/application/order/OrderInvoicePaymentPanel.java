package com.netbrasoft.gnuob.application.order;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
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
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.Payment;
import com.netbrasoft.gnuob.api.generic.XMLGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OrderInvoicePaymentPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 9191172039973638020L;

      public AddAjaxLink() {
         super("add", Model.of(OrderInvoicePaymentPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(OrderInvoicePaymentPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         orderInvoicePaymentViewOrEditPanel.setDefaultModelObject(new Payment());
         target.add(orderInvoicePaymentViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OrderInvoicePaymentEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OrderInvoicePaymentEditFragement() {
         super("orderInvoicePaymentViewOrEditFragement", "orderInvoicePaymentEditFragement", OrderInvoicePaymentPanel.this, OrderInvoicePaymentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(paymentEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OrderInvoicePaymentViewFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      public OrderInvoicePaymentViewFragement() {
         super("orderInvoicePaymentViewOrEditFragement", "orderInvoicePaymentViewFragement", OrderInvoicePaymentPanel.this, OrderInvoicePaymentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(paymentViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class PaymentDataview extends DataView<Payment> {

      private static final long serialVersionUID = 8996562822101409998L;

      private boolean removeAjaxLinkVisable;

      private long selectedObjectId;

      protected PaymentDataview() {
         super("paymentDataview", paymentListDataProvider, ITEMS_PER_PAGE);
      }

      public boolean isRemoveAjaxLinkVisable() {
         return removeAjaxLinkVisable;
      }

      @Override
      protected Item<Payment> newItem(String id, int index, IModel<Payment> model) {
         final Item<Payment> item = super.newItem(id, index, model);
         final long modelObjectId = ((Payment) orderInvoicePaymentViewOrEditPanel.getDefaultModelObject()).getId();

         if ((model.getObject().getId() == modelObjectId) || modelObjectId == 0) {
            item.add(new BootstrapBaseBehavior() {

               private static final long serialVersionUID = -4903722864597601489L;

               @Override
               public void onComponentTag(Component component, ComponentTag tag) {
                  Attributes.addClass(tag, "info");
               }
            });
         }
         return item;
      }

      @Override
      protected void onConfigure() {
         if (selectedObjectId != ((Order) OrderInvoicePaymentPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Order) OrderInvoicePaymentPanel.this.getDefaultModelObject()).getId();
            orderInvoicePaymentViewOrEditPanel.setDefaultModelObject(new Payment());
         }
         super.onConfigure();
      }

      @Override
      protected void populateItem(Item<Payment> item) {
         final IModel<Payment> compound = new CompoundPropertyModel<Payment>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("paymentDate") {

            private static final long serialVersionUID = 3621260522785287715L;

            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(final Class<C> type) {
               return (IConverter<C>) new XMLGregorianCalendarConverter();
            }
         });
         item.add(new Label("paymentStatus"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               orderInvoicePaymentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(paymentDataviewContainer.setOutputMarkupId(true));
               target.add(orderInvoicePaymentViewOrEditPanel.setOutputMarkupId(true));
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
         }).setVisible(isRemoveAjaxLinkVisable()));

         if (item.getIndex() == 0 && ((Payment) orderInvoicePaymentViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            orderInvoicePaymentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }

      public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
         this.removeAjaxLinkVisable = removeAjaxLinkVisable;
      }
   }

   class PaymentListDataProvider extends ListDataProvider<Payment> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<Payment> getData() {
         return ((Order) OrderInvoicePaymentPanel.this.getDefaultModelObject()).getInvoice().getPayments();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Payment> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink(final IModel<Payment> model) {
         super("remove", model, Buttons.Type.Default, Model.of(OrderInvoicePaymentPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            paymentListDataProvider.getData().remove(getDefaultModelObject());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(paymentDataviewContainer.setOutputMarkupId(true));
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordPanel.class);

   private static final long serialVersionUID = -4119480413347414297L;

   private static final long ITEMS_PER_PAGE = 10;

   private final WebMarkupContainer paymentDataviewContainer;

   private final PaymentDataview paymentDataview;

   private final BootstrapPagingNavigator paymentPagingNavigator;

   private final PaymentListDataProvider paymentListDataProvider;

   private final OrderInvoicePaymentViewOrEditPanel orderInvoicePaymentViewOrEditPanel;

   private final WebMarkupContainer paymentEditTable;

   private final WebMarkupContainer paymentViewTable;

   public OrderInvoicePaymentPanel(final String id, final IModel<Order> model) {
      super(id, model);
      paymentListDataProvider = new PaymentListDataProvider();
      paymentDataview = new PaymentDataview();
      paymentPagingNavigator = new BootstrapPagingNavigator("paymentPagingNavigator", paymentDataview);
      paymentDataviewContainer = new WebMarkupContainer("paymentDataviewContainer") {

         private static final long serialVersionUID = 1L;

         @Override
         protected void onInitialize() {
            add(paymentDataview);
            super.onInitialize();
         }
      };
      paymentEditTable = new WebMarkupContainer("paymentEditTable", getDefaultModel()) {

         private static final long serialVersionUID = 459122691621477233L;

         @Override
         protected void onInitialize() {
            paymentDataview.setRemoveAjaxLinkVisable(true);
            add(new AddAjaxLink().setOutputMarkupId(true));
            add(paymentPagingNavigator.setOutputMarkupId(true));
            add(paymentDataviewContainer.setOutputMarkupId(true));
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
            add(new TableBehavior());
            super.onInitialize();
         }
      };
      paymentViewTable = new WebMarkupContainer("paymentViewTable", getDefaultModel()) {

         private static final long serialVersionUID = 8276100341510505878L;

         @Override
         protected void onInitialize() {
            paymentDataview.setRemoveAjaxLinkVisable(false);
            add(paymentPagingNavigator.setOutputMarkupId(true));
            add(paymentDataviewContainer.setOutputMarkupId(true));
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentViewFragement()).setOutputMarkupId(true));
            add(new TableBehavior());
            super.onInitialize();
         }
      };
      orderInvoicePaymentViewOrEditPanel = new OrderInvoicePaymentViewOrEditPanel("paymentViewOrEditPanel", Model.of(new Payment()), paymentEditTable);
   }
}
