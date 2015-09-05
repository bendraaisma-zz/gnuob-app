package com.netbrasoft.gnuob.application.order;

import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.Payment;
import com.netbrasoft.gnuob.api.generic.XMLGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OrderInvoicePaymentViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OrderInvoicePaymentEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      private final WebMarkupContainer paymentEditTable;

      public OrderInvoicePaymentEditFragement() {
         super("orderInvoicePaymentViewOrEditFragement", "orderInvoicePaymentEditFragement", OrderInvoicePaymentViewOrEditPanel.this, OrderInvoicePaymentViewOrEditPanel.this.getDefaultModel());

         paymentEditTable = new WebMarkupContainer("paymentEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -6051033065197862976L;

            @Override
            protected void onInitialize() {
               final Form<Payment> paymentEditForm = new Form<Payment>("paymentEditForm");
               paymentEditForm.setModel(new CompoundPropertyModel<Payment>((IModel<Payment>) getDefaultModel()));
               paymentEditForm.add(new TextField<String>("paymentRequestId").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new TextField<String>("storeId").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new TextField<String>("terminalId").add(StringValidator.maximumLength(128)));
               paymentEditForm.add(new RequiredTextField<String>("transactionId").add(StringValidator.maximumLength(64)));
               paymentEditForm.add(new RequiredTextField<String>("transactionType").add(StringValidator.maximumLength(62)));
               paymentEditForm.add(new TextField<String>("protectionEligibilityType").add(StringValidator.maximumLength(128)));
               paymentEditForm.add(new RequiredTextField<String>("paymentType").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new RequiredTextField<String>("paymentStatus").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new TextField<String>("pendingReason").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new TextField<String>("holdDecision").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new TextField<String>("reasonCode").add(StringValidator.maximumLength(20)));
               paymentEditForm.add(new DatetimePicker("paymentDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

                  private static final long serialVersionUID = 1209354725150726556L;

                  @Override
                  public <C> IConverter<C> getConverter(final Class<C> type) {
                     if (XMLGregorianCalendar.class.isAssignableFrom(type)) {
                        return (IConverter<C>) new XMLGregorianCalendarConverter();
                     } else {
                        return super.getConverter(type);
                     }
                  }
               });
               paymentEditForm.add(new TextField<String>("exchangeRate").add(StringValidator.maximumLength(17)));
               paymentEditForm.add(new NumberTextField<Integer>("installmentCount"));
               paymentEditForm.add(new NumberTextField<Integer>("grossAmount").setRequired(true));
               paymentEditForm.add(new NumberTextField<Integer>("settleAmount"));
               paymentEditForm.add(new NumberTextField<Integer>("feeAmount"));
               paymentEditForm.add(new NumberTextField<Integer>("taxAmount"));
               paymentEditForm.add(new SaveAjaxButton(paymentEditForm).setOutputMarkupId(true));
               add(paymentEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(paymentEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class OrderInvoicePaymentViewFragement extends Fragment {

      private static final long serialVersionUID = 6927997909191615786L;

      private final WebMarkupContainer paymentViewTable;

      public OrderInvoicePaymentViewFragement() {
         super("orderInvoicePaymentViewOrEditFragement", "orderInvoicePaymentViewFragement", OrderInvoicePaymentViewOrEditPanel.this, OrderInvoicePaymentViewOrEditPanel.this.getDefaultModel());
         paymentViewTable = new WebMarkupContainer("paymentViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 4831933162858730026L;

            @Override
            protected void onInitialize() {
               final Form<Payment> paymentViewForm = new Form<Payment>("paymentViewForm");
               paymentViewForm.setModel(new CompoundPropertyModel<Payment>((IModel<Payment>) getDefaultModel()));
               paymentViewForm.add(new Label("paymentRequestId"));
               paymentViewForm.add(new Label("storeId"));
               paymentViewForm.add(new Label("terminalId"));
               paymentViewForm.add(new Label("transactionId"));
               paymentViewForm.add(new Label("protectionEligibilityType"));
               paymentViewForm.add(new Label("paymentType"));
               paymentViewForm.add(new Label("paymentStatus"));
               paymentViewForm.add(new Label("pendingReason"));
               paymentViewForm.add(new Label("holdDecision"));
               paymentViewForm.add(new Label("reasonCode"));
               paymentViewForm.add(new Label("paymentDate") {

                  private static final long serialVersionUID = 3621260522785287715L;

                  @Override
                  public <C> IConverter<C> getConverter(final Class<C> type) {
                     return (IConverter<C>) new XMLGregorianCalendarConverter();
                  }
               });
               paymentViewForm.add(new Label("exchangeRate"));
               paymentViewForm.add(new Label("installmentCount"));
               paymentViewForm.add(new Label("grossAmount"));
               paymentViewForm.add(new Label("settleAmount"));
               paymentViewForm.add(new Label("feeAmount"));
               paymentViewForm.add(new Label("taxAmount"));
               add(paymentViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(paymentViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString("saveMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString("saveMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString("saveMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Payment payment = (Payment) form.getDefaultModelObject();

            if (payment.getId() == 0) {
               ((Order) markupContainer.getDefaultModelObject()).getInvoice().getPayments().add(payment);
            }
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(markupContainer.setOutputMarkupId(true));
            target.add(form.setOutputMarkupId(true));
            target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderInvoicePaymentViewOrEditPanel.this.getString("saveMessage")))));
         }
      }
   }

   private static final long serialVersionUID = -7002701340914975498L;

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordViewOrEditPanel.class);

   private final MarkupContainer markupContainer;

   public OrderInvoicePaymentViewOrEditPanel(final String id, final IModel<Payment> model, MarkupContainer markupContainer) {
      super(id, model);
      this.markupContainer = markupContainer;
   }
}
