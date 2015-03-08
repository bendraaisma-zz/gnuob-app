package com.netbrasoft.gnuob.application.security;

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

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class GroupViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "GroupDataProvider", required = true)
   private GenericTypeDataProvider<Group> groupDataProvider;

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", groupDataProvider);
   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description",
         groupDataProvider);
   private DataView<Group> groupDataview = new DataView<Group>("groupDataview", groupDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<Group> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Group>(paramItem.getModelObject()));
         paramItem.add(new Label("name"));
         paramItem.add(new Label("description"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {

            }
         });
      }
   };
   private ItemsPerPagePagingNavigator groupPagingNavigator = new ItemsPerPagePagingNavigator("groupPagingNavigator",
         groupDataview);

   public GroupViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      groupDataProvider.setUser(roleSession.getUsername());
      groupDataProvider.setPassword(roleSession.getPassword());
      groupDataProvider.setSite(roleSession.getSite());

      add(orderByName);
      add(orderByDescription);
      add(groupDataview);
      add(groupPagingNavigator);
   }
}
