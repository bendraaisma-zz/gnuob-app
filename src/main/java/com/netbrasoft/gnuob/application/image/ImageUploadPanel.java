package com.netbrasoft.gnuob.application.image;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.lang.Bytes;

import com.netbrasoft.gnuob.api.Content;

public abstract class ImageUploadPanel extends Panel {

  private static final long serialVersionUID = 3022054803231316826L;
  private static final int MAX_MEGA_BYTES = 10;
  private FileUploadField imageUploadLocation;
  private NonCachingImage imagePreview;
  private FileUpload imageUpload;

  public ImageUploadPanel(final String id, final IModel<Content> model) {
    super(id, model);

    Form<Content> imageUploadForm = new Form<Content>("imageUploadForm", model) {

      private static final long serialVersionUID = 6739020979971170136L;

      @Override
      protected void onSubmit() {
        imageUpload = imageUploadLocation.getFileUpload();
      }
    };

    imageUploadForm.setMultiPart(true);
    imageUploadForm.setMaxSize(Bytes.megabytes(MAX_MEGA_BYTES));
    imageUploadForm.add(imageUploadLocation = new FileUploadField("imageUploadLocation"));
    imageUploadForm.add(createImageUploadIndicatingAjaxButton(imageUploadForm));
    imageUploadForm.add(imagePreview = new NonCachingImage("imagePreview", new DynamicImageResource() {

      private static final long serialVersionUID = -8506217031647332254L;

      @Override
      protected byte[] getImageData(Attributes paramAttributes) {
        return imageUpload == null ? new byte[0] : imageUpload.getBytes();
      }
    }));

    add(imageUploadForm.setOutputMarkupId(true));
    add(createSaveAndCloseAjaxLink(imageUploadForm));
  }

  private IndicatingAjaxButton createImageUploadIndicatingAjaxButton(Form<Content> form) {
    return new IndicatingAjaxButton("imageUpload", form) {

      private static final long serialVersionUID = 7065583221733867864L;

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        target.add(imagePreview);
      }
    };
  }

  private AjaxButton createSaveAndCloseAjaxLink(Form<Content> form) {
    return new AjaxButton("saveAndClose", form) {

      private static final long serialVersionUID = -822737148437189545L;

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        Content content = (Content) form.getDefaultModelObject();

        if (imageUpload != null) {

          content.setFormat(imageUpload.getContentType());
          content.setName(imageUpload.getClientFileName());
          content.setContent(imageUpload.getBytes());

          uploadedImage(target);
        }
      }
    };
  }

  @Override
  protected void onInitialize() {
    imagePreview.setOutputMarkupId(true);

    setOutputMarkupId(true);
    super.onInitialize();
  }

  public abstract void uploadedImage(AjaxRequestTarget paramAjaxRequestTarget);
}
