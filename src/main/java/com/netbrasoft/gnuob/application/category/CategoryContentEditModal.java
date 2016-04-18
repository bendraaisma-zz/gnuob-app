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
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_EDIT_FORM_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FEEDBACK_MARKUP_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FORMAT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.TinyMceBehavior;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
public class CategoryContentEditModal extends Modal<Content> {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class SaveAjaxButton extends BootstrapAjaxButton {

    private static final long serialVersionUID = 2695394292963384938L;

    public SaveAjaxButton(final String id, final IModel<String> model, final Form<?> form, final Buttons.Type type) {
      super(id, model, form, type);
    }

    @Override
    protected void onInitialize() {
      super.onInitialize();
      setSize(Buttons.Size.Small);
    }

    @Override
    protected void onError(final AjaxRequestTarget target, final Form<?> form) {
      form.add(getTooltipValidation());
      target.add(form);
      target.add(getSaveButtonComponent());
    }

    private TooltipValidation getTooltipValidation() {
      return new TooltipValidation();
    }

    private Component getSaveButtonComponent() {
      return SaveAjaxButton.this.add(getLoadingBehavior());
    }

    private LoadingBehavior getLoadingBehavior() {
      return new LoadingBehavior(Model.of(CategoryContentEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)));
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
      try {
        final Category category = getCategory();
        category.getContents().add((Content) form.getDefaultModelObject());
        updateCategory(category);
      } catch (final RuntimeException e) {
        feedbackPanel.warn(e.getLocalizedMessage());
        target.add(getFeedbackComponent());
        LOGGER.warn(e.getMessage(), e);
      } finally {
        target.add(getSaveButtonComponent());
      }
    }

    private Category getCategory() {
      return CategoryContentEditModal.this.selectedCategory.getObject();
    }

    private void updateCategory(Category category) {
      if (category.getId() > 0) {
        categoryDataProvider.merge(category);
      } else {
        categoryDataProvider.persist(category);
      }
    }

    private Component getFeedbackComponent() {
      return feedbackPanel.setOutputMarkupId(true);
    }
  }

  private static final long serialVersionUID = 2097286419831069632L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryContentEditModal.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private final BootstrapForm<Content> contentEditForm;
  private final SaveAjaxButton saveButton;
  private final NotificationPanel feedbackPanel;
  private IModel<Category> selectedCategory;

  public CategoryContentEditModal(String id, IModel<Content> model) {
    super(id, model);
    contentEditForm = createContentEditForm();
    saveButton = createSaveButton();
    feedbackPanel = createFeedbackPanel();
    selectedCategory = createSelectedCategoryModel();
  }

  private Model<Category> createSelectedCategoryModel() {
    return Model.of(new Category());
  }

  public IModel<Category> getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(IModel<Category> selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  private BootstrapForm<Content> createContentEditForm() {
    return new BootstrapForm<>(CONTENT_EDIT_FORM_ID,
        new CompoundPropertyModel<>((IModel<Content>) CategoryContentEditModal.this.getDefaultModel()));
  }

  private SaveAjaxButton createSaveButton() {
    return new SaveAjaxButton(BUTTON_MARKUP_ID,
        Model.of(CategoryContentEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)), contentEditForm,
        Buttons.Type.Primary);
  }

  private NotificationPanel createFeedbackPanel() {
    return new NotificationPanel(FEEDBACK_MARKUP_ID);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
    addCloseButton(Model.of(CategoryContentEditModal.this.getString(CANCEL_MESSAGE_KEY)));
    addButton(getSaveButtonComponent());
    header(Model.of(getString(CONTENT_MESSAGE_KEY)));
    add(getFeedbackComponent());
    add(getContentEditFormComponent());
  }

  private Component getSaveButtonComponent() {
    return saveButton.add(new TinyMceAjaxSubmitModifier()).setOutputMarkupId(true);
  }

  private Component getFeedbackComponent() {
    return feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true);
  }

  private Component getContentEditFormComponent() {
    contentEditForm.add(getNameComponent());
    contentEditForm.add(getFormatComponent());
    contentEditForm.add(getContentComponent());
    return contentEditForm.setOutputMarkupId(true);
  }

  private Component getContentComponent() {
    return new TextArea<byte[]>(CONTENT_ID) {

      private static final long serialVersionUID = -7341359315847579440L;

      @Override
      public <C> IConverter<C> getConverter(final Class<C> type) {
        if (byte[].class.isAssignableFrom(type)) {
          return (IConverter<C>) new ByteArrayConverter();
        } else {
          return super.getConverter(type);
        }
      }
    }.setRequired(true).add(new TinyMceBehavior()).setOutputMarkupId(true);
  }

  private Component getFormatComponent() {
    return new TextField<String>(FORMAT_ID).add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private Component getNameComponent() {
    return new TextField<String>(NAME_ID).add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }
}
