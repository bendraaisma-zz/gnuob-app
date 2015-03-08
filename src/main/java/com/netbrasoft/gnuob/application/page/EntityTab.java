package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.category.CategoryTab;
import com.netbrasoft.gnuob.application.content.ContentTab;
import com.netbrasoft.gnuob.application.contract.ContractTab;
import com.netbrasoft.gnuob.application.customer.CustomerTab;
import com.netbrasoft.gnuob.application.offer.OfferTab;
import com.netbrasoft.gnuob.application.order.OrderTab;
import com.netbrasoft.gnuob.application.product.ProductTab;
import com.netbrasoft.gnuob.application.security.GroupTab;
import com.netbrasoft.gnuob.application.security.SiteTab;
import com.netbrasoft.gnuob.application.security.UserTab;
import com.netbrasoft.gnuob.application.setting.SettingTab;
import com.netbrasoft.gnuob.wicket.bootstrap.extensions.markup.html.tabs.BootstrapTabbedPanel;

public class EntityTab extends AbstractTab {

   private static final long serialVersionUID = 3355248856566175516L;

   private ITab categoryTab = new CategoryTab(new Model<String>("Category"));
   private ITab contentTab = new ContentTab(new Model<String>("Content"));
   private ITab contractTab = new ContractTab(new Model<String>("Contract"));
   private ITab customerTab = new CustomerTab(new Model<String>("Customer"));
   private ITab groupTab = new GroupTab(new Model<String>("Group"));
   private ITab offerTab = new OfferTab(new Model<String>("Offer"));
   private ITab orderTab = new OrderTab(new Model<String>("Order"));
   private ITab productTab = new ProductTab(new Model<String>("Product"));
   private ITab settingTab = new SettingTab(new Model<String>("Setting"));
   private ITab siteTab = new SiteTab(new Model<String>("Site"));
   private ITab userTab = new UserTab(new Model<String>("User"));

   public EntityTab(IModel<String> title) {
      super(title);
   }

   @Override
   public WebMarkupContainer getPanel(String panelId) {
      BootstrapTabbedPanel<ITab> entityTabbedPanel = new BootstrapTabbedPanel<ITab>(panelId, new ArrayList<ITab>()) {

         private static final long serialVersionUID = -8650291789763661400L;

         @Override
         public String getVariation() {
            return BootstrapTabbedPanel.PILLS_STACKED_VARIATION;
         };
      };

      entityTabbedPanel.getTabs().add(categoryTab);
      entityTabbedPanel.getTabs().add(contentTab);
      entityTabbedPanel.getTabs().add(contractTab);
      entityTabbedPanel.getTabs().add(customerTab);
      entityTabbedPanel.getTabs().add(groupTab);
      entityTabbedPanel.getTabs().add(offerTab);
      entityTabbedPanel.getTabs().add(orderTab);
      entityTabbedPanel.getTabs().add(productTab);
      entityTabbedPanel.getTabs().add(settingTab);
      entityTabbedPanel.getTabs().add(siteTab);
      entityTabbedPanel.getTabs().add(userTab);

      return entityTabbedPanel;
   }
}
