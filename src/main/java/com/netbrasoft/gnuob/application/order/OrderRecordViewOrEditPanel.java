package com.netbrasoft.gnuob.application.order;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.apache.wicket.markup.html.form.UrlTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.OrderRecord;
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
public class OrderRecordViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class OrderRecordEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class OrderRecordEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(final String id, final IModel<String> model, final Form<OrderRecord> form, final Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY))));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          target.add(form.add(new TooltipValidation()));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          if (((OrderRecord) form.getDefaultModelObject()).getId() == 0) {
            ((Order) OrderRecordViewOrEditPanel.this.getDefaultModelObject()).getRecords().add((OrderRecord) form.getDefaultModelObject());
          }
          target.add(form.setOutputMarkupId(true));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
          target.add(OrderRecordViewOrEditPanel.this.getParent().setOutputMarkupId(true));
        }
      }

      private static final long serialVersionUID = 8716967873428915351L;

      private final BootstrapForm<OrderRecord> orderRecordEditForm;

      private final SaveAjaxButton saveAjaxButton;

      public OrderRecordEditTable(final String id, final IModel<Order> model) {
        super(id, model);
        orderRecordEditForm = new BootstrapForm<OrderRecord>("orderRecordEditForm", new CompoundPropertyModel<OrderRecord>(OrderRecordViewOrEditPanel.this.selectedModel));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(OrderRecordViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)), orderRecordEditForm,
            Buttons.Type.Primary);
      }

      @Override
      protected void onInitialize() {
        orderRecordEditForm.add(new RequiredTextField<String>("orderRecordId").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        orderRecordEditForm.add(new DatetimePicker("deliveryDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
        orderRecordEditForm.add(new RequiredTextField<String>("productNumber").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        orderRecordEditForm.add(new RequiredTextField<String>("name").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        orderRecordEditForm.add(new TextField<String>("option").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        orderRecordEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        orderRecordEditForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(orderRecordEditForm.getDefaultModelObject(), "itemUrl")).setOutputMarkupId(true));
        orderRecordEditForm
            .add(new NumberTextField<BigInteger>("quantity").setRequired(true).add(RangeValidator.range(BigDecimal.ZERO, BigDecimal.valueOf(50))).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("amount").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("discount").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("shippingCost").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("tax").setRequired(true).add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("itemHeight").setOutputMarkupId(true));
        orderRecordEditForm.add(new TextField<String>("itemHeightUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("itemLength").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new TextField<String>("itemLengthUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("itemWeight").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new TextField<String>("itemWeightUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderRecordEditForm.add(new NumberTextField<BigDecimal>("itemWidth").add(RangeValidator.minimum(BigDecimal.ZERO)).setOutputMarkupId(true));
        orderRecordEditForm.add(new TextField<String>("itemWidthUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        orderRecordEditForm.add(saveAjaxButton.setOutputMarkupId(true));
        add(orderRecordEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }


    private static final long serialVersionUID = 3709791409078428685L;

    private final OrderRecordEditTable orderRecordEditTable;

    public OrderRecordEditFragment() {
      super("orderRecordViewOrEditFragment", "orderRecordEditFragment", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());
      orderRecordEditTable = new OrderRecordEditTable("orderRecordEditTable", (IModel<Order>) OrderRecordEditFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderRecordEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class OrderRecordViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class OrderRecordViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = 5781878310530984048L;

      private final BootstrapForm<OrderRecord> orderRecorViewForm;

      public OrderRecordViewTable(final String id, final IModel<Order> model) {
        super(id, model);
        orderRecorViewForm = new BootstrapForm<OrderRecord>("orderRecordViewForm", new CompoundPropertyModel<OrderRecord>(OrderRecordViewOrEditPanel.this.selectedModel));
      }

      @Override
      protected void onInitialize() {
        orderRecorViewForm.add(new RequiredTextField<String>("orderRecordId").setOutputMarkupId(true));
        orderRecorViewForm.add(new DatetimePicker("deliveryDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
        orderRecorViewForm.add(new RequiredTextField<String>("productNumber").setOutputMarkupId(true));
        orderRecorViewForm.add(new RequiredTextField<String>("name").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextField<String>("option").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextArea<String>("description").setOutputMarkupId(true));
        orderRecorViewForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(orderRecorViewForm.getDefaultModelObject(), "itemUrl")).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("quantity").setRequired(true).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("amount").setRequired(true).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("discount").setRequired(true).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("shippingCost").setRequired(true).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("tax").setRequired(true).setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("itemHeight").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextField<String>("itemHeightUnit").setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("itemLength").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextField<String>("itemLengthUnit").setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("itemWeight").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextField<String>("itemWeightUnit").setOutputMarkupId(true));
        orderRecorViewForm.add(new NumberTextField<Integer>("itemWidth").setOutputMarkupId(true));
        orderRecorViewForm.add(new TextField<String>("itemWidthUnit").setOutputMarkupId(true));
        add(orderRecorViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 6927997909191615786L;

    private final OrderRecordViewTable orderRecordViewTable;

    public OrderRecordViewFragment() {
      super("orderRecordViewOrEditFragment", "orderRecordViewFragment", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());
      orderRecordViewTable = new OrderRecordViewTable("orderRecordViewTable", (IModel<Order>) OrderRecordViewFragment.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(orderRecordViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = -7002701340914975498L;

  private IModel<OrderRecord> selectedModel;

  public OrderRecordViewOrEditPanel(final String id, final IModel<Order> model) {
    super(id, model);
    selectedModel = Model.of(new OrderRecord());
  }

  public void setSelectedModel(final IModel<OrderRecord> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
