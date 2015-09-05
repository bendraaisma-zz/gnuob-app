package com.netbrasoft.gnuob.application.category;

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
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.WellBehavior.Size;
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
public class SubCategoryContentPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 9191172039973638020L;

      public AddAjaxLink() {
         super("add", Model.of(SubCategoryContentPanel.this.getString("addMessage")), Buttons.Type.Primary, Model.of(SubCategoryContentPanel.this.getString("addMessage")));
         setIconType(GlyphIconType.plus);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         subCategoryContentViewOrEditPanel.setDefaultModelObject(new Content());
         target.add(subCategoryContentViewOrEditPanel.setOutputMarkupId(true));
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ContentDataview extends DataView<Content> {

      private static final long serialVersionUID = 2246346365193989354L;

      private boolean removeAjaxLinkVisable;

      private long selectedObjectId;

      protected ContentDataview() {
         super("contentDataview", new ContentListDataProvider(), ITEMS_PER_PAGE);
      }

      public boolean isRemoveAjaxLinkVisable() {
         return removeAjaxLinkVisable;
      }

      @Override
      protected Item<Content> newItem(String id, int index, IModel<Content> model) {
         final Item<Content> item = super.newItem(id, index, model);
         final long modelObjectId = ((Content) subCategoryContentViewOrEditPanel.getDefaultModelObject()).getId();

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
         if (selectedObjectId != ((SubCategory) SubCategoryContentPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((SubCategory) SubCategoryContentPanel.this.getDefaultModelObject()).getId();
            subCategoryContentViewOrEditPanel.setDefaultModelObject(new Content());
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
               subCategoryContentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
               target.add(contentDataviewContainer.setOutputMarkupId(true));
               target.add(subCategoryContentViewOrEditPanel.setOutputMarkupId(true));
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

         if (item.getIndex() == 0 && ((Content) subCategoryContentViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            subCategoryContentViewOrEditPanel.setDefaultModelObject(item.getModelObject());
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
         return ((SubCategory) SubCategoryContentPanel.this.getDefaultModelObject()).getContents();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<Content> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink(final IModel<Content> model) {
         super("remove", model, Buttons.Type.Default, Model.of(SubCategoryContentPanel.this.getString("removeMessage")));
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

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SubCategoryContentEditFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public SubCategoryContentEditFragement() {
         super("subCategoryContentViewOrEditFragement", "subCategoryContentEditFragement", SubCategoryContentPanel.this, SubCategoryContentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentEditTable.setOutputMarkupId(true));
         add(new WellBehavior(Size.Small));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class SubCategoryContentViewFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public SubCategoryContentViewFragement() {
         super("subCategoryContentViewOrEditFragement", "subCategoryContentViewFragement", SubCategoryContentPanel.this, SubCategoryContentPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(contentViewTable.setOutputMarkupId(true));
         add(new WellBehavior(Size.Small));
         super.onInitialize();
      }
   }

   private static final long serialVersionUID = 180343040391839545L;

   private static final long ITEMS_PER_PAGE = 10;

   private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryContentPanel.class);

   private final WebMarkupContainer contentDataviewContainer;

   private final ContentDataview contentDataview;

   private final BootstrapPagingNavigator contentPagingNavigator;

   private final ContentListDataProvider contentListDataProvider;

   private final WebMarkupContainer contentEditTable;

   private final WebMarkupContainer contentViewTable;

   private final SubCategoryContentViewOrEditPanel subCategoryContentViewOrEditPanel;

   public SubCategoryContentPanel(final String id, final IModel<SubCategory> model) {
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
            add(subCategoryContentViewOrEditPanel.add(subCategoryContentViewOrEditPanel.new SubCategoryContentEditFragement()).setOutputMarkupId(true));
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
            add(subCategoryContentViewOrEditPanel.add(subCategoryContentViewOrEditPanel.new SubCategoryContentViewFragement()).setOutputMarkupId(true));
            add(new TableBehavior());
            super.onInitialize();
         }
      };
      subCategoryContentViewOrEditPanel = new SubCategoryContentViewOrEditPanel("contentViewOrEditPanel", Model.of(new Content()), contentEditTable);
   }
}
