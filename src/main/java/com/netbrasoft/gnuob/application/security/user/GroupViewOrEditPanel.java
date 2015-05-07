package com.netbrasoft.gnuob.application.security.user;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.User;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

public class GroupViewOrEditPanel extends Panel {

   class GroupDataview extends DataView<Group> {

      private static final long serialVersionUID = 2246346365193989354L;

      public GroupDataview() {
         super("groupDataview", new GroupListDataProvider(), ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<Group> item) {
         IModel<Group> compound = new CompoundPropertyModel<Group>(item.getModelObject());
         item.setModel(compound);
         item.add(new Label("name"));
         item.add(new Label("description"));
         item.add(new RemoveAjaxLink());
      }
   }

   class GroupEditFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public GroupEditFragement() {
         super("groupViewOrEditFragement", "groupEditFragement", GroupViewOrEditPanel.this, GroupViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(groupDataviewContainer.setOutputMarkupId(true));
         add(groupPagingNavigator);
         super.onInitialize();
      }
   }

   class GroupListDataProvider extends ListDataProvider<Group> {

      private static final long serialVersionUID = 5259243752700177690L;

      @Override
      protected List<Group> getData() {
         return ((User) GroupViewOrEditPanel.this.getDefaultModelObject()).getGroups();
      }
   }

   class GroupViewFragement extends Fragment {

      private static final long serialVersionUID = 8640403483040526601L;

      public GroupViewFragement() {
         super("groupViewOrEditFragement", "groupViewFragement", GroupViewOrEditPanel.this, GroupViewOrEditPanel.this.getDefaultModel());
      }

      @Override
      protected void onInitialize() {
         add(groupDataviewContainer.setOutputMarkupId(true));
         add(groupPagingNavigator);
         super.onInitialize();
      }
   }

   @AuthorizeAction(action = Action.RENDER, roles = { AppRoles.MANAGER })
   class RemoveAjaxLink extends AjaxLink<Void> {

      private static final long serialVersionUID = -6950515027229520882L;

      public RemoveAjaxLink() {
         super("remove");
      }

      @Override
      public void onClick(AjaxRequestTarget target) {
         // TODO Auto-generated method stub
      }
   }

   private static final long serialVersionUID = -2575007609797589274L;

   private static final long ITEMS_PER_PAGE = 5;

   private WebMarkupContainer groupDataviewContainer = new WebMarkupContainer("groupDataviewContainer") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onInitialize() {
         add(groupDataview);
         super.onInitialize();
      }
   };

   private GroupDataview groupDataview = new GroupDataview();

   private BootstrapPagingNavigator groupPagingNavigator = new BootstrapPagingNavigator("groupPagingNavigator", groupDataview);

   public GroupViewOrEditPanel(final String id, final IModel<User> model) {
      super(id, model);
   }
}
