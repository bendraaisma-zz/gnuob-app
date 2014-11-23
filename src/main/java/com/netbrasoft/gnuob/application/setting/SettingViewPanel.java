package com.netbrasoft.gnuob.application.setting;

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

import com.netbrasoft.gnuob.api.Setting;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class SettingViewPanel extends Panel {
   
   private static final long serialVersionUID = 3703226064705246155L;
   
   private static final int ITEMS_PER_PAGE = 10;
   
   @SpringBean(name = "SettingDataProvider", required = true)
   private GenericTypeDataProvider<Setting> settingDataProvider;
   
   private OrderByBorder<String> orderByProperty = new OrderByBorder<String>("orderByProperty", "property", settingDataProvider);
   private OrderByBorder<String> orderByValue = new OrderByBorder<String>("orderByValue", "value", settingDataProvider);
   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", settingDataProvider);
   private DataView<Setting> settingDataview = new DataView<Setting>("settingDataview", settingDataProvider, ITEMS_PER_PAGE) {
      
      private static final long serialVersionUID = -5039874949058607907L;
      
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
               
            }
         });
      }
   };
   private ItemsPerPagePagingNavigator settingPagingNavigator = new ItemsPerPagePagingNavigator("settingPagingNavigator", settingDataview);
   
   public SettingViewPanel(String id) {
      super(id);
   }
   
   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();
      
      settingDataProvider.setUser(roleSession.getUsername());
      settingDataProvider.setPassword(roleSession.getPassword());
      settingDataProvider.setSite(roleSession.getSite());
      
      add(orderByProperty);
      add(orderByValue);
      add(orderByDescription);
      add(settingDataview);
      add(settingPagingNavigator);
   }
}
