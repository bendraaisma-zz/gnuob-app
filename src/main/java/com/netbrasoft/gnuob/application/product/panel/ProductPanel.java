package com.netbrasoft.gnuob.application.product.panel;

import java.math.BigDecimal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.RangeValidator;

import com.netbrasoft.gnuob.Product;
import com.netbrasoft.gnuob.application.border.EntityBorder;
import com.netbrasoft.gnuob.application.paging.ItemsPerPagePagingNavigator;
import com.netbrasoft.gnuob.application.product.generic.GenericProductDataProvider;

public class ProductPanel extends Panel {

	private class ProductDataView extends DataView<Product> {

		private static final long serialVersionUID = -7766152164676332454L;
		private static final int ITEMS_PER_PAGE = 10;

		protected ProductDataView(String id, IDataProvider<Product> dataProvider) {
			super(id, dataProvider);
		}

		@Override
		public long getItemsPerPage() {
			return ITEMS_PER_PAGE;
		}

		@Override
		protected void populateItem(Item<Product> item) {
			Product product = item.getModelObject();

			item.add(new Label("name", product.getName()));
			item.add(new Label("number", product.getNumber()));
			item.add(new Label("amount", product.getAmount()));
			item.add(new Label("tax", product.getTax()));
			item.add(new Label("recommended", product.isRecommended()));
			item.add(new Label("rating", product.getRating()));
			item.add(new Label("discount", product.getDiscount()));
			item.add(new Label("bestseller", product.isBestsellers()));
			item.add(new Label("latestCollection", product.isLatestCollection()));
		}
	}

	private class ProductInputForm extends Form<Product> {

		private static final long serialVersionUID = -5675866937114244756L;

		public ProductInputForm(String id) {
			super(id, new CompoundPropertyModel<Product>(new Product()));
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			add(new TextField<String>("name").setRequired(true).setLabel(new Model<String>("Product name")));
			add(new TextArea<String>("description").setRequired(false).setLabel(new Model<String>("Product description")));
			add(new TextField<String>("number").setRequired(false).setLabel(new Model<String>("Product number")));
			add(new TextField<BigDecimal>("amount", BigDecimal.class).setRequired(true).setLabel(new Model<String>("Product amount")));
			add(new TextField<BigDecimal>("tax", BigDecimal.class).setRequired(true).add(new RangeValidator<BigDecimal>(BigDecimal.valueOf(0.0), BigDecimal.valueOf(100.0))).setLabel(new Model<String>("Product tax")));
			add(new TextField<BigDecimal>("itemWeight", BigDecimal.class).setRequired(false).setLabel(new Model<String>("Product item weight")));
			add(new TextField<String>("itemWeightUnit").setRequired(false).setLabel(new Model<String>("Product item weight Unit")));
			add(new TextField<BigDecimal>("itemLength", BigDecimal.class).setRequired(false).setLabel(new Model<String>("Product item length")));
			add(new TextField<String>("itemLengthUnit").setRequired(false).setLabel(new Model<String>("Product item length unit")));
			add(new TextField<BigDecimal>("itemWidth", BigDecimal.class).setRequired(false).setLabel(new Model<String>("Product item width")));
			add(new TextField<String>("itemWidthUnit").setRequired(false).setLabel(new Model<String>("Product item width unit")));
			add(new TextField<BigDecimal>("itemHeight", BigDecimal.class).setRequired(false).setLabel(new Model<String>("Product item height")));
			add(new TextField<String>("itemHeightUnit").setRequired(false).setLabel(new Model<String>("Product item height unit")));
			add(new TextField<String>("itemUrl").setRequired(false).setLabel(new Model<String>("Product item URL")));
			add(new TextField<BigDecimal>("rating", BigDecimal.class).setRequired(false).add(new RangeValidator<BigDecimal>(BigDecimal.valueOf(0.0), BigDecimal.valueOf(100.0))).setLabel(new Model<String>("Product rating")));
			add(new TextField<BigDecimal>("discount", BigDecimal.class).setRequired(false).add(new RangeValidator<BigDecimal>(BigDecimal.valueOf(0.0), BigDecimal.valueOf(100.0))).setLabel(new Model<String>("Product discount")));
			add(new CheckBox("recommended").setRequired(false).setLabel(new Model<String>("Product recommended")));
			add(new CheckBox("bestsellers").setRequired(false).setLabel(new Model<String>("Product bestseller")));
			add(new CheckBox("latestCollection").setRequired(false).setLabel(new Model<String>("Product latest collection")));

			add(new AjaxButton("saveButton") {

				private static final long serialVersionUID = -8020333568706595998L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {

				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				}
			});

			setOutputMarkupId(true);
		}
	}

	private class ProductOrderByBorder extends OrderByBorder<String> {

		private static final long serialVersionUID = 2521149256104535510L;

		public ProductOrderByBorder(String id, String property, ISortStateLocator<String> stateLocator) {
			super(id, property, stateLocator);
		}

	}

	private static final long serialVersionUID = 4247417332412794134L;

	private final GenericProductDataProvider<Product> genericTypeDataProvider = new GenericProductDataProvider<Product>();

	public ProductPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		ProductDataView productDataView = new ProductDataView("productTableRow", genericTypeDataProvider);

		ItemsPerPagePagingNavigator productPagingNavigator = new ItemsPerPagePagingNavigator("productPagingNavigator", productDataView);

		ProductOrderByBorder orderByNameProductOrderByBorder = new ProductOrderByBorder("orderByName", "name", genericTypeDataProvider);
		ProductOrderByBorder orderByAmountProductOrderByBorder = new ProductOrderByBorder("orderByAmount", "amount", genericTypeDataProvider);
		ProductOrderByBorder orderByRecommendedProductOrderByBorder = new ProductOrderByBorder("orderByRecommended", "recommended", genericTypeDataProvider);
		ProductOrderByBorder orderByRatingProductOrderByBorder = new ProductOrderByBorder("orderByRating", "rating", genericTypeDataProvider);
		ProductOrderByBorder orderByDiscountProductOrderByBorder = new ProductOrderByBorder("orderByDiscount", "discount", genericTypeDataProvider);
		ProductOrderByBorder orderByBestsellersProductOrderByBorder = new ProductOrderByBorder("orderByBestsellers", "bestseller", genericTypeDataProvider);
		ProductOrderByBorder orderByLatestCollectionProductOrderByBorder = new ProductOrderByBorder("orderByLatestCollection", "latestCollection", genericTypeDataProvider);

		ProductInputForm productInputForm = new ProductInputForm("productInputForm");

		EntityBorder entityBorder = new EntityBorder("entityBorder");
		entityBorder.add(productDataView);
		entityBorder.add(orderByNameProductOrderByBorder);
		entityBorder.add(orderByAmountProductOrderByBorder);
		entityBorder.add(orderByRecommendedProductOrderByBorder);
		entityBorder.add(orderByRatingProductOrderByBorder);
		entityBorder.add(orderByDiscountProductOrderByBorder);
		entityBorder.add(orderByBestsellersProductOrderByBorder);
		entityBorder.add(orderByLatestCollectionProductOrderByBorder);
		entityBorder.add(productPagingNavigator);

		entityBorder.add(productInputForm);

		add(entityBorder);
	}
}
