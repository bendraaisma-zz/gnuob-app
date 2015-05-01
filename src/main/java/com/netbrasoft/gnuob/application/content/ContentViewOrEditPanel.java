package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContentViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ContentViewOrEditPanel.this.removeAll();
         ContentViewOrEditPanel.this.add(new ContentViewFragement().setOutputMarkupId(true));
         target.add(target.getPage());
      }
   }

   class ContentEditFragement extends Fragment {

      private static final long serialVersionUID = -8862288407883660764L;

      public ContentEditFragement() {
         super("contentViewOrEditFragement", "contentEditFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Content> contentEditForm = new Form<Content>("contentEditForm");
         contentEditForm.setModel(new CompoundPropertyModel<Content>((IModel<Content>) getDefaultModel()));
         contentEditForm.add(new TextField<String>("name"));
         contentEditForm.add(new TextField<String>("format"));
         contentEditForm.add(new NonCachingImage("imagePreview", new DynamicImageResource() {

            private static final long serialVersionUID = -8506217031647332254L;

            @Override
            protected byte[] getImageData(Attributes paramAttributes) {
               Content content = (Content) getDefaultModelObject();
               return content == null ? new byte[0] : content.getContent();
            }
         }));

         add(contentEditForm.setOutputMarkupId(true));
         add(new SaveAjaxButton(contentEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class ContentViewFragement extends Fragment {

      private static final long serialVersionUID = -8862288407883660764L;

      public ContentViewFragement() {
         super("contentViewOrEditFragement", "contentViewFragement", ContentViewOrEditPanel.this, ContentViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Content> contentViewForm = new Form<Content>("contentViewForm");
         contentViewForm.setModel(new CompoundPropertyModel<Content>((IModel<Content>) getDefaultModel()));
         contentViewForm.add(new Label("name"));
         contentViewForm.add(new Label("format"));
         contentViewForm.add(new NonCachingImage("imagePreview", new DynamicImageResource() {

            private static final long serialVersionUID = -8506217031647332254L;

            @Override
            protected byte[] getImageData(Attributes paramAttributes) {
               Content content = (Content) getDefaultModelObject();
               return content == null ? new byte[0] : content.getContent();
            }
         }));

         add(contentViewForm.setOutputMarkupId(true));
         add(new EditAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         ContentViewOrEditPanel.this.removeAll();
         ContentViewOrEditPanel.this.add(new ContentEditFragement().setOutputMarkupId(true));
         target.add(ContentViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
         form.add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Content content = (Content) form.getDefaultModelObject();

            if (content.getId() == 0) {
               contentDataProvider.persist(content);
            } else {
               contentDataProvider.merge(content);
            }

            ContentViewOrEditPanel.this.removeAll();
            ContentViewOrEditPanel.this.add(new ContentViewFragement().setOutputMarkupId(true));
         } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);

            String[] messages = e.getMessage().split(": ");
            String message = messages[messages.length - 1];

            warn(message.substring(0, 1).toUpperCase() + message.substring(1));
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
      add(new ContentViewFragement().setOutputMarkupId(true));
   }
}
