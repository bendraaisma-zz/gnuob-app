package com.netbrasoft.gnuob.application.page;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import com.netbrasoft.gnuob.application.NetbrasoftApplication;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public abstract class BasePage extends WebPage {

   private static final long serialVersionUID = 8192334293970678397L;

   private static final String GNUOB_SITE_TITLE_PROPERTY = "gnuob.site.title";

   private static final JavaScriptReferenceHeaderItem JS_JQUERY_COOKIE = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"));

   private static final JavaScriptReferenceHeaderItem JS_BOOTSTRAP_3_DATEPICKER = JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("/ajax/libs/bootstrap-datepicker/1.4.0/js/bootstrap-datepicker.min.js"));

   private static final CssReferenceHeaderItem CSS_BOOTSTRAP_3_DATEPICKER = CssContentHeaderItem.forReference(new WebjarsCssResourceReference("/ajax/libs/bootstrap-datepicker/1.4.0/css/bootstrap-datepicker.min.css"));

   private static final JavaScriptReferenceHeaderItem JS_JQUERY = JavaScriptHeaderItem.forReference(NetbrasoftApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

   @Override
   protected void onInitialize() {
      final String site = getRequest().getClientUrl().getHost();
      final String title = site.replaceFirst("www.", "").split("\\.")[0];

      add(new Label(GNUOB_SITE_TITLE_PROPERTY, System.getProperty(GNUOB_SITE_TITLE_PROPERTY, WordUtils.capitalize(title))));

      super.onInitialize();
   }

   @Override
   public void renderHead(IHeaderResponse response) {
      response.render(JS_JQUERY);
      response.render(JS_JQUERY_COOKIE);
      //TODO: BD use wicket bootstrap data picker.
      response.render(JS_BOOTSTRAP_3_DATEPICKER);
      response.render(CSS_BOOTSTRAP_3_DATEPICKER);

      super.renderHead(response);
   }
}
