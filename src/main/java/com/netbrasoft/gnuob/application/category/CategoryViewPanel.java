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

import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_PANEL_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_PANEL_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_VIEW_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_VIEW_FORM_COMPONENT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_VIEW_TABLE_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.DESCRIPTION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.POSITION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategoryViewPanel extends Panel {

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CategoryViewContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategoryViewTableContainer extends WebMarkupContainer {

      private static final long serialVersionUID = 8000374832364819124L;
      private final BootstrapForm<Category> categoryViewForm;
      private final CategoryContentPanel categoryContentPanel;
      private final CategorySubCategoryPanel categorySubCategoryPanel;

      public CategoryViewTableContainer(final String id, final IModel<Category> model) {
        super(id, model);
        categoryViewForm = createCategoryViewForm();
        categoryContentPanel = createCategoryContentPanel();
        categorySubCategoryPanel = createCategorySubCategoryPanel();
      }

      private BootstrapForm<Category> createCategoryViewForm() {
        return new BootstrapForm<>(CATEGORY_VIEW_FORM_COMPONENT_ID,
            new CompoundPropertyModel<Category>((IModel<Category>) CategoryViewTableContainer.this.getDefaultModel()));
      }

      private CategoryContentPanel createCategoryContentPanel() {
        return new CategoryContentPanel(CATEGORY_CONTENT_PANEL_ID,
            (IModel<Category>) CategoryViewTableContainer.this.getDefaultModel());
      }

      private CategorySubCategoryPanel createCategorySubCategoryPanel() {
        return new CategorySubCategoryPanel(CATEGORY_SUB_CATEGORY_PANEL_ID,
            (IModel<Category>) CategoryViewTableContainer.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
        add(getCategoryViewForm());
        super.onInitialize();
      }

      private Component getCategoryViewForm() {
        categoryViewForm.add(createPositionComponent());
        categoryViewForm.add(createNameComponent());
        categoryViewForm.add(createDescriptionComponent());
        categoryViewForm.add(getCategoryContentPanelComponent());
        categoryViewForm.add(getCategorySubCategoryComponent());
        return categoryViewForm.add(createFormBehavior()).setOutputMarkupId(true);
      }

      private Component createPositionComponent() {
        return new NumberTextField<Integer>(POSITION_ID).setOutputMarkupId(true);
      }

      private Component createNameComponent() {
        return new TextField<String>(NAME_ID).setOutputMarkupId(true);
      }

      private Component createDescriptionComponent() {
        return new TextArea<String>(DESCRIPTION_ID).setOutputMarkupId(true);
      }

      private Component getCategoryContentPanelComponent() {
        return categoryContentPanel.setOutputMarkupId(true);
      }

      private Component getCategorySubCategoryComponent() {
        return categorySubCategoryPanel.setOutputMarkupId(true);
      }

      private FormBehavior createFormBehavior() {
        return new FormBehavior(FormType.Horizontal);
      }
    }

    private static final long serialVersionUID = 8512847604990517862L;
    private final CategoryViewTableContainer categoryViewTableContainer;

    public CategoryViewContainer(String id, IModel<?> model) {
      super(id, model);
      categoryViewTableContainer = createCategoryViewTableContainer();
    }

    private CategoryViewTableContainer createCategoryViewTableContainer() {
      return new CategoryViewTableContainer(CATEGORY_VIEW_TABLE_CONTAINER_ID,
          (IModel<Category>) CategoryViewPanel.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategoryViewTableContainerComponent());
    }

    private Component getCategoryViewTableContainerComponent() {
      return categoryViewTableContainer.add(createTableBehavior()).setOutputMarkupId(true);
    }

    private TableBehavior createTableBehavior() {
      return new TableBehavior();
    }
  }

  private static final long serialVersionUID = 3968615764565588442L;
  private final CategoryViewContainer categoryViewContainer;

  public CategoryViewPanel(final String id, final IModel<Category> model) {
    super(id, model);
    categoryViewContainer = createCategoryViewContainer();
  }

  private CategoryViewContainer createCategoryViewContainer() {
    return new CategoryViewContainer(CATEGORY_VIEW_CONTAINER_ID, CategoryViewPanel.this.getDefaultModel());
  }

  @Override
  protected void onInitialize() {
    add(getCategoryViewContainerComponent());
    super.onInitialize();
  }

  private Component getCategoryViewContainerComponent() {
    return categoryViewContainer.setOutputMarkupId(true);
  }
}
