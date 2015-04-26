package com.netbrasoft.gnuob.application.security;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.api.Rule;
import com.netbrasoft.gnuob.api.User;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;

@SuppressWarnings("unchecked")
public class UserViewOrEditPanel extends Panel {

   private static final long serialVersionUID = 7692943659495394940L;

   public UserViewOrEditPanel(final String id, final IModel<User> model) {
      super(id, model);
      add(createUserViewFragement().setOutputMarkupId(true));
   }

   private AjaxLink<Void> createCancelAjaxLink() {
      return new AjaxLink<Void>("cancel") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            UserViewOrEditPanel.this.removeAll();
            UserViewOrEditPanel.this.add(createUserViewFragement()).setOutputMarkupId(true);
            paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
         }
      };
   }

   private AjaxLink<Void> createEditAjaxLink() {
      return new AjaxLink<Void>("edit") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            UserViewOrEditPanel.this.removeAll();
            UserViewOrEditPanel.this.add(createUserEditFragement().setOutputMarkupId(true));
            paramAjaxRequestTarget.add(UserViewOrEditPanel.this);
         }
      };
   }

   private AjaxLink<Void> createSaveAjaxLink() {
      return new AjaxLink<Void>("save") {

         private static final long serialVersionUID = 2695394292963384938L;

         @Override
         public void onClick(AjaxRequestTarget target) {
            // TODO Auto-generated method stub
         }
      };
   }

   private Fragment createUserEditFragement() {
      return new Fragment("userViewOrEditFragement", "userEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -8347518285280404106L;

         @Override
         protected void onInitialize() {
            Form<User> userEditForm = new Form<User>("userEditForm");

            userEditForm.setModel(new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
            userEditForm.add(new BootstrapSelect<Rule>("access", Arrays.asList(Rule.values())));
            userEditForm.add(new TextField<String>("name"));
            userEditForm.add(new PasswordTextField("password"));
            userEditForm.add(new PasswordTextField("cpassword", Model.of("")));
            userEditForm.add(new TextField<String>("role"));
            userEditForm.add(new TextArea<String>("description"));

            add(userEditForm.setOutputMarkupId(true));
            add(createCancelAjaxLink().setOutputMarkupId(true));
            add(createSaveAjaxLink().setOutputMarkupId(true));

            super.onInitialize();
         }

      };
   }

   private Fragment createUserViewFragement() {
      return new Fragment("userViewOrEditFragement", "userViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = -8347518285280404106L;

         @Override
         protected void onInitialize() {
            Form<User> userViewForm = new Form<User>("userViewForm");

            userViewForm.setModel(new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
            userViewForm.add(new Label("access"));
            userViewForm.add(new Label("name"));
            userViewForm.add(new Label("role"));
            userViewForm.add(new Label("description"));

            add(createEditAjaxLink().setOutputMarkupId(true));
            add(userViewForm.setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

}
