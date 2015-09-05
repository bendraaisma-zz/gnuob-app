package com.netbrasoft.gnuob.application.category;

import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.application.category.table.SubCategoryTableTree;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class SubCategoryPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends BootstrapAjaxLink<SubCategory> {

      private static final long serialVersionUID = -8317730269644885290L;

      public RemoveAjaxLink(final IModel<SubCategory> model) {
         super("remove", model, Buttons.Type.Default, Model.of(SubCategoryPanel.this.getString("removeMessage")));
         setIconType(GlyphIconType.remove);
         setSize(Buttons.Size.Mini);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         try {
            removeSubCategory(((Category) SubCategoryPanel.this.getDefaultModelObject()).getSubCategories(), (SubCategory) getDefaultModelObject());
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(subCategoriesDataViewContainer.setOutputMarkupId(true));
         }
      }

      private void removeSubCategory(List<SubCategory> subCategories, SubCategory subCategory) {
         if (subCategories.contains(subCategory)) {
            subCategories.remove(subCategory);
         } else {
            for (final SubCategory sub : subCategories) {
               removeSubCategory(sub.getSubCategories(), subCategory);
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   class RemoveSubCategoryPanel extends Panel {

      static final long serialVersionUID = 1136516888736878750L;

      public RemoveSubCategoryPanel(final String id, final IModel<SubCategory> model) {
         super(id, model);
      }

      @Override
      protected void onInitialize() {
         add(new RemoveAjaxLink((IModel<SubCategory>) getDefaultModel()).add(new ConfirmationBehavior() {

            private static final long serialVersionUID = 7744720444161839031L;

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
               response.render($(component)
                     .chain("confirmation", new ConfirmationConfig().withTitle(getString("confirmationTitleMessage")).withSingleton(true).withPopout(true).withBtnOkLabel(getString("confirmMessage")).withBtnCancelLabel(getString("cancelMessage")))
                     .asDomReadyScript());
            }
         }));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class SubCategoryDataview extends SubCategoryTableTree {

      private static final long serialVersionUID = 7493141095626794439L;

      private boolean removeAjaxLinkVisable;

      private long selectedObjectId;

      private Item<SubCategory> selectedItem;

      public SubCategoryDataview(List<? extends IColumn<SubCategory, String>> columns) {
         super("subCategoriesDataView", columns, subCategoryTreeProvider, Integer.MAX_VALUE);
      }

      public boolean isRemoveAjaxLinkVisable() {
         return removeAjaxLinkVisable;
      }

      @Override
      public Item<SubCategory> newItem(Item<SubCategory> item, IModel<SubCategory> model) {
         final long modelObjectId = ((SubCategory) subCategoryViewOrEditPanel.getDefaultModelObject()).getId();

         if ((model.getObject().getId() == modelObjectId) || modelObjectId == 0) {
            item.add(new BootstrapBaseBehavior() {

               private static final long serialVersionUID = -4903722864597601489L;

               @Override
               public void onComponentTag(Component component, ComponentTag tag) {
                  Attributes.addClass(tag, "info");
               }
            });
            selectedItem = item;
         }
         return item;
      }

      @Override
      protected void onConfigure() {
         if (selectedObjectId != ((Category) SubCategoryPanel.this.getDefaultModelObject()).getId()) {
            selectedObjectId = ((Category) SubCategoryPanel.this.getDefaultModelObject()).getId();
            subCategoryViewOrEditPanel.setDefaultModelObject(new SubCategory());
         }
         super.onConfigure();
      }

      @Override
      public void populateItem(Item<SubCategory> item) {
         item.add(new AjaxEventBehavior("click") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               selectedItem.add(new BootstrapBaseBehavior() {

                  private static final long serialVersionUID = -4903722864597601489L;

                  @Override
                  public void onComponentTag(Component component, ComponentTag tag) {
                     Attributes.removeClass(tag, "info");
                  }
               });
               selectedItem = item;
               item.add(new BootstrapBaseBehavior() {

                  private static final long serialVersionUID = -4903722864597601489L;

                  @Override
                  public void onComponentTag(Component component, ComponentTag tag) {
                     Attributes.addClass(tag, "info");
                  }
               });
               subCategoryViewOrEditPanel.setDefaultModelObject(item.getDefaultModelObject());
               target.add(subCategoriesDataViewContainer.setOutputMarkupId(true));
               target.add(subCategoryViewOrEditPanel.setOutputMarkupId(true));
            }
         });

         if (item.getIndex() == 0 && ((SubCategory) subCategoryViewOrEditPanel.getDefaultModelObject()).getId() == 0) {
            subCategoryViewOrEditPanel.setDefaultModelObject(item.getModelObject());
         }
      }

      public void setRemoveAjaxLinkVisable(boolean removeAjaxLinkVisable) {
         this.removeAjaxLinkVisable = removeAjaxLinkVisable;
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SubCategoryEditFragement extends Fragment {

      private static final long serialVersionUID = 3162058383568556008L;

      public SubCategoryEditFragement() {
         super("subCategoryViewOrEditFragement", "subCategoryEditFragement", SubCategoryPanel.this, SubCategoryPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(subCategoryEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class SubCategoryTreeProvider implements ITreeProvider<SubCategory> {
      private static final long serialVersionUID = -592161727647897932L;

      public SubCategoryTreeProvider() {
      }

      @Override
      public void detach() {
         return;
      }

      @Override
      public Iterator<? extends SubCategory> getChildren(SubCategory node) {
         return node.getSubCategories().iterator();
      }

      @Override
      public Iterator<? extends SubCategory> getRoots() {
         return ((Category) SubCategoryPanel.this.getDefaultModelObject()).getSubCategories().iterator();
      }

      @Override
      public boolean hasChildren(SubCategory node) {
         return !node.getSubCategories().isEmpty();
      }

      @Override
      public IModel<SubCategory> model(SubCategory object) {
         return Model.of(object);
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class SubCategoryViewFragement extends Fragment {

      private static final long serialVersionUID = 3162058383568556008L;

      public SubCategoryViewFragement() {
         super("subCategoryViewOrEditFragement", "subCategoryViewFragement", SubCategoryPanel.this, SubCategoryPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(subCategoryViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryPanel.class);

   private static final long serialVersionUID = 4492979061717676247L;

   private final SubCategoryViewOrEditPanel subCategoryViewOrEditPanel;

   private final WebMarkupContainer subCategoriesDataViewContainer;

   private final SubCategoryTreeProvider subCategoryTreeProvider;

   private final SubCategoryDataview subCategoryDataView;

   private final WebMarkupContainer subCategoryEditTable;

   private final WebMarkupContainer subCategoryViewTable;

   public SubCategoryPanel(final String id, final IModel<Category> model) {
      super(id, model);

      subCategoryTreeProvider = new SubCategoryTreeProvider();
      subCategoryDataView = new SubCategoryDataview(createColumns());
      subCategoriesDataViewContainer = new WebMarkupContainer("subCategoriesDataViewContainer", getDefaultModel()) {

         private static final long serialVersionUID = 586368973894377938L;

         @Override
         protected void onInitialize() {
            add(subCategoryDataView.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      subCategoryEditTable = new WebMarkupContainer("subCategoryEditTable", getDefaultModel()) {

         private static final long serialVersionUID = 1L;

         @Override
         protected void onInitialize() {
            subCategoryDataView.setRemoveAjaxLinkVisable(true);
            subCategoryDataView.getTable().addBottomToolbar((AbstractToolbar) subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryEditFragement()).setOutputMarkupId(true));
            add(subCategoriesDataViewContainer.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      subCategoryViewTable = new WebMarkupContainer("subCategoryViewTable", getDefaultModel()) {

         private static final long serialVersionUID = 1L;

         @Override
         protected void onInitialize() {
            subCategoryDataView.setRemoveAjaxLinkVisable(false);
            subCategoryDataView.getTable().addBottomToolbar((AbstractToolbar) subCategoryViewOrEditPanel.add(subCategoryViewOrEditPanel.new SubCategoryViewFragement()).setOutputMarkupId(true));
            add(subCategoriesDataViewContainer.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
      subCategoryViewOrEditPanel = new SubCategoryViewOrEditPanel(Model.of(new SubCategory()), subCategoryDataView.getTable(), subCategoryEditTable);
   }

   private List<IColumn<SubCategory, String>> createColumns() {
      final List<IColumn<SubCategory, String>> columns = new ArrayList<IColumn<SubCategory, String>>();

      columns.add(new TreeColumn<SubCategory, String>(Model.of(getString("nameMessage"))) {
         private static final long serialVersionUID = -8544017108974205690L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });

      columns.add(new PropertyColumn<SubCategory, String>(Model.of(getString("descriptionMessage")), "description") {
         private static final long serialVersionUID = -1013188144051609487L;

         @Override
         public String getCssClass() {
            return "small";
         }
      });

      columns.add(new AbstractColumn<SubCategory, String>(Model.of(getString("operationMessage"))) {
         private static final long serialVersionUID = 1L;

         @Override
         public String getCssClass() {
            return "small";
         }

         @Override
         public void populateItem(Item<ICellPopulator<SubCategory>> cellItem, String componentId, IModel<SubCategory> rowModel) {
            cellItem.add(new RemoveSubCategoryPanel(componentId, rowModel).setVisibilityAllowed(subCategoryDataView.isRemoveAjaxLinkVisable()).setOutputMarkupId(true));
         }
      });

      return columns;
   }
}
