package com.netbrasoft.gnuob.application.border;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.FooterPanel;
import com.netbrasoft.gnuob.application.panel.HeaderPanel;
import com.netbrasoft.gnuob.application.panel.NavigationPanel;

public class ContentBorder extends Border {

	private static final long serialVersionUID = 6569587142042286311L;

	public ContentBorder(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		HeaderPanel headerPanel = new HeaderPanel("headerPanel");
		NavigationPanel categoryPanel = new NavigationPanel("navigationPanel");
		FooterPanel footerPanel = new FooterPanel("footerPanel");

		addToBorder(headerPanel);
		addToBorder(categoryPanel);
		addToBorder(footerPanel);
	}

}
