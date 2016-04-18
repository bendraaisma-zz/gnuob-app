/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.application.border;

import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.FOOTER_PANEL_ID;
import static com.netbrasoft.gnuob.application.NetbrasoftApplicationConstants.HEADER_PANEL_ID;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.FooterPanel;
import com.netbrasoft.gnuob.application.panel.HeaderPanel;

public class ContentBorder extends Border {

  private static final long serialVersionUID = 6569587142042286311L;

  private final HeaderPanel headerPanel;
  private final FooterPanel footerPanel;

  public ContentBorder(final String id) {
    super(id);
    headerPanel = new HeaderPanel(HEADER_PANEL_ID);
    footerPanel = new FooterPanel(FOOTER_PANEL_ID);
  }

  @Override
  protected void onInitialize() {
    addToBorder(headerPanel.setOutputMarkupId(true));
    addToBorder(footerPanel.setOutputMarkupId(true));
    super.onInitialize();
  }
}
