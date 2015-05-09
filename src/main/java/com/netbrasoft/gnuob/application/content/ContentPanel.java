package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class ContentPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class AddAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = -8317730269644885290L;

      public AddAjaxLink() {
         super("add");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   class ContentDataview extends DataView<Content> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected ContentDataview() {
         super("contentDataview", contentDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Content> newItem(String id, int index, IModel<Content> model) {
         Item<Content> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Content) contentViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

      @Override
      protected void populateItem(Item<Content> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Content>(paramItem.getModelObject()));
         paramItem.add(new Label("name"));
         paramItem.add(new Label("format"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               contentViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ContentDataProvider", required = true)
   private GenericTypeDataProvider<Content> contentDataProvider;

   private OrderByBorder<String> orderByFormat = new OrderByBorder<String>("orderByFormat", "format", contentDataProvider);

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", contentDataProvider);

   private DataView<Content> contentDataview = new ContentDataview();

   private WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(contentDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator contentPagingNavigator = new BootstrapPagingNavigator("contentPagingNavigator", contentDataview);

   private ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", new Model<Content>(new Content()));

   public ContentPanel(final String id, final IModel<Content> model) {
      super(id, model);
      model.getObject().setActive(true);
   }

   @Override
   protected void onInitialize() {
      contentDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      contentDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      contentDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      contentDataProvider.setType((Content) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByFormat);
      add(orderByName);
      add(contentDataviewContainer.setOutputMarkupId(true));
      add(contentPagingNavigator);
      add(contentViewOrEditPanel.setOutputMarkupId(true));

      super.onInitialize();
   }
}
