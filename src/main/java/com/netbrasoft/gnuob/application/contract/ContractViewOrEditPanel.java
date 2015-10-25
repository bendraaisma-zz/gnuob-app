package com.netbrasoft.gnuob.application.contract;

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
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Contract;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.content.ContentViewOrEditPanel;
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
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ContractViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ContractEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class ContractEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CancelAjaxLink extends BootstrapAjaxLink<Contract> {

        private static final long serialVersionUID = 4267535261864907719L;

        public CancelAjaxLink(String id, IModel<Contract> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ContractViewOrEditPanel.this.removeAll();
          if (((Contract) CancelAjaxLink.this.getDefaultModelObject()).getId() > 0) {
            CancelAjaxLink.this.setDefaultModelObject(contractDataProvider.findById((Contract) CancelAjaxLink.this.getDefaultModelObject()));
          }
          target.add(ContractViewOrEditPanel.this.add(new ContractViewFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY))),
              new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target
              .add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          boolean isException = false;
          try {
            if (((Contract) form.getDefaultModelObject()).getId() == 0) {
              ContractEditTable.this.setDefaultModelObject(contractDataProvider.findById(contractDataProvider.persist(((Contract) form.getDefaultModelObject()))));
            } else {
              ContractEditTable.this.setDefaultModelObject(contractDataProvider.findById(contractDataProvider.merge(((Contract) form.getDefaultModelObject()))));
            }
          } catch (final RuntimeException e) {
            isException = true;
            LOGGER.warn(e.getMessage(), e);
            feedbackPanel.warn(e.getLocalizedMessage());
            target.add(feedbackPanel.setOutputMarkupId(true));
            target.add(
                SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
          } finally {
            if (!isException) {
              ContractViewOrEditPanel.this.removeAll();
              target.add(ContractViewOrEditPanel.this.add(ContractViewOrEditPanel.this.new ContractViewFragement()).setOutputMarkupId(true));
            }
          }
        }
      }

      private static final long serialVersionUID = -1708404592209286996L;

      private final BootstrapForm<Contract> contractEditForm;

      private final CancelAjaxLink cancelAjaxLink;

      private final SaveAjaxButton saveAjaxButton;

      private final NotificationPanel feedbackPanel;

      public ContractEditTable(final String id, final IModel<Contract> model) {
        super(id, model);
        contractEditForm = new BootstrapForm<Contract>("contractEditForm", new CompoundPropertyModel<Contract>((IModel<Contract>) ContractEditTable.this.getDefaultModel()));
        cancelAjaxLink =
            new CancelAjaxLink("cancel", model, Buttons.Type.Default, Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)),
            contractEditForm, Buttons.Type.Primary);
        feedbackPanel = new NotificationPanel("feedback");
      }

      @Override
      protected void onInitialize() {
        contractEditForm.add(new RequiredTextField<String>("contractId").add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.friendlyName").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.buyerMarketingEmail").setLabel(Model.of(getString("buyerMarketingEmailMessage")))
            .add(EmailAddressValidator.getInstance()).add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.contactPhone").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.payer").setLabel(Model.of(getString("payerMessage"))).add(EmailAddressValidator.getInstance())
            .add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.payerBusiness").setLabel(Model.of(getString("payerBusinessMessage"))).add(EmailAddressValidator.getInstance())
            .add(StringValidator.maximumLength(127)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.payerId").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.payerStatus").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.taxId").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        contractEditForm.add(new TextField<String>("customer.taxIdType").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        add(contractEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(saveAjaxButton.setOutputMarkupId(true));
        add(cancelAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4702333788976660894L;

    private final WebMarkupContainer contractEditTable;

    public ContractEditFragement() {
      super("contractViewOrEditFragement", "contractEditFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      contractEditTable = new ContractEditTable("contractEditTable", (IModel<Contract>) ContractEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contractEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ContractViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ContractViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class EditAjaxLink extends BootstrapAjaxLink<Contract> {

        private static final long serialVersionUID = 4267535261864907719L;

        public EditAjaxLink(String id, IModel<Contract> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.edit);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ContractViewOrEditPanel.this.removeAll();
          target.add(ContractViewOrEditPanel.this.add(new ContractEditFragement().setOutputMarkupId(true)));
        }
      }

      private static final long serialVersionUID = 7285844181873490052L;

      private final Form<Contract> contractViewForm;

      private final EditAjaxLink editAjaxLink;

      public ContractViewTable(final String id, final IModel<Contract> model) {
        super(id, model);
        contractViewForm = new Form<Contract>("contractViewForm", new CompoundPropertyModel<Contract>((IModel<Contract>) ContractViewTable.this.getDefaultModel()));
        editAjaxLink =
            new EditAjaxLink("edit", model, Buttons.Type.Primary, Model.of(ContractViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY)));
      }

      @Override
      protected void onInitialize() {
        contractViewForm.add(new RequiredTextField<String>("contractId").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.friendlyName").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.buyerMarketingEmail").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.contactPhone").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.payer").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.payerBusiness").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.payerId").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.payerStatus").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.taxId").setOutputMarkupId(true));
        contractViewForm.add(new TextField<String>("customer.taxIdType").setOutputMarkupId(true));
        add(contractViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(editAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4702333788976660894L;

    private final ContractViewTable contractViewTable;

    public ContractViewFragement() {
      super("contractViewOrEditFragement", "contractViewFragement", ContractViewOrEditPanel.this, ContractViewOrEditPanel.this.getDefaultModel());
      contractViewTable = new ContractViewTable("contractViewTable", (IModel<Contract>) ContractViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contractViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
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
    contractDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    contractDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    contractDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    contractDataProvider.setType(new Contract());
    contractDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
