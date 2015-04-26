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

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class GroupPanel extends Panel {

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "GroupDataProvider", required = true)
   private GenericTypeDataProvider<Group> groupDataProvider;

   private AjaxLink<Void> add = new AjaxLink<Void>("add") {

      private static final long serialVersionUID = 9191172039973638020L;

      @Override
      public void onClick(AjaxRequestTarget paramAjaxRequestTarget) {
      }
   };

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", groupDataProvider);

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
               groupViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(groupViewOrEditPanel);
            }
         });
      }
   };

   private WebMarkupContainer groupDataviewContainer = new WebMarkupContainer("groupDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(groupDataview);
         super.onInitialize();
      };
   };

   private ItemsPerPagePagingNavigator groupPagingNavigator = new ItemsPerPagePagingNavigator("groupPagingNavigator", groupDataview);

   private GroupViewOrEditPanel groupViewOrEditPanel = new GroupViewOrEditPanel("groupViewOrEditPanel", new Model<Group>(new Group()));

   public GroupPanel(final String id, final IModel<Group> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      RolesSession roleSession = (RolesSession) Session.get();

      groupDataProvider.setUser(roleSession.getUsername());
      groupDataProvider.setPassword(roleSession.getPassword());
      groupDataProvider.setSite(roleSession.getSite());
      groupDataProvider.setType((Group) getDefaultModelObject());

      add(add);
      add(orderByName);
      add(groupDataviewContainer.setOutputMarkupId(true));
      add(groupPagingNavigator);
      add(groupViewOrEditPanel.setOutputMarkupId(true));

      super.onInitialize();
   }
}
