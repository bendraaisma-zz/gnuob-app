package com.netbrasoft.gnuob.application.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;

import com.netbrasoft.gnuob.application.authorization.RolesSession;

public class HeaderPanel extends Panel {

   private static final long serialVersionUID = 3137234732197409313L;

   public HeaderPanel(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() throws RuntimeException {
      try {
         super.onInitialize();

         RolesSession roleSession = (RolesSession) Session.get();
         ServletContext application = WebApplication.get().getServletContext();
         InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
         Attributes attributes = new Manifest(inputStream).getMainAttributes();

         add(new Label("implementationVendor", attributes.getValue("Implementation-Vendor")));
         add(new Label("implementationTitle", attributes.getValue("Implementation-Title")));
         add(new Label("implementationVersion", attributes.getValue("Implementation-Version")));
         add(new Label("login", roleSession.getUsername()));
      } catch (IOException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }
}
