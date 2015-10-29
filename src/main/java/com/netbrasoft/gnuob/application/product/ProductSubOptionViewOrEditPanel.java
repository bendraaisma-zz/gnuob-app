package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import com.netbrasoft.gnuob.api.Option;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.SubOption;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ProductSubOptionViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductSubOptionEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class SubOptionEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(final String id, final IModel<String> model, final Form<SubOption> form, final Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(ProductSubOptionViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY))), new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductSubOptionViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          if (((SubOption) form.getDefaultModelObject()).getId() == 0) {
            ProductSubOptionViewOrEditPanel.this.selectedParentModel.getObject().getSubOptions().add((SubOption) form.getDefaultModelObject());
          }
          target.add(form.setOutputMarkupId(true));
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductSubOptionViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
          target.add(ProductSubOptionViewOrEditPanel.this.getParent().setOutputMarkupId(true));
        }
      }

      private static final long serialVersionUID = -7519943626345095089L;

      private final BootstrapForm<SubOption> subOptionEditForm;

      private final SaveAjaxButton saveAjaxButton;

      public SubOptionEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        subOptionEditForm = new BootstrapForm<SubOption>("subOptionEditForm", new CompoundPropertyModel<SubOption>(ProductSubOptionViewOrEditPanel.this.selectedModel));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(ProductSubOptionViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)), subOptionEditForm,
            Buttons.Type.Primary);
      }

      @Override
      protected void onInitialize() {
        subOptionEditForm.add(new TextArea<String>("value").add(StringValidator.maximumLength(128)).setRequired(true).setOutputMarkupId(true));
        subOptionEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)).setRequired(true).setOutputMarkupId(true));
        subOptionEditForm.add(new BootstrapCheckbox("disabled").setOutputMarkupId(true));
        subOptionEditForm.add(saveAjaxButton.setOutputMarkupId(true));
        add(subOptionEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -4032029235917033204L;

    private final SubOptionEditTable subOptionEditTable;

    public ProductSubOptionEditFragment() {
      super("productSubOptionViewOrEditFragment", "productSubOptionEditFragment", ProductSubOptionViewOrEditPanel.this, ProductSubOptionViewOrEditPanel.this.getDefaultModel());
      subOptionEditTable = new SubOptionEditTable("subOptionEditTable", (IModel<Product>) ProductSubOptionViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subOptionEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductSubOptionViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class SubOptionViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = -7519943626345095089L;

      private final BootstrapForm<SubOption> subOptionViewForm;

      public SubOptionViewTable(final String id, final IModel<Product> model) {
        super(id, model);
        subOptionViewForm = new BootstrapForm<SubOption>("subOptionViewForm", new CompoundPropertyModel<SubOption>(ProductSubOptionViewOrEditPanel.this.selectedModel));
      }

      @Override
      protected void onInitialize() {
        subOptionViewForm.add(new TextArea<String>("value").setOutputMarkupId(true));
        subOptionViewForm.add(new TextArea<String>("description").setOutputMarkupId(true));
        subOptionViewForm.add(new TextField<String>("disabled").setOutputMarkupId(true));
        add(subOptionViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -4032029235917033204L;

    private final SubOptionViewTable subOptionViewTable;

    public ProductSubOptionViewFragment() {
      super("productSubOptionViewOrEditFragment", "productSubOptionViewFragment", ProductSubOptionViewOrEditPanel.this, ProductSubOptionViewOrEditPanel.this.getDefaultModel());
      subOptionViewTable = new SubOptionViewTable("subOptionViewTable", (IModel<Product>) ProductSubOptionViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(subOptionViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 8609291357690450348L;

  private IModel<SubOption> selectedModel;

  private IModel<Option> selectedParentModel;

  public ProductSubOptionViewOrEditPanel(final String id, final IModel<Product> model) {
    super(id, model);
    selectedModel = Model.of(new SubOption());
    selectedParentModel = Model.of(new Option());
  }

  public void setSelectedModel(final IModel<SubOption> selectedModel, IModel<Option> selectedParentModel) {
    this.selectedModel = selectedModel;
    this.selectedParentModel = selectedParentModel;
  }
}
