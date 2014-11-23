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

import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class UserViewPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "UserDataProvider", required = true)
   private GenericTypeDataProvider<User> userDataProvider;

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", userDataProvider);
   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", userDataProvider);
   private DataView<User> userDataview = new DataView<User>("userDataview", userDataProvider, ITEMS_PER_PAGE) {

      private static final long serialVersionUID = -5039874949058607907L;

      @Override
      protected void populateItem(Item<User> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<User>(paramItem.getModelObject()));
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
   private ItemsPerPagePagingNavigator userPagingNavigator = new ItemsPerPagePagingNavigator("userPagingNavigator", userDataview);

   public UserViewPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      userDataProvider.setUser(roleSession.getUsername());
      userDataProvider.setPassword(roleSession.getPassword());
      userDataProvider.setSite(roleSession.getSite());

      add(orderByName);
      add(orderByDescription);
      add(userDataview);
      add(userPagingNavigator);
   }
}
