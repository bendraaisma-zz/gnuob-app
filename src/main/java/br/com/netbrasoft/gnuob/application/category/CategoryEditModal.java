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
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_EDIT_FORM_COMPONENT_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CATEGORY_MESSAGE;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.DESCRIPTION_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.POSITION_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getPassword;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getSite;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getUserName;
import static de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size.Small;
import static de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type.Primary;
import static org.apache.wicket.model.Model.of;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;

import br.com.netbrasoft.gnuob.api.Category;
import br.com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import br.com.netbrasoft.gnuob.application.security.AppRoles;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
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
      setSize(Small);
      super.onInitialize();
    }

    @Override
    protected void onError(final AjaxRequestTarget target, final Form<?> form) {
      target.add(form.add(getTooltipValidation()));
      target.add(getSaveButtonComponent());
    }

    private TooltipValidation getTooltipValidation() {
      return new TooltipValidation();
    }

    private Component getSaveButtonComponent() {
      return SaveAjaxButton.this.add(getLoadingBehavior()).setOutputMarkupId(true);
    }

    private LoadingBehavior getLoadingBehavior() {
      return new LoadingBehavior(of(CategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)));
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
      try {
        ((Category) form.getDefaultModelObject()).setActive(true);
        form.setDefaultModelObject(((Category) form.getDefaultModelObject()).getId() > 0
            ? form.setDefaultModelObject(categoryDataProvider.merge((Category) form.getDefaultModelObject()))
            : categoryDataProvider.persist((Category) form.getDefaultModelObject()));
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        warn(e.getLocalizedMessage());
      }
      success("Sucessfully saved the category: " + ((Category) form.getDefaultModelObject()).getName());
      close(target);
    }
  }

  private static final long serialVersionUID = 3548295972868471635L;
  private static final Logger LOGGER = getLogger(CategoryEditModal.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;


  public CategoryEditModal(String id, IModel<Category> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    initializeCategoryDataProvider();
    final BootstrapForm<Category> categoryForm = getCategoryForm();
    addCloseButton(of(CategoryEditModal.this.getString(CANCEL_MESSAGE_KEY)));
    addButton(getSaveButtonComponent(categoryForm));
    header(of(getString(CATEGORY_MESSAGE)));
    add(getCategoryFormComponent(categoryForm));
  }

  @Override
  protected void onClose(IPartialPageRequestHandler target) {
    target.add(CategoryEditModal.this.getParent().setOutputMarkupId(true));
    super.show(false);
    super.onClose(target);
  }

  private void initializeCategoryDataProvider() {
    categoryDataProvider.setUser(getUserName());
    categoryDataProvider.setPassword(getPassword());
    categoryDataProvider.setSite(getSite());
    categoryDataProvider.setType(new Category());
    categoryDataProvider.getType().setActive(true);
  }

  private Component getSaveButtonComponent(BootstrapForm<Category> categoryForm) {
    return getSaveButton(categoryForm).setOutputMarkupId(true);
  }

  private SaveAjaxButton getSaveButton(BootstrapForm<Category> categoryForm) {
    return new SaveAjaxButton(BUTTON_MARKUP_ID, of(CategoryEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)),
        categoryForm, Primary);
  }

  private Component getCategoryFormComponent(BootstrapForm<Category> categoryForm) {
    return categoryForm.add(getPositionComponent()).add(getNameComponent()).add(getDescriptionComponent())
        .setOutputMarkupId(true);
  }

  private Component getPositionComponent() {
    return getPositionNumberTextField().add(new PropertyValidator<Integer>()).setOutputMarkupId(true);
  }

  private NumberTextField<Integer> getPositionNumberTextField() {
    return new NumberTextField<>(POSITION_ID);
  }

  private Component getNameComponent() {
    return getNameTextField().add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private TextField<String> getNameTextField() {
    return new TextField<>(NAME_ID);
  }

  private Component getDescriptionComponent() {
    return getDescriptionTextArea().add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private TextArea<String> getDescriptionTextArea() {
    return new TextArea<>(DESCRIPTION_ID);
  }

  private BootstrapForm<Category> getCategoryForm() {
    return new BootstrapForm<>(CATEGORY_EDIT_FORM_COMPONENT_ID,
        new CompoundPropertyModel<Category>((IModel<Category>) CategoryEditModal.this.getDefaultModel()));
  }
}
