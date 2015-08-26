package com.netbrasoft.gnuob.application.contract;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
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
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Contract;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.content.ContentViewOrEditPanel;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContractViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(ContractViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(ContractViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(new ContractViewFragement()).setOutputMarkupId(true);

         if (((Contract) ContractViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            ContractViewOrEditPanel.this.setDefaultModelObject(contractDataProvider.findById((Contract) ContractViewOrEditPanel.this.getDefaultModelObject()));
         }

         paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class ContractEditFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public ContractEditFragement() {
         super("contractViewOrEditFragement", "contractEditFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         final Form<Contract> contractEditForm = new Form<Contract>("contractEditForm");
         contractEditForm.setModel(new CompoundPropertyModel<Contract>((IModel<Contract>) getDefaultModel()));
         contractEditForm.add(new RequiredTextField<String>("contractId").add(StringValidator.maximumLength(127)));
         contractEditForm.add(new TextField<String>("customer.friendlyName").add(StringValidator.maximumLength(128)));
         contractEditForm.add(new TextField<String>("customer.buyerMarketingEmail").setLabel(Model.of(getString("buyerMarketingEmailMessage"))).add(EmailAddressValidator.getInstance()).add(StringValidator.maximumLength(127)));
         contractEditForm.add(new TextField<String>("customer.contactPhone").add(StringValidator.maximumLength(20)));
         contractEditForm.add(new TextField<String>("customer.payer").setLabel(Model.of(getString("payerMessage"))).add(EmailAddressValidator.getInstance()).add(StringValidator.maximumLength(127)));
         contractEditForm.add(new TextField<String>("customer.payerBusiness").setLabel(Model.of(getString("payerBusinessMessage"))).add(EmailAddressValidator.getInstance()).add(StringValidator.maximumLength(127)));
         contractEditForm.add(new TextField<String>("customer.payerId").add(StringValidator.maximumLength(20)));
         contractEditForm.add(new TextField<String>("customer.payerStatus").add(StringValidator.maximumLength(20)));
         contractEditForm.add(new TextField<String>("customer.taxId").add(StringValidator.maximumLength(20)));
         contractEditForm.add(new TextField<String>("customer.taxIdType").add(StringValidator.maximumLength(20)));

         add(contractEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new SaveAjaxButton(contractEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER })
   class ContractViewFragement extends Fragment {

      private static final long serialVersionUID = 4702333788976660894L;

      public ContractViewFragement() {
         super("contractViewOrEditFragement", "contractViewFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         final Form<Contract> contractViewForm = new Form<Contract>("contractViewForm");
         contractViewForm.setModel(new CompoundPropertyModel<Contract>((IModel<Contract>) getDefaultModel()));
         contractViewForm.add(new Label("contractId"));
         contractViewForm.add(new Label("customer.friendlyName"));
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
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(ContractViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(ContractViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ContractViewOrEditPanel.this.removeAll();
         ContractViewOrEditPanel.this.add(new ContractEditFragement().setOutputMarkupId(true));
         target.add(ContractViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(ContractViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(ContractViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContractViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Contract contract = (Contract) form.getDefaultModelObject();
            contract.setActive(true);

            if (contract.getId() == 0) {
               ContractViewOrEditPanel.this.setDefaultModel(Model.of(contractDataProvider.findById(contractDataProvider.persist(contract))));;
            } else {
               ContractViewOrEditPanel.this.setDefaultModel(Model.of(contractDataProvider.findById(contractDataProvider.merge(contract))));
            }

            ContractViewOrEditPanel.this.removeAll();
            ContractViewOrEditPanel.this.add(new ContractViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
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
   }

   @Override
   protected void onInitialize() {
      add(new ContractViewFragement().setOutputMarkupId(true));
      super.onInitialize();
   }
}
