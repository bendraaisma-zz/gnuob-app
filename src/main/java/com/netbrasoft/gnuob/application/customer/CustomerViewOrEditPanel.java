package com.netbrasoft.gnuob.application.customer;

import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Customer;
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
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CustomerViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class CustomerEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class CustomerEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CancelAjaxLink extends BootstrapAjaxLink<Customer> {

        private static final long serialVersionUID = 4267535261864907719L;

        public CancelAjaxLink(String id, IModel<Customer> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          CustomerViewOrEditPanel.this.removeAll();
          if (((Customer) CancelAjaxLink.this.getDefaultModelObject()).getId() > 0) {
            CancelAjaxLink.this.setDefaultModelObject(customerDataProvider.findById((Customer) CancelAjaxLink.this.getDefaultModelObject()));
          }
          target.add(CustomerViewOrEditPanel.this.add(new CustomerViewFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY))),
              new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target
              .add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          boolean isException = false;
          try {
            if (((Customer) form.getDefaultModelObject()).getId() == 0) {
              CustomerEditTable.this.setDefaultModelObject(customerDataProvider.findById(customerDataProvider.persist(((Customer) form.getDefaultModelObject()))));
            } else {
              CustomerEditTable.this.setDefaultModelObject(customerDataProvider.findById(customerDataProvider.merge(((Customer) form.getDefaultModelObject()))));
            }
          } catch (final RuntimeException e) {
            isException = true;
            LOGGER.warn(e.getMessage(), e);
            feedbackPanel.warn(e.getLocalizedMessage());
            target.add(feedbackPanel.setOutputMarkupId(true));
            target.add(
                SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
          } finally {
            if (!isException) {
              CustomerViewOrEditPanel.this.removeAll();
              target.add(CustomerViewOrEditPanel.this.add(CustomerViewOrEditPanel.this.new CustomerViewFragement()).setOutputMarkupId(true));
            }
          }
        }
      }

      private static final long serialVersionUID = -906207026151446160L;

      private final BootstrapForm<Customer> customerEditForm;

      private final CancelAjaxLink cancelAjaxLink;

      private final SaveAjaxButton saveAjaxButton;

      private final NotificationPanel feedbackPanel;

      public CustomerEditTable(final String id, final IModel<Customer> model) {
        super(id, model);
        customerEditForm = new BootstrapForm<Customer>("customerEditForm", new CompoundPropertyModel<Customer>((IModel<Customer>) CustomerEditTable.this.getDefaultModel()));
        cancelAjaxLink =
            new CancelAjaxLink("cancel", model, Buttons.Type.Default, Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)),
            customerEditForm, Buttons.Type.Primary);
        feedbackPanel = new NotificationPanel("feedback");
      }

      @Override
      protected void onInitialize() {
        customerEditForm.add(new TextField<String>("salutation").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("suffix").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        customerEditForm
            .add(new RequiredTextField<String>("firstName").setLabel(Model.of(getString("firstNameMessage"))).add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("middleName").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        customerEditForm
            .add(new RequiredTextField<String>("lastName").setLabel(Model.of(getString("lastNameMessage"))).add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        customerEditForm.add(new DatetimePicker("dateOfBirth", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
        customerEditForm.add(new RequiredTextField<String>("buyerEmail").setLabel(Model.of(getString("buyerEmailMessage"))).add(EmailAddressValidator.getInstance())
            .add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        customerEditForm.add(new RequiredTextField<String>("address.postalCode").setLabel(Model.of(getString("postalCodeMessage")))
            .add(new PatternValidator("([0-9]){5}([-])([0-9]){3}")).add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.number").add(StringValidator.maximumLength(10)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.country", Model.of("Brasil")).setLabel(Model.of(getString("countryNameMessage")))
            .add(StringValidator.maximumLength(40)).setEnabled(false).setOutputMarkupId(true));
        customerEditForm.add(
            new RequiredTextField<String>("address.street1").setLabel(Model.of(getString("street1Message"))).add(StringValidator.maximumLength(100)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.street2").add(StringValidator.maximumLength(100)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.complement").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.district").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        customerEditForm.add(
            new RequiredTextField<String>("address.cityName").setLabel(Model.of(getString("cityNameMessage"))).add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        customerEditForm.add(new RequiredTextField<String>("address.stateOrProvince").setLabel(Model.of(getString("stateOrProvinceMessage"))).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.countryName").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.internationalStreet").add(StringValidator.maximumLength(40)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.internationalStateAndCity").add(StringValidator.maximumLength(80)).setOutputMarkupId(true));
        customerEditForm.add(new TextField<String>("address.phone").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        add(customerEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(saveAjaxButton.setOutputMarkupId(true));
        add(cancelAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4702333788976660894L;

    private final CustomerEditTable customerEditTable;

    public CustomerEditFragement() {
      super("customerViewOrEditFragement", "customerEditFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());
      customerEditTable = new CustomerEditTable("customerEditTable", (IModel<Customer>) CustomerEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(customerEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CustomerViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CustomerViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class EditAjaxLink extends BootstrapAjaxLink<Customer> {

        private static final long serialVersionUID = 4267535261864907719L;

        public EditAjaxLink(String id, IModel<Customer> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.edit);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          CustomerViewOrEditPanel.this.removeAll();
          target.add(CustomerViewOrEditPanel.this.add(new CustomerEditFragement().setOutputMarkupId(true)));
        }
      }

      private static final long serialVersionUID = 989923448088758813L;

      private final Form<Customer> customerViewForm;

      private final EditAjaxLink editAjaxLink;

      public CustomerViewTable(final String id, final IModel<Customer> model) {
        super(id, model);
        customerViewForm = new Form<Customer>("customerViewForm", new CompoundPropertyModel<Customer>((IModel<Customer>) CustomerViewTable.this.getDefaultModel()));
        editAjaxLink =
            new EditAjaxLink("edit", model, Buttons.Type.Primary, Model.of(CustomerViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY)));
      }

      @Override
      protected void onInitialize() {
        customerViewForm.add(new TextField<String>("salutation").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("suffix").setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("firstName").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("middleName").setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("lastName").setOutputMarkupId(true));
        customerViewForm.add(new DatetimePicker("dateOfBirth", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

          private static final long serialVersionUID = 1209354725150726556L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (XMLGregorianCalendar.class.isAssignableFrom(type)) {
              return (IConverter<C>) new XmlGregorianCalendarConverter();
            } else {
              return super.getConverter(type);
            }
          }
        });
        customerViewForm.add(new RequiredTextField<String>("buyerEmail").setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("address.postalCode").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.number").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.country", Model.of("Brasil")).setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("address.street1").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.street2").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.complement").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.district").setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("address.cityName").setOutputMarkupId(true));
        customerViewForm.add(new RequiredTextField<String>("address.stateOrProvince").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.countryName").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.internationalStreet").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.internationalStateAndCity").setOutputMarkupId(true));
        customerViewForm.add(new TextField<String>("address.phone").setOutputMarkupId(true));
        add(customerViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(editAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4702333788976660894L;

    private final CustomerViewTable customerViewTable;

    public CustomerViewFragement() {
      super("customerViewOrEditFragement", "customerViewFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());
      customerViewTable = new CustomerViewTable("customerViewTable", (IModel<Customer>) CustomerViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(customerViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerViewOrEditPanel.class);

  private static final long serialVersionUID = 5273022766621299743L;

  @SpringBean(name = "CustomerDataProvider", required = true)
  private GenericTypeDataProvider<Customer> customerDataProvider;

  public CustomerViewOrEditPanel(final String id, final IModel<Customer> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    customerDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    customerDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    customerDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    customerDataProvider.setType(new Customer());
    customerDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
