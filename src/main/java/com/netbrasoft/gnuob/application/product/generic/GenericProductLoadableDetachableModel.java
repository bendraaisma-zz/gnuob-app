package com.netbrasoft.gnuob.application.product.generic;

import org.apache.wicket.model.LoadableDetachableModel;

import com.netbrasoft.gnuob.Product;

public class GenericProductLoadableDetachableModel<P extends Product> extends LoadableDetachableModel<P> {

	private static final long serialVersionUID = 2803887803933459687L;

	private final P product;

	public GenericProductLoadableDetachableModel(P product) {
		this.product = product;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof GenericProductLoadableDetachableModel) {
			GenericProductLoadableDetachableModel<?> other = (GenericProductLoadableDetachableModel<?>) obj;
			return other.product.getId() == product.getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(product.getId()).hashCode();
	}

	@Override
	protected P load() {
		return product;
	}

}
