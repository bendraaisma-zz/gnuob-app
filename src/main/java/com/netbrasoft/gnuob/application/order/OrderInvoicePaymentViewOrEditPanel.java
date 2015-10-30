package com.netbrasoft.gnuob.application.order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.Payment;
import com.netbrasoft.gnuob.api.generic.converter.XmlGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class OrderInvoicePaymentViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OrderInvoicePaymentEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class PaymentEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(final String id, final IModel<String> model, final Form<Payment> form, final Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY))));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          target.add(form.add(new TooltipValidation()));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          if (((Payment) form.getDefaultModelObject()).getId() == 0) {
            ((Order) OrderInvoicePaymentViewOrEditPanel.this.getDefaultModelObject()).getInvoice().getPayments().add((Payment) form.getDefaultModelObject());
          }
          target.add(form.setOutputMarkupId(true));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
          target.add(OrderInvoicePaymentViewOrEditPanel.this.getParent().setOutputMarkupId(true));
        }
      }

      private static final long serialVersionUID = -317942480731012722L;

      private final BootstrapForm<Payment> paymentEditForm;

      private final SaveAjaxButton saveAjaxButton;

      public PaymentEditTable(final String id, final IModel<Order> model) {
        super(id, model);
        paymentEditForm = new BootstrapForm<Payment>("paymentEditForm", new CompoundPropertyModel<Payment>(OrderInvoicePaymentViewOrEditPanel.this.selectedModel));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)), paymentEditForm,
            Buttons.Type.Primary);
      }

      @Override
      protected void onInitialize() {
        paymentEditForm.add(new TextField<String>("paymentRequestId").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("storeId").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("terminalId").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        paymentEditForm.add(new RequiredTextField<String>("transactionId").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        paymentEditForm.add(new RequiredTextField<String>("transactionType").add(StringValidator.maximumLength(62)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("protectionEligibilityType").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        paymentEditForm.add(new RequiredTextField<String>("paymentType").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new RequiredTextField<String>("paymentStatus").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("pendingReason").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("holdDecision").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new TextField<String>("reasonCode").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        paymentEditForm.add(new DatetimePicker("paymentDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
        paymentEditForm.add(new TextField<String>("exchangeRate").add(StringValidator.maximumLength(17)).setOutputMarkupId(true));
        paymentEditForm.add(new NumberTextField<BigInteger>("installmentCount").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        paymentEditForm.add(new NumberTextField<BigDecimal>("grossAmount").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        paymentEditForm.add(new NumberTextField<BigDecimal>("settleAmount").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        paymentEditForm.add(new NumberTextField<BigDecimal>("feeAmount").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        paymentEditForm.add(new NumberTextField<BigDecimal>("taxAmount").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        paymentEditForm.add(saveAjaxButton.setOutputMarkupId(true));
        add(paymentEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 3709791409078428685L;

    private final WebMarkupContainer paymentEditTable;

    public OrderInvoicePaymentEditFragement() {
      super("orderInvoicePaymentViewOrEditFragment", "orderInvoicePaymentEditFragment", OrderInvoicePaymentViewOrEditPanel.this,
          OrderInvoicePaymentViewOrEditPanel.this.getDefaultModel());
      paymentEditTable = new PaymentEditTable("paymentEditTable", (IModel<Order>) OrderInvoicePaymentEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(paymentEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderInvoicePaymentViewFragement extends Fragment {
    
    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class PaymentViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = 3485437486331806341L;
      
      private final BootstrapForm<Payment> paymentViewTable;

      public PaymentViewTable(final String id, final IModel<Order> model) {
        super(id, model);
        paymentViewTable = new BootstrapForm<Payment>("paymentViewTable", new CompoundPropertyModel<Payment>(OrderInvoicePaymentViewOrEditPanel.this.selectedModel));
      }
      
      @Override
      protected void onInitialize() {
        paymentViewTable.add(new TextField<String>("paymentRequestId").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("storeId").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("terminalId").setOutputMarkupId(true));
        paymentViewTable.add(new RequiredTextField<String>("transactionId").setOutputMarkupId(true));
        paymentViewTable.add(new RequiredTextField<String>("transactionType").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("protectionEligibilityType").setOutputMarkupId(true));
        paymentViewTable.add(new RequiredTextField<String>("paymentType").setOutputMarkupId(true));
        paymentViewTable.add(new RequiredTextField<String>("paymentStatus").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("pendingReason").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("holdDecision").setOutputMarkupId(true));
        paymentViewTable.add(new TextField<String>("reasonCode").setOutputMarkupId(true));
        paymentViewTable.add(new DatetimePicker("paymentDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
        paymentViewTable.add(new TextField<String>("exchangeRate").setOutputMarkupId(true));
        paymentViewTable.add(new NumberTextField<BigInteger>("installmentCount").setOutputMarkupId(true));
        paymentViewTable.add(new NumberTextField<BigDecimal>("grossAmount").setOutputMarkupId(true));
        paymentViewTable.add(new NumberTextField<BigDecimal>("settleAmount").setOutputMarkupId(true));
        paymentViewTable.add(new NumberTextField<BigDecimal>("feeAmount").setOutputMarkupId(true));
        paymentViewTable.add(new NumberTextField<BigDecimal>("taxAmount").setOutputMarkupId(true));
        add(paymentViewTable.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 6927997909191615786L;

    private final WebMarkupContainer paymentViewTable;

    public OrderInvoicePaymentViewFragement() {
      super("orderInvoicePaymentViewOrEditFragment", "orderInvoicePaymentViewFragment", OrderInvoicePaymentViewOrEditPanel.this,
          OrderInvoicePaymentViewOrEditPanel.this.getDefaultModel());
      paymentViewTable = new PaymentViewTable("paymentViewTable", (IModel<Order>) OrderInvoicePaymentViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(paymentViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = -7002701340914975498L;

  private IModel<Payment> selectedModel;

  public OrderInvoicePaymentViewOrEditPanel(final String id, final IModel<Order> model) {
    super(id, model);
    selectedModel = Model.of(new Payment());
  }

  public void setSelectedModel(IModel<Payment> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
