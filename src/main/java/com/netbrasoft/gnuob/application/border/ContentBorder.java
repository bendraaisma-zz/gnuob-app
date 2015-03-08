package com.netbrasoft.gnuob.application.border;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.FooterPanel;
import com.netbrasoft.gnuob.application.panel.HeaderPanel;

public class ContentBorder extends Border {

   private static final long serialVersionUID = 6569587142042286311L;
   private static final HeaderPanel headerPanel = new HeaderPanel("headerPanel");
   private static final FooterPanel footerPanel = new FooterPanel("footerPanel");

   public ContentBorder(String id) {
      super(id);
   }

   @Override
   protected void onInitialize() {
      super.onInitialize();
      addToBorder(headerPanel);
      addToBorder(footerPanel);
   }
}
