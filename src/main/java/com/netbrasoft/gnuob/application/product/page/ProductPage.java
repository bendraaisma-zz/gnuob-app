package com.netbrasoft.gnuob.application.product.page;

import org.apache.wicket.markup.head.IHeaderResponse;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.application.page.BasePage;
import com.netbrasoft.gnuob.application.product.panel.ProductPanel;

public class ProductPage extends BasePage {

    private static final long serialVersionUID = -4939575689434256761L;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ContentBorder contentBorder = new ContentBorder("contentBorder");
        ProductPanel productPanel = new ProductPanel("productPanel");

        contentBorder.add(productPanel);
        add(contentBorder);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}
