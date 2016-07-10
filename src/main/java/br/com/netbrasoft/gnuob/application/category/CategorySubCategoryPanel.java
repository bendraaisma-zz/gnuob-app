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

import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.ADD_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_EDIT_MODAL_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_TABLE_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SUB_CATEGORY_DATAVIEW_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SUB_CATEGORY_DATA_VIEW_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import br.com.netbrasoft.gnuob.api.Category;
import br.com.netbrasoft.gnuob.api.SubCategory;
import br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import br.com.netbrasoft.gnuob.application.category.table.SubCategoryTableTree;
import br.com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategorySubCategoryPanel extends Panel {

  class CategorySubCategoryContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategorySubCategoryTableContainer extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class AddAjaxLink extends BootstrapAjaxLink<SubCategory> {

        private static final long serialVersionUID = -8317730269644885290L;

        public AddAjaxLink(final String id, final IModel<SubCategory> model, final Buttons.Type type,
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
            makeAddLinkVisable();
          } else {
            makeAddLinkInvisable();
          }
          super.onConfigure();
        }

        private boolean isCategorySelected() {
          return ((Category) CategorySubCategoryTableContainer.this.getDefaultModelObject()).getId() > 0;
        }

        private void makeAddLinkVisable() {
          AddAjaxLink.this.setVisible(true);
        }

        private void makeAddLinkInvisable() {
          AddAjaxLink.this.setVisible(false);
        }

        @Override
        public void onClick(final AjaxRequestTarget target) {
          categorySubCategoryEditModal
              .setSelectedCategory((IModel<Category>) CategorySubCategoryTableContainer.this.getDefaultModel());
          target.add(getCategoryContentEditModelComponent());
        }

        private Component getCategoryContentEditModelComponent() {
          return categorySubCategoryEditModal.show(true).setDefaultModelObject(AddAjaxLink.this.getDefaultModelObject())
              .setOutputMarkupId(true);
        }
      }

      @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
      class SubCategoryDataViewContainer extends WebMarkupContainer {

        @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
        class SubCategoryDataview extends SubCategoryTableTree {

          private static final long serialVersionUID = -448497372708880312L;
          private final CategorySubCategoryViewPanel categorySubCategoryViewPanel;

          public SubCategoryDataview(String id, IModel<Category> model) {
            super(id, model);
            categorySubCategoryViewPanel = createCategorySubCategoryViewPanel();
          }

          private CategorySubCategoryViewPanel createCategorySubCategoryViewPanel() {
            return new CategorySubCategoryViewPanel(getSubCategoryModel(), getSubCategoryDataTable());
          }

          private DataTable<SubCategory, String> getSubCategoryDataTable() {
            return SubCategoryDataview.this.getTable();
          }

          private Model<SubCategory> getSubCategoryModel() {
            return Model.of(new SubCategory());
          }

          @Override
          protected void onInitialize() {
            super.onInitialize();
            getSubCategoryDataTable().addBottomToolbar(getCategorySubCategoryViewPanelAbstractToolbar());
          }

          private AbstractToolbar getCategorySubCategoryViewPanelAbstractToolbar() {
            return (AbstractToolbar) categorySubCategoryViewPanel.setOutputMarkupId(true);
          }
        }

        private static final long serialVersionUID = -4242252883591333646L;
        private final SubCategoryDataview subCategoryDataview;

        public SubCategoryDataViewContainer(String id, IModel<Category> model) {
          super(id, model);
          subCategoryDataview = createSubCategoryDataview();
        }

        private SubCategoryDataview createSubCategoryDataview() {
          return new SubCategoryDataview(SUB_CATEGORY_DATAVIEW_ID,
              (IModel<Category>) SubCategoryDataViewContainer.this.getDefaultModel());
        }

        @Override
        protected void onInitialize() {
          super.onInitialize();
          add(getSubCategoryDataviewComponent());
        }

        private Component getSubCategoryDataviewComponent() {
          return subCategoryDataview.setOutputMarkupId(true);
        }
      }

      private static final long serialVersionUID = -1998732261084486464L;
      private final AddAjaxLink addLink;
      private final SubCategoryDataViewContainer subCategoryDataViewContainer;
      private final CategorySubCategoryEditModal categorySubCategoryEditModal;

      public CategorySubCategoryTableContainer(String id, IModel<Category> model) {
        super(id, model);
        addLink = createAddLink();
        subCategoryDataViewContainer = createSubCategoryDataViewContainer();
        categorySubCategoryEditModal = createCategorySubCategoryEditModel();
      }

      private AddAjaxLink createAddLink() {
        return new AddAjaxLink(ADD_ID, getSubCategoryModel(), Buttons.Type.Primary,
            Model.of(CategorySubCategoryTableContainer.this.getString(NetbrasoftApplicationConstants.ADD_MESSAGE_KEY)));
      }

      private Model<SubCategory> getSubCategoryModel() {
        return Model.of(new SubCategory());
      }

      private SubCategoryDataViewContainer createSubCategoryDataViewContainer() {
        return new SubCategoryDataViewContainer(SUB_CATEGORY_DATA_VIEW_CONTAINER_ID, getCategoryModel());
      }

      private IModel<Category> getCategoryModel() {
        return (IModel<Category>) CategorySubCategoryTableContainer.this.getDefaultModel();
      }

      private CategorySubCategoryEditModal createCategorySubCategoryEditModel() {
        return new CategorySubCategoryEditModal(CATEGORY_SUB_CATEGORY_EDIT_MODAL_ID, getSubCategoryModel());
      }

      @Override
      protected void onInitialize() {
        super.onInitialize();
        add(getAddLinkComponent());
        add(getSubCategoryDataViewContainerComponent());
        add(getCategorySubCategoryEditModelComponent());
      }

      private Component getAddLinkComponent() {
        return addLink.setOutputMarkupId(true);
      }

      private Component getSubCategoryDataViewContainerComponent() {
        return subCategoryDataViewContainer.setOutputMarkupId(true);
      }

      private Component getCategorySubCategoryEditModelComponent() {
        return categorySubCategoryEditModal.setOutputMarkupId(true);
      }
    }

    private static final long serialVersionUID = 8808758051427562232L;
    private final CategorySubCategoryTableContainer categorySubCategoryTableContainer;

    public CategorySubCategoryContainer(String id, IModel<Category> model) {
      super(id, model);
      categorySubCategoryTableContainer = createCategorySubCategoryTableContainer();
    }

    private CategorySubCategoryTableContainer createCategorySubCategoryTableContainer() {
      return new CategorySubCategoryTableContainer(CATEGORY_SUB_CATEGORY_TABLE_CONTAINER_ID,
          (IModel<Category>) CategorySubCategoryContainer.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategorySubCategoryTableContainerComponent());
    }

    private Component getCategorySubCategoryTableContainerComponent() {
      return categorySubCategoryTableContainer.setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = -6032220714679141094L;
  private final CategorySubCategoryContainer categorySubCategoryContainer;

  public CategorySubCategoryPanel(String id, IModel<Category> model) {
    super(id, model);
    categorySubCategoryContainer = createCategorySubCategoryContainer();
  }

  private CategorySubCategoryContainer createCategorySubCategoryContainer() {
    return new CategorySubCategoryContainer(CATEGORY_SUB_CATEGORY_CONTAINER_ID,
        (IModel<Category>) CategorySubCategoryPanel.this.getDefaultModel());
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(getCategorySubCategoryContainerComponent());
  }

  private Component getCategorySubCategoryContainerComponent() {
    return categorySubCategoryContainer.setOutputMarkupId(true);
  }
}
