package com.netbrasoft.gnuob.application.order;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
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
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.OrderRecord;
import com.netbrasoft.gnuob.api.Payment;
import com.netbrasoft.gnuob.api.generic.converter.XmlGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class OrderInvoicePaymentPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OrderInvoicePaymentEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class PaymentEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<Order> {

        private static final long serialVersionUID = 9191172039973638020L;

        public AddAjaxLink(String id, IModel<Order> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.plus);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ((Order) AddAjaxLink.this.getDefaultModelObject()).getRecords().add(new OrderRecord());
          paymentDataviewContainer.paymentDataview.index = ((Order) AddAjaxLink.this.getDefaultModelObject()).getInvoice().getPayments().size() - 1;
          orderInvoicePaymentViewOrEditPanel.removeAll();
          target.add(paymentDataviewContainer.setOutputMarkupId(true));
          target.add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class PaymentDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
        class PaymentDataview extends DataView<Payment> {

          @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
          class RemoveAjaxLink extends BootstrapAjaxLink<Payment> {

            private static final long serialVersionUID = -6950515027229520882L;

            public RemoveAjaxLink(String id, IModel<Payment> model, Buttons.Type type, IModel<String> labelModel) {
              super(id, model, type, labelModel);
              setIconType(GlyphIconType.remove);
              setSize(Buttons.Size.Mini);
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
              ((Order) PaymentDataviewContainer.this.getDefaultModelObject()).getInvoice().getPayments().remove(RemoveAjaxLink.this.getDefaultModelObject());
              paymentDataview.index = ((Order) PaymentDataviewContainer.this.getDefaultModelObject()).getInvoice().getPayments().size() - 1;
              orderInvoicePaymentViewOrEditPanel.removeAll();
              target.add(paymentDataviewContainer.setOutputMarkupId(true));
              target.add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
            }
          }

          private static final long serialVersionUID = 8996562822101409998L;

          private int index = 0;

          protected PaymentDataview(final String id, final IDataProvider<Payment> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Payment> newItem(String id, int index, IModel<Payment> model) {
            final Item<Payment> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            final IModel<Order> model = (IModel<Order>) PaymentDataviewContainer.this.getDefaultModel();
            if (!model.getObject().getInvoice().getPayments().isEmpty()) {
              orderInvoicePaymentViewOrEditPanel.setEnabled(true);
              orderInvoicePaymentViewOrEditPanel.removeAll();
              orderInvoicePaymentViewOrEditPanel.setSelectedModel(Model.of(model.getObject().getInvoice().getPayments().get(index)));
              orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true);
            } else {
              orderInvoicePaymentViewOrEditPanel.setEnabled(false);
              orderInvoicePaymentViewOrEditPanel.removeAll();
              orderInvoicePaymentViewOrEditPanel.setSelectedModel(Model.of(new Payment()));
              orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Payment> item) {
            item.setModel(new CompoundPropertyModel<Payment>(item.getModelObject()));
            item.add(new Label("paymentDate") {

              private static final long serialVersionUID = 3621260522785287715L;

              @Override
              public <C> IConverter<C> getConverter(final Class<C> type) {
                return (IConverter<C>) new XmlGregorianCalendarConverter();
              }
            });
            item.add(new Label("paymentStatus"));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                orderInvoicePaymentViewOrEditPanel.setSelectedModel(item.getModel());
                orderInvoicePaymentViewOrEditPanel.removeAll();
                target.add(paymentDataviewContainer.setOutputMarkupId(true));
                target.add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
              }
            });
            item.add(new RemoveAjaxLink("remove", item.getModel(), Buttons.Type.Default,
                Model.of(OrderInvoicePaymentPanel.this.getString(NetbrasoftApplicationConstants.REMOVE_MESSAGE_KEY))).add(new ConfirmationBehavior() {

                  private static final long serialVersionUID = 7744720444161839031L;

                  @Override
                  public void renderHead(Component component, IHeaderResponse response) {
                    response.render($(component).chain("confirmation",
                        new ConfirmationConfig().withTitle(getString(NetbrasoftApplicationConstants.CONFIRMATION_TITLE_MESSAGE_KEY)).withSingleton(true).withPopout(true)
                            .withBtnOkLabel(getString(NetbrasoftApplicationConstants.CONFIRM_MESSAGE_KEY))
                            .withBtnCancelLabel(getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)))
                        .asDomReadyScript());
                  }
                }));
          }
        }

        private static final long serialVersionUID = -3279939331726432855L;

        private final PaymentDataview paymentDataview;

        private final ListDataProvider<Payment> paymentListDataProvider;

        public PaymentDataviewContainer(final String id, final IModel<Order> model) {
          super(id, model);
          paymentListDataProvider = new ListDataProvider<Payment>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<Payment> getData() {
              return ((Order) PaymentDataviewContainer.this.getDefaultModelObject()).getInvoice().getPayments();
            }
          };
          paymentDataview = new PaymentDataview("paymentDataview", paymentListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(paymentDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -6153754314386660526L;

      private final AddAjaxLink addAjaxLink;

      private final PaymentDataviewContainer paymentDataviewContainer;

      private final BootstrapPagingNavigator paymentPagingNavigator;

      private final OrderInvoicePaymentViewOrEditPanel orderInvoicePaymentViewOrEditPanel;

      public PaymentEditTable(final String id, final IModel<Order> model) {
        super(id, model);
        addAjaxLink = new AddAjaxLink("add", (IModel<Order>) PaymentEditTable.this.getDefaultModel(), Buttons.Type.Primary,
            Model.of(OrderInvoicePaymentPanel.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
        paymentDataviewContainer = new PaymentDataviewContainer("orderRecordDataviewContainer", (IModel<Order>) PaymentEditTable.this.getDefaultModel());
        paymentPagingNavigator = new BootstrapPagingNavigator("orderRecordPagingNavigator", paymentDataviewContainer.paymentDataview);
        orderInvoicePaymentViewOrEditPanel = new OrderInvoicePaymentViewOrEditPanel("orderInvoicePaymentViewOrEditPanel", (IModel<Order>) PaymentEditTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(addAjaxLink.setOutputMarkupId(true));
        add(paymentDataviewContainer.setOutputMarkupId(true));
        add(paymentPagingNavigator.setOutputMarkupId(true));
        add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 3709791409078428685L;

    private final PaymentEditTable paymentEditTable;

    public OrderInvoicePaymentEditFragment() {
      super("orderInvoicePaymentViewOrEditFragment", "orderInvoicePaymentEditFragment", OrderInvoicePaymentPanel.this, OrderInvoicePaymentPanel.this.getDefaultModel());
      paymentEditTable = new PaymentEditTable("paymentEditTable", (IModel<Order>) OrderInvoicePaymentEditFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(paymentEditTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderInvoicePaymentViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class PaymentViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class PaymentDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class PaymentDataview extends DataView<Payment> {

          private static final long serialVersionUID = 8996562822101409998L;

          private int index = 0;

          protected PaymentDataview(final String id, final IDataProvider<Payment> dataProvider, final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Payment> newItem(String id, int index, IModel<Payment> model) {
            final Item<Payment> item = super.newItem(id, index, model);
            if (this.index == index) {
              item.add(new AttributeModifier("class", "info"));
            }
            return item;
          }

          @Override
          protected void onConfigure() {
            final IModel<Order> model = (IModel<Order>) PaymentDataviewContainer.this.getDefaultModel();
            if (!model.getObject().getInvoice().getPayments().isEmpty()) {
              orderInvoicePaymentViewOrEditPanel.removeAll();
              orderInvoicePaymentViewOrEditPanel.setSelectedModel(Model.of(model.getObject().getInvoice().getPayments().get(index)));
              orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentViewFragement()).setOutputMarkupId(true);
            } else {
              orderInvoicePaymentViewOrEditPanel.removeAll();
              orderInvoicePaymentViewOrEditPanel.setSelectedModel(Model.of(new Payment()));
              orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentViewFragement()).setOutputMarkupId(true);
            }
            super.onConfigure();
          }

          @Override
          protected void populateItem(Item<Payment> item) {
            item.setModel(new CompoundPropertyModel<Payment>(item.getModelObject()));
            item.add(new Label("paymentDate") {

              private static final long serialVersionUID = 3621260522785287715L;

              @Override
              public <C> IConverter<C> getConverter(final Class<C> type) {
                return (IConverter<C>) new XmlGregorianCalendarConverter();
              }
            });
            item.add(new Label("paymentStatus"));
            item.add(new AjaxEventBehavior("click") {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(AjaxRequestTarget target) {
                index = item.getIndex();
                orderInvoicePaymentViewOrEditPanel.setSelectedModel(item.getModel());
                orderInvoicePaymentViewOrEditPanel.removeAll();
                target.add(paymentDataviewContainer.setOutputMarkupId(true));
                target.add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentViewFragement()).setOutputMarkupId(true));
              }
            });
          }
        }

        private static final long serialVersionUID = -3279939331726432855L;

        private final PaymentDataview paymentDataview;

        private final ListDataProvider<Payment> paymentListDataProvider;

        public PaymentDataviewContainer(final String id, final IModel<Order> model) {
          super(id, model);
          paymentListDataProvider = new ListDataProvider<Payment>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<Payment> getData() {
              return ((Order) PaymentDataviewContainer.this.getDefaultModelObject()).getInvoice().getPayments();
            }
          };
          paymentDataview = new PaymentDataview("paymentDataview", paymentListDataProvider, ITEMS_PER_PAGE);

        }

        @Override
        protected void onInitialize() {
          add(paymentDataview.setOutputMarkupId(true));
          super.onInitialize();
        }
      }

      private static final long serialVersionUID = -6153754314386660526L;

      private final PaymentDataviewContainer paymentDataviewContainer;

      private final BootstrapPagingNavigator paymentPagingNavigator;

      private final OrderInvoicePaymentViewOrEditPanel orderInvoicePaymentViewOrEditPanel;

      public PaymentViewTable(final String id, final IModel<Order> model) {
        super(id, model);
        paymentDataviewContainer = new PaymentDataviewContainer("orderRecordDataviewContainer", (IModel<Order>) PaymentViewTable.this.getDefaultModel());
        paymentPagingNavigator = new BootstrapPagingNavigator("orderRecordPagingNavigator", paymentDataviewContainer.paymentDataview);
        orderInvoicePaymentViewOrEditPanel = new OrderInvoicePaymentViewOrEditPanel("orderInvoicePaymentViewOrEditPanel", (IModel<Order>) PaymentViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(paymentDataviewContainer.setOutputMarkupId(true));
        add(paymentPagingNavigator.setOutputMarkupId(true));
        add(orderInvoicePaymentViewOrEditPanel.add(orderInvoicePaymentViewOrEditPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 3709791409078428685L;

    private final PaymentViewTable paymentViewTable;

    public OrderInvoicePaymentViewFragment() {
      super("orderInvoicePaymentViewOrEditFragment", "orderInvoicePaymentViewFragment", OrderInvoicePaymentPanel.this, OrderInvoicePaymentPanel.this.getDefaultModel());
      paymentViewTable = new PaymentViewTable("paymentViewTable", (IModel<Order>) OrderInvoicePaymentViewFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(paymentViewTable.add(new TableBehavior().hover()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = -4119480413347414297L;

  private static final long ITEMS_PER_PAGE = 10;

  public OrderInvoicePaymentPanel(final String id, final IModel<Order> model) {
    super(id, model);
  }
}
