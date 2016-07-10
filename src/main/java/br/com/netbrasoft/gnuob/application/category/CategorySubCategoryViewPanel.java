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

import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_VIEW_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_SUB_CATEGORY_VIEW_TABLE_CONTAINER_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.DESCRIPTION_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SUB_CATEGORY_VIEW_FORM_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;
import static br.com.netbrasoft.gnuob.application.security.AppRoles.EMPLOYEE;
import static br.com.netbrasoft.gnuob.application.security.AppRoles.MANAGER;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import br.com.netbrasoft.gnuob.api.SubCategory;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.ENABLE, roles = {MANAGER, EMPLOYEE})
public class CategorySubCategoryViewPanel extends AbstractToolbar {

  @AuthorizeAction(action = Action.ENABLE, roles = {MANAGER, EMPLOYEE})
  class CategorySubCategoryViewContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.ENABLE, roles = {MANAGER, EMPLOYEE})
    class CategorySubCategoryViewTableContainer extends WebMarkupContainer {

      private static final long serialVersionUID = 2311449979659425388L;
      private final BootstrapForm<SubCategory> subCategoryViewForm;

      public CategorySubCategoryViewTableContainer(String id, IModel<SubCategory> model) {
        super(id, model);
        subCategoryViewForm = createSubCategoryViewForm();
      }

      private BootstrapForm<SubCategory> createSubCategoryViewForm() {
        return new BootstrapForm<>(SUB_CATEGORY_VIEW_FORM_ID, createSubCategoryCompountPropertyModel());
      }

      private CompoundPropertyModel<SubCategory> createSubCategoryCompountPropertyModel() {
        return new CompoundPropertyModel<>(getCategoryModel());
      }

      private IModel<SubCategory> getCategoryModel() {
        return (IModel<SubCategory>) CategorySubCategoryViewTableContainer.this.getDefaultModel();
      }

      @Override
      protected void onInitialize() {
        super.onInitialize();
        add(getSubCategoryViewFormComponent());
      }

      private Component getSubCategoryViewFormComponent() {
        subCategoryViewForm.add(createNameComponent());
        subCategoryViewForm.add(createDescriptionComponent());
        return subCategoryViewForm.setOutputMarkupId(true);
      }

      private Component createNameComponent() {
        return new TextField<String>(NAME_ID).setOutputMarkupId(true);
      }

      private Component createDescriptionComponent() {
        return new TextArea<String>(DESCRIPTION_ID).setOutputMarkupId(true);
      }
    }

    private static final long serialVersionUID = 6553335439633063852L;
    private final CategorySubCategoryViewTableContainer categorySubCategoryViewTableContainer;

    public CategorySubCategoryViewContainer(String id, IModel<SubCategory> model) {
      super(id, model);
      categorySubCategoryViewTableContainer = createCategorySubCategoryViewTableContainer();
    }

    private CategorySubCategoryViewTableContainer createCategorySubCategoryViewTableContainer() {
      return new CategorySubCategoryViewTableContainer(CATEGORY_SUB_CATEGORY_VIEW_TABLE_CONTAINER_ID,
          Model.of(new SubCategory()));
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategorySubCategoryViewTableContainerComponent());
    }

    private Component getCategorySubCategoryViewTableContainerComponent() {
      return categorySubCategoryViewTableContainer.add(new TableBehavior()).setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = 3872427840824181023L;
  private final CategorySubCategoryViewContainer categorySubCategoryViewContainer;

  public CategorySubCategoryViewPanel(IModel<SubCategory> model, final DataTable<SubCategory, String> table) {
    super(model, table);
    categorySubCategoryViewContainer = createCategorySubCategoryViewContainer();
  }

  private CategorySubCategoryViewContainer createCategorySubCategoryViewContainer() {
    return new CategorySubCategoryViewContainer(CATEGORY_SUB_CATEGORY_VIEW_CONTAINER_ID, getSubCategoryModel());
  }

  private IModel<SubCategory> getSubCategoryModel() {
    return (IModel<SubCategory>) CategorySubCategoryViewPanel.this.getDefaultModel();
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(getCategorySubCategoryViewContainerComponent());
  }

  private Component getCategorySubCategoryViewContainerComponent() {
    return categorySubCategoryViewContainer.setOutputMarkupId(true);
  }
}
