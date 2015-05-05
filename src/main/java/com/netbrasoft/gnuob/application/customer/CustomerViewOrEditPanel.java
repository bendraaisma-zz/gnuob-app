package com.netbrasoft.gnuob.application.customer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Customer;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CustomerViewOrEditPanel extends Panel {

   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         CustomerViewOrEditPanel.this.removeAll();
         CustomerViewOrEditPanel.this.add(new CustomerViewFragement()).setOutputMarkupId(true);
         target.add(target.getPage());
      }
   }

   class CustomerEditFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public CustomerEditFragement() {
         super("customerViewOrEditFragement", "customerEditFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Customer> customerEditForm = new Form<Customer>("customerEditForm");
         customerEditForm.setModel(new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel()));
         customerEditForm.add(new TextField<String>("salutation"));
         customerEditForm.add(new TextField<String>("suffix"));
         customerEditForm.add(new TextField<String>("firstName"));
         customerEditForm.add(new TextField<String>("middleName"));
         customerEditForm.add(new TextField<String>("lastName"));
         customerEditForm.add(new TextField<String>("dateOfBirth"));
         customerEditForm.add(new TextField<String>("address.postalCode"));
         customerEditForm.add(new TextField<String>("address.number"));
         customerEditForm.add(new TextField<String>("address.country"));
         customerEditForm.add(new TextField<String>("address.street1"));
         customerEditForm.add(new TextField<String>("address.street2"));
         customerEditForm.add(new TextField<String>("address.complement"));
         customerEditForm.add(new TextField<String>("address.district"));
         customerEditForm.add(new TextField<String>("address.cityName"));
         customerEditForm.add(new TextField<String>("address.stateOrProvince"));
         customerEditForm.add(new TextField<String>("address.countryName"));
         customerEditForm.add(new TextField<String>("address.internationalStreet"));
         customerEditForm.add(new TextField<String>("address.internationalStateAndCity"));
         customerEditForm.add(new TextField<String>("address.phone"));

         add(customerEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new SaveAjaxButton(customerEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class CustomerViewFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public CustomerViewFragement() {
         super("customerViewOrEditFragement", "customerViewFragement", CustomerViewOrEditPanel.this, CustomerViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Customer> customerViewForm = new Form<Customer>("customerViewForm");
         customerViewForm.setModel(new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel()));
         customerViewForm.add(new Label("salutation"));
         customerViewForm.add(new Label("suffix"));
         customerViewForm.add(new Label("firstName"));
         customerViewForm.add(new Label("middleName"));
         customerViewForm.add(new Label("lastName"));
         customerViewForm.add(new Label("dateOfBirth"));
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

         add(new EditAjaxLink());
         add(customerViewForm.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         CustomerViewOrEditPanel.this.removeAll();
         CustomerViewOrEditPanel.this.add(new CustomerEditFragement().setOutputMarkupId(true));
         paramAjaxRequestTarget.add(CustomerViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Customer customer = (Customer) form.getDefaultModelObject();

            if (customer.getId() == 0) {
               customerDataProvider.persist(customer);
            } else {
               customerDataProvider.merge(customer);
            }

            CustomerViewOrEditPanel.this.removeAll();
            CustomerViewOrEditPanel.this.add(new CustomerViewFragement().setOutputMarkupId(true));
         } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);

            String[] messages = e.getMessage().split(": ");
            String message = messages[messages.length - 1];

            warn(message.substring(0, 1).toUpperCase() + message.substring(1));
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
      add(new CustomerViewFragement().setOutputMarkupId(true));
   }
}
