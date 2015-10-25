package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.TinyMceBehavior;
import wicket.contrib.tinymce4.ajax.TinyMceAjaxSubmitModifier;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
public class ContentViewOrEditPanel extends Panel {

  @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
  class ContentEditFragement extends Fragment {

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class ContentEditTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class CancelAjaxLink extends BootstrapAjaxLink<Content> {

        private static final long serialVersionUID = 4267535261864907719L;

        public CancelAjaxLink(String id, IModel<Content> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ContentViewOrEditPanel.this.removeAll();
          if (((Content) CancelAjaxLink.this.getDefaultModelObject()).getId() > 0) {
            CancelAjaxLink.this.setDefaultModelObject(contentDataProvider.findById((Content) CancelAjaxLink.this.getDefaultModelObject()));
          }
          target.add(ContentViewOrEditPanel.this.add(new ContentViewFragement()).setOutputMarkupId(true));
        }
      }

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class SaveAjaxButton extends BootstrapAjaxButton {

        private static final long serialVersionUID = 2695394292963384938L;

        public SaveAjaxButton(String id, IModel<String> model, Form<?> form, Buttons.Type type) {
          super(id, model, form, type);
          setSize(Buttons.Size.Small);
          add(new LoadingBehavior(Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY))),
              new TinyMceAjaxSubmitModifier());
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
          form.add(new TooltipValidation());
          target.add(form);
          target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          boolean isException = false;
          try {
            if (((Content) form.getDefaultModelObject()).getId() == 0) {
              ContentEditTable.this.setDefaultModelObject(contentDataProvider.findById(contentDataProvider.persist(((Content) form.getDefaultModelObject()))));
            } else {
              ContentEditTable.this.setDefaultModelObject(contentDataProvider.findById(contentDataProvider.merge(((Content) form.getDefaultModelObject()))));
            }
          } catch (final RuntimeException e) {
            isException = true;
            LOGGER.warn(e.getMessage(), e);
            feedbackPanel.warn(e.getLocalizedMessage());
            target.add(feedbackPanel.setOutputMarkupId(true));
            target
                .add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)))));
          } finally {
            if (!isException) {
              ContentViewOrEditPanel.this.removeAll();
              target.add(ContentViewOrEditPanel.this.add(ContentViewOrEditPanel.this.new ContentViewFragement()).setOutputMarkupId(true));
            }
          }
        }
      }

      private static final long serialVersionUID = -1112121331425285822L;

      private final BootstrapForm<Content> contentEditForm;

      private final CancelAjaxLink cancelAjaxLink;

      private final SaveAjaxButton saveAjaxButton;

      private final NotificationPanel feedbackPanel;

      public ContentEditTable(final String id, final IModel<Content> model) {
        super(id, model);
        contentEditForm = new BootstrapForm<Content>("contentEditForm", new CompoundPropertyModel<Content>((IModel<Content>) ContentEditTable.this.getDefaultModel()));
        cancelAjaxLink =
            new CancelAjaxLink("cancel", model, Buttons.Type.Default, Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.CANCEL_MESSAGE_KEY)));
        saveAjaxButton = new SaveAjaxButton("save", Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.SAVE_AND_CLOSE_MESSAGE_KEY)),
            contentEditForm, Buttons.Type.Primary);
        feedbackPanel = new NotificationPanel("feedback");
      }

      @Override
      protected void onInitialize() {
        contentEditForm.add(new RequiredTextField<String>("name").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        contentEditForm.add(new RequiredTextField<String>("format").add(StringValidator.maximumLength(128)).setOutputMarkupId(true));
        contentEditForm.add(new TextArea<byte[]>("content") {

          private static final long serialVersionUID = -7341359315847579440L;

          @Override
          public <C> IConverter<C> getConverter(Class<C> type) {
            if (byte[].class.isAssignableFrom(type)) {
              return (IConverter<C>) new ByteArrayConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setRequired(true).add(new TinyMceBehavior()).setOutputMarkupId(true));
        add(contentEditForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(feedbackPanel.hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
        add(saveAjaxButton.setOutputMarkupId(true));
        add(cancelAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    private static final long serialVersionUID = -8862288407883660764L;

    private final ContentEditTable contentEditTable;

    public ContentEditFragement() {
      super("contentViewOrEditFragement", "contentEditFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      contentEditTable = new ContentEditTable("contentEditTable", (IModel<Content>) ContentEditFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contentEditTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
  class ContentViewFragement extends Fragment {

    @AuthorizeAction(action = Action.ENABLE, roles = {AppRoles.MANAGER, AppRoles.EMPLOYEE})
    class ContentViewTable extends WebMarkupContainer {

      @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
      class EditAjaxLink extends BootstrapAjaxLink<Content> {

        private static final long serialVersionUID = 4267535261864907719L;

        public EditAjaxLink(String id, IModel<Content> model, Buttons.Type type, IModel<String> labelModel) {
          super(id, model, type, labelModel);
          setIconType(GlyphIconType.edit);
          setSize(Buttons.Size.Small);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
          ContentViewOrEditPanel.this.removeAll();
          target.add(ContentViewOrEditPanel.this.add(new ContentEditFragement().setOutputMarkupId(true)));
        }
      }

      private static final long serialVersionUID = 3338005815718092161L;

      private final EditAjaxLink editAjaxLink;

      private final BootstrapForm<Content> contentViewForm;

      public ContentViewTable(final String id, final IModel<Content> model) {
        super(id, model);
        contentViewForm = new BootstrapForm<Content>("contentViewForm", new CompoundPropertyModel<Content>((IModel<Content>) ContentViewTable.this.getDefaultModel()));
        editAjaxLink = new EditAjaxLink("edit", model, Buttons.Type.Primary, Model.of(ContentViewOrEditPanel.this.getString(NetbrasoftApplicationConstants.EDIT_MESSAGE_KEY)));
      }

      @Override
      protected void onInitialize() {
        contentViewForm.add(new RequiredTextField<String>("name").setOutputMarkupId(true));
        contentViewForm.add(new RequiredTextField<String>("format").setOutputMarkupId(true));
        contentViewForm.add(new Label("content") {

          private static final long serialVersionUID = 721587245052671908L;

          @Override
          public <C> IConverter<C> getConverter(Class<C> type) {
            if (byte[].class.isAssignableFrom(type)) {
              return (IConverter<C>) new ByteArrayConverter();
            } else {
              return super.getConverter(type);
            }
          }
        }.setEscapeModelStrings(false).setOutputMarkupId(true));
        add(contentViewForm.add(new FormBehavior(FormType.Horizontal)).setOutputMarkupId(true));
        add(editAjaxLink.setOutputMarkupId(true));
        super.onInitialize();
      }
    }

    @AuthorizeAction(action = Action.RENDER, roles = {AppRoles.MANAGER})
    class EditAjaxLink extends BootstrapAjaxLink<Content> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink(String id, IModel<Content> model, Buttons.Type type, IModel<String> labelModel) {
        super(id, model, type, labelModel);
        setIconType(GlyphIconType.edit);
        setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
        ContentViewOrEditPanel.this.removeAll();
        target.add(ContentViewOrEditPanel.this.add(new ContentEditFragement().setOutputMarkupId(true)));
      }
    }

    private static final long serialVersionUID = -8862288407883660764L;

    private final ContentViewTable contentViewTable;

    public ContentViewFragement() {
      super("contentViewOrEditFragement", "contentViewFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      contentViewTable = new ContentViewTable("contentViewTable", (IModel<Content>) ContentViewFragement.this.getDefaultModel());
    }

    @Override
    protected void onInitialize() {
      add(contentViewTable.add(new TableBehavior()).setOutputMarkupId(true));
      super.onInitialize();
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(ContentViewOrEditPanel.class);

  private static final long serialVersionUID = -3061472875418422947L;

  @SpringBean(name = "ContentDataProvider", required = true)
  private GenericTypeDataProvider<Content> contentDataProvider;

  public ContentViewOrEditPanel(final String id, final IModel<Content> model) {
    super(id, model);
  }

  @Override
  protected void onInitialize() {
    contentDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
    contentDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
    contentDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
    contentDataProvider.setType(new Content());
    contentDataProvider.getType().setActive(true);
    super.onInitialize();
  }
}
