package com.netbrasoft.gnuob.application.border;

import org.apache.wicket.markup.html.border.Border;

import com.netbrasoft.gnuob.application.panel.EntityPanel;

public class EntityBorder extends Border {

    private static final long serialVersionUID = 7784950540079689156L;

    public EntityBorder(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        EntityPanel entityPanel = new EntityPanel("entityPanel");
        addToBorder(entityPanel);
    }

}
