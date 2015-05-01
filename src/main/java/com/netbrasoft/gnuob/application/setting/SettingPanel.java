package com.netbrasoft.gnuob.application.setting;

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

import com.netbrasoft.gnuob.api.Setting;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.BootstrapPagingNavigator;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER })
public class SettingPanel extends Panel {

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

   class SettingDataview extends DataView<Setting> {

      private static final long serialVersionUID = -5039874949058607907L;

      protected SettingDataview() {
         super("settingDataview", settingDataProvider, ITEMS_PER_PAGE);
      }

      @Override
      protected void populateItem(Item<Setting> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Setting>(paramItem.getModelObject()));
         paramItem.add(new Label("property"));
         paramItem.add(new Label("value"));
         paramItem.add(new Label("description"));
         paramItem.add(new AjaxEventBehavior("onclick") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(AjaxRequestTarget target) {
               settingViewOrEditPanel.setDefaultModelObject(paramItem.getModelObject());
               target.add(settingViewOrEditPanel);
            }
         });
      }
   }

   private static final long serialVersionUID = 3703226064705246155L;

   private static final int ITEMS_PER_PAGE = 10;

   @SpringBean(name = "SettingDataProvider", required = true)
   private GenericTypeDataProvider<Setting> settingDataProvider;

   private OrderByBorder<String> orderByProperty = new OrderByBorder<String>("orderByProperty", "property", settingDataProvider);

   private OrderByBorder<String> orderByValue = new OrderByBorder<String>("orderByValue", "value", settingDataProvider);

   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", settingDataProvider);

   private SettingDataview settingDataview = new SettingDataview();

   private WebMarkupContainer settingDataviewContainer = new WebMarkupContainer("settingDataviewContainer") {

      private static final long serialVersionUID = -497527332092449028L;

      @Override
      protected void onInitialize() {
         add(settingDataview);
         super.onInitialize();
      }
   };

   private BootstrapPagingNavigator settingPagingNavigator = new BootstrapPagingNavigator("settingPagingNavigator", settingDataview);

   private SettingViewOrEditPanel settingViewOrEditPanel = new SettingViewOrEditPanel("settingViewOrEditPanel", new Model<Setting>(new Setting()));

   public SettingPanel(final String id, final IModel<Setting> model) {
      super(id, model);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();

      settingDataProvider.setUser(roleSession.getUsername());
      settingDataProvider.setPassword(roleSession.getPassword());
      settingDataProvider.setSite(roleSession.getSite());
      settingDataProvider.setType((Setting) getDefaultModelObject());

      add(new AddAjaxLink());
      add(orderByProperty);
      add(orderByValue);
      add(orderByDescription);
      add(settingDataviewContainer.setOutputMarkupId(true));
      add(settingPagingNavigator);
      add(settingViewOrEditPanel.setOutputMarkupId(true));
   }
}
