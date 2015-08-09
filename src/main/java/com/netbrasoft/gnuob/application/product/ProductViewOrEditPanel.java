package com.netbrasoft.gnuob.application.product;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.UrlTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapCheckbox;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ProductViewOrEditPanel.this.removeAll();
         ProductViewOrEditPanel.this.add(new ProductViewFragement()).setOutputMarkupId(true);
         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ProductViewOrEditPanel.this.removeAll();
         ProductViewOrEditPanel.this.add(new ProductEditFragement().setOutputMarkupId(true));
         target.add(ProductViewOrEditPanel.this);
      }
   }

   class ProductEditFragement extends Fragment {

      private static final long serialVersionUID = 4745878764173533323L;

      public ProductEditFragement() {
         super("productViewOrEditFragement", "productEditFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Product>) getDefaultModel());
         SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) getDefaultModel());
         Form<Product> productEditForm = new Form<Product>("productEditForm");
         productEditForm.setModel(new CompoundPropertyModel<Product>((IModel<Product>) getDefaultModel()));

         productEditForm.add(new TextField<String>("number"));
         productEditForm.add(new TextField<String>("name"));
         productEditForm.add(new TextArea<String>("description"));
         productEditForm.add(new UrlTextField(ITEM_URL, new PropertyModel<String>(getDefaultModelObject(), ITEM_URL)));
         productEditForm.add(new NumberTextField<Integer>("amount"));
         productEditForm.add(new NumberTextField<Integer>("discount"));
         productEditForm.add(new NumberTextField<Integer>("shippingCost"));
         productEditForm.add(new NumberTextField<Integer>("tax"));
         productEditForm.add(new NumberTextField<Integer>("itemHeight"));
         productEditForm.add(new TextField<String>("itemHeightUnit"));
         productEditForm.add(new NumberTextField<Integer>("itemLength"));
         productEditForm.add(new TextField<String>("itemLengthUnit"));
         productEditForm.add(new NumberTextField<Integer>("itemWeight"));
         productEditForm.add(new TextField<String>("itemWeightUnit"));
         productEditForm.add(new NumberTextField<Integer>("itemWidth"));
         productEditForm.add(new TextField<String>("itemWidthUnit"));
         productEditForm.add(new BootstrapCheckbox("bestsellers"));
         productEditForm.add(new BootstrapCheckbox("latestCollection"));
         productEditForm.add(new NumberTextField<Integer>("rating"));
         productEditForm.add(new BootstrapCheckbox("recommended"));
         productEditForm.add(new NumberTextField<Integer>("stock.maxQuantity"));
         productEditForm.add(new NumberTextField<Integer>("stock.minQuantity"));
         productEditForm.add(new NumberTextField<Integer>("stock.quantity"));

         add(productEditForm.setOutputMarkupId(true));
         add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentEditFragement()).setOutputMarkupId(true));
         add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         add(new SaveAjaxButton(productEditForm).setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class ProductViewFragement extends Fragment {

      private static final long serialVersionUID = 4745878764173533323L;

      public ProductViewFragement() {
         super("productViewOrEditFragement", "productViewFragement", ProductViewOrEditPanel.this, ProductViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", (IModel<Product>) getDefaultModel());
         SubCategoryViewOrEditPanel subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel("subCategoryViewOrEditPanel", (IModel<Product>) getDefaultModel());
         Form<Product> productViewForm = new Form<Product>("productViewForm");
         productViewForm.setModel(new CompoundPropertyModel<Product>((IModel<Product>) getDefaultModel()));

         productViewForm.add(new Label("number"));
         productViewForm.add(new Label("name"));
         productViewForm.add(new Label("description"));
         productViewForm.add(new Label(ITEM_URL));
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

         add(new EditAjaxLink().setOutputMarkupId(true));
         add(productViewForm.setOutputMarkupId(true));
         add(contentViewOrEditPanel.add(contentViewOrEditPanel.new ContentViewFragement()).setOutputMarkupId(true));
         add(subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
         super.onInitialize();
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
            Product product = (Product) form.getDefaultModelObject();

            if (product.getId() == 0) {
               product.setActive(true);

               productDataProvider.persist(product);
            } else {
               productDataProvider.merge(product);
            }

            ProductViewOrEditPanel.this.removeAll();
            ProductViewOrEditPanel.this.add(new ProductViewFragement().setOutputMarkupId(true));
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

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewOrEditPanel.class);

   private static final String ITEM_URL = "itemUrl";

   private static final long serialVersionUID = -4234081101243453856L;

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   public ProductViewOrEditPanel(final String id, final IModel<?> model) {
      super(id, model);
   }
}
