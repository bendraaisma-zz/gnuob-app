package com.netbrasoft.gnuob.application.category;

import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.converter.ByteArrayConverter;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation.TooltipValidation;
import wicket.contrib.tinymce4.TinyMceBehavior;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class SubCategoryContentViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SaveAjaxButton extends BootstrapAjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", Model.of(SubCategoryContentViewOrEditPanel.this.getString("saveMessage")), form, Buttons.Type.Primary);
         setSize(Buttons.Size.Small);
         add(new LoadingBehavior(Model.of(SubCategoryContentViewOrEditPanel.this.getString("saveMessage"))));
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
         form.add(new TooltipValidation());
         target.add(form);
         target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(SubCategoryContentViewOrEditPanel.this.getString("saveMessage")))));
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            final Content content = (Content) form.getDefaultModelObject();

            if (content.getId() == 0) {
               ((Category) markupContainer.getDefaultModelObject()).getContents().add(content);
            }
         } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            warn(e.getLocalizedMessage());
         } finally {
            target.add(markupContainer.setOutputMarkupId(true));
            target.add(form.setOutputMarkupId(true));
            target.add(SaveAjaxButton.this.add(new LoadingBehavior(Model.of(SubCategoryContentViewOrEditPanel.this.getString("saveMessage")))));
         }
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class SubCategoryContentEditFragement extends Fragment {

      private static final long serialVersionUID = 2975560176491444462L;

      private final WebMarkupContainer contentEditTable;

      public SubCategoryContentEditFragement() {
         super("subCategoryContentViewOrEditFragement", "subCategoryContentEditFragement", SubCategoryContentViewOrEditPanel.this, SubCategoryContentViewOrEditPanel.this.getDefaultModel());

         contentEditTable = new WebMarkupContainer("contentEditTable", getDefaultModel()) {

            private static final long serialVersionUID = 630333471990816489L;

            @SuppressWarnings("unchecked")
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
               contentEditForm.add(new SaveAjaxButton(contentEditForm).setOutputMarkupId(true));
               add(contentEditForm.setOutputMarkupId(true));
               add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
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
   class SubCategoryContentViewFragement extends Fragment {

      private static final long serialVersionUID = -8203670073215506846L;

      private final WebMarkupContainer contentViewTable;

      public SubCategoryContentViewFragement() {
         super("subCategoryContentViewOrEditFragement", "subCategoryContentViewFragement", SubCategoryContentViewOrEditPanel.this, SubCategoryContentViewOrEditPanel.this.getDefaultModel());

         contentViewTable = new WebMarkupContainer("contentViewTable", getDefaultModel()) {

            private static final long serialVersionUID = 630333471990816489L;

            @SuppressWarnings("unchecked")
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

   private static final long serialVersionUID = 7032777283917504797L;

   private static final Logger LOGGER = LoggerFactory.getLogger(CategoryContentViewOrEditPanel.class);

   private final MarkupContainer markupContainer;

   public SubCategoryContentViewOrEditPanel(final String id, final IModel<Content> model, final MarkupContainer markupContainer) {
      super(id, model);
      this.markupContainer = markupContainer;
   }
}
