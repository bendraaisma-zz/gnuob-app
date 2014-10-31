package com.netbrasoft.gnuob.application.generic;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class HighlitableDataItem<T> extends Item<T> {

    private static final long serialVersionUID = -5451276403258662262L;

    private boolean highlite = false;

    public HighlitableDataItem(String id, int index, IModel<T> model) {
        super(id, index, model);
    }

    public boolean isHighLite() {
        return highlite;
    }

    public void toggleHighlite() {
        highlite = !highlite;
    }
}
