package com.netbrasoft.gnuob.application.customer.page;

import org.apache.wicket.markup.head.IHeaderResponse;

import com.netbrasoft.gnuob.application.border.ContentBorder;
import com.netbrasoft.gnuob.application.page.BasePage;
import com.netbrasoft.gnuob.application.product.panel.ProductPanel;

public class CustomerPage extends BasePage {

	private static final long serialVersionUID = 8809237733214492296L;

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