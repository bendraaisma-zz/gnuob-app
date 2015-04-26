package com.netbrasoft.gnuob.application;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.devutils.inspector.InspectorPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Service;

import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.authorization.UserRolesAuthorizer;
import com.netbrasoft.gnuob.application.page.LogoutPage;
import com.netbrasoft.gnuob.application.page.MainPage;

import de.agilecoders.wicket.core.Bootstrap;
import net.ftlines.wicketsource.WicketSource;

@Service("wicketApplication")
public class NetbrasoftApplication extends WebApplication {

   private static final String INSPECTOR_PAGE_HTML = "InspectorPage.html";

   @Override
   public Class<MainPage> getHomePage() {
      return MainPage.class;
   }

   @Override
   protected void init() {
      super.init();

      Bootstrap.install(this);

      getComponentInstantiationListeners().add(new SpringComponentInjector(this));
      getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRolesAuthorizer()));
      getApplicationSettings().setUploadProgressUpdatesEnabled(true);

      if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {

         mountPage(INSPECTOR_PAGE_HTML, InspectorPage.class);

         getDebugSettings().setDevelopmentUtilitiesEnabled(true);
         getDebugSettings().setAjaxDebugModeEnabled(true);

         WicketSource.configure(this);
      }

      mountPage("LogoutPage.html", LogoutPage.class);
   }

   @Override
   public Session newSession(Request request, Response response) {
      return new RolesSession(request);
   }
}
