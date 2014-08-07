package com.netbrasoft.gnuob.generic.product;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.netbrasoft.gnuob.api.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.api.MetaData;
import com.netbrasoft.gnuob.api.Paging;
import com.netbrasoft.gnuob.api.Product;
import com.netbrasoft.gnuob.api.Stock;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class ProductWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private MetaData paramMetaData = null;
	private ProductWebServiceRepository productWebServiceRepository = null;
	Product paramProduct = null;

	@Test
	public void createNewProduct() throws GNUOpenBusinessServiceException_Exception {
		paramProduct = new Product();
		paramProduct.setName("A New Zealand Auckland overhemd");
		paramProduct.setDescription("New Zealand Auckland overhemd");
		paramProduct.setNumber("C22_2B9_2B9_840196");
		paramProduct.setAmount(BigDecimal.valueOf(89.95));
		paramProduct.setTax(BigDecimal.valueOf(21));

		Stock stock = new Stock();
		stock.setMaxQuantity(BigInteger.valueOf(100));
		stock.setMinQuantity(BigInteger.ZERO);
		stock.setQuantity(BigInteger.valueOf(30));

		paramProduct.setStock(stock);

		paramProduct = productWebServiceRepository.persist(paramMetaData, paramProduct);

		Assert.assertTrue(paramProduct.getId() > 0);

		Paging paramPaging = new Paging();
		paramPaging.setFirst(0);
		paramPaging.setMax(2);
	}

	@Before
	public void init() throws GNUOpenBusinessServiceException_Exception {
		paramMetaData = Utils.paramMetaData();
		productWebServiceRepository = new ProductWebServiceRepository();
		// createNewProduct();
	}
}
