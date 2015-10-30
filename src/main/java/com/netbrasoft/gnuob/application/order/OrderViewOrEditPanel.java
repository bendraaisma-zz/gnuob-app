package com.netbrasoft.gnuob.application.order;

import java.math.BigDecimal;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.api.generic.converter.XmlGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class OrderViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OrderEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class OrderEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CancelAjaxLink extends BootstrapAjaxLink<Order> {

        private static final long serialVersionUID = 4267535261864907719L;

        public CancelAjaxLink(String id, IModel<Order> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          OrderViewOrEditPanel.this.removeAll();
          if (((Order) CancelAjaxLink.this.getDefaultModelObject()).getId() > 0) {
            OrderViewOrEditPanel.this.setDefaultModelObject(orderDataProvider.findById((Order) CancelAjaxLink.this.getDefaultModelObject()));
          }
          target.add(OrderViewOrEditPanel.this.add(OrderViewOrEditPanel.this.new OrderViewFragment()).setOutputMarkupPlaceholderTag(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY))));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          boolean isException = false;
          try {
            if (((Order) form.getDefaultModelObject()).getId() == 0) {
              OrderEditTable.this.setDefaultModelObject(orderDataProvider.findById(orderDataProvider.persist((Order) form.getDefaultModelObject())));
            } else {
              OrderEditTable.this.setDefaultModelObject(orderDataProvider.findById(orderDataProvider.merge((Order) form.getDefaultModelObject())));
            }
          } catch (final RuntimeException e) {
            isException = true;
            LOGGER.warn(e.getMessage(), e);
            feedbackPanel.warn(e.getLocalizedMessage());
            target.add(feedbackPanel.setOutputMarkupId(true));
            target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
          } finally {
            if (!isException) {
              OrderViewOrEditPanel.this.removeAll();
              target.add(OrderViewOrEditPanel.this.add(OrderViewOrEditPanel.this.new OrderViewFragment()).setOutputMarkupId(true));
            }
          }
        }
      }

      private static final long serialVersionUID = 6328203994858830738L;

      private final BootstrapForm<Order> orderEditForm;

      private final CancelAjaxLink cancelAjaxLink;

      private final SaveAjaxButton saveAjaxButton;

      private final NotificationPanel feedbackPanel;

      private final OrderRecordPanel orderRecordPanel;

      private final OrderInvoicePaymentPanel orderInvoicePaymentPanel;

      public OrderEditTable(final String id, final IModel<Order> model) {
        super(id, model);
        orderEditForm = new BootstrapForm<Order>("orderEditForm", new CompoundPropertyModel<Order>((IModel<Order>) OrderEditTable.this.getDefaultModel()));
        cancelAjaxLink =
            new CancelAjaxLink("cancel", model, Buttons.Type.Default, Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)), orderEditForm,
            Buttons.Type.Primary);
        feedbackPanel = new NotificationPanel("feedback");
        orderRecordPanel = new OrderRecordPanel("orderRecordPanel", (IModel<Order>) OrderEditTable.this.getDefaultModel());
        orderInvoicePaymentPanel = new OrderInvoicePaymentPanel("orderInvoicePaymentPanel", (IModel<Order>) OrderEditTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        orderEditForm.add(new RequiredTextField<String>("orderId").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("contract.contractId").add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        orderEditForm.add(new DatetimePicker("orderDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

          private static final long serialVersionUID = 1209354725150726556L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (XMLGregorianCalendar.class.isAssignableFrom(type)) {
              return (IConverter<C>) new XmlGregorianCalendarConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("token").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("transactionId").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("billingAgreementId").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("checkoutStatus").add(StringValidator.maximumLength(255)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("itemTotal").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new TextArea<String>("orderDescription").add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("custom").add(StringValidator.maximumLength(255)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("note").add(StringValidator.maximumLength(165)).setOutputMarkupId(true));
        orderEditForm.add(new TextArea<String>("noteText").add(StringValidator.maximumLength(255)).setOutputMarkupId(true));
        orderEditForm.add(new BootstrapCheckbox("insuranceOptionOffered").setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("giftWrapAmount").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("extraAmount").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("discountTotal").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("handlingTotal").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("shippingTotal").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("insuranceTotal").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("shippingDiscount").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("taxTotal").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new NumberTextField<BigDecimal>("orderTotal").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderEditForm.add(new BootstrapCheckbox("giftMessageEnable").setOutputMarkupId(true));
        orderEditForm.add(new BootstrapCheckbox("giftReceiptEnable").setOutputMarkupId(true));
        orderEditForm.add(new BootstrapCheckbox("giftWrapEnable").setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("giftWrapName").add(StringValidator.maximumLength(25)).setOutputMarkupId(true));
        orderEditForm.add(new TextArea<String>("giftMessage").add(StringValidator.maximumLength(150)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.shipmentType").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("shipment.address.postalCode").setLabel(Model.of(getString("postalCodeMessage")))
            .add(new PatternValidator("([0-9]){5}([-])([0-9]){3}")).add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.number").add(StringValidator.maximumLength(10)).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("shipment.address.country", Model.of("Brasil")).setLabel(Model.of(getString("countryNameMessage")))
            .add(StringValidator.maximumLength(40)).setEnabled(false).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("shipment.address.street1").setLabel(Model.of(getString("street1Message"))).add(StringValidator.maximumLength(100))
            .setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.street2").add(StringValidator.maximumLength(100)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.complement").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.district").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("shipment.address.cityName").setLabel(Model.of(getString("cityNameMessage"))).add(StringValidator.maximumLength(40))
            .setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("shipment.address.stateOrProvince").add(StringValidator.maximumLength(2))
            .setLabel(Model.of(getString("stateOrProvinceMessage"))).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.countryName").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.internationalStreet").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.internationalStateAndCity").add(StringValidator.maximumLength(80)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("shipment.address.phone").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderEditForm.add(orderRecordPanel.add(orderRecordPanel.new OrderRecordEditFragment()).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.invoiceId"));
        orderEditForm.add(new RequiredTextField<String>("invoice.address.postalCode").setLabel(Model.of(getString("postalCodeMessage")))
            .add(new PatternValidator("([0-9]){5}([-])([0-9]){3}")).add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.number").add(StringValidator.maximumLength(10)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.country", Model.of("Brasil")).setLabel(Model.of(getString("countryNameMessage")))
            .add(StringValidator.maximumLength(40)).setEnabled(false).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("invoice.address.street1").setLabel(Model.of(getString("street1Message"))).add(StringValidator.maximumLength(100))
            .setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.street2").add(StringValidator.maximumLength(100)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.complement").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.district").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("invoice.address.cityName").setLabel(Model.of(getString("cityNameMessage"))).add(StringValidator.maximumLength(40))
            .setOutputMarkupId(true));
        orderEditForm.add(new RequiredTextField<String>("invoice.address.stateOrProvince").setLabel(Model.of(getString("stateOrProvinceMessage"))).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.countryName").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.internationalStreet").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.internationalStateAndCity").add(StringValidator.maximumLength(80)).setOutputMarkupId(true));
        orderEditForm.add(new TextField<String>("invoice.address.phone").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderEditForm.add(orderInvoicePaymentPanel.add(orderInvoicePaymentPanel.new OrderInvoicePaymentEditFragment()).setOutputMarkupId(true));
        add(orderEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(cancelAjaxLink.setOutputMarkupId(true));
        add(saveAjaxButton.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -5645656866901827543L;

    private final WebMarkupContainer orderEditTable;

    public OrderEditFragment() {
      super("orderViewOrEditFragment", "orderEditFragment", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());
      orderEditTable = new OrderEditTable("orderEditTable", (IModel<Order>) OrderEditFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class OrderViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class EditAjaxLink extends BootstrapAjaxLink<Order> {

        private static final long serialVersionUID = 4267535261864907719L;

        public EditAjaxLink(String id, IModel<Order> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.edit);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          OrderViewOrEditPanel.this.removeAll();
          target.add(OrderViewOrEditPanel.this.add(OrderViewOrEditPanel.this.new OrderEditFragment().setOutputMarkupId(true)));
        }
      }

      private static final long serialVersionUID = 8819766347985798200L;

      private final EditAjaxLink editAjaxLink;

      private final BootstrapForm<Order> orderViewForm;

      private final OrderRecordPanel orderRecordPanel;

      private final OrderInvoicePaymentPanel orderInvoicePaymentPanel;

      public OrderViewTable(final String id, final IModel<Order> model) {
        super(id, model);
        orderViewForm = new BootstrapForm<Order>("orderViewForm", new CompoundPropertyModel<Order>((IModel<Order>) OrderViewTable.this.getDefaultModel()));
        editAjaxLink = new EditAjaxLink("edit", model, Buttons.Type.Primary, Model.of(OrderViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY)));
        orderRecordPanel = new OrderRecordPanel("orderRecordPanel", (IModel<Order>) OrderViewTable.this.getDefaultModel());
        orderInvoicePaymentPanel = new OrderInvoicePaymentPanel("orderInvoicePaymentPanel", (IModel<Order>) OrderViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        orderViewForm.add(new RequiredTextField<String>("orderId").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("contract.contractId").setOutputMarkupId(true));
        orderViewForm.add(new DatetimePicker("orderDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

          private static final long serialVersionUID = 1209354725150726556L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (XMLGregorianCalendar.class.isAssignableFrom(type)) {
              return (IConverter<C>) new XmlGregorianCalendarConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("token").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("transactionId").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("billingAgreementId").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("checkoutStatus").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("itemTotal").setOutputMarkupId(true));
        orderViewForm.add(new TextArea<String>("orderDescription").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("custom").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("note").setOutputMarkupId(true));
        orderViewForm.add(new TextArea<String>("noteText").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("insuranceOptionOffered").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("giftWrapAmount").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("extraAmount").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("discountTotal").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("handlingTotal").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("shippingTotal").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("insuranceTotal").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("shippingDiscount").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("taxTotal").setOutputMarkupId(true));
        orderViewForm.add(new NumberTextField<BigDecimal>("orderTotal").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("giftMessageEnable").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("giftReceiptEnable").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("giftWrapEnable").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("giftWrapName").setOutputMarkupId(true));
        orderViewForm.add(new TextArea<String>("giftMessage").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.shipmentType").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("shipment.address.postalCode").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.number").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("shipment.address.country", Model.of("Brasil")).setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("shipment.address.street1").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.street2").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.complement").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.district").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("shipment.address.cityName").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("shipment.address.stateOrProvince").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.countryName").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.internationalStreet").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.internationalStateAndCity").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("shipment.address.phone").setOutputMarkupId(true));
        orderViewForm.add(orderRecordPanel.add(orderRecordPanel.new OrderRecordViewFragment()).setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.invoiceId"));
        orderViewForm.add(new RequiredTextField<String>("invoice.address.postalCode").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.number").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.country", Model.of("Brasil")).setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("invoice.address.street1").setLabel(Model.of(getString("street1Message"))).setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.street2").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.complement").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.district").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("invoice.address.cityName").setOutputMarkupId(true));
        orderViewForm.add(new RequiredTextField<String>("invoice.address.stateOrProvince").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.countryName").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.internationalStreet").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.internationalStateAndCity").setOutputMarkupId(true));
        orderViewForm.add(new TextField<String>("invoice.address.phone").setOutputMarkupId(true));
        orderViewForm.add(orderInvoicePaymentPanel.add(orderInvoicePaymentPanel.new OrderInvoicePaymentViewFragment()).setOutputMarkupId(true));
        add(orderViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(editAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 2134263849806147209L;

    private final OrderViewTable orderViewTable;

    public OrderViewFragment() {
      super("orderViewOrEditFragment", "orderViewFragment", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());
      orderViewTable = new OrderViewTable("orderViewTable", (IModel<Order>) OrderViewFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 4702200954395165271L;

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderViewOrEditPanel.class);

  @SpringBean(name = "OrderDataProvider", required = true)
  private GenericTypeDataProvider<Order> orderDataProvider;

  public OrderViewOrEditPanel(final String id, final IModel<Order> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    orderDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    orderDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    orderDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    orderDataProvider.setType(new Order());
    orderDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
