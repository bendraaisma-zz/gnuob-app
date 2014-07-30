package com.netbrasoft.gnuob.application;

import net.ftlines.wicketsource.WicketSource;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netbrasoft.gnuob.application.page.EntitiesPage;
import com.netbrasoft.gnuob.application.product.page.ProductPage;
import com.netbrasoft.gnuob.generic.category.CategoryWebServiceRepository;
import com.netbrasoft.gnuob.generic.content.ContentWebServiceRepository;
import com.netbrasoft.gnuob.generic.contract.ContractWebServiceRepository;
import com.netbrasoft.gnuob.generic.customer.CustomerWebServiceRepository;
import com.netbrasoft.gnuob.generic.offer.OfferWebServiceRepository;
import com.netbrasoft.gnuob.generic.order.OrderWebServiceRepository;
import com.netbrasoft.gnuob.generic.order.PayPalExpressCheckOutWebServiceRepository;
import com.netbrasoft.gnuob.generic.product.ProductWebServiceRepository;
import com.netbrasoft.gnuob.generic.security.GroupWebServiceRepository;
import com.netbrasoft.gnuob.generic.security.SiteWebServiceRepository;
import com.netbrasoft.gnuob.generic.security.UserWebServiceRepository;

@Service("wicketApplication")
public class NetbrasoftApplication extends WebApplication {

	private static final String BOOTSTRAP_MIN_JS = "//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js";

	private static final String BOOTSTRAP_MIN_CSS = "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css";

	@Autowired(required = true)
	private CategoryWebServiceRepository categoryWebServiceRepository;

	@Autowired(required = true)
	private PayPalExpressCheckOutWebServiceRepository payPalExpressCheckOutWebServiceRepository;

	@Autowired(required = true)
	private OrderWebServiceRepository orderWebServiceRepository;

	@Autowired(required = true)
	private ProductWebServiceRepository productWebServiceRepository;

	@Autowired(required = true)
	private OfferWebServiceRepository offerWebServiceRepository;

	@Autowired(required = true)
	private ContentWebServiceRepository contentWebServiceRepository;

	@Autowired(required = true)
	private ContractWebServiceRepository contractWebServiceRepository;

	@Autowired(required = true)
	private CustomerWebServiceRepository customerWebServiceRepository;

	@Autowired(required = true)
	private SiteWebServiceRepository siteWebServiceRepository;

	@Autowired(required = true)
	private UserWebServiceRepository userWebServiceRepository;

	@Autowired(required = true)
	private GroupWebServiceRepository groupWebServiceRepository;

	public CategoryWebServiceRepository getCategoryWebServiceRepository() {
		return categoryWebServiceRepository;
	}

	public ContentWebServiceRepository getContentWebServiceRepository() {
		return contentWebServiceRepository;
	}

	public ContractWebServiceRepository getContractWebServiceRepository() {
		return contractWebServiceRepository;
	}

	public CustomerWebServiceRepository getCustomerWebServiceRepository() {
		return customerWebServiceRepository;
	}

	public GroupWebServiceRepository getGroupWebServiceRepository() {
		return groupWebServiceRepository;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return EntitiesPage.class;
	}

	public OfferWebServiceRepository getOfferWebServiceRepository() {
		return offerWebServiceRepository;
	}

	public OrderWebServiceRepository getOrderWebServiceRepository() {
		return orderWebServiceRepository;
	}

	public PayPalExpressCheckOutWebServiceRepository getPayPalExpressCheckOutWebServiceRepository() {
		return payPalExpressCheckOutWebServiceRepository;
	}

	public ProductWebServiceRepository getProductWebServiceRepository() {
		return productWebServiceRepository;
	}

	public SiteWebServiceRepository getSiteWebServiceRepository() {
		return siteWebServiceRepository;
	}

	public UserWebServiceRepository getUserWebServiceRepository() {
		return userWebServiceRepository;
	}

	@Override
	protected void init() {
		super.init();

		if ("true".equalsIgnoreCase(System.getProperty("gnuob.debug.enabled", "false"))) {
			WicketSource.configure(this);
		}

		mountPage("ProductPage.html", ProductPage.class);
		mountPage("EntitiesPage.html", EntitiesPage.class);
	}
}
