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
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_EDIT_FORM_COMPONENT_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_MESSAGE;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.DESCRIPTION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FEEDBACK_MARKUP_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.POSITION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;

@SuppressWarnings(UNCHECKED)
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
public class CategoryEditModal extends Modal<Category> {

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
      return new LoadingBehavior(Model.of(CategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)));
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
      try {
        updateCategory((Category) form.getDefaultModelObject());
      } catch (final RuntimeException e) {
        feedbackPanel.warn(e.getLocalizedMessage());
        target.add(getFeedbackComponent());
        LOGGER.warn(e.getMessage(), e);
      } finally {
        target.add(getSaveButtonComponent());
      }
    }

    private void updateCategory(Category category) {
      category.setActive(true);
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

  private static final long serialVersionUID = 3548295972868471635L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryEditModal.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private final BootstrapForm<Category> categoryForm;
  private final SaveAjaxButton saveButton;
  private final NotificationPanel feedbackPanel;

  public CategoryEditModal(String id, IModel<Category> model) {
    super(id, model);
    categoryForm = createCategoryForm();
    saveButton = createSaveButton();
    feedbackPanel = createFeedbackPanel();
  }

  private BootstrapForm<Category> createCategoryForm() {
    return new BootstrapForm<>(CATEGORY_EDIT_FORM_COMPONENT_ID,
        new CompoundPropertyModel<Category>((IModel<Category>) CategoryEditModal.this.getDefaultModel()));
  }

  private SaveAjaxButton createSaveButton() {
    return new SaveAjaxButton(BUTTON_MARKUP_ID, Model.of(CategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)),
        categoryForm, Buttons.Type.Primary);
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
    addCloseButton(Model.of(CategoryEditModal.this.getString(CANCEL_MESSAGE_KEY)));
    addButton(getSaveButtonComponent());
    header(Model.of(getString(CATEGORY_MESSAGE)));
    add(getFeedbackComponent());
    add(getCategoryFormComponent());
  }

  private Component getCategoryFormComponent() {
    categoryForm.add(getPositionComponent());
    categoryForm.add(getNameComponent());
    categoryForm.add(getDescriptionComponent());
    return categoryForm.setOutputMarkupId(true);
  }

  private Component getSaveButtonComponent() {
    return saveButton.setOutputMarkupId(true);
  }

  private Component getPositionComponent() {
    final NumberTextField<Integer> numberTextFieldPosition = new NumberTextField<>(POSITION_ID);
    return numberTextFieldPosition.add(new PropertyValidator<Integer>()).setOutputMarkupId(true);
  }

  private Component getNameComponent() {
    final TextField<String> textFieldName = new TextField<>(NAME_ID);
    return textFieldName.add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private Component getDescriptionComponent() {
    final TextArea<String> textAreaDescription = new TextArea<>(DESCRIPTION_ID);
    return textAreaDescription.add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private Component getFeedbackComponent() {
    return feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true);
  }
}
