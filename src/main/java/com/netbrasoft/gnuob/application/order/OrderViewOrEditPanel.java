package com.netbrasoft.gnuob.application.order;

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
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.api.generic.converter.XMLGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class OrderViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(OrderViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(OrderViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(new OrderViewFragement()).setOutputMarkupId(true);

         if (((Order) OrderViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            OrderViewOrEditPanel.this.setDefaultModelObject(orderDataProvider.findById((Order) OrderViewOrEditPanel.this.getDefaultModelObject()));
         }

         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(OrderViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(OrderViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         OrderViewOrEditPanel.this.removeAll();
         OrderViewOrEditPanel.this.add(new OrderEditFragment().setOutputMarkupId(true));
         target.add(OrderViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OrderEditFragment extends Fragment {

      private static final long serialVersionUID = -5645656866901827543L;

      private final WebMarkupContainer orderEditTable;

      private final OrderRecordPanel orderRecordPanel;

      private final OrderInvoicePaymentPanel orderInvoicePaymentPanel;

      public OrderEditFragment() {
         super("orderViewOrEditFragement", "orderEditFragement", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());

         orderEditTable = new WebMarkupContainer("orderEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -7673177886381156731L;

            @Override
            protected void onInitialize() {
               final Form<Order> orderEditForm = new Form<Order>("orderEditForm");
               orderEditForm.setModel(new CompoundPropertyModel<Order>((IModel<Order>) getDefaultModel()));
               orderEditForm.add(new RequiredTextField<String>("orderId").add(StringValidator.maximumLength(64)));
               orderEditForm.add(new RequiredTextField<String>("contract.contractId").add(StringValidator.maximumLength(127)));
               orderEditForm.add(new DatetimePicker("orderDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
               orderEditForm.add(new TextField<String>("token").add(StringValidator.maximumLength(40)));
               orderEditForm.add(new TextField<String>("transactionId").add(StringValidator.maximumLength(64)));
               orderEditForm.add(new TextField<String>("billingAgreementId").add(StringValidator.maximumLength(20)));
               orderEditForm.add(new TextField<String>("checkoutStatus").add(StringValidator.maximumLength(255)));
               orderEditForm.add(new NumberTextField<Integer>("itemTotal"));
               orderEditForm.add(new TextArea<String>("orderDescription").add(StringValidator.maximumLength(127)));
               orderEditForm.add(new TextField<String>("custom").add(StringValidator.maximumLength(255)));
               orderEditForm.add(new TextField<String>("note").add(StringValidator.maximumLength(165)));
               orderEditForm.add(new TextArea<String>("noteText").add(StringValidator.maximumLength(255)));
               orderEditForm.add(new BootstrapCheckbox("insuranceOptionOffered"));
               orderEditForm.add(new NumberTextField<Integer>("giftWrapAmount"));
               orderEditForm.add(new NumberTextField<Integer>("extraAmount").setRequired(true));
               orderEditForm.add(new NumberTextField<Integer>("discountTotal"));
               orderEditForm.add(new NumberTextField<Integer>("handlingTotal").setRequired(true));
               orderEditForm.add(new NumberTextField<Integer>("shippingTotal"));
               orderEditForm.add(new NumberTextField<Integer>("insuranceTotal").setRequired(true));
               orderEditForm.add(new NumberTextField<Integer>("shippingDiscount").setRequired(true));
               orderEditForm.add(new NumberTextField<Integer>("taxTotal"));
               orderEditForm.add(new NumberTextField<Integer>("orderTotal"));
               orderEditForm.add(new BootstrapCheckbox("giftMessageEnable"));
               orderEditForm.add(new BootstrapCheckbox("giftReceiptEnable"));
               orderEditForm.add(new BootstrapCheckbox("giftWrapEnable"));
               orderEditForm.add(new TextField<String>("giftWrapName").add(StringValidator.maximumLength(25)));
               orderEditForm.add(new TextArea<String>("giftMessage").add(StringValidator.maximumLength(150)));
               orderEditForm.add(new TextField<String>("shipment.shipmentType").add(StringValidator.maximumLength(128)));
               orderEditForm.add(new RequiredTextField<String>("shipment.address.postalCode").setLabel(Model.of(getString("postalCodeMessage"))).add(new PatternValidator("([0-9]){5}([-])([0-9]){3}")).add(StringValidator.maximumLength(20)));
               orderEditForm.add(new TextField<String>("shipment.address.number").add(StringValidator.maximumLength(10)));
               orderEditForm.add(new RequiredTextField<String>("shipment.address.country", Model.of("Brasil")).setLabel(Model.of(getString("countryNameMessage"))).add(StringValidator.maximumLength(40)).setEnabled(false));
               orderEditForm.add(new RequiredTextField<String>("shipment.address.street1").setLabel(Model.of(getString("street1Message"))).add(StringValidator.maximumLength(100)));
               orderEditForm.add(new TextField<String>("shipment.address.street2").add(StringValidator.maximumLength(100)));
               orderEditForm.add(new TextField<String>("shipment.address.complement").add(StringValidator.maximumLength(40)));
               orderEditForm.add(new TextField<String>("shipment.address.district").add(StringValidator.maximumLength(40)));
               orderEditForm.add(new RequiredTextField<String>("shipment.address.cityName").setLabel(Model.of(getString("cityNameMessage"))).add(StringValidator.maximumLength(40)));
               orderEditForm.add(new RequiredTextField<String>("shipment.address.stateOrProvince").add(StringValidator.maximumLength(2)).setLabel(Model.of(getString("stateOrProvinceMessage"))));
               orderEditForm.add(new TextField<String>("shipment.address.countryName").add(StringValidator.maximumLength(40)));
               orderEditForm.add(new TextField<String>("shipment.address.internationalStreet").add(StringValidator.maximumLength(40)));
               orderEditForm.add(new TextField<String>("shipment.address.internationalStateAndCity").add(StringValidator.maximumLength(80)));
               orderEditForm.add(new TextField<String>("shipment.address.phone").add(StringValidator.maximumLength(20)));
               orderEditForm.add(orderRecordPanel.add(orderRecordPanel.new OrderRecordEditFragement()).setOutputMarkupId(true));
               orderEditForm.add(orderInvoicePaymentPanel.add(orderInvoicePaymentPanel.new OrderInvoicePaymentEditFragement()).setOutputMarkupId(true));
               add(orderEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new SaveAjaxButton(orderEditForm).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }

         };
         orderRecordPanel = new OrderRecordPanel("orderRecordPanel", (IModel<Order>) getDefaultModel());
         orderInvoicePaymentPanel = new OrderInvoicePaymentPanel("orderInvoicePaymentPanel", (IModel<Order>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(orderEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class OrderViewFragement extends Fragment {

      private static final long serialVersionUID = 2134263849806147209L;

      private final WebMarkupContainer orderViewTable;

      private final OrderRecordPanel orderRecordPanel;

      private final OrderInvoicePaymentPanel orderInvoicePaymentPanel;

      public OrderViewFragement() {
         super("orderViewOrEditFragement", "orderViewFragement", OrderViewOrEditPanel.this, OrderViewOrEditPanel.this.getDefaultModel());

         orderViewTable = new WebMarkupContainer("orderViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 8686499909946092186L;

            @Override
            protected void onInitialize() {
               final Form<Order> orderViewForm = new Form<Order>("orderViewForm");
               orderViewForm.setModel(new CompoundPropertyModel<Order>((IModel<Order>) getDefaultModel()));
               orderViewForm.add(new Label("orderId"));
               orderViewForm.add(new Label("contract.contractId"));
               orderViewForm.add(new Label("orderDate") {

                  private static final long serialVersionUID = 3621260522785287715L;

                  @Override
                  public <C> IConverter<C> getConverter(final Class<C> type) {
                     return (IConverter<C>) new XMLGregorianCalendarConverter();
                  }
               });
               orderViewForm.add(new Label("token"));
               orderViewForm.add(new Label("transactionId"));
               orderViewForm.add(new Label("billingAgreementId"));
               orderViewForm.add(new Label("checkoutStatus"));
               orderViewForm.add(new Label("itemTotal"));
               orderViewForm.add(new Label("orderDescription"));
               orderViewForm.add(new Label("custom"));
               orderViewForm.add(new Label("note"));
               orderViewForm.add(new Label("noteText"));
               orderViewForm.add(new Label("insuranceOptionOffered"));
               orderViewForm.add(new Label("giftWrapAmount"));
               orderViewForm.add(new Label("extraAmount"));
               orderViewForm.add(new Label("discountTotal"));
               orderViewForm.add(new Label("handlingTotal"));
               orderViewForm.add(new Label("shippingTotal"));
               orderViewForm.add(new Label("insuranceTotal"));
               orderViewForm.add(new Label("shippingDiscount"));
               orderViewForm.add(new Label("taxTotal"));
               orderViewForm.add(new Label("orderTotal"));
               orderViewForm.add(new Label("invoice.invoiceId"));
               orderViewForm.add(new Label("invoice.address.postalCode"));
               orderViewForm.add(new Label("invoice.address.number"));
               orderViewForm.add(new Label("invoice.address.country"));
               orderViewForm.add(new Label("invoice.address.street1"));
               orderViewForm.add(new Label("invoice.address.street2"));
               orderViewForm.add(new Label("invoice.address.complement"));
               orderViewForm.add(new Label("invoice.address.district"));
               orderViewForm.add(new Label("invoice.address.cityName"));
               orderViewForm.add(new Label("invoice.address.stateOrProvince"));
               orderViewForm.add(new Label("invoice.address.countryName"));
               orderViewForm.add(new Label("invoice.address.internationalStreet"));
               orderViewForm.add(new Label("invoice.address.internationalStateAndCity"));
               orderViewForm.add(new Label("invoice.address.phone"));
               orderViewForm.add(new Label("giftMessageEnable"));
               orderViewForm.add(new Label("giftReceiptEnable"));
               orderViewForm.add(new Label("giftWrapEnable"));
               orderViewForm.add(new Label("giftWrapName"));
               orderViewForm.add(new Label("giftMessage"));
               orderViewForm.add(new Label("shipment.shipmentType"));
               orderViewForm.add(new Label("shipment.address.postalCode"));
               orderViewForm.add(new Label("shipment.address.number"));
               orderViewForm.add(new Label("shipment.address.country"));
               orderViewForm.add(new Label("shipment.address.street1"));
               orderViewForm.add(new Label("shipment.address.street2"));
               orderViewForm.add(new Label("shipment.address.complement"));
               orderViewForm.add(new Label("shipment.address.district"));
               orderViewForm.add(new Label("shipment.address.cityName"));
               orderViewForm.add(new Label("shipment.address.stateOrProvince"));
               orderViewForm.add(new Label("shipment.address.countryName"));
               orderViewForm.add(new Label("shipment.address.internationalStreet"));
               orderViewForm.add(new Label("shipment.address.internationalStateAndCity"));
               orderViewForm.add(new Label("shipment.address.phone"));
               orderViewForm.add(orderRecordPanel.add(orderRecordPanel.new OrderRecordViewFragement()).setOutputMarkupId(true));
               orderViewForm.add(orderInvoicePaymentPanel.add(orderInvoicePaymentPanel.new OrderInvoicePaymentViewFragement()).setOutputMarkupId(true));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(orderViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }

         };
         orderRecordPanel = new OrderRecordPanel("orderRecordPanel", (IModel<Order>) getDefaultModel());
         orderInvoicePaymentPanel = new OrderInvoicePaymentPanel("orderInvoicePaymentPanel", (IModel<Order>) getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(orderViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(OrderViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(OrderViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Order order = (Order) form.getDefaultModelObject();
            order.setActive(true);

            if (order.getId() == 0) {
               OrderViewOrEditPanel.this.setDefaultModelObject(orderDataProvider.findById(orderDataProvider.persist(order)));
            } else {
               OrderViewOrEditPanel.this.setDefaultModelObject(orderDataProvider.findById(orderDataProvider.merge(order)));
            }

            OrderViewOrEditPanel.this.removeAll();
            OrderViewOrEditPanel.this.add(new OrderViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(target.getPage());
         }
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
