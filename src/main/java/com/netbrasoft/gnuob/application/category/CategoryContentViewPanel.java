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

import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_VIEW_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_CONTENT_VIEW_TABLE_CONTAINER_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_VIEW_FORM_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FORMAT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class CategoryContentViewPanel extends Panel {

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class CategoryContentViewContainer extends WebMarkupContainer {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class CategoryContentViewTableContainer extends WebMarkupContainer {

      private static final long serialVersionUID = -8067077655681760267L;
      private final BootstrapForm<Content> contentViewForm;

      public CategoryContentViewTableContainer(final String id, final IModel<Content> model) {
        super(id, model);
        contentViewForm = getContentViewForm();
      }

      private BootstrapForm<Content> getContentViewForm() {
        return new BootstrapForm<>(CONTENT_VIEW_FORM_ID,
            new CompoundPropertyModel<Content>((IModel<Content>) CategoryContentViewPanel.this.getDefaultModel()));
      }

      @Override
      protected void onInitialize() {
        super.onInitialize();
        add(getContentViewFormComponent());
      }

      private Component getContentViewFormComponent() {
        contentViewForm.add(getNameComponent());
        contentViewForm.add(getFormatComponent());
        contentViewForm.add(getLabelComponent());
        return contentViewForm.setOutputMarkupId(true);
      }

      private Component getNameComponent() {
        return new TextField<String>(NAME_ID).setOutputMarkupId(true);
      }

      private Component getFormatComponent() {
        return new TextField<String>(FORMAT_ID).setOutputMarkupId(true);
      }

      private Component getLabelComponent() {
        return new Label(CONTENT_ID) {

          private static final long serialVersionUID = 721587245052671908L;

          @Override
          public <C> IConverter<C> getConverter(final Class<C> type) {
            if (byte[].class.isAssignableFrom(type)) {
              return (IConverter<C>) new ByteArrayConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setEscapeModelStrings(false);
      }
    }

    private static final long serialVersionUID = -1738761585689545961L;
    private final CategoryContentViewTableContainer categoryContentViewTableContainer;

    public CategoryContentViewContainer(String id, IModel<Content> model) {
      super(id, model);
      categoryContentViewTableContainer = getCategoryContentViewTableContainer();
    }

    private CategoryContentViewTableContainer getCategoryContentViewTableContainer() {
      return new CategoryContentViewTableContainer(CATEGORY_CONTENT_VIEW_TABLE_CONTAINER_ID,
          (IModel<Content>) CategoryContentViewContainer.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      add(getCategoryContentViewTableContainerComponent());
    }

    private Component getCategoryContentViewTableContainerComponent() {
      return categoryContentViewTableContainer.add(new TableBehavior()).setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = 7032777283917504797L;
  private final CategoryContentViewContainer categoryContentViewContainer;

  public CategoryContentViewPanel(final String id, final IModel<Content> model) {
    super(id, model);
    categoryContentViewContainer = getCategoryContentViewContainer();
  }

  private CategoryContentViewContainer getCategoryContentViewContainer() {
    return new CategoryContentViewContainer(CATEGORY_CONTENT_VIEW_CONTAINER_ID,
        (IModel<Content>) CategoryContentViewPanel.this.getDefaultModel());
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    add(getCategoryContentViewContainerComponent());
  }

  private Component getCategoryContentViewContainerComponent() {
    return categoryContentViewContainer.setOutputMarkupId(true);
  }
}
