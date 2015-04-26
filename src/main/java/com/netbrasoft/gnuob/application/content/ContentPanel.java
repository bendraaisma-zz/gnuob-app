package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class ContentPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ContentDataProvider", required = true)
   private GenericTypeDataProvider<Content> contentDataProvider;

   private AjaxLink<Void> add = new AjaxLink<Void>("add") {

      private static final long serialVersionUID = 9191172039973638020L;

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      }
   };

   private OrderByBorder<String> orderByFormat = new OrderByBorder<String>("orderByFormat", "format", contentDataProvider);

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", contentDataProvider);

   private DataView<Content> contentDataview = new DataView<Content>("contentDataview", contentDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

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
               target.add(contentViewOrEditPanel);
            }
         });
      }
   };

   private WebMarkupContainer contentDataviewContainer = new WebMarkupContainer("contentDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(contentDataview);

         setOutputMarkupId(true);
         super.onInitialize();
      };
   };

   private ItemsPerPagePagingNavigator contentPagingNavigator = new ItemsPerPagePagingNavigator("contentPagingNavigator", contentDataview);

   private ContentViewOrEditPanel contentViewOrEditPanel = new ContentViewOrEditPanel("contentViewOrEditPanel", new Model<Content>(new Content()));

   public ContentPanel(final String id, final IModel<Content> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      RolesSession roleSession = (RolesSession) Session.get();

      contentDataProvider.setUser(roleSession.getUsername());
      contentDataProvider.setPassword(roleSession.getPassword());
      contentDataProvider.setSite(roleSession.getSite());
      contentDataProvider.setType((Content) getDefaultModelObject());

      add(add);
      add(orderByFormat);
      add(orderByName);
      add(contentDataviewContainer);
      add(contentPagingNavigator);
      add(contentViewOrEditPanel.setOutputMarkupId(true));

      super.onInitialize();
   }
}
