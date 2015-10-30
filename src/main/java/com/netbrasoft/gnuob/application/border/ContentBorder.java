package com.netbrasoft.gnuob.application.border;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.FooterPanel;
import com.netbrasoft.gnuob.application.panel.HeaderPanel;

public class ContentBorder extends Border {

  private static final long serialVersionUID = 6569587142042286311L;

  public ContentBorder(final String id) {
    super(id);
  }

  @Override
  protected void onInitialize() {
    addToBorder(new HeaderPanel("headerPanel"));
    addToBorder(new FooterPanel("footerPanel"));
    super.onInitialize();
  }
}
