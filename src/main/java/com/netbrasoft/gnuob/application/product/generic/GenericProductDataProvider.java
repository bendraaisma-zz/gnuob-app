package com.netbrasoft.gnuob.application.product.generic;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.stereotype.Service;

import com.netbrasoft.gnuob.api.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.api.MetaData;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.Paging;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.product.ProductWebServiceRepository;
import com.netbrasoft.gnuob.application.NetbrasoftApplication;
import com.netbrasoft.gnuob.application.generic.GenericTypeDataProvider;

@Service("ProductDataProvider")
public class GenericProductDataProvider<P extends Product> extends SortableDataProvider<P, String> implements GenericTypeDataProvider<P> {

    private static ProductWebServiceRepository getProductWebServiceRepository() {
        NetbrasoftApplication app = (NetbrasoftApplication) WebApplication.get();

        return app.getProductWebServiceRepository();
    }

    private static final long serialVersionUID = -7147810111954260412L;

    private P product;
    private OrderBy orderBy;
    private MetaData metaData;

    @SuppressWarnings("unchecked")
    public GenericProductDataProvider() {
        this((P) new Product());
    }

    public GenericProductDataProvider(P product) {
        this(product, OrderBy.NONE);
        setSort("name", SortOrder.ASCENDING);
    }

    public GenericProductDataProvider(P product, OrderBy orderBy) {
        this.product = product;
        this.orderBy = orderBy;
        metaData = new MetaData();
    }

    @Override
    public void detach() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public P findById(P product) {
        try {
            return (P) getProductWebServiceRepository().find(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private OrderBy getOrderBy() {

        SortParam<String> sortOrder = getSort();

        switch (sortOrder.getProperty()) {

        case "name":
            orderBy = sortOrder.isAscending() ? OrderBy.TITLE_A_Z : OrderBy.TITLE_Z_A;
            break;
        case "amount":
            orderBy = sortOrder.isAscending() ? OrderBy.HIGHEST_PRICE : OrderBy.LOWEST_PRICE;
            break;
        case "recommended":
            orderBy = OrderBy.RECOMMENDED;
            break;
        case "rating":
            orderBy = sortOrder.isAscending() ? OrderBy.HIGHEST_RATING : OrderBy.LOWEST_RATING;
            break;
        case "discount":
            orderBy = sortOrder.isAscending() ? OrderBy.HIGHEST_DISCOUNT : OrderBy.LOWEST_DISCOUNT;
            break;
        case "bestseller":
            orderBy = OrderBy.BESTSELLERS;
            break;
        default:
            orderBy = OrderBy.LATEST_COLLECTION;
            break;
        }

        return orderBy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<? extends P> iterator(long first, long max) {
        try {
            Paging paramPaging = new Paging();
            paramPaging.setFirst((int) first);
            paramPaging.setMax((int) max);

            return (Iterator<? extends P>) getProductWebServiceRepository().find(metaData, product, paramPaging, getOrderBy()).iterator();
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public P merge(P product) {
        try {
            return (P) getProductWebServiceRepository().merge(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public IModel<P> model(P product) {
        return new GenericProductLoadableDetachableModel<P>(product);
    }

    @SuppressWarnings("unchecked")
    @Override
    public P persist(P product) {
        try {
            return (P) getProductWebServiceRepository().persist(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public P refresh(P product) {
        try {
            return (P) getProductWebServiceRepository().refresh(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void remove(P product) {
        try {
            getProductWebServiceRepository().remove(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public void setPassword(String password) {
        this.metaData.setPassword(password);
    }

    public void setProduct(P product) {
        this.product = product;
    }

    public void setSite(String site) {
        this.metaData.setSite(site);
    }

    public void setUser(String user) {
        this.metaData.setUser(user);
    }

    @Override
    public long size() {
        try {
            return getProductWebServiceRepository().count(metaData, product);
        } catch (GNUOpenBusinessServiceException_Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
