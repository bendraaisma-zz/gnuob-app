package com.netbrasoft.gnuob.application.security;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
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

import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER })
public class UserPanel extends Panel {

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR })
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

   class UserDataview extends DataView<User> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected UserDataview() {
         super("userDataview", userDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<User> newItem(String id, int index, IModel<User> model) {
         Item<User> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((User) userViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

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
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "UserDataProvider", required = true)
   private GenericTypeDataProvider<User> userDataProvider;

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", userDataProvider);

   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", userDataProvider);

   private UserDataview userDataview = new UserDataview();

   private WebMarkupContainer userDataviewContainer = new WebMarkupContainer("userDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(userDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator userPagingNavigator = new BootstrapPagingNavigator("userPagingNavigator", userDataview);

   private UserViewOrEditPanel userViewOrEditPanel = new UserViewOrEditPanel("userViewOrEditPanel", new Model<User>(new User()));

   public UserPanel(final String id, final IModel<User> model) {
      super(id, model);
      model.getObject().setActive(true);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      userDataProvider.setUser(roleSession.getUsername());
      userDataProvider.setPassword(roleSession.getPassword());
      userDataProvider.setSite(roleSession.getSite());
      userDataProvider.setType((User) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByName);
      add(orderByDescription);
      add(userDataviewContainer.setOutputMarkupId(true));
      add(userPagingNavigator);
      add(userViewOrEditPanel.setOutputMarkupId(true));
   }
}
