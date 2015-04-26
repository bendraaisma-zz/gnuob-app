package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.border.ContentBorder;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class MainPage extends WebPage {

   private static final long serialVersionUID = 2104311609974795936L;
   private static final String GNUOB_SITE_TITLE = "title";
   private static final JavaScriptReferenceHeaderItem JS_VALIDATOR_REFERENCE = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("bootstrap-validator/0.8.1/dist/validator.min.js"));
   private static final JavaScriptReferenceHeaderItem JS_JQUERY_COOKIE = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("jquery.cookie/1.4.1/jquery.cookie.js"));

   private final ITab entityTab = new EntityTab(new Model<String>("Entities"));
   private final ITab crmTab = new CrmTab(new Model<String>("CRM"));
   private final ITab accountancyTab = new AccountancyTab(new Model<String>("Accountancy"));
   private final ITab alertTab = new AlertTab(new Model<String>("Alerts"));
   private final ITab reportTab = new ReportTab(new Model<String>("Reports"));
   private final ITab administrationTab = new AdministrationTab(new Model<String>("Administration"));

   private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());

   private final ContentBorder contentBorder = new ContentBorder("contentBorder");

   @Override
   protected void onInitialize() {
      add(new Label(GNUOB_SITE_TITLE, getString("gnuob.site.title")));

      mainMenuTabbedPanel.getTabs().add(entityTab);
      mainMenuTabbedPanel.getTabs().add(crmTab);
      mainMenuTabbedPanel.getTabs().add(accountancyTab);
      mainMenuTabbedPanel.getTabs().add(alertTab);
      mainMenuTabbedPanel.getTabs().add(reportTab);
      mainMenuTabbedPanel.getTabs().add(administrationTab);

      contentBorder.add(mainMenuTabbedPanel);
      add(contentBorder);

      super.onInitialize();
   }

   @Override
   public void renderHead(IHeaderResponse response) {
      response.render(JS_VALIDATOR_REFERENCE);
      response.render(JS_JQUERY_COOKIE);

      super.renderHead(response);
   }
}
