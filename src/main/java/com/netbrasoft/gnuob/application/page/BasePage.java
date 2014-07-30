package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.markup.head.CssUrlReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptUrlReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;

public abstract class BasePage extends WebPage {

	private static final long serialVersionUID = 2104311609974795936L;

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));

		// TODO deze regel verwijderen zodra wicket bootstrap mbv resource deze URL's kan herleiden.
		response.render(new CssUrlReferenceHeaderItem("//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css", "", ""));
		response.render(new CssUrlReferenceHeaderItem("//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css", "", ""));
		//response.render(new JavaScriptUrlReferenceHeaderItem("//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js", "bootstrap", false, "UTF-8", ""));

		response.render(new JavaScriptUrlReferenceHeaderItem("https://raw.github.com/carhartl/jquery-cookie/master/jquery.cookie.js", "jquery.cookie", false, "UTF-8", ""));
	}
}
