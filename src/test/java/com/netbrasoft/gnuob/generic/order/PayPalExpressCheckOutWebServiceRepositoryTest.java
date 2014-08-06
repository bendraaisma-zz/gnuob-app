package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.netbrasoft.gnuob.Address;
import com.netbrasoft.gnuob.Contract;
import com.netbrasoft.gnuob.Customer;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.Invoice;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.Order;
import com.netbrasoft.gnuob.OrderRecord;
import com.netbrasoft.gnuob.Product;
import com.netbrasoft.gnuob.Stock;
import com.netbrasoft.gnuob.generic.product.ProductWebServiceRepository;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class PayPalExpressCheckOutWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private ProductWebServiceRepository productWebServiceRepository = null;
	private PayPalExpressCheckOutWebServiceRepository payPalExpressCheckOutWebServiceRepository = null;
	private OrderWebServiceRepository orderWebServiceRepository = null;
	private MetaData paramMetaData = null;
	private Product paramProduct = null;

	@Test
	public void createNewOrderAndDoCheckout() throws GNUOpenBusinessServiceException_Exception {
		Customer customer = new Customer();
		Address address = new Address();

		address.setCityName("Zwolle");
		address.setStreet1("My street");

		customer.setBuyerEmail("bendraaisma@gmail.com");
		customer.setAdress(address);

		Contract contract = new Contract();

		contract.setCustomer(customer);

		Order paramOrder = new Order();
		OrderRecord orderRecord = new OrderRecord();

		orderRecord.setProduct(paramProduct);
		orderRecord.setQuantity(BigInteger.valueOf(2));

		paramOrder.getRecords().add(orderRecord);

		Invoice invoice = new Invoice();
		invoice.setAddress(address);

		paramOrder.setContract(contract);
		paramOrder.setInvoice(invoice);

		paramOrder = orderWebServiceRepository.persist(paramMetaData, paramOrder);
		Assert.assertTrue(paramOrder.getId() > 0);

		paramOrder = payPalExpressCheckOutWebServiceRepository.doCheckout(paramMetaData, paramOrder);
		Assert.assertNotNull(paramOrder.getToken());
	}

	@Before
	public void init() throws GNUOpenBusinessServiceException_Exception {
		productWebServiceRepository = new ProductWebServiceRepository();
		payPalExpressCheckOutWebServiceRepository = new PayPalExpressCheckOutWebServiceRepository();
		orderWebServiceRepository = new OrderWebServiceRepository();
		paramMetaData = Utils.paramMetaData();

		paramProduct = new Product();
		paramProduct.setName("New Zealand Auckland overhemd");
		paramProduct.setDescription("New Zealand Auckland overhemd");
		paramProduct.setNumber("C22_2B9_2B9_840196");
		paramProduct.setAmount(BigDecimal.valueOf(79.95));
		paramProduct.setTax(BigDecimal.valueOf(21));

		Stock stock = new Stock();
		stock.setMaxQuantity(BigInteger.valueOf(100));
		stock.setMinQuantity(BigInteger.ZERO);
		stock.setQuantity(BigInteger.valueOf(30));

		paramProduct.setStock(stock);

		paramProduct = productWebServiceRepository.persist(paramMetaData, paramProduct);
	}
}
