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
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.DESCRIPTION_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FEEDBACK_MARKUP_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SUB_CATEGORY_EDIT_FORM_ID;
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
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.SubCategory;
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
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings(UNCHECKED)
public class CategorySubCategoryEditModal extends Modal<SubCategory> {

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
      form.add(createTooltipValidation());
      target.add(createSaveButtonComponent());
      target.add(form);
    }

    private TooltipValidation createTooltipValidation() {
      return new TooltipValidation();
    }

    private Component createSaveButtonComponent() {
      return SaveAjaxButton.this.add(getLoadingBehavior());
    }

    private LoadingBehavior getLoadingBehavior() {
      return new LoadingBehavior(Model.of(CategorySubCategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)));
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
      try {
        final Category category = getCategory();
        category.getSubCategories().add((SubCategory) form.getDefaultModelObject());
        updateCategory(category);
      } catch (final RuntimeException e) {
        feedbackPanel.warn(e.getLocalizedMessage());
        LOGGER.warn(e.getMessage(), e);
        target.add(getFeedbackComponent());
      } finally {
        target.add(createSaveButtonComponent());
      }
    }

    private Category getCategory() {
      return CategorySubCategoryEditModal.this.selectedCategory.getObject();
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

  private static final long serialVersionUID = -8104757742113733811L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategorySubCategoryEditModal.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private final BootstrapForm<SubCategory> subCategoryForm;
  private final SaveAjaxButton saveButton;
  private final NotificationPanel feedbackPanel;
  private IModel<Category> selectedCategory;

  public CategorySubCategoryEditModal(String id, IModel<SubCategory> model) {
    super(id, model);
    subCategoryForm = createSubCategoryForm();
    saveButton = createSaveButton();
    feedbackPanel = createFeedbackPanel();
    selectedCategory = createSelectedCategoryModel();
  }

  private BootstrapForm<SubCategory> createSubCategoryForm() {
    return new BootstrapForm<>(SUB_CATEGORY_EDIT_FORM_ID,
        new CompoundPropertyModel<>((IModel<SubCategory>) CategorySubCategoryEditModal.this.getDefaultModel()));
  }

  private SaveAjaxButton createSaveButton() {
    return new SaveAjaxButton(BUTTON_MARKUP_ID,
        Model.of(CategorySubCategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)), subCategoryForm,
        Buttons.Type.Primary);
  }

  private NotificationPanel createFeedbackPanel() {
    return new NotificationPanel(FEEDBACK_MARKUP_ID);
  }

  private Model<Category> createSelectedCategoryModel() {
    return Model.of(new Category());
  }

  public void setSelectedCategory(IModel<Category> selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    categoryDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    categoryDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    categoryDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
    addCloseButton(getCloseLabelModel());
    addButton(getSaveButtonComponent());
    header(getHeaderLabelModel());
    add(getFeedbackComponent());
    add(getSubCategoryFormComponent());
  }

  private Model<String> getCloseLabelModel() {
    return Model.of(CategorySubCategoryEditModal.this.getString(CANCEL_MESSAGE_KEY));
  }

  private Component getSaveButtonComponent() {
    return saveButton.add(new TinyMceAjaxSubmitModifier()).setOutputMarkupId(true);
  }

  private Model<String> getHeaderLabelModel() {
    return Model.of(getString(CONTENT_MESSAGE_KEY));
  }

  private Component getFeedbackComponent() {
    return feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true);
  }

  private Component getSubCategoryFormComponent() {
    subCategoryForm.add(createNameComponent());
    subCategoryForm.add(createDescriptionComponent());
    return subCategoryForm.setOutputMarkupId(true);
  }

  private Component createNameComponent() {
    final TextField<String> textFieldName = new TextField<>(NAME_ID);
    return textFieldName.add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private Component createDescriptionComponent() {
    final TextArea<String> textAreaDescription = new TextArea<>(DESCRIPTION_ID);
    return textAreaDescription.add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }
}
