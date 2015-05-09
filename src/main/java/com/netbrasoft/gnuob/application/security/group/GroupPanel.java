package com.netbrasoft.gnuob.application.security.group;

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

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.AppServletContainerAuthenticatedWebSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER })
public class GroupPanel extends Panel {

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

   class GroupDataview extends DataView<Group> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected GroupDataview() {
         super("groupDataview", groupDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected Item<Group> newItem(String id, int index, IModel<Group> model) {
         Item<Group> item = super.newItem(id, index, model);

         if (model.getObject().getId() == ((Group) groupViewOrEditPanel.getDefaultModelObject()).getId()) {
            item.add(new AttributeModifier("class", "info"));
         }

         return item;
      }

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
               target.add(getPage());
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "GroupDataProvider", required = true)
   private GenericTypeDataProvider<Group> groupDataProvider;

   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", groupDataProvider);

   private GroupDataview groupDataview = new GroupDataview();

   private WebMarkupContainer groupDataviewContainer = new WebMarkupContainer("groupDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(groupDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator groupPagingNavigator = new BootstrapPagingNavigator("groupPagingNavigator", groupDataview);

   private GroupViewOrEditPanel groupViewOrEditPanel = new GroupViewOrEditPanel("groupViewOrEditPanel", new Model<Group>(new Group()));

   public GroupPanel(final String id, final IModel<Group> model) {
      super(id, model);
      model.getObject().setActive(true);
   }

   @Override
   protected void onInitialize() {
      groupDataProvider.setUser(AppServletContainerAuthenticatedWebSession.getUserName());
      groupDataProvider.setPassword(AppServletContainerAuthenticatedWebSession.getPassword());
      groupDataProvider.setSite(AppServletContainerAuthenticatedWebSession.getSite());
      groupDataProvider.setType((Group) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByName);
      add(groupDataviewContainer.setOutputMarkupId(true));
      add(groupPagingNavigator);
      add(groupViewOrEditPanel.add(groupViewOrEditPanel.new GroupViewFragement()).setOutputMarkupId(true));

      super.onInitialize();
   }
}
