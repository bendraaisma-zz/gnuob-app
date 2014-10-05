package com.netbrasoft.gnuob.application.generic;

import org.apache.wicket.markup.repeater.data.IDataProvider;

import com.netbrasoft.gnuob.api.GNUOpenBusinessServiceException_Exception;

public interface GenericTypeDataProvider<T> extends IDataProvider<T> {

    T findById(T type);

    T merge(T type) throws GNUOpenBusinessServiceException_Exception;

    T persist(T type);

    T refresh(T type);

    void remove(T type);
}
