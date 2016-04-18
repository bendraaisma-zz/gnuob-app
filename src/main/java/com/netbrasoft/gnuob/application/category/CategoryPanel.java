/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.application.category;

import static com.netbrasoft.gnuob.api.generic.NetbrasoftApiConstants.CATEGORY_DATA_PROVIDER_NAME;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.ADD_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_DATAVIEW_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_DATAVIEW_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_EDIT_MODAL_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_PAGING_NAVIGATOR_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_PANEL_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_TABLE_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_VIEW_PANEL_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CLASS_ATTRIBUTE;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CLICK_EVENT;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRMATION_FUNCTION_NAME;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRMATION_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRM_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.EDIT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FEEDBACK_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.INFO_VALUE;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_PROPERTY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.ORDER_BY_NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.ORDER_BY_POSITION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.POSITION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.POSITION_PROPERTY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.REMOVE_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.REMOVE_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;
import static de.agilecoders.wicket.jquery.JQuery.$;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal.Backdrop;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.layout.col.MediumSpanType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategoryPanel extends Panel {

  class CategoryPanelBootstrapBehavior extends BootstrapBaseBehavior {
    private static final long serialVersionUID = -4903722864597601489L;

    @Override
    public void onComponentTag(final Component component, final ComponentTag tag) {
      Attributes.addClass(tag, MediumSpanType.SPAN10);
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CategoryPanelContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategoryTableContainer extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<Category> {

        private static final long serialVersionUID = -8317730269644885290L;

        public AddAjaxLink(final String id, final IModel<Category> model, final Buttons.Type type,
            final IModel<String> labelModel) {
          super(id, model, type, labelModel);
        }

        @Override
        protected void onInitialize() {
          super.onInitialize();
          setIconType(GlyphIconType.plus);
          setSize(Buttons.Size.Mini);
        }

        @Override
        public void onClick(final AjaxRequestTarget target) {
          target.add(getCategoryEditModelComponent());
        }

        private Component getCategoryEditModelComponent() {
          return categoryEditModal.show(true).setDefaultModelObject(AddAjaxLink.this.getDefaultModelObject())
              .setOutputMarkupId(true);
        }
      }

      @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class CategoryDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class CategoryDataview extends DataView<Category> {

          @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
          class RemoveAjaxLink extends BootstrapAjaxLink<Category> {

            class RemoveAjaxConfirmationBehavior extends ConfirmationBehavior {

              private static final long serialVersionUID = 3400966286682633771L;

              @Override
              public void renderHead(final Component component, final IHeaderResponse response) {
                response.render($(component).chain(CONFIRMATION_FUNCTION_NAME,
                    new ConfirmationConfig().withTitle(getString(CONFIRMATION_MESSAGE_KEY)).withSingleton(true)
                        .withPopout(true).withBtnOkLabel(getString(CONFIRM_MESSAGE_KEY))
                        .withBtnCancelLabel(getString(CANCEL_MESSAGE_KEY)))
                    .asDomReadyScript());
              }
            }

            private static final long serialVersionUID = -8317730269644885290L;

            public RemoveAjaxLink(final String id, final IModel<Category> model, final Buttons.Type type,
                final IModel<String> labelModel) {
              super(id, model, type, labelModel);
            }

            @Override
            protected void onInitialize() {
              super.onInitialize();
              setIconType(GlyphIconType.remove);
              setSize(Buttons.Size.Mini);
              add(getRemoveConfirmationBehavior());
            }

            private RemoveAjaxConfirmationBehavior getRemoveConfirmationBehavior() {
              return new RemoveAjaxConfirmationBehavior();
            }

            @Override
            public void onClick(final AjaxRequestTarget target) {
              try {
                removeCategory((Category) RemoveAjaxLink.this.getDefaultModelObject());
              } catch (final RuntimeException e) {
                LOGGER.warn(e.getMessage(), e);
                feedbackPanel.warn(e.getLocalizedMessage());
                target.add(feedbackPanel.setOutputMarkupId(true));
              } finally {
                target.add(categoryPanelContainer.setOutputMarkupId(true));
              }
            }

            private void removeCategory(Category category) {
              categoryDataProvider.remove(category);
            }
          }

          private static final long serialVersionUID = -5039874949058607907L;

          protected CategoryDataview(final String id, final IDataProvider<Category> dataProvider,
              final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Category> newItem(final String id, final int index, final IModel<Category> model) {
            final Item<Category> item = super.newItem(id, index, model);
            if (isSelected(model)) {
              item.add(getSelectAttributeModifier());
            } else {
              item.add(getUnSelectAttributeModifier());
            }
            return item;
          }

          private boolean isSelected(final IModel<Category> model) {
            return ((IModel<Category>) CategoryDataviewContainer.this.getDefaultModel()).getObject().getId() == model
                .getObject().getId();
          }

          private AttributeModifier getSelectAttributeModifier() {
            return new AttributeModifier(CLASS_ATTRIBUTE, INFO_VALUE);
          }

          private AttributeModifier getUnSelectAttributeModifier() {
            return new AttributeModifier(CLASS_ATTRIBUTE, StringUtils.EMPTY);
          }

          @Override
          protected void populateItem(final Item<Category> item) {
            item.setModel(new CompoundPropertyModel<Category>(item.getModelObject()));
            item.add(getNameLabel());
            item.add(getPositionLabel());
            item.add(getEventBehavior(item.getModel()));
            item.add(getEditLink(item.getModel()));
            item.add(getRemoveLink(item.getModel()));
          }

          private Label getNameLabel() {
            return new Label(NAME_ID);
          }

          private Label getPositionLabel() {
            return new Label(POSITION_ID);
          }

          private AddAjaxLink getEditLink(final IModel<Category> model) {
            return new AddAjaxLink(EDIT_ID, model, Buttons.Type.Default,
                Model.of(CategoryPanel.this.getString(EDIT_MESSAGE_KEY)));
          }

          private RemoveAjaxLink getRemoveLink(final IModel<Category> model) {
            return new RemoveAjaxLink(REMOVE_ID, model, Buttons.Type.Default,
                Model.of(CategoryPanel.this.getString(REMOVE_MESSAGE_KEY)));
          }

          private AjaxEventBehavior getEventBehavior(final IModel<Category> model) {
            return new AjaxEventBehavior(CLICK_EVENT) {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(final AjaxRequestTarget target) {
                CategoryDataviewContainer.this.setDefaultModelObject(model.getObject());
                target.add(getCategoryDataviewContainerComponent());
                target.add(getCategoryViewPanelComponent());
              }

              private Component getCategoryDataviewContainerComponent() {
                return CategoryDataviewContainer.this.setOutputMarkupId(true);
              }

              private Component getCategoryViewPanelComponent() {
                return categoryViewPanel.setOutputMarkupId(true);
              }
            };
          }
        }

        private static final long serialVersionUID = 4066874119655951656L;
        private final CategoryDataview categoryDataview;

        public CategoryDataviewContainer(final String id, final IModel<Category> model) {
          super(id, model);
          categoryDataview = getCategoryDataview();
        }

        private CategoryDataview getCategoryDataview() {
          return new CategoryDataview(CATEGORY_DATAVIEW_ID, categoryDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(getCategoryDataviewComponent());
          super.onInitialize();
        }

        private Component getCategoryDataviewComponent() {
          return categoryDataview.setOutputMarkupId(true);
        }
      }

      private static final long serialVersionUID = -6196639251081246943L;

      private final NotificationPanel feedbackPanel;
      private final AddAjaxLink addLink;
      private final OrderByBorder<String> orderByPosition;
      private final OrderByBorder<String> orderByName;
      private final CategoryDataviewContainer categoryDataviewContainer;
      private final BootstrapPagingNavigator categoryPagingNavigator;

      public CategoryTableContainer(final String id, final IModel<Category> model) {
        super(id, model);
        feedbackPanel = getFeedbackPanel();
        addLink = getAddLink();
        orderByPosition = getOrderByPosition();
        orderByName = getOrderByName();
        categoryDataviewContainer = getCategoryDataViewContainer();
        categoryPagingNavigator = getCategoryPaginingNavigator();
      }

      private NotificationPanel getFeedbackPanel() {
        return new NotificationPanel(FEEDBACK_ID);
      }

      private AddAjaxLink getAddLink() {
        return new AddAjaxLink(ADD_ID, Model.of(new Category()), Buttons.Type.Primary,
            Model.of(CategoryTableContainer.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
      }

      private OrderByBorder<String> getOrderByPosition() {
        return new OrderByBorder<>(ORDER_BY_POSITION_ID, POSITION_PROPERTY, categoryDataProvider);
      }

      private OrderByBorder<String> getOrderByName() {
        return new OrderByBorder<>(ORDER_BY_NAME_ID, NAME_PROPERTY, categoryDataProvider);
      }

      private CategoryDataviewContainer getCategoryDataViewContainer() {
        return new CategoryDataviewContainer(CATEGORY_DATAVIEW_CONTAINER_ID,
            (IModel<Category>) CategoryTableContainer.this.getDefaultModel());
      }

      private BootstrapPagingNavigator getCategoryPaginingNavigator() {
        return new BootstrapPagingNavigator(CATEGORY_PAGING_NAVIGATOR_ID, categoryDataviewContainer.categoryDataview);
      }

      @Override
      protected void onInitialize() {
        super.onInitialize();
        add(getFeedbackPanelComponent());
        add(getAddLinkComponent());
        add(getOrderByPositionComponent());
        add(getOrderByNameComponent());
        add(getCategoryDataViewContainerComponent());
        add(getCategoryPagingNavigatorComponent());
      }

      private Component getFeedbackPanelComponent() {
        return feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true);
      }

      private Component getAddLinkComponent() {
        return addLink.setOutputMarkupId(true);
      }

      private Component getOrderByPositionComponent() {
        return orderByPosition.setOutputMarkupId(true);
      }

      private Component getOrderByNameComponent() {
        return orderByName.setOutputMarkupId(true);
      }

      private Component getCategoryDataViewContainerComponent() {
        return categoryDataviewContainer.setOutputMarkupId(true);
      }

      private Component getCategoryPagingNavigatorComponent() {
        return categoryPagingNavigator.setOutputMarkupId(true);
      }
    }

    private static final long serialVersionUID = 3737455694760798719L;
    private final CategoryViewPanel categoryViewPanel;
    private final CategoryTableContainer categoryTableContainer;
    private final CategoryEditModal categoryEditModal;

    public CategoryPanelContainer(final String id, final IModel<Category> model) {
      super(id, model);
      categoryTableContainer = getCategoryTableContainer();
      categoryViewPanel = getCategoryViewPanel();
      categoryEditModal = getCategoryEditModal();
    }

    private CategoryTableContainer getCategoryTableContainer() {
      return new CategoryTableContainer(CATEGORY_TABLE_CONTAINER_ID,
          (IModel<Category>) CategoryPanelContainer.this.getDefaultModel());
    }

    private CategoryViewPanel getCategoryViewPanel() {
      return new CategoryViewPanel(CATEGORY_VIEW_PANEL_ID,
          (IModel<Category>) CategoryPanelContainer.this.getDefaultModel());
    }

    private CategoryEditModal getCategoryEditModal() {
      return new CategoryEditModal(CATEGORY_EDIT_MODAL_ID, Model.of(new Category()));
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategoryTableContainerComponent());
      add(getCategoryViewPanelComponent());
      add(getCategoryEditModalComponent());
    }

    private Component getCategoryTableContainerComponent() {
      return categoryTableContainer.add(new TableBehavior().hover()).setOutputMarkupId(true);
    }

    private Component getCategoryViewPanelComponent() {
      return categoryViewPanel.setOutputMarkupId(true);
    }

    private Component getCategoryEditModalComponent() {
      return categoryEditModal.setUseCloseHandler(true).setFadeIn(true).setUseKeyboard(true).size(Size.Large)
          .setBackdrop(Backdrop.STATIC).setCloseOnEscapeKey(false).setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = 3703226064705246155L;
  private static final int ITEMS_PER_PAGE = 20;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryPanel.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private final CategoryPanelContainer categoryPanelContainer;

  public CategoryPanel(final String id, final IModel<Category> model) {
    super(id, model);
    categoryPanelContainer = getCategoryPanelContainer();
  }

  private CategoryPanelContainer getCategoryPanelContainer() {
    return new CategoryPanelContainer(CATEGORY_PANEL_CONTAINER_ID,
        (IModel<Category>) CategoryPanel.this.getDefaultModel());
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
    categoryDataProvider.setOrderBy(OrderBy.POSITION_A_Z);
    add(getCategoryPanelContainerComponent());
  }

  private Component getCategoryPanelContainerComponent() {
    return categoryPanelContainer.add(new CategoryPanelBootstrapBehavior()).setOutputMarkupId(true);
  }
}
