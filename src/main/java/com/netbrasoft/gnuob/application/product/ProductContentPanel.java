package com.netbrasoft.gnuob.application.product;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ProductContentPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 9191172039973638020L;

      public AddAjaxLink() {
         super("add", Model.of(ProductContentPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(ProductContentPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         productContentViewOrEditPanel.setDefaultModelObject(new Content());
         target.add(productContentViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ContentDataview extends DataView<Content> {

      private static final long serialVersionUID = 2246346365193989354L;

      private boolean removeAjaxLinkVisable;

      private long selectedObjectId;

      protected ContentDataview() {
         super("contentDataview", contentListDataProvider, ITEMS_PER_PAGE);
      }

      public boolean isRemoveAjaxLinkVisable() {
         return removeAjaxLinkVisable;
      }

      @Override
      protected Item<Content> newItem(String id, int index, IModel<Content> model) {
         final Item<Content> item = super.newItem(id, index, model);
         final long modelObjectId = ((Content) productContentViewOrEditPanel.getDefaultModelObject()).getId();

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
         if (selectedObjectId != ((Product) ProductContentPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Product) ProductContentPanel.this.getDefaultModelObject()).getId();
            productContentViewOrEditPanel.setDefaultModelObject(new Content());
         }
         super.onConfigure();
      }

      @Override
      protected void populateItem(Item<Content> item) {
         final IModel<Content> compound = new CompoundPropertyModel<Content>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("format"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               productContentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(contentDataviewContainer.setOutputMarkupId(true));
               target.add(productContentViewOrEditPanel.setOutputMarkupId(true));
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
         }).setVisible(isRemoveAjaxLinkVisable()));

         if (item.getIndex() == 0 && ((Content) productContentViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            productContentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }

      public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
         this.removeAjaxLinkVisable = removeAjaxLinkVisable;
      }
   }

   class ContentListDataProvider extends ListDataProvider<Content> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<Content> getData() {
         return ((Product) ProductContentPanel.this.getDefaultModelObject()).getContents();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class ProductContentEditFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public ProductContentEditFragement() {
         super("productContentViewOrEditFragement", "productContentEditFragement", ProductContentPanel.this, ProductContentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ProductContentViewFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public ProductContentViewFragement() {
         super("productContentViewOrEditFragement", "productContentViewFragement", ProductContentPanel.this, ProductContentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Content> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink(final IModel<Content> model) {
         super("remove", model, Buttons.Type.Default, Model.of(ProductContentPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            contentListDataProvider.getData().remove(getDefaultModelObject());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            contentEditTable.warn(e.getLocalizedMessage());
         } finally {
            target.add(contentDataviewContainer.setOutputMarkupId(true));
         }
      }
   }

   private static final long serialVersionUID = 180343040391839545L;

   private static final long ITEMS_PER_PAGE = 10;

   private static final Logger LOGGER = LoggerFactory.getLogger(ProductContentPanel.class);

   private final WebMarkupContainer contentDataviewContainer;

   private final ContentDataview contentDataview;

   private final BootstrapPagingNavigator contentPagingNavigator;

   private final ContentListDataProvider contentListDataProvider;

   private final WebMarkupContainer contentEditTable;

   private final WebMarkupContainer contentViewTable;

   private final ProductContentViewOrEditPanel productContentViewOrEditPanel;

   public ProductContentPanel(final String id, final IModel<Product> model) {
      super(id, model);
      contentListDataProvider = new ContentListDataProvider();
      contentDataview = new ContentDataview();
      contentPagingNavigator = new BootstrapPagingNavigator("contentPagingNavigator", contentDataview);
      contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer", getDefaultModel()) {

         private static final long serialVersionUID = 1L;

         @Override
         protected void onInitialize() {
            add(contentDataview);
            super.onInitialize();
         }
      };
      contentEditTable = new WebMarkupContainer("contentEditTable", getDefaultModel()) {

         private static final long serialVersionUID = 4858719401860781077L;

         @Override
         protected void onInitialize() {
            contentDataview.setRemoveAjaxLinkVisable(true);
            add(new AddAjaxLink().setOutputMarkupId(true));
            add(contentPagingNavigator.setOutputMarkupId(true));
            add(contentDataviewContainer.setOutputMarkupId(true));
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(productContentViewOrEditPanel.add(productContentViewOrEditPanel.new ProductContentEditFragement()).setOutputMarkupId(true));
            add(new TableBehavior());
            super.onInitialize();
         }
      };
      contentViewTable = new WebMarkupContainer("contentViewTable", getDefaultModel()) {

         private static final long serialVersionUID = 4858719401860781077L;

         @Override
         protected void onInitialize() {
            contentDataview.setRemoveAjaxLinkVisable(false);
            add(contentPagingNavigator.setOutputMarkupId(true));
            add(contentDataviewContainer.setOutputMarkupId(true));
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(productContentViewOrEditPanel.add(productContentViewOrEditPanel.new ProductContentViewFragement()).setOutputMarkupId(true));
            add(new TableBehavior());
            super.onInitialize();
         }
      };
      productContentViewOrEditPanel = new ProductContentViewOrEditPanel("contentViewOrEditPanel", Model.of(new Content()), contentEditTable);
   }
}
