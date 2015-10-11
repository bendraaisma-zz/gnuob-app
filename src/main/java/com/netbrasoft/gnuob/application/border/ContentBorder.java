package com.netbrasoft.gnuob.application.border;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.FooterPanel;
import com.netbrasoft.gnuob.application.panel.HeaderPanel;

public class ContentBorder extends Border {

  private static final long serialVersionUID = 6569587142042286311L;

  private final HeaderPanel headerPanel;
  private final FooterPanel footerPanel;

  public ContentBorder(final String id) {
    super(id);
    headerPanel = new HeaderPanel("headerPanel");
    footerPanel = new FooterPanel("footerPanel");
  }

  @Override
  protected void onInitialize() {
    addToBorder(headerPanel);
    addToBorder(footerPanel);
    super.onInitialize();
  }
}
