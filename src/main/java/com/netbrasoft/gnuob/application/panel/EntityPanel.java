package com.netbrasoft.gnuob.application.panel;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import com.netbrasoft.gnuob.application.page.EntitiesPage;
import com.netbrasoft.gnuob.application.product.page.ProductPage;

public class EntityPanel extends Panel {

	private static final long serialVersionUID = -781157374524823043L;

	public EntityPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		MarkupContainer contractListItem = new WebMarkupContainer("contractListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof EntitiesPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer customerListItem = new WebMarkupContainer("customerListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer orderListItem = new WebMarkupContainer("orderListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer offerListItem = new WebMarkupContainer("offerListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer productListItem = new WebMarkupContainer("productListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer categoryListItem = new WebMarkupContainer("categoryListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer contentListItem = new WebMarkupContainer("contentListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		MarkupContainer settingListItem = new WebMarkupContainer("settingListItem") {

			private static final long serialVersionUID = -5576414809606952223L;

			@Override
			protected void onInitialize() {
				super.onInitialize();

				if (getPage() instanceof ProductPage) {
					add(new AttributeAppender("class", "active"));
				}
			}
		};

		add(contractListItem);
		add(customerListItem);
		add(orderListItem);
		add(offerListItem);
		add(productListItem);
		add(categoryListItem);
		add(contentListItem);
		add(settingListItem);
	}

}
