package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.TinyMceBehavior;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContentViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel", Model.of(ContentViewOrEditPanel.this.getString("cancelMessage")), Buttons.Type.Default, Model.of(ContentViewOrEditPanel.this.getString("cancelMessage")));
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ContentViewOrEditPanel.this.removeAll();
         ContentViewOrEditPanel.this.add(new ContentViewFragement().setOutputMarkupId(true));

         if (((Content) ContentViewOrEditPanel.this.getDefaultModelObject()).getId() > 0) {
            ContentViewOrEditPanel.this.setDefaultModelObject(contentDataProvider.findById((Content) ContentViewOrEditPanel.this.getDefaultModelObject()));
         }

         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ContentEditFragement extends Fragment {

      private static final long serialVersionUID = -8862288407883660764L;

      private final WebMarkupContainer contentEditTable;

      public ContentEditFragement() {
         super("contentViewOrEditFragement", "contentEditFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());

         contentEditTable = new WebMarkupContainer("contentEditTable", getDefaultModel()) {

            private static final long serialVersionUID = -3380666766696288690L;

            @Override
            protected void onInitialize() {
               final Form<Content> contentEditForm = new Form<Content>("contentEditForm");
               contentEditForm.setModel(new CompoundPropertyModel<Content>((IModel<Content>) getDefaultModel()));
               contentEditForm.add(new TextField<String>("name"));
               contentEditForm.add(new TextField<String>("format"));
               contentEditForm.add(new TextArea<byte[]>("content") {

                  private static final long serialVersionUID = -7341359315847579440L;

                  @Override
                  public <C> IConverter<C> getConverter(Class<C> type) {
                     if (byte[].class.isAssignableFrom(type)) {
                        return (IConverter<C>) new ByteArrayConverter();
                     } else {
                        return super.getConverter(type);
                     }
                  };
               }.add(new TinyMceBehavior()));
               add(contentEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
               add(new SaveAjaxButton(contentEditForm).setOutputMarkupId(true));
               add(new CancelAjaxLink().setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(contentEditTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.ENABLE, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
   class ContentViewFragement extends Fragment {

      private static final long serialVersionUID = -8862288407883660764L;

      private final WebMarkupContainer contentViewTable;

      public ContentViewFragement() {
         super("contentViewOrEditFragement", "contentViewFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());

         contentViewTable = new WebMarkupContainer("contentViewTable", getDefaultModel()) {

            private static final long serialVersionUID = -1400173307943196404L;

            @Override
            protected void onInitialize() {
               final Form<Content> contentViewForm = new Form<Content>("contentViewForm");
               contentViewForm.setModel(new CompoundPropertyModel<Content>((IModel<Content>) getDefaultModel()));
               contentViewForm.add(new Label("name"));
               contentViewForm.add(new Label("format"));
               contentViewForm.add(new Label("content") {

                  private static final long serialVersionUID = 721587245052671908L;

                  @Override
                  public <C> IConverter<C> getConverter(Class<C> type) {
                     if (byte[].class.isAssignableFrom(type)) {
                        return (IConverter<C>) new ByteArrayConverter();
                     } else {
                        return super.getConverter(type);
                     }
                  };
               }.setEscapeModelStrings(false));
               add(contentViewForm.setOutputMarkupId(true));
               add(new EditAjaxLink().setOutputMarkupId(true));
               add(new TableBehavior());
               super.onInitialize();
            }
         };
      }

      @Override
      protected void onInitialize() {
         add(contentViewTable.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends BootstrapAjaxLink<String> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit", Model.of(ContentViewOrEditPanel.this.getString("editMessage")), Buttons.Type.Primary, Model.of(ContentViewOrEditPanel.this.getString("editMessage")));
         setIconType(GlyphIconType.edit);
         setSize(Buttons.Size.Small);
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ContentViewOrEditPanel.this.removeAll();
         ContentViewOrEditPanel.this.add(new ContentEditFragement().setOutputMarkupId(true));
         target.add(ContentViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(ContentViewOrEditPanel.this.getString("saveAndCloseMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(ContentViewOrEditPanel.this.getString("saveAndCloseMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(ContentViewOrEditPanel.this.getString("saveAndCloseMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Content content = (Content) form.getDefaultModelObject();
            content.setActive(true);

            if (content.getId() == 0) {
               ContentViewOrEditPanel.this.setDefaultModelObject(contentDataProvider.findById(contentDataProvider.persist(content)));
            } else {
               ContentViewOrEditPanel.this.setDefaultModelObject(contentDataProvider.findById(contentDataProvider.merge(content)));
            }

            ContentViewOrEditPanel.this.removeAll();
            ContentViewOrEditPanel.this.add(new ContentViewFragement().setOutputMarkupId(true));
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(target.getPage());
         }
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(ContentViewOrEditPanel.class);

   private static final long serialVersionUID = -3061472875418422947L;

   @SpringBean(name = "ContentDataProvider", required = true)
   private GenericTypeDataProvider<Content> contentDataProvider;

   public ContentViewOrEditPanel(String id, IModel<Content> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      contentDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      contentDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      contentDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      contentDataProvider.setType((Content) getDefaultModelObject());
      contentDataProvider.getType().setActive(true);
      super.onInitialize();
   }
}
