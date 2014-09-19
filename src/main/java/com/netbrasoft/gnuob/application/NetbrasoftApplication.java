package com.netbrasoft.gnuob.application;

import net.ftlines.wicketsource.WicketSource;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netbrasoft.gnuob.api.category.CategoryWebServiceRepository;
import com.netbrasoft.gnuob.api.content.ContentWebServiceRepository;
import com.netbrasoft.gnuob.api.contract.ContractWebServiceRepository;
import com.netbrasoft.gnuob.api.customer.CustomerWebServiceRepository;
import com.netbrasoft.gnuob.api.offer.OfferWebServiceRepository;
import com.netbrasoft.gnuob.api.order.OrderWebServiceRepository;
import com.netbrasoft.gnuob.api.order.PayPalExpressCheckOutWebServiceRepository;
import com.netbrasoft.gnuob.api.product.ProductWebServiceRepository;
import com.netbrasoft.gnuob.api.security.GroupWebServiceRepository;
import com.netbrasoft.gnuob.api.security.SiteWebServiceRepository;
import com.netbrasoft.gnuob.api.security.UserWebServiceRepository;
import com.netbrasoft.gnuob.api.setting.SettingWebServiceRepository;
import com.netbrasoft.gnuob.application.authorization.RolesSession;
import com.netbrasoft.gnuob.application.authorization.UserRolesAuthorizer;
import com.netbrasoft.gnuob.application.category.page.CategoryPage;
import com.netbrasoft.gnuob.application.content.page.ContentPage;
import com.netbrasoft.gnuob.application.contract.page.ContractPage;
import com.netbrasoft.gnuob.application.customer.page.CustomerPage;
import com.netbrasoft.gnuob.application.offer.page.OfferPage;
import com.netbrasoft.gnuob.application.order.page.OrderPage;
import com.netbrasoft.gnuob.application.page.LogoutPage;
import com.netbrasoft.gnuob.application.product.page.ProductPage;
import com.netbrasoft.gnuob.application.setting.page.SettingPage;

@Service("wicketApplication")
public class NetbrasoftApplication extends WebApplication {

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

	@Autowired(required = true)
	private SettingWebServiceRepository settingWebServiceRepository;

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
		return ContractPage.class;
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

	public SettingWebServiceRepository getSettingWebServiceRepository() {
		return settingWebServiceRepository;
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

		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRolesAuthorizer()));

		if ("true".equalsIgnoreCase(System.getProperty("gnuob.debug.enabled", "false"))) {
			WicketSource.configure(this);
		}

		mountPage("ContractPage.html", ContractPage.class);
		mountPage("CustomerPage.html", CustomerPage.class);
		mountPage("OrderPage.html", OrderPage.class);
		mountPage("OfferPage.html", OfferPage.class);
		mountPage("ProductPage.html", ProductPage.class);
		mountPage("CategoryPage.html", CategoryPage.class);
		mountPage("ContentPage.html", ContentPage.class);
		mountPage("SettingPage.html", SettingPage.class);
		mountPage("LogoutPage.html", LogoutPage.class);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new RolesSession(request);
	}
}
