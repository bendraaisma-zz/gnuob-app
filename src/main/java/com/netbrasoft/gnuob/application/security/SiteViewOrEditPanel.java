package com.netbrasoft.gnuob.application.security;

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

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.Site;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;

@SuppressWarnings("unchecked")
@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER })
public class SiteViewOrEditPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR })
   class CancelAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = 4267535261864907719L;

      public CancelAjaxLink() {
         super("cancel");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         SiteViewOrEditPanel.this.removeAll();
         SiteViewOrEditPanel.this.add(new SiteViewFragement()).setOutputMarkupId(true);
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
         SiteViewOrEditPanel.this.removeAll();
         SiteViewOrEditPanel.this.add(new SiteEditFragement().setOutputMarkupId(true));
         target.add(SiteViewOrEditPanel.this);
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
            Site site = (Site) form.getDefaultModelObject();

            if (site.getId() == 0) {
               site.setActive(true);

               siteDataProvider.persist(site);
            } else {
               siteDataProvider.merge(site);
            }

            SiteViewOrEditPanel.this.removeAll();
            SiteViewOrEditPanel.this.add(new SiteViewFragement().setOutputMarkupId(true));
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

   class SiteEditFragement extends Fragment {

      private static final long serialVersionUID = 8971798392355786447L;

      public SiteEditFragement() {
         super("siteViewOrEditFragement", "siteEditFragement", SiteViewOrEditPanel.this, SiteViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Group> siteEditForm = new Form<Group>("siteEditForm");

         siteEditForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
         siteEditForm.add(new TextField<String>("name"));
         siteEditForm.add(new TextArea<String>("description"));

         add(siteEditForm.setOutputMarkupId(true));
         add(new NotificationPanel("feedback").hideAfter(Duration.seconds(5)).setOutputMarkupId(true));
         add(new SaveAjaxButton(siteEditForm).setOutputMarkupId(true));
         add(new CancelAjaxLink().setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   class SiteViewFragement extends Fragment {

      private static final long serialVersionUID = 498703216819003839L;

      public SiteViewFragement() {
         super("siteViewOrEditFragement", "siteViewFragement", SiteViewOrEditPanel.this, SiteViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         Form<Group> siteViewForm = new Form<Group>("siteViewForm");

         siteViewForm.setModel(new CompoundPropertyModel<Group>((IModel<Group>) getDefaultModel()));
         siteViewForm.add(new Label("name"));
         siteViewForm.add(new Label("description"));

         add(new EditAjaxLink());
         add(siteViewForm.setOutputMarkupId(true));
         super.onInitialize();
      }
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(SiteViewOrEditPanel.class);

   private static final long serialVersionUID = 7813473163481178540L;

   @SpringBean(name = "SiteDataProvider", required = true)
   private GenericTypeDataProvider<Site> siteDataProvider;

   public SiteViewOrEditPanel(final String id, final IModel<Site> model) {
      super(id, model);
      add(new SiteViewFragement().setOutputMarkupId(true));
   }
}
