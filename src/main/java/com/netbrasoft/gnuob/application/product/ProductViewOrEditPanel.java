package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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

import com.netbrasoft.gnuob.api.Offer;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(ProductViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(ProductViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ProductViewOrEditPanel.this.removeAll();
         ProductViewOrEditPanel.this.add(new ProductViewFragement()).setOutputMarkupId(true);

         if (((Offer) ProductViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            ProductViewOrEditPanel.this.setDefaultModelObject(productDataProvider.findById((Product) ProductViewOrEditPanel.this.getDefaultModelObject()));
         }
         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(ProductViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(ProductViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ProductViewOrEditPanel.this.removeAll();
         ProductViewOrEditPanel.this.add(new ProductEditFragement().setOutputMarkupId(true));
         target.add(ProductViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ProductEditFragement extends Fragment {

      private static final long serialVersionUID = 4745878764173533323L;

      private final WebMarkupContainer productEditTable;

      public ProductEditFragement() {
         super("productViewOrEditFragement", "productEditFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());

         productEditTable = new WebMarkupContainer("productEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -8852597719271552653L;

            @Override
            protected void onInitialize() {
               final ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Product>) getDefaultModel());
               final SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) getDefaultModel());
               final Form<Product> productEditForm = new Form<Product>("productEditForm");
               productEditForm.setModel(new CompoundPropertyModel<Product>((IModel<Product>) getDefaultModel()));
               productEditForm.add(new RequiredTextField<String>("number").add(StringValidator.maximumLength(64)));
               productEditForm.add(new TextField<String>("name").add(StringValidator.maximumLength(128)));
               productEditForm.add(new TextArea<String>("description").add(StringValidator.maximumLength(128)).setRequired(true));
               productEditForm.add(new UrlTextField("itemUrl", new PropertyModel<String>(getDefaultModelObject(), "itemUrl")));
               productEditForm.add(new NumberTextField<Integer>("amount").setRequired(true));
               productEditForm.add(new NumberTextField<Integer>("discount").setRequired(true));
               productEditForm.add(new NumberTextField<Integer>("shippingCost").setRequired(true));
               productEditForm.add(new NumberTextField<Integer>("tax"));
               productEditForm.add(new NumberTextField<Integer>("itemHeight"));
               productEditForm.add(new TextField<String>("itemHeightUnit").add(StringValidator.maximumLength(20)));
               productEditForm.add(new NumberTextField<Integer>("itemLength"));
               productEditForm.add(new TextField<String>("itemLengthUnit").add(StringValidator.maximumLength(20)));
               productEditForm.add(new NumberTextField<Integer>("itemWeight").setRequired(true));
               productEditForm.add(new TextField<String>("itemWeightUnit").add(StringValidator.maximumLength(20)));
               productEditForm.add(new NumberTextField<Integer>("itemWidth"));
               productEditForm.add(new TextField<String>("itemWidthUnit").add(StringValidator.maximumLength(20)));
               productEditForm.add(new BootstrapCheckbox("bestsellers"));
               productEditForm.add(new BootstrapCheckbox("latestCollection"));
               productEditForm.add(new NumberTextField<Integer>("rating"));
               productEditForm.add(new BootstrapCheckbox("recommended"));
               productEditForm.add(new NumberTextField<Integer>("stock.maxQuantity"));
               productEditForm.add(new NumberTextField<Integer>("stock.minQuantity"));
               productEditForm.add(new NumberTextField<Integer>("stock.quantity"));
               productEditForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentEditFragement()).setOutputMarkupId(true));
               productEditForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
               add(productEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new SaveAjaxButton(productEditForm).setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(productEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ProductViewFragement extends Fragment {

      private static final long serialVersionUID = 4745878764173533323L;

      private final WebMarkupContainer productViewTable;

      public ProductViewFragement() {
         super("productViewOrEditFragement", "productViewFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());

         productViewTable = new WebMarkupContainer("productViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 8845702139495733433L;

            @Override
            protected void onInitialize() {
               final ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Product>) getDefaultModel());
               final SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) getDefaultModel());
               final Form<Product> productViewForm = new Form<Product>("productViewForm");
               productViewForm.setModel(new CompoundPropertyModel<Product>((IModel<Product>) getDefaultModel()));
               productViewForm.add(new Label("number"));
               productViewForm.add(new Label("name"));
               productViewForm.add(new Label("description"));
               productViewForm.add(new Label("itemUrl"));
               productViewForm.add(new Label("amount"));
               productViewForm.add(new Label("discount"));
               productViewForm.add(new Label("shippingCost"));
               productViewForm.add(new Label("tax"));
               productViewForm.add(new Label("itemHeight"));
               productViewForm.add(new Label("itemHeightUnit"));
               productViewForm.add(new Label("itemLength"));
               productViewForm.add(new Label("itemLengthUnit"));
               productViewForm.add(new Label("itemWeight"));
               productViewForm.add(new Label("itemWeightUnit"));
               productViewForm.add(new Label("itemWidth"));
               productViewForm.add(new Label("itemWidthUnit"));
               productViewForm.add(new Label("bestsellers"));
               productViewForm.add(new Label("latestCollection"));
               productViewForm.add(new Label("rating"));
               productViewForm.add(new Label("recommended"));
               productViewForm.add(new Label("stock.maxQuantity"));
               productViewForm.add(new Label("stock.minQuantity"));
               productViewForm.add(new Label("stock.quantity"));
               productViewForm.add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
               productViewForm.add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentViewFragement()).setOutputMarkupId(true));
               add(productViewForm.setOutputMarkupId(true));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(productViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(ProductViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(ProductViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Product product = (Product) form.getDefaultModelObject();
            product.setActive(true);

            if (product.getId() == 0) {
               ProductViewOrEditPanel.this.setDefaultModelObject(productDataProvider.findById(productDataProvider.persist(product)));
            } else {
               ProductViewOrEditPanel.this.setDefaultModelObject(productDataProvider.findById(productDataProvider.merge(product)));
            }

            ProductViewOrEditPanel.this.removeAll();
            ProductViewOrEditPanel.this.add(new ProductViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(target.getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewOrEditPanel.class);

   private static final long serialVersionUID = -4234081101243453856L;

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   public ProductViewOrEditPanel(final String id, final IModel<?> model) {
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
