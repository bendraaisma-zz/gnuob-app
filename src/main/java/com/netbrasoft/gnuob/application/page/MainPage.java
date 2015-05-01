package com.netbrasoft.gnuob.application.page;

import java.util.ArrayList;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.application.page.tab.AdministrationTab;
import com.netbrasoft.gnuob.application.page.tab.AlertTab;
import com.netbrasoft.gnuob.application.page.tab.CrmTab;
import com.netbrasoft.gnuob.application.page.tab.PmTab;
import com.netbrasoft.gnuob.application.page.tab.ReportTab;
import com.netbrasoft.gnuob.application.security.AppRoles;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

@AuthorizeAction(action = Action.RENDER, roles = { AppRoles.ADMINISTRATOR, AppRoles.MANAGER, AppRoles.EMPLOYEE })
public class MainPage extends WebPage {

   private static final long serialVersionUID = 2104311609974795936L;
   private static final JavaScriptReferenceHeaderItem JS_VALIDATOR_REFERENCE = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("bootstrap-validator/0.8.1/dist/validator.min.js"));
   private static final JavaScriptReferenceHeaderItem JS_JQUERY_COOKIE = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("jquery.cookie/1.4.1/jquery.cookie.js"));
   private static final JavaScriptReferenceHeaderItem JS_BOOTSTRAP_3_DATEPICKER = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("bootstrap-3-datepicker/1.4.0/dist/js/bootstrap-datepicker.min.js"));
   private static final CssReferenceHeaderItem CSS_BOOTSTRAP_3_DATEPICKER = CssContentHeaderItem.forReference(new WebjarsCssResourceReference("bootstrap-3-datepicker/1.4.0/dist/css/bootstrap-datepicker3.min.css"));

   private final ITab crmTab = new CrmTab(new Model<String>("CRM"));
   private final ITab pmTab = new PmTab(new Model<String>("PM"));
   private final ITab alertTab = new AlertTab(new Model<String>("Alerts"));
   private final ITab reportTab = new ReportTab(new Model<String>("Reports"));
   private final ITab administrationTab = new AdministrationTab(new Model<String>("Administration"));

   private final BootstrapTabbedPanel<ITab> mainMenuTabbedPanel = new BootstrapTabbedPanel<ITab>("mainMenuTabbedPanel", new ArrayList<ITab>());

   private final ContentBorder contentBorder = new ContentBorder("contentBorder");

   @Override
   protected void onInitialize() {
      add(new Label("title", "Netbrasoft.com"));

      mainMenuTabbedPanel.getTabs().add(crmTab);
      mainMenuTabbedPanel.getTabs().add(pmTab);
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
      response.render(JS_BOOTSTRAP_3_DATEPICKER);
      response.render(CSS_BOOTSTRAP_3_DATEPICKER);

      super.renderHead(response);
   }
}
