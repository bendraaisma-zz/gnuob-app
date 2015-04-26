package com.netbrasoft.gnuob.application.contract;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Contract;
import com.netbrasoft.gnuob.application.security.Roles;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER, Roles.EMPLOYEE })
public class ContractViewOrEditPanel extends Panel {

   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(createContractViewFragement()).setOutputMarkupId(true);
         paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(createContractEditFragement().setOutputMarkupId(true));
         paramAjaxRequestTarget.add(ContractViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { Roles.MANAGER })
   class SaveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxLink() {
         super("save");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = 5269452849219756769L;

   public ContractViewOrEditPanel(final String id, final IModel<Contract> model) {
      super(id, model);
      add(createContractViewFragement().setOutputMarkupId(true));
   }

   private Fragment createContractEditFragement() {
      return new Fragment("contractViewOrEditFragement", "contractEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 4702333788976660894L;

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
            add(new SaveAjaxLink().setOutputMarkupId(true));
            add(new CancelAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   };

   private Fragment createContractViewFragement() {
      return new Fragment("contractViewOrEditFragement", "contractViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 4702333788976660894L;

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
      };
   }

}
