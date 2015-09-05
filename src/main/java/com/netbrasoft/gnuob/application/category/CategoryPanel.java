package com.netbrasoft.gnuob.application.category;

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

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Contract;
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

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class CategoryPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = -8317730269644885290L;

      public AddAjaxLink() {
         super("add", Model.of(CategoryPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(CategoryPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         categoryViewOrEditPanel.setDefaultModelObject(new Category());
         target.add(categoryViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class CategoryDataview extends DataView<Category> {

      private static final long serialVersionUID = -5039874949058607907L;

      private long selectedObjectId;

      protected CategoryDataview() {
         super("categoryDataview", categoryDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Category> newItem(String id, int index, IModel<Category> model) {
         final Item<Category> item = super.newItem(id, index, model);
         final long modelObjectId = ((Category) categoryViewOrEditPanel.getDefaultModelObject()).getId();

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
         if (selectedObjectId != ((Category) CategoryPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Category) CategoryPanel.this.getDefaultModelObject()).getId();
         }
         super.onConfigure();
      }

      @Override
      protected void populateItem(Item<Category> item) {
         item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
         item.add(new Label("name"));
         item.add(new Label("position"));
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               categoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(categoryDataviewContainer.setOutputMarkupId(true));
               target.add(categoryViewOrEditPanel.setOutputMarkupId(true));
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

         if (item.getIndex() == 0 && ((Category) categoryViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            categoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Category> {

      private static final long serialVersionUID = -8317730269644885290L;

      public RemoveAjaxLink(final IModel<Category> model) {
         super("remove", model, Buttons.Type.Default, Model.of(CategoryPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            getModelObject().setActive(false);
            categoryDataProvider.merge(getModelObject());
            categoryViewOrEditPanel.setDefaultModelObject(new Contract());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            categoryTableContainer.warn(e.getLocalizedMessage());
         } finally {
            target.add(categoryTableContainer.setOutputMarkupId(true));
         }
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   private static final Logger LOGGER = LoggerFactory.getLogger(CategoryPanel.class);

   private final CategoryDataview categoryDataview = new CategoryDataview();

   @SpringBean(name = "CategoryDataProvider", required = true)
   private GenericTypeDataProvider<Category> categoryDataProvider;

   private final OrderByBorder<String> orderByposition;

   private final OrderByBorder<String> orderByName;

   private final WebMarkupContainer categoryDataviewContainer;

   private final WebMarkupContainer categoryPanelContainer;

   private final WebMarkupContainer categoryTableContainer;

   private final BootstrapPagingNavigator categoryPagingNavigator;

   private final CategoryViewOrEditPanel categoryViewOrEditPanel;

   public CategoryPanel(final String id, final IModel<Category> model) {
      super(id, model);

      orderByposition = new OrderByBorder<String>("orderByPosition", "position", categoryDataProvider);
      orderByName = new OrderByBorder<String>("orderByName", "name", categoryDataProvider);
      categoryPagingNavigator = new BootstrapPagingNavigator("categoryPagingNavigator", categoryDataview);
      categoryDataviewContainer = new WebMarkupContainer("categoryDataviewContainer") {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(categoryDataview);
            super.onInitialize();
         }
      };
      categoryTableContainer = new WebMarkupContainer("categoryTableContainer", getDefaultModel()) {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(new AddAjaxLink());
            add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
            add(orderByposition);
            add(orderByName);
            add(categoryDataviewContainer.setOutputMarkupId(true));
            add(categoryPagingNavigator);
            add(new TableBehavior().hover());
            super.onInitialize();
         }
      };
      categoryPanelContainer = new WebMarkupContainer("categoryPanelContainer", getDefaultModel()) {

         private static final long serialVersionUID = -497527332092449028L;

         @Override
         protected void onInitialize() {
            add(categoryTableContainer.setOutputMarkupId(true));
            add(categoryViewOrEditPanel.add(categoryViewOrEditPanel.new CategoryViewFragement()).setOutputMarkupId(true));
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
      categoryViewOrEditPanel = new CategoryViewOrEditPanel("categoryViewOrEditPanel", (IModel<Category>) getDefaultModel()) {

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
      categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      categoryDataProvider.setType(new Category());
      categoryDataProvider.getType().setActive(true);
      add(categoryPanelContainer.setOutputMarkupId(true));
      super.onInitialize();
   }
}
