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
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Order;
import com.netbrasoft.gnuob.api.OrderRecord;
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
public class OrderRecordViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class OrderRecordEditFragement extends Fragment {

      private static final long serialVersionUID = 3709791409078428685L;

      private final WebMarkupContainer orderRecordEditTable;

      public OrderRecordEditFragement() {
         super("orderRecordViewOrEditFragement", "orderRecordEditFragement", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());

         orderRecordEditTable = new WebMarkupContainer("orderRecordEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -6051033065197862976L;

            @Override
            protected void onInitialize() {
               final Form<OrderRecord> orderRecordEditForm = new Form<OrderRecord>("orderRecordEditForm");
               orderRecordEditForm.setModel(new CompoundPropertyModel<OrderRecord>((IModel<OrderRecord>) getDefaultModel()));
               orderRecordEditForm.add(new RequiredTextField<String>("orderRecordId").add(StringValidator.maximumLength(64)));
               orderRecordEditForm.add(new DatetimePicker("deliveryDate", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
               orderRecordEditForm.add(new RequiredTextField<String>("productNumber").add(StringValidator.maximumLength(64)));
               orderRecordEditForm.add(new RequiredTextField<String>("name").add(StringValidator.maximumLength(128)));
               orderRecordEditForm.add(new TextField<String>("option").add(StringValidator.maximumLength(128)));
               orderRecordEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)));
               orderRecordEditForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(getDefaultModelObject(), "itemUrl")).add(StringValidator.maximumLength(255)));
               orderRecordEditForm.add(new NumberTextField<Integer>("quantity").setRequired(true));
               orderRecordEditForm.add(new NumberTextField<Integer>("amount").setRequired(true));
               orderRecordEditForm.add(new NumberTextField<Integer>("discount").setRequired(true));
               orderRecordEditForm.add(new NumberTextField<Integer>("shippingCost").setRequired(true));
               orderRecordEditForm.add(new NumberTextField<Integer>("tax").setRequired(true));
               orderRecordEditForm.add(new NumberTextField<Integer>("itemHeight"));
               orderRecordEditForm.add(new TextField<String>("itemHeightUnit").add(StringValidator.maximumLength(20)));
               orderRecordEditForm.add(new NumberTextField<Integer>("itemLength"));
               orderRecordEditForm.add(new TextField<String>("itemLengthUnit").add(StringValidator.maximumLength(20)));
               orderRecordEditForm.add(new NumberTextField<Integer>("itemWeight"));
               orderRecordEditForm.add(new TextField<String>("itemWeightUnit").add(StringValidator.maximumLength(20)));
               orderRecordEditForm.add(new NumberTextField<Integer>("itemWidth"));
               orderRecordEditForm.add(new TextField<String>("itemWidthUnit").add(StringValidator.maximumLength(20)));
               orderRecordEditForm.add(new SaveAjaxButton(orderRecordEditForm).setOutputMarkupId(true));
               add(orderRecordEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(orderRecordEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class OrderRecordViewFragement extends Fragment {

      private static final long serialVersionUID = 6927997909191615786L;

      private final WebMarkupContainer orderRecordViewTable;

      public OrderRecordViewFragement() {
         super("orderRecordViewOrEditFragement", "orderRecordViewFragement", OrderRecordViewOrEditPanel.this, OrderRecordViewOrEditPanel.this.getDefaultModel());
         orderRecordViewTable = new WebMarkupContainer("orderRecordViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 4831933162858730026L;

            @Override
            protected void onInitialize() {
               final Form<OrderRecord> orderRecorViewForm = new Form<OrderRecord>("orderRecordViewForm");
               orderRecorViewForm.setModel(new CompoundPropertyModel<OrderRecord>((IModel<OrderRecord>) getDefaultModel()));
               orderRecorViewForm.add(new Label("orderRecordId"));
               orderRecorViewForm.add(new Label("deliveryDate") {

                  private static final long serialVersionUID = 3621260522785287715L;

                  @Override
                  public <C> IConverter<C> getConverter(final Class<C> type) {
                     return (IConverter<C>) new XMLGregorianCalendarConverter();
                  }
               });
               orderRecorViewForm.add(new Label("productNumber"));
               orderRecorViewForm.add(new Label("name"));
               orderRecorViewForm.add(new Label("option"));
               orderRecorViewForm.add(new Label("description"));
               orderRecorViewForm.add(new Label("itemUrl"));
               orderRecorViewForm.add(new Label("quantity"));
               orderRecorViewForm.add(new Label("amount"));
               orderRecorViewForm.add(new Label("discount"));
               orderRecorViewForm.add(new Label("shippingCost"));
               orderRecorViewForm.add(new Label("tax"));
               orderRecorViewForm.add(new Label("itemHeight"));
               orderRecorViewForm.add(new Label("itemHeightUnit"));
               orderRecorViewForm.add(new Label("itemLength"));
               orderRecorViewForm.add(new Label("itemLengthUnit"));
               orderRecorViewForm.add(new Label("itemWeight"));
               orderRecorViewForm.add(new Label("itemWeightUnit"));
               orderRecorViewForm.add(new Label("itemWidth"));
               orderRecorViewForm.add(new Label("itemWidthUnit"));
               add(orderRecorViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(orderRecordViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(OrderRecordViewOrEditPanel.this.getString("saveMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString("saveMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString("saveMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final OrderRecord orderRecordForm = (OrderRecord) form.getDefaultModelObject();

            if (orderRecordForm.getId() == 0) {
               ((Order) markupContainer.getDefaultModelObject()).getRecords().add(orderRecordForm);
            }
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(markupContainer.setOutputMarkupId(true));
            target.add(form.setOutputMarkupId(true));
            target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(OrderRecordViewOrEditPanel.this.getString("saveMessage")))));
         }
      }
   }

   private static final long serialVersionUID = -7002701340914975498L;

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordViewOrEditPanel.class);

   private final MarkupContainer markupContainer;

   public OrderRecordViewOrEditPanel(final String id, final IModel<OrderRecord> model, MarkupContainer markupContainer) {
      super(id, model);
      this.markupContainer = markupContainer;
   }
}
