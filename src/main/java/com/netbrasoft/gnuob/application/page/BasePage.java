package com.netbrasoft.gnuob.application.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssUrlReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptUrlReferenceHeaderItem;
import org.apache.wicket.markup.html.WebPage;

@AuthorizeInstantiation("Administrator")
public abstract class BasePage extends WebPage {

    private static final long serialVersionUID = 2104311609974795936L;

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
        response.render(new CssUrlReferenceHeaderItem("./css/bootstrap.css", "", ""));
        response.render(new CssUrlReferenceHeaderItem("./css/bootstrap-theme.css", "", ""));
        response.render(new JavaScriptUrlReferenceHeaderItem("./js/bootstrap.min.js", "bootstrap", false, "UTF-8", ""));
        response.render(new JavaScriptUrlReferenceHeaderItem("./js/jcookie.js", "jquery.cookie", false, "UTF-8", ""));
    }
}
