package com.netbrasoft.gnuob.application.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Group;

@SuppressWarnings("unchecked")
public class GroupViewOrEditPanel extends Panel {

   private static final long serialVersionUID = -8401960249843479048L;

   public GroupViewOrEditPanel(final String id, final IModel<Group> model) {
      super(id, model);
      add(createGroupViewFragement().setOutputMarkupId(true));
   }

   private AjaxLink<Void> createCancelAjaxLink() {
      return new AjaxLink<Void>("cancel") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            GroupViewOrEditPanel.this.removeAll();
            GroupViewOrEditPanel.this.add(createGroupViewFragement()).setOutputMarkupId(true);
            paramAjaxRequestTarget.add(paramAjaxRequestTarget.getPage());
         }
      };
   }

   private AjaxLink<Void> createEditAjaxLink() {
      return new AjaxLink<Void>("edit") {

         private static final long serialVersionUID = 4267535261864907719L;

         @Override
         public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
            GroupViewOrEditPanel.this.removeAll();
            GroupViewOrEditPanel.this.add(createGroupEditFragement().setOutputMarkupId(true));
            paramAjaxRequestTarget.add(GroupViewOrEditPanel.this);
         }
      };
   }

   private Fragment createGroupEditFragement() {
      return new Fragment("groupViewOrEditFragement", "groupEditFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 8971798392355786447L;

         @Override
         protected void onInitialize() {
            Form<Group> groupEditForm = new Form<Group>("groupEditForm");

            groupEditForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
            groupEditForm.add(new TextField<String>("name"));
            groupEditForm.add(new TextArea<String>("description"));

            add(groupEditForm.setOutputMarkupId(true));
            add(createSaveAjaxLink().setOutputMarkupId(true));
            add(createCancelAjaxLink().setOutputMarkupId(true));
            super.onInitialize();
         }
      };
   }

   private Fragment createGroupViewFragement() {
      return new Fragment("groupViewOrEditFragement", "groupViewFragement", this, getDefaultModel()) {

         private static final long serialVersionUID = 498703216819003839L;

         @Override
         protected void onInitialize() {
            Form<Group> groupViewForm = new Form<Group>("groupViewForm");

            groupViewForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
            groupViewForm.add(new Label("name"));
            groupViewForm.add(new Label("description"));

            add(createEditAjaxLink());
            add(groupViewForm.setOutputMarkupId(true));
            super.onInitialize();
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
}
