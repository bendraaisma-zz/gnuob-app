package com.netbrasoft.gnuob.application.contract;

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

import com.netbrasoft.gnuob.api.Contract;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.content.ContentViewOrEditPanel;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContractViewOrEditPanel extends Panel {

   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(new ContractViewFragement()).setOutputMarkupId(true);
         paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
      }
   }

   class ContractEditFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public ContractEditFragement() {
         super("contractViewOrEditFragement", "contractEditFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Contract> contractEditForm = new Form<Contract>("contractEditForm");
         contractEditForm.setModel(new CompoundPropertyModel<Contract>((IModel<Contract>) getDefaultModel()));
         contractEditForm.add(new TextField<String>("contractId"));
         contractEditForm.add(new TextField<String>("customer.friendlyName"));
         contractEditForm.add(new TextField<String>("customer.buyerEmail"));
         contractEditForm.add(new TextField<String>("customer.buyerMarketingEmail"));
         contractEditForm.add(new TextField<String>("customer.contactPhone"));
         contractEditForm.add(new TextField<String>("customer.payer"));
         contractEditForm.add(new TextField<String>("customer.payerBusiness"));
         contractEditForm.add(new TextField<String>("customer.payerId"));
         contractEditForm.add(new TextField<String>("customer.payerStatus"));
         contractEditForm.add(new TextField<String>("customer.taxId"));
         contractEditForm.add(new TextField<String>("customer.taxIdType"));

         add(contractEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new SaveAjaxButton(contractEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class ContractViewFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public ContractViewFragement() {
         super("contractViewOrEditFragement", "contractViewFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Contract> contractViewForm = new Form<Contract>("contractViewForm");
         contractViewForm.setModel(new CompoundPropertyModel<Contract>((IModel<Contract>) getDefaultModel()));
         contractViewForm.add(new Label("contractId"));
         contractViewForm.add(new Label("customer.friendlyName"));
         contractViewForm.add(new Label("customer.buyerEmail"));
         contractViewForm.add(new Label("customer.buyerMarketingEmail"));
         contractViewForm.add(new Label("customer.contactPhone"));
         contractViewForm.add(new Label("customer.payer"));
         contractViewForm.add(new Label("customer.payerBusiness"));
         contractViewForm.add(new Label("customer.payerId"));
         contractViewForm.add(new Label("customer.payerStatus"));
         contractViewForm.add(new Label("customer.taxId"));
         contractViewForm.add(new Label("customer.taxIdType"));

         add(new EditAjaxLink());
         add(contractViewForm.setOutputMarkupId(true));
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
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(new ContractEditFragement().setOutputMarkupId(true));
         paramAjaxRequestTarget.add(ContractViewOrEditPanel.this);
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
            Contract contract = (Contract) form.getDefaultModelObject();

            if (contract.getId() == 0) {
               contractDataProvider.persist(contract);
            } else {
               contractDataProvider.merge(contract);
            }

            ContractViewOrEditPanel.this.removeAll();
            ContractViewOrEditPanel.this.add(new ContractViewFragement().setOutputMarkupId(true));
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

   private static final long serialVersionUID = 5269452849219756769L;

   private static final Logger LOGGER = LoggerFactory.getLogger(ContentViewOrEditPanel.class);

   @SpringBean(name = "ContractDataProvider", required = true)
   private GenericTypeDataProvider<Contract> contractDataProvider;

   public ContractViewOrEditPanel(final String id, final IModel<Contract> model) {
      super(id, model);
      add(new ContractViewFragement().setOutputMarkupId(true));
   }
}
