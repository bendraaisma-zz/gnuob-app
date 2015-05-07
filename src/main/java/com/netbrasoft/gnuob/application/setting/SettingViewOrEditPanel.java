package com.netbrasoft.gnuob.application.setting;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netbrasoft.gnuob.api.Setting;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER })
public class SettingViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         SettingViewOrEditPanel.this.removeAll();
         SettingViewOrEditPanel.this.add(new SettingEditFragement()).setOutputMarkupId(true);
         target.add(target.getPage());
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR })
   class EditAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public EditAjaxLink() {
         super("edit");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         SettingViewOrEditPanel.this.removeAll();
         SettingViewOrEditPanel.this.add(new SettingEditFragement().setOutputMarkupId(true));
         target.add(SettingViewOrEditPanel.this);
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR })
   class SaveAjaxButton extends AjaxButton {

      private static final long serialVersionUID = 2695394292963384938L;

      public SaveAjaxButton(Form<?> form) {
         super("save", form);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
         try {
            Setting setting = (Setting) form.getDefaultModelObject();

            if (setting.getId() == 0) {
               setting.setActive(true);

               settingDataProvider.persist(setting);
            } else {
               settingDataProvider.merge(setting);
            }

            SettingViewOrEditPanel.this.removeAll();
            SettingViewOrEditPanel.this.add(new SettingViewFragement().setOutputMarkupId(true));
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

   class SettingEditFragement extends Fragment {

      private static final long serialVersionUID = 8971798392355786447L;

      public SettingEditFragement() {
         super("settingViewOrEditFragement", "settingEditFragement", SettingViewOrEditPanel.this, SettingViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Setting> settingEditForm = new Form<Setting>("settingEditForm");

         settingEditForm.setModel(new CompoundPropertyModel<Setting>((IModel<Setting>) getDefaultModel()));
         settingEditForm.add(new TextField<String>("property"));
         settingEditForm.add(new TextField<String>("value"));
         settingEditForm.add(new TextArea<String>("description"));

         add(settingEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new SaveAjaxButton(settingEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class SettingViewFragement extends Fragment {

      private static final long serialVersionUID = 498703216819003839L;

      public SettingViewFragement() {
         super("settingViewOrEditFragement", "settingViewFragement", SettingViewOrEditPanel.this, SettingViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Setting> settingViewForm = new Form<Setting>("settingViewForm");

         settingViewForm.setModel(new CompoundPropertyModel<Setting>((IModel<Setting>) getDefaultModel()));
         settingViewForm.add(new Label("property"));
         settingViewForm.add(new Label("value"));
         settingViewForm.add(new Label("description"));

         add(new EditAjaxLink());
         add(settingViewForm.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(SettingViewOrEditPanel.class);

   private static final long serialVersionUID = -8401960249843479048L;

   @SpringBean(name = "SettingDataProvider", required = true)
   private GenericTypeDataProvider<Setting> settingDataProvider;

   public SettingViewOrEditPanel(final String id, final IModel<Setting> model) {
      super(id, model);
   }
}
