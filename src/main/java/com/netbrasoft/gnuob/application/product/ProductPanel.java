package com.netbrasoft.gnuob.application.product;

import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.layout.col.MediumSpanType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = -8317730269644885290L;

      public AddAjaxLink() {
         super("add", Model.of(ProductPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(ProductPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         productViewOrEditPanel.setDefaultModelObject(new Product());
         target.add(productViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ProductDataview extends DataView<Product> {

      private static final long serialVersionUID = -5039874949058607907L;

      private long selectedObjectId;

      protected ProductDataview() {
         super("productDataview", productDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Product> newItem(String id, int index, IModel<Product> model) {
         final Item<Product> item = super.newItem(id, index, model);
         final long modelObjectId = ((Product) productViewOrEditPanel.getDefaultModelObject()).getId();

         if ((model.getObject().getId() == modelObjectId) || modelObjectId == 0) {
            item.add(new BootstrapBaseBehavior() {

               private static final long serialVersionUID = -4903722864597601489L;

               @Override
               public void onComponentTag(Component component, ComponentTag tag) {
                  Attributes.addClass(tag, "info");
               }
            });
         }
         return item;
      }

      @Override
      protected void onConfigure() {
         if(selectedObjectId  != ((Product)ProductPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Product)ProductPanel.this.getDefaultModelObject()).getId();
         }
         super.onConfigure();
      }

      @Override
      protected void populateItem(Item<Product> item) {
         item.setModel(new CompoundPropertyModel<Product>(item.getModelObject()));
         item.add(new Label("number"));
         item.add(new Label("name"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               productViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(productDataviewContainer.setOutputMarkupId(true));
               target.add(productViewOrEditPanel.setOutputMarkupId(true));
            }
         });
         item.add(new RemoveAjaxLink(item.getModel()).add(new ConfirmationBehavior() {

            private static final long serialVersionUID = 7744720444161839031L;

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
               response.render($(component)
                     .chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true).withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage")))
                     .asDomReadyScript());
            }
         }));

         if (item.getIndex() == 0 && ((Product) productViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            productViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Product> {

      private static final long serialVersionUID = -8317730269644885290L;

      public RemoveAjaxLink(final IModel<Product> model) {
         super("remove", model, Buttons.Type.Default, Model.of(ProductPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            getModelObject().setActive(false);
            productDataProvider.merge(getModelObject());
            productViewOrEditPanel.setDefaultModelObject(new Product());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductPanel.class);

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   private final ProductDataview productDataview;

   @SpringBean(name = "ProductDataProvider", required = true)
   private GenericTypeDataProvider<Product> productDataProvider;

   private final OrderByBorder<String> orderByNumber;

   private final OrderByBorder<String> orderByName;

   private final WebMarkupContainer productDataviewContainer;

   private final BootstrapPagingNavigator productPagingNavigator;

   private final ProductViewOrEditPanel productViewOrEditPanel;

   private final WebMarkupContainer productPanelContainer;

   private final WebMarkupContainer productTableContainer;

   public ProductPanel(final String id, final IModel<Product> model) {
      super(id, model);

      orderByNumber = new OrderByBorder<String>("orderByNumber", "number", productDataProvider);
      orderByName = new OrderByBorder<String>("orderByName", "name", productDataProvider);
      productDataview = new ProductDataview();
      productPagingNavigator = new BootstrapPagingNavigator("productPagingNavigator", productDataview);
      productDataviewContainer = new WebMarkupContainer("productDataviewContainer") {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(productDataview);
            super.onInitialize();
         }
      };
      productTableContainer = new WebMarkupContainer("productTableContainer", getDefaultModel()) {

         private static final long serialVersionUID = -4706369076595798457L;

         @Override
         protected void onInitialize() {
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(new AddAjaxLink().setOutputMarkupId(true));
            add(orderByNumber.setOutputMarkupId(true));
            add(orderByName.setOutputMarkupId(true));
            add(productDataviewContainer.setOutputMarkupId(true));
            add(productPagingNavigator.setOutputMarkupId(true));
            add(new TableBehavior().hover());
            super.onInitialize();
         }
      };
      productPanelContainer = new WebMarkupContainer("productPanelContainer", getDefaultModel()) {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(productTableContainer.setOutputMarkupId(true));
            add(productViewOrEditPanel.add(productViewOrEditPanel.new ProductViewFragement()).setOutputMarkupId(true));
            add(new BootstrapBaseBehavior() {

               private static final long serialVersionUID = -4903722864597601489L;

               @Override
               public void onComponentTag(Component component, ComponentTag tag) {
                  Attributes.addClass(tag, MediumSpanType.SPAN10);
               }
            });
            super.onInitialize();
         }
      };
      productViewOrEditPanel = new ProductViewOrEditPanel("productViewOrEditPanel", Model.of(new Product())){

         private static final long serialVersionUID = -8723947139234708667L;

         @Override
         protected void onInitialize() {
            add(new WellBehavior(Size.Small));
            super.onInitialize();
         }
      };
   }

   @Override
   protected void onInitialize() {
      productDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      productDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      productDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      productDataProvider.setType((Product) getDefaultModelObject());
      add(productPanelContainer.setOutputMarkupId(true));
      super.onInitialize();
   }
}
