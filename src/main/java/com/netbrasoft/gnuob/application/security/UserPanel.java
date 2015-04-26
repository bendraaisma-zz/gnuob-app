package com.netbrasoft.gnuob.application.security;

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

import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class UserPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "UserDataProvider", required = true)
   private GenericTypeDataProvider<User> userDataProvider;

   private AjaxLink<Void> add = new AjaxLink<Void>("add") {

      private static final long serialVersionUID = 9191172039973638020L;

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      }
   };

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
               userViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(userViewOrEditPanel);
            }
         });
      }
   };

   private WebMarkupContainer userDataviewContainer = new WebMarkupContainer("userDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(userDataview);
         super.onInitialize();
      };
   };

   private ItemsPerPagePagingNavigator userPagingNavigator = new ItemsPerPagePagingNavigator("userPagingNavigator", userDataview);

   private UserViewOrEditPanel userViewOrEditPanel = new UserViewOrEditPanel("userViewOrEditPanel", new Model<User>(new User()));

   public UserPanel(final String id, final IModel<User> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      userDataProvider.setUser(roleSession.getUsername());
      userDataProvider.setPassword(roleSession.getPassword());
      userDataProvider.setSite(roleSession.getSite());
      userDataProvider.setType((User) getDefaultModelObject());

      add(add);
      add(orderByName);
      add(orderByDescription);
      add(userDataviewContainer.setOutputMarkupId(true));
      add(userPagingNavigator);
      add(userViewOrEditPanel.setOutputMarkupId(true));
   }
}
