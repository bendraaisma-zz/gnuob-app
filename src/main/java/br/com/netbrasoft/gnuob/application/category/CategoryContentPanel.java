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

package br.com.netbrasoft.gnuob.application.category;

import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiConstants.CATEGORY_DATA_PROVIDER_NAME;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.ADD_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_EDIT_MODAL_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_TABLE_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_VIEW_PANEL_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CLASS_ATTRIBUTE;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CLICK_EVENT;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRMATION_FUNCTION_NAME;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRMATION_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONFIRM_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_DATAVIEW_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_DATAVIEW_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_PAGING_NAVIGATOR_MARKUP_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.EDIT_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FORMAT_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.INFO_VALUE;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.REMOVE_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.REMOVE_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;
import static de.agilecoders.wicket.jquery.JQuery.$;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.netbrasoft.gnuob.api.Category;
import br.com.netbrasoft.gnuob.api.Content;
import br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import br.com.netbrasoft.gnuob.application.security.AppRoles;

import br.com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal.Backdrop;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal.Size;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.confirmation.ConfirmationConfig;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategoryContentPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CategoryContentContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategoryContentTableContainer extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<Content> {

        private static final long serialVersionUID = -8317730269644885290L;

        public AddAjaxLink(final String id, final IModel<Content> model, final Buttons.Type type,
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
        protected void onConfigure() {
          if (isCategorySelected()) {
            setVisable();
          } else {
            setInvisable();
          }
          super.onConfigure();
        }

        private boolean isCategorySelected() {
          return ((Category) CategoryContentTableContainer.this.getDefaultModelObject()).getId() > 0;
        }

        private void setVisable() {
          AddAjaxLink.this.setVisible(true);
        }

        private void setInvisable() {
          AddAjaxLink.this.setVisible(false);
        }

        @Override
        public void onClick(final AjaxRequestTarget target) {
          categoryContentEditModal
              .setParentModel((IModel<Category>) CategoryContentTableContainer.this.getDefaultModel());
          target.add(getCategoryContentEditModelComponent());
        }

        private Component getCategoryContentEditModelComponent() {
          return categoryContentEditModal.show(true).setDefaultModelObject(AddAjaxLink.this.getDefaultModelObject())
              .setOutputMarkupId(true);
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class ContentDataviewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class ContentDataview extends DataView<Content> {

          @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
          class RemoveAjaxLink extends BootstrapAjaxLink<Content> {

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

            public RemoveAjaxLink(final String id, final IModel<Content> model, final Buttons.Type type,
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
                warn(e.getLocalizedMessage());
              } finally {
                target.add(categoryContentContainer.setOutputMarkupId(true));
              }
            }

            private void removeCategory(Category category) {
              categoryDataProvider.remove(category);
            }
          }

          private static final long serialVersionUID = 2246346365193989354L;

          protected ContentDataview(final String id, final IDataProvider<Content> dataProvider,
              final long itemsPerPage) {
            super(id, dataProvider, itemsPerPage);
          }

          @Override
          protected Item<Content> newItem(final String id, final int index, final IModel<Content> model) {
            final Item<Content> item = super.newItem(id, index, model);
            if (isSelected(model)) {
              item.add(getSelectAttributeModifier());
            } else {
              item.add(getUnSelectAttributeModifier());
            }
            return item;
          }

          private boolean isSelected(final IModel<Content> model) {
            return ((IModel<Content>) categoryContentViewPanel.getDefaultModel()).getObject().getId() == model
                .getObject().getId();
          }

          private AttributeModifier getSelectAttributeModifier() {
            return new AttributeModifier(CLASS_ATTRIBUTE, INFO_VALUE);
          }

          private AttributeModifier getUnSelectAttributeModifier() {
            return new AttributeModifier(CLASS_ATTRIBUTE, StringUtils.EMPTY);
          }

          @Override
          protected void populateItem(final Item<Content> item) {
            final IModel<Content> compound = new CompoundPropertyModel<>(item.getModelObject());
            item.setModel(compound);
            item.add(getNameLabel());
            item.add(getFormatLabel());
            item.add(getRemoveLink(item.getModel()));
            item.add(getEditLink(item.getModel()));
            item.add(getItemBehavior(item.getModel()));
          }

          private Label getNameLabel() {
            return new Label(NAME_ID);
          }

          private Label getFormatLabel() {
            return new Label(FORMAT_ID);
          }

          private AddAjaxLink getEditLink(final IModel<Content> model) {
            return new AddAjaxLink(EDIT_ID, model, Buttons.Type.Default,
                Model.of(CategoryContentPanel.this.getString(EDIT_MESSAGE_KEY)));
          }

          private RemoveAjaxLink getRemoveLink(final IModel<Content> model) {
            return new RemoveAjaxLink(REMOVE_ID, model, Buttons.Type.Default,
                Model.of(CategoryContentPanel.this.getString(REMOVE_MESSAGE_KEY)));
          }

          private AjaxEventBehavior getItemBehavior(final IModel<Content> model) {
            return new AjaxEventBehavior(CLICK_EVENT) {

              private static final long serialVersionUID = 1L;

              @Override
              public void onEvent(final AjaxRequestTarget target) {
                categoryContentViewPanel.setDefaultModelObject(model.getObject());
                target.add(getContentDataviewContainerComponent());
                target.add(getContentViewPanelComponent());
              }

              private Component getContentDataviewContainerComponent() {
                return ContentDataviewContainer.this.setOutputMarkupId(true);
              }

              private Component getContentViewPanelComponent() {
                return categoryContentViewPanel.setOutputMarkupId(true);
              }
            };
          }
        }

        private static final long serialVersionUID = 9165996901588092749L;
        private final ContentDataview contentDataview;
        private final ListDataProvider<Content> contentListDataProvider;

        public ContentDataviewContainer(final String id, final IModel<Category> model) {
          super(id, model);
          contentListDataProvider = getListDataProvider();
          contentDataview = getContentDataview();
        }

        private ListDataProvider<Content> getListDataProvider() {
          return new ListDataProvider<Content>() {

            private static final long serialVersionUID = -3261859241046697057L;

            @Override
            protected List<Content> getData() {
              return getCategory().getContents();
            }

            private Category getCategory() {
              return (Category) ContentDataviewContainer.this.getDefaultModelObject();
            }
          };
        }

        private ContentDataview getContentDataview() {
          return new ContentDataview(CONTENT_DATAVIEW_ID, contentListDataProvider, ITEMS_PER_PAGE);
        }

        @Override
        protected void onInitialize() {
          add(getContentDataViewComponent());
          super.onInitialize();
        }

        private Component getContentDataViewComponent() {
          return contentDataview.setOutputMarkupId(true);
        }
      }

      private static final long serialVersionUID = -615589482625248433L;
      private final AddAjaxLink addLink;
      private final ContentDataviewContainer contentDataviewContainer;
      private final BootstrapPagingNavigator contentPagingNavigator;
      private final CategoryContentViewPanel categoryContentViewPanel;
      private final CategoryContentEditModal categoryContentEditModal;

      public CategoryContentTableContainer(final String id, final IModel<Category> model) {
        super(id, model);
        addLink = getAddLink();
        contentDataviewContainer = getContentDataviewContainer();
        contentPagingNavigator = getContentPagingNavigator();
        categoryContentViewPanel = getCategoryContentViewPanel();
        categoryContentEditModal = getCategoryContentEditModal();
      }

      private AddAjaxLink getAddLink() {
        return new AddAjaxLink(ADD_ID, Model.of(new Content()), Buttons.Type.Primary,
            Model.of(CategoryContentTableContainer.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
      }

      private ContentDataviewContainer getContentDataviewContainer() {
        return new ContentDataviewContainer(CONTENT_DATAVIEW_CONTAINER_ID,
            (IModel<Category>) CategoryContentTableContainer.this.getDefaultModel());
      }

      private BootstrapPagingNavigator getContentPagingNavigator() {
        return new BootstrapPagingNavigator(CONTENT_PAGING_NAVIGATOR_MARKUP_ID,
            contentDataviewContainer.contentDataview);
      }

      private CategoryContentViewPanel getCategoryContentViewPanel() {
        return new CategoryContentViewPanel(CATEGORY_CONTENT_VIEW_PANEL_ID, Model.of(new Content()));
      }

      private CategoryContentEditModal getCategoryContentEditModal() {
        return new CategoryContentEditModal(CATEGORY_CONTENT_EDIT_MODAL_ID, Model.of(new Content()));
      }

      @Override
      protected void onInitialize() {
        super.onInitialize();
        add(getAddLinkComponent());
        add(getContentDataviewContainerComponent());
        add(getContentPagingNavigatorComponent());
        add(getCategoryContentViewPanelComponent());
        add(getCategoryContentEditModaComponent());
      }

      private Component getAddLinkComponent() {
        return addLink.setOutputMarkupId(true);
      }

      private Component getContentDataviewContainerComponent() {
        return contentDataviewContainer.setOutputMarkupId(true);
      }

      private Component getContentPagingNavigatorComponent() {
        return contentPagingNavigator.setOutputMarkupId(true);
      }

      private Component getCategoryContentViewPanelComponent() {
        return categoryContentViewPanel.setOutputMarkupId(true);
      }

      private Component getCategoryContentEditModaComponent() {
        return categoryContentEditModal.setUseCloseHandler(true).setFadeIn(true).setUseKeyboard(true).size(Size.Large)
            .setBackdrop(Backdrop.STATIC).setCloseOnEscapeKey(false).setOutputMarkupId(true);
      }
    }

    private static final long serialVersionUID = 1920723644304058353L;
    private final CategoryContentTableContainer categoryContentTableContainer;

    public CategoryContentContainer(String id, IModel<?> model) {
      super(id, model);
      categoryContentTableContainer = getCategoryContentTableContainer();
    }

    private CategoryContentTableContainer getCategoryContentTableContainer() {
      return new CategoryContentTableContainer(CATEGORY_CONTENT_TABLE_CONTAINER_ID,
          (IModel<Category>) CategoryContentContainer.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategoryContentTableComponent());
    }

    private Component getCategoryContentTableComponent() {
      return categoryContentTableContainer.add(new TableBehavior().hover()).setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = 180343040391839545L;
  private static final int ITEMS_PER_PAGE = 10;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryContentPanel.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private final CategoryContentContainer categoryContentContainer;

  public CategoryContentPanel(final String id, final IModel<Category> model) {
    super(id, model);
    categoryContentContainer = getCategoryContentViewContainer();
  }

  private CategoryContentContainer getCategoryContentViewContainer() {
    return new CategoryContentContainer(CATEGORY_CONTENT_CONTAINER_ID, CategoryContentPanel.this.getDefaultModel());
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
    add(getCategoryContentViewContainerComponent());
  }

  private Component getCategoryContentViewContainerComponent() {
    return categoryContentContainer.setOutputMarkupId(true);
  }
}
