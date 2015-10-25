package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
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
public class ProductOptionViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductOptionEditFragment extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class OptionEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(final String id, final IModel<String> model, final Form<Option> form,
            final Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(ProductOptionViewOrEditPanel.this
              .getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY))),
              new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          target.add(form.add(new TooltipValidation()));
          target.add(
              SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductOptionViewOrEditPanel.this
                  .getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          if (((Option) form.getDefaultModelObject()).getId() == 0) {
            ((Product) ProductOptionViewOrEditPanel.this.getDefaultModelObject()).getOptions()
                .add((Option) form.getDefaultModelObject());
          }
          target.add(form.setOutputMarkupId(true));
          target.add(
              SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductOptionViewOrEditPanel.this
                  .getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)))));
          target.add(ProductOptionViewOrEditPanel.this.getParent().setOutputMarkupId(true));
        }
      }

      private static final long serialVersionUID = -7519943626345095089L;

      private final BootstrapForm<Option> optionEditForm;

      private final ProductSubOptionPanel productSubOptionPanel;

      private final SaveAjaxButton saveAjaxButton;

      public OptionEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        optionEditForm = new BootstrapForm<Option>("optionEditForm",
            new CompoundPropertyModel<Option>(ProductOptionViewOrEditPanel.this.selectedModel));
        productSubOptionPanel = new ProductSubOptionPanel("productSubOptionPanel",
            (IModel<Product>) OptionEditTable.this.getDefaultModel());
        productSubOptionPanel.setSelectedModel(ProductOptionViewOrEditPanel.this.selectedModel);
        saveAjaxButton = new SaveAjaxButton("save",
            Model.of(ProductOptionViewOrEditPanel.this
                .getString(NetbrasoftApplicationConstants.SAVE_MESSAGE_KEY)),
            optionEditForm, Buttons.Type.Primary);
      }

      @Override
      protected void onInitialize() {
        optionEditForm
            .add(new NumberTextField<Integer>("position").setMinimum(0).setOutputMarkupId(true));
        optionEditForm.add(new TextArea<String>("value").add(StringValidator.maximumLength(128))
            .setRequired(true).setOutputMarkupId(true));
        optionEditForm.add(new TextArea<String>("description")
            .add(StringValidator.maximumLength(128)).setRequired(true).setOutputMarkupId(true));
        optionEditForm.add(new BootstrapCheckbox("disabled").setOutputMarkupId(true));
        optionEditForm.add(saveAjaxButton.setOutputMarkupId(true));
        optionEditForm.add(
            productSubOptionPanel.add(productSubOptionPanel.new ProductSubOptionEditFragement())
                .setOutputMarkupId(true));
        add(optionEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -4032029235917033204L;

    private final OptionEditTable optionEditTable;

    public ProductOptionEditFragment() {
      super("productOptionViewOrEditFragment", "productOptionEditFragment",
          ProductOptionViewOrEditPanel.this, ProductOptionViewOrEditPanel.this.getDefaultModel());
      optionEditTable = new OptionEditTable("optionEditTable",
          (IModel<Product>) ProductOptionViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(optionEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ProductOptionViewFragment extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class OptionViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = -7519943626345095089L;

      private final BootstrapForm<Option> optionViewForm;

      private final ProductSubOptionPanel productSubOptionPanel;

      public OptionViewTable(final String id, final IModel<Product> model) {
        super(id, model);
        optionViewForm = new BootstrapForm<Option>("optionViewForm",
            new CompoundPropertyModel<Option>(ProductOptionViewOrEditPanel.this.selectedModel));
        productSubOptionPanel = new ProductSubOptionPanel("productSubOptionPanel",
            (IModel<Product>) OptionViewTable.this.getDefaultModel());
        productSubOptionPanel.setSelectedModel(ProductOptionViewOrEditPanel.this.selectedModel);
      }

      @Override
      protected void onInitialize() {
        optionViewForm.add(new NumberTextField<Integer>("position").setOutputMarkupId(true));
        optionViewForm.add(new TextArea<String>("value").setOutputMarkupId(true));
        optionViewForm.add(new TextArea<String>("description").setOutputMarkupId(true));
        optionViewForm.add(new TextField<String>("disabled").setOutputMarkupId(true));
        optionViewForm.add(
            productSubOptionPanel.add(productSubOptionPanel.new ProductSubOptionViewFragement())
                .setOutputMarkupId(true));
        add(optionViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -4032029235917033204L;

    private final OptionViewTable optionViewTable;

    public ProductOptionViewFragment() {
      super("productOptionViewOrEditFragment", "productOptionViewFragment",
          ProductOptionViewOrEditPanel.this, ProductOptionViewOrEditPanel.this.getDefaultModel());
      optionViewTable = new OptionViewTable("optionViewTable",
          (IModel<Product>) ProductOptionViewOrEditPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(optionViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final long serialVersionUID = 8609291357690450348L;

  private IModel<Option> selectedModel;

  public ProductOptionViewOrEditPanel(final String id, final IModel<Product> model) {
    super(id, model);
    selectedModel = Model.of(new Option());
  }

  public void setSelectedModel(final IModel<Option> selectedModel) {
    this.selectedModel = selectedModel;
  }
}
