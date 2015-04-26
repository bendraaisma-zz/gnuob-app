package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;

@SuppressWarnings("unchecked")
public class ContentViewOrEditPanel extends Panel {

   private static final long serialVersionUID = -3061472875418422947L;

   @SpringBean(name = "ContentDataProvider", required = true)
   private GenericTypeDataProvider<Content> contentDataProvider;

   public ContentViewOrEditPanel(String id, IModel<Content> model) {
      super(id, model);
      add(createContentViewFragement().setOutputMarkupId(true));
   }

   private AjaxLink<Void> createCancelAjaxLink() {
      return new AjaxLink<Void>("cancel") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget target) {
            ContentViewOrEditPanel.this.removeAll();
            ContentViewOrEditPanel.this.add(createContentViewFragement().setOutputMarkupId(true));
            target.add(target.getPage());
         }
      };
   }

   private Fragment createContentEditFragement() {
      return new Fragment("contentViewOrEditFragement", "contentEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -8862288407883660764L;

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
            add(createSaveAjaxLink(contentEditForm).setOutputMarkupId(true));
            add(createCancelAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private Fragment createContentViewFragement() {
      return new Fragment("contentViewOrEditFragement", "contentViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -8862288407883660764L;

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
            add(createEditAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private AjaxLink<Void> createEditAjaxLink() {
      return new AjaxLink<Void>("edit") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget target) {
            ContentViewOrEditPanel.this.removeAll();
            ContentViewOrEditPanel.this.add(createContentEditFragement().setOutputMarkupId(true));
            target.add(ContentViewOrEditPanel.this);
         }
      };
   }

   private AjaxButton createSaveAjaxLink(Form<Content> form) {
      return new AjaxButton("save", form) {

         private static final long serialVersionUID = 2695394292963384938L;

         @Override
         protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            Content content = (Content) form.getDefaultModelObject();

            if (content.getId() == 0) {
               contentDataProvider.persist(content);
            } else {
               contentDataProvider.merge(content);
            }

            ContentViewOrEditPanel.this.removeAll();
            ContentViewOrEditPanel.this.add(createContentViewFragement().setOutputMarkupId(true));
            target.add(target.getPage());
         }
      };
   }
}
