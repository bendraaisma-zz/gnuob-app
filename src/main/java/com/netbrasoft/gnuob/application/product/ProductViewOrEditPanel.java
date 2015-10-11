package com.netbrasoft.gnuob.application.product;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ProductViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ProductEditFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ProductEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CancelAjaxLink extends BootstrapAjaxLink<Product> {

        private static final long serialVersionUID = 4267535261864907719L;

        public CancelAjaxLink(String id, IModel<Product> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ProductViewOrEditPanel.this.removeAll();
          if (((Product) CancelAjaxLink.this.getDefaultModelObject()).getId() > 0) {
            CancelAjaxLink.this.setDefaultModelObject(productDataProvider.findById((Product) CancelAjaxLink.this.getDefaultModelObject()));
          }
          target.add(ProductViewOrEditPanel.this.add(new ProductViewFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY))));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          boolean isException = false;
          try {
            if (((Product) form.getDefaultModelObject()).getId() == 0) {
              ProductEditTable.this.setDefaultModelObject(productDataProvider.findById(productDataProvider.persist(((Product) form.getDefaultModelObject()))));
            } else {
              ProductEditTable.this.setDefaultModelObject(productDataProvider.findById(productDataProvider.merge(((Product) form.getDefaultModelObject()))));
            }
          } catch (final RuntimeException e) {
            isException = true;
            LOGGER.warn(e.getMessage(), e);
            feedbackPanel.warn(e.getLocalizedMessage());
            target.add(feedbackPanel.setOutputMarkupId(true));
            target
                .add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
          } finally {
            if (!isException) {
              ProductViewOrEditPanel.this.removeAll();
              target.add(ProductViewOrEditPanel.this.add(ProductViewOrEditPanel.this.new ProductViewFragement()).setOutputMarkupId(true));
            }
          }
        }
      }

      private static final long serialVersionUID = 4144078478707460879L;

      private final BootstrapForm<Product> productEditForm;

      private final ProductOptionPanel productOptionPanel;

      private final ProductContentPanel contentViewOrEditPanel;

      private final ProductSubCategoryPanel subCategoryViewOrEditPanel;

      private final CancelAjaxLink cancelAjaxLink;

      private final SaveAjaxButton saveAjaxButton;

      private final NotificationPanel feedbackPanel;

      public ProductEditTable(final String id, final IModel<Product> model) {
        super(id, model);
        productEditForm = new BootstrapForm<Product>("productEditForm", new CompoundPropertyModel<Product>((IModel<Product>) ProductEditTable.this.getDefaultModel()));
        productOptionPanel = new ProductOptionPanel("optionViewOrEditPanel", (IModel<Product>) ProductEditTable.this.getDefaultModel());
        contentViewOrEditPanel = new ProductContentPanel("contentViewOrEditPanel", (IModel<Product>) ProductEditTable.this.getDefaultModel());
        subCategoryViewOrEditPanel = new ProductSubCategoryPanel("subCategoryViewOrEditPanel", (IModel<Product>) ProductEditTable.this.getDefaultModel());
        cancelAjaxLink =
            new CancelAjaxLink("cancel", model, Buttons.Type.Default, Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)),
            productEditForm, Buttons.Type.Primary);
        feedbackPanel = new NotificationPanel("feedback");
      }

      @Override
      protected void onInitialize() {
        productEditForm.add(new RequiredTextField<String>("number").add(StringValidator.maximumLength(64)).setOutputMarkupId(true));
        productEditForm.add(new RequiredTextField<String>("name").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        productEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(getDefaultModelObject(), "itemUrl")).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("amount").setMinimum(BigDecimal.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("discount").setMinimum(BigDecimal.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("shippingCost").setMinimum(BigDecimal.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("tax").setMinimum(BigDecimal.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("itemHeight").setMinimum(BigDecimal.ZERO).setOutputMarkupId(true));
        productEditForm.add(new TextField<String>("itemHeightUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("itemLength").setMinimum(BigDecimal.ZERO).setOutputMarkupId(true));
        productEditForm.add(new TextField<String>("itemLengthUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("itemWeight").setMinimum(BigDecimal.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new TextField<String>("itemWeightUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigDecimal>("itemWidth").setMinimum(BigDecimal.ZERO).setOutputMarkupId(true));
        productEditForm.add(new TextField<String>("itemWidthUnit").add(StringValidator.maximumLength(20)).setOutputMarkupId(true));
        productEditForm.add(new BootstrapCheckbox("bestsellers").setOutputMarkupId(true));
        productEditForm.add(new BootstrapCheckbox("latestCollection").setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<Integer>("rating").setMinimum(0).setMinimum(0).setMaximum(5).setOutputMarkupId(true));
        productEditForm.add(new BootstrapCheckbox("recommended").setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigInteger>("stock.maxQuantity").setMinimum(BigInteger.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigInteger>("stock.minQuantity").setMinimum(BigInteger.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(new NumberTextField<BigInteger>("stock.quantity").setMinimum(BigInteger.ZERO).setRequired(true).setOutputMarkupId(true));
        productEditForm.add(productOptionPanel.add(productOptionPanel.new ProductOptionEditFragement()).setOutputMarkupId(true));
        productEditForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ProductContentEditFragement()).setOutputMarkupId(true));
        productEditForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new ProductSubCategoryEditFragement()).setOutputMarkupId(true));
        add(productEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(cancelAjaxLink.setOutputMarkupId(true));
        add(saveAjaxButton.setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4745878764173533323L;

    private final ProductEditTable productEditTable;

    public ProductEditFragement() {
      super("productViewOrEditFragement", "productEditFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());
      productEditTable = new ProductEditTable("productEditTable", (IModel<Product>) ProductEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(productEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ProductViewFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class EditAjaxLink extends BootstrapAjaxLink<Product> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink(String id, IModel<Product> model, Buttons.Type type, IModel<String> labelModel) {
        super(id, model, type, labelModel);
        setIconType(GlyphIconType.edit);
        setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
        ProductViewOrEditPanel.this.removeAll();
        target.add(ProductViewOrEditPanel.this.add(new ProductEditFragement().setOutputMarkupId(true)));
      }
    }

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ProductViewTable extends WebMarkupContainer {

      private static final long serialVersionUID = -6470226164583172027L;

      private final EditAjaxLink editAjaxLink;

      private final BootstrapForm<Product> productViewForm;

      private final ProductOptionPanel productOptionPanel;

      private final ProductContentPanel contentViewOrEditPanel;

      private final ProductSubCategoryPanel subCategoryViewOrEditPanel;

      public ProductViewTable(String id, IModel<Product> model) {
        super(id, model);
        productViewForm = new BootstrapForm<Product>("productViewForm", new CompoundPropertyModel<Product>((IModel<Product>) ProductViewTable.this.getDefaultModel()));
        editAjaxLink = new EditAjaxLink("edit", model, Buttons.Type.Primary, Model.of(ProductViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY)));
        productOptionPanel = new ProductOptionPanel("optionViewOrEditPanel", (IModel<Product>) ProductViewTable.this.getDefaultModel());
        contentViewOrEditPanel = new ProductContentPanel("contentViewOrEditPanel", (IModel<Product>) ProductViewTable.this.getDefaultModel());
        subCategoryViewOrEditPanel = new ProductSubCategoryPanel("subCategoryViewOrEditPanel", (IModel<Product>) ProductViewTable.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        productViewForm.add(new RequiredTextField<String>("number").setOutputMarkupId(true));
        productViewForm.add(new RequiredTextField<String>("name").setOutputMarkupId(true));
        productViewForm.add(new TextArea<String>("description").setOutputMarkupId(true));
        productViewForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(getDefaultModelObject(), "itemUrl")).setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("amount").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("discount").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("shippingCost").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("tax").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("itemHeight").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("itemHeightUnit").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("itemLength").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("itemLengthUnit").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("itemWeight").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("itemWeightUnit").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("itemWidth").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("itemWidthUnit").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("bestsellers").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("latestCollection").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("rating").setOutputMarkupId(true));
        productViewForm.add(new TextField<String>("recommended").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("stock.maxQuantity").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("stock.minQuantity").setOutputMarkupId(true));
        productViewForm.add(new NumberTextField<Integer>("stock.quantity").setOutputMarkupId(true));
        productViewForm.add(productOptionPanel.add(productOptionPanel.new ProductOptionViewFragement()).setOutputMarkupId(true));
        productViewForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ProductContentViewFragement()).setOutputMarkupId(true));
        productViewForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new ProductSubCategoryViewFragement()).setOutputMarkupId(true));
        add(productViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(editAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = 4745878764173533323L;

    private final ProductViewTable productViewTable;

    public ProductViewFragement() {
      super("productViewOrEditFragement", "productViewFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());
      productViewTable = new ProductViewTable("productViewTable", (IModel<Product>) ProductViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(productViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewOrEditPanel.class);

  private static final long serialVersionUID = -4234081101243453856L;

  @SpringBean(name = "ProductDataProvider", required = true)
  private GenericTypeDataProvider<Product> productDataProvider;

  public ProductViewOrEditPanel(final String id, final IModel<Product> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    productDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    productDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    productDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    productDataProvider.setType(new Product());
    productDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
