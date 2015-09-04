package com.netbrasoft.gnuob.application.customer;

import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import com.netbrasoft.gnuob.api.generic.XMLGregorianCalendarConverter;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CustomerViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(CustomerViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(CustomerViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         CustomerViewOrEditPanel.this.removeAll();
         CustomerViewOrEditPanel.this.add(new CustomerViewFragement()).setOutputMarkupId(true);

         if (((Customer) CustomerViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            CustomerViewOrEditPanel.this.setDefaultModelObject(customerDataProvider.findById((Customer) CustomerViewOrEditPanel.this.getDefaultModelObject()));
         }

         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CustomerEditFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      private final WebMarkupContainer customerEditTable;

      public CustomerEditFragement() {
         super("customerViewOrEditFragement", "customerEditFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());

         customerEditTable = new WebMarkupContainer("customerEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -7661119812141655082L;

            @Override
            protected void onInitialize() {
               final Form<Customer> customerEditForm = new Form<Customer>("customerEditForm");

               customerEditForm.setModel(new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel()));
               customerEditForm.add(new TextField<String>("salutation").add(StringValidator.maximumLength(20)));
               customerEditForm.add(new TextField<String>("suffix").add(StringValidator.maximumLength(20)));
               customerEditForm.add(new RequiredTextField<String>("firstName").setLabel(Model.of(getString("firstNameMessage"))).add(StringValidator.maximumLength(64)));
               customerEditForm.add(new TextField<String>("middleName").add(StringValidator.maximumLength(64)));
               customerEditForm.add(new RequiredTextField<String>("lastName").setLabel(Model.of(getString("lastNameMessage"))).add(StringValidator.maximumLength(64)));
               customerEditForm.add(new DatetimePicker("dateOfBirth", new DatetimePickerConfig().useLocale(Locale.getDefault().toString()).withFormat("dd-MM-YYYY")) {

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
               customerEditForm.add(new RequiredTextField<String>("buyerEmail").setLabel(Model.of(getString("buyerEmailMessage"))).add(EmailAddressValidator.getInstance()).add(StringValidator.maximumLength(127)));
               customerEditForm.add(new RequiredTextField<String>("address.postalCode").setLabel(Model.of(getString("postalCodeMessage"))).add(new PatternValidator("([0-9]){5}([-])([0-9]){3}")).add(StringValidator.maximumLength(20)));
               customerEditForm.add(new TextField<String>("address.number").add(StringValidator.maximumLength(10)));
               customerEditForm.add(new TextField<String>("address.country", Model.of("Brasil")).setLabel(Model.of(getString("countryNameMessage"))).add(StringValidator.maximumLength(40)).setEnabled(false));
               customerEditForm.add(new RequiredTextField<String>("address.street1").setLabel(Model.of(getString("street1Message"))).add(StringValidator.maximumLength(100)));
               customerEditForm.add(new TextField<String>("address.street2").add(StringValidator.maximumLength(100)));
               customerEditForm.add(new TextField<String>("address.complement").add(StringValidator.maximumLength(40)));
               customerEditForm.add(new TextField<String>("address.district").add(StringValidator.maximumLength(40)));
               customerEditForm.add(new RequiredTextField<String>("address.cityName").setLabel(Model.of(getString("cityNameMessage"))).add(StringValidator.maximumLength(40)));
               customerEditForm.add(new RequiredTextField<String>("address.stateOrProvince").setLabel(Model.of(getString("stateOrProvinceMessage"))));
               customerEditForm.add(new TextField<String>("address.countryName").add(StringValidator.maximumLength(40)));
               customerEditForm.add(new TextField<String>("address.internationalStreet").add(StringValidator.maximumLength(40)));
               customerEditForm.add(new TextField<String>("address.internationalStateAndCity").add(StringValidator.maximumLength(80)));
               customerEditForm.add(new TextField<String>("address.phone").add(StringValidator.maximumLength(20)));
               add(customerEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new SaveAjaxButton(customerEditForm).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(customerEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class CustomerViewFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      private final WebMarkupContainer customerViewTable;

      public CustomerViewFragement() {
         super("customerViewOrEditFragement", "customerViewFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());

         customerViewTable = new WebMarkupContainer("customerViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 5873771045655249852L;

            @Override
            protected void onInitialize() {
               final Form<Customer> customerViewForm = new Form<Customer>("customerViewForm");

               customerViewForm.setModel(new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel()));
               customerViewForm.add(new Label("salutation"));
               customerViewForm.add(new Label("suffix"));
               customerViewForm.add(new Label("firstName"));
               customerViewForm.add(new Label("middleName"));
               customerViewForm.add(new Label("lastName"));
               customerViewForm.add(new Label("dateOfBirth") {

                  private static final long serialVersionUID = 3621260522785287715L;

                  @Override
                  public <C> IConverter<C> getConverter(final Class<C> type) {
                     return (IConverter<C>) new XMLGregorianCalendarConverter();
                  }
               });
               customerViewForm.add(new Label("buyerEmail"));
               customerViewForm.add(new Label("address.postalCode"));
               customerViewForm.add(new Label("address.number"));
               customerViewForm.add(new Label("address.country"));
               customerViewForm.add(new Label("address.street1"));
               customerViewForm.add(new Label("address.street2"));
               customerViewForm.add(new Label("address.complement"));
               customerViewForm.add(new Label("address.district"));
               customerViewForm.add(new Label("address.cityName"));
               customerViewForm.add(new Label("address.stateOrProvince"));
               customerViewForm.add(new Label("address.countryName"));
               customerViewForm.add(new Label("address.internationalStreet"));
               customerViewForm.add(new Label("address.internationalStateAndCity"));
               customerViewForm.add(new Label("address.phone"));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(customerViewForm.setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(customerViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(CustomerViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(CustomerViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         CustomerViewOrEditPanel.this.removeAll();
         CustomerViewOrEditPanel.this.add(new CustomerEditFragement().setOutputMarkupId(true));
         target.add(CustomerViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(CustomerViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(CustomerViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(CustomerViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Customer customer = (Customer) form.getDefaultModelObject();
            // FIXME: BD change this to configuration or geonames etc.
            customer.getAddress().setCountry("BR");
            customer.setActive(true);

            if (customer.getId() == 0) {
               CustomerViewOrEditPanel.this.setDefaultModel(Model.of(customerDataProvider.findById(customerDataProvider.persist(customer))));
            } else {
               CustomerViewOrEditPanel.this.setDefaultModel(Model.of(customerDataProvider.findById(customerDataProvider.merge(customer))));
            }

            CustomerViewOrEditPanel.this.removeAll();
            CustomerViewOrEditPanel.this.add(new CustomerViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(target.getPage());
         }
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
