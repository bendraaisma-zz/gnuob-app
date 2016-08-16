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
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_EDIT_FORM_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.CONTENT_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FORMAT_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.NAME_ID;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY;
import static br.com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.UNCHECKED;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getPassword;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getSite;
import static br.com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession.getUserName;
import static de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size.Small;
import static de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type.Primary;
import static org.apache.wicket.model.Model.of;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.netbrasoft.gnuob.api.Category;
import br.com.netbrasoft.gnuob.api.Content;
import br.com.netbrasoft.gnuob.api.generic.IGenericTypeDataProvider;
import br.com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import br.com.netbrasoft.gnuob.application.security.AppRoles;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
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
      setSize(Small);
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
      return SaveAjaxButton.this.add(getLoadingBehavior());
    }

    private LoadingBehavior getLoadingBehavior() {
      return new LoadingBehavior(of(CategoryContentEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)));
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
      try {
        if (!parentModel.getObject().getContents().contains(form.getDefaultModelObject())) {
          parentModel.getObject().getContents().add((Content) form.getDefaultModelObject());
        }
        parentModel.setObject(parentModel.getObject().getId() > 0 ? categoryDataProvider.merge(parentModel.getObject())
            : categoryDataProvider.persist(parentModel.getObject()));
      } catch (final RuntimeException e) {
        LOGGER.warn(e.getMessage(), e);
        warn(e.getLocalizedMessage());
      }
      success("Sucessfully saved the content: " + ((Content) form.getDefaultModelObject()).getName());
      close(target);
    }
  }

  private static final long serialVersionUID = 2097286419831069632L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryContentEditModal.class);

  @SpringBean(name = CATEGORY_DATA_PROVIDER_NAME, required = true)
  private IGenericTypeDataProvider<Category> categoryDataProvider;
  private IModel<Category> parentModel;

  public CategoryContentEditModal(String id, IModel<Content> model) {
    super(id, model);
    parentModel = of(new Category());
  }

  public void setParentModel(IModel<Category> parentModel) {
    this.parentModel = parentModel;
  }

  private BootstrapForm<Content> createContentEditForm() {
    return new BootstrapForm<>(CONTENT_EDIT_FORM_ID,
        new CompoundPropertyModel<>((IModel<Content>) CategoryContentEditModal.this.getDefaultModel()));
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    initializeCategoryDataProvider();
    final BootstrapForm<Content> contentEditForm = createContentEditForm();
    addCloseButton(of(CategoryContentEditModal.this.getString(CANCEL_MESSAGE_KEY)));
    addButton(getSaveButtonComponent(contentEditForm));
    header(of(getString(CONTENT_MESSAGE_KEY)));
    add(getContentEditFormComponent(contentEditForm));
  }

  @Override
  protected void onClose(IPartialPageRequestHandler target) {
    target.add(CategoryContentEditModal.this.getParent().setOutputMarkupId(true));
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

  private Component getSaveButtonComponent(BootstrapForm<Content> contentEditForm) {
    return getSaveButton(contentEditForm).add(new TinyMceAjaxSubmitModifier()).setOutputMarkupId(true);
  }

  private SaveAjaxButton getSaveButton(BootstrapForm<Content> contentEditForm) {
    return new SaveAjaxButton(BUTTON_MARKUP_ID, of(CategoryContentEditModal.this.getString(SAVE_AND_CLOSE_MESSAGE_KEY)),
        contentEditForm, Primary);
  }

  private Component getContentEditFormComponent(BootstrapForm<Content> contentEditForm) {
    return contentEditForm.add(getNameComponent()).add(getFormatComponent()).add(getContentComponent())
        .setOutputMarkupId(true);
  }

  private Component getContentComponent() {
    return getContentTextArea().setRequired(true).add(new TinyMceBehavior()).setOutputMarkupId(true);
  }

  private TextArea<byte[]> getContentTextArea() {
    return new TextArea<byte[]>(CONTENT_ID) {

      private static final long serialVersionUID = -7341359315847579440L;

      @Override
      public <C> IConverter<C> getConverter(final Class<C> type) {
        return byte[].class.isAssignableFrom(type) ? (IConverter<C>) new ByteArrayConverter()
            : super.getConverter(type);
      }
    };
  }

  private Component getFormatComponent() {
    return getFormatTextField().add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private TextField<String> getFormatTextField() {
    return new TextField<>(FORMAT_ID);
  }

  private Component getNameComponent() {
    return getNameTextField().add(new PropertyValidator<String>()).setOutputMarkupId(true);
  }

  private TextField<String> getNameTextField() {
    return new TextField<>(NAME_ID);
  }
}
