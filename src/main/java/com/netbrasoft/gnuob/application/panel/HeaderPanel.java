package com.netbrasoft.gnuob.application.panel;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.netbrasoft.gnuob.application.authorization.RolesSession;

public class HeaderPanel extends Panel {

    private static final long serialVersionUID = 3137234732197409313L;

    public HeaderPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        RolesSession roleSession = (RolesSession) Session.get();

        add(new Label("title", "Netbrasoft GNU Open Business Platform"));
        add(new Label("username", roleSession.getUsername()));
    }
}
