package com.netbrasoft.gnuob.application.content;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class ContentViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "ContentDataProvider", required = true)
   private GenericTypeDataProvider<Content> contentDataProvider;

   private OrderByBorder<String> orderByFormat = new OrderByBorder<String>("orderByFormat", "format",
         contentDataProvider);
   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", contentDataProvider);
   private DataView<Content> contentDataview = new DataView<Content>("contentDataview", contentDataProvider,
         ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Content> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Content>(paramItem.getModelObject()));
         paramItem.add(new Label("format"));
         paramItem.add(new Label("name"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator contentPagingNavigator = new ItemsPerPagePagingNavigator(
         "contentPagingNavigator", contentDataview);

   public ContentViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      contentDataProvider.setUser(roleSession.getUsername());
      contentDataProvider.setPassword(roleSession.getPassword());
      contentDataProvider.setSite(roleSession.getSite());

      add(orderByFormat);
      add(orderByName);
      add(contentDataview);
      add(contentPagingNavigator);
   }
}
