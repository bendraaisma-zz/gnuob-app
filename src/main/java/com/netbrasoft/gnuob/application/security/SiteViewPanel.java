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

import com.netbrasoft.gnuob.api.Site;
import com.netbrasoft.gnuob.api.generic.GenericTypeDataProvider;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;

public class SiteViewPanel extends Panel {
   
   private static final long serialVersionUID = 3703226064705246155L;
   
   private static final int ITEMS_PER_PAGE = 10;
   
   @SpringBean(name = "SiteDataProvider", required = true)
   private GenericTypeDataProvider<Site> siteDataProvider;
   
   private OrderByBorder<String> orderByName = new OrderByBorder<String>("orderByName", "name", siteDataProvider);
   private OrderByBorder<String> orderByDescription = new OrderByBorder<String>("orderByDescription", "description", siteDataProvider);
   private DataView<Site> siteDataview = new DataView<Site>("siteDataview", siteDataProvider, ITEMS_PER_PAGE) {
      
      private static final long serialVersionUID = -5039874949058607907L;
      
      @Override
      protected void populateItem(Item<Site> paramItem) {
         paramItem.setModel(new CompoundPropertyModel<Site>(paramItem.getModelObject()));
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
   private ItemsPerPagePagingNavigator sitePagingNavigator = new ItemsPerPagePagingNavigator("sitePagingNavigator", siteDataview);
   
   public SiteViewPanel(String id) {
      super(id);
   }
   
   @Override
   protected void onInitialize() {
      super.onInitialize();
      RolesSession roleSession = (RolesSession) Session.get();
      
      siteDataProvider.setUser(roleSession.getUsername());
      siteDataProvider.setPassword(roleSession.getPassword());
      siteDataProvider.setSite(roleSession.getSite());
      
      add(orderByName);
      add(orderByDescription);
      add(siteDataview);
      add(sitePagingNavigator);
   }
}
