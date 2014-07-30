package com.netbrasoft.gnuob.generic.product;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.CountProduct;
import com.netbrasoft.gnuob.CountProductResponse;
import com.netbrasoft.gnuob.FindProduct;
import com.netbrasoft.gnuob.FindProductById;
import com.netbrasoft.gnuob.FindProductByIdResponse;
import com.netbrasoft.gnuob.FindProductResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeProduct;
import com.netbrasoft.gnuob.MergeProductResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistProduct;
import com.netbrasoft.gnuob.PersistProductResponse;
import com.netbrasoft.gnuob.Product;
import com.netbrasoft.gnuob.ProductWebServiceImpl;
import com.netbrasoft.gnuob.ProductWebServiceImplService;
import com.netbrasoft.gnuob.RefreshProduct;
import com.netbrasoft.gnuob.RefreshProductResponse;
import com.netbrasoft.gnuob.RemoveProduct;

@Repository("ProductWebServiceRepository")
public class ProductWebServiceRepository {

	private static final String GNUOB_PRODUCT_WEB_SERVICE = System.getProperty("gnuob.product-service.url", "http://localhost:8080/gnuob-soap/ProductWebServiceImpl?wsdl");
	private ProductWebServiceImpl productWebServiceImpl;

	public ProductWebServiceRepository() {
		try {
			ProductWebServiceImplService productWebServiceImplService = new ProductWebServiceImplService(new URL(GNUOB_PRODUCT_WEB_SERVICE));
			productWebServiceImpl = productWebServiceImplService.getProductWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		CountProduct paramCountProduct = new CountProduct();
		paramCountProduct.setProduct(paramProduct);
		CountProductResponse countProductResponse = productWebServiceImpl.countProduct(paramCountProduct, paramMetaData);
		return countProductResponse.getReturn();
	}

	public Product find(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		FindProductById paramFindProductById = new FindProductById();
		paramFindProductById.setProduct(paramProduct);
		FindProductByIdResponse findProductByIdResponse = productWebServiceImpl.findProductById(paramFindProductById, paramMetaData);
		return findProductByIdResponse.getReturn();

	}

	public List<Product> find(MetaData paramMetaData, Product paramProduct, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindProduct paramFindProduct = new FindProduct();
		paramFindProduct.setProduct(paramProduct);
		paramFindProduct.setPaging(paramPaging);
		paramFindProduct.setOrderBy(paramOrderBy);
		FindProductResponse findProductResponse = productWebServiceImpl.findProduct(paramFindProduct, paramMetaData);

		return findProductResponse.getReturn();
	}

	public Product merge(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		MergeProduct paramMergeProduct = new MergeProduct();
		paramMergeProduct.setProduct(paramProduct);
		MergeProductResponse mergeProductResponse = productWebServiceImpl.mergeProduct(paramMergeProduct, paramMetaData);
		return mergeProductResponse.getReturn();
	}

	public Product persist(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		PersistProduct paramPersistProduct = new PersistProduct();
		paramPersistProduct.setProduct(paramProduct);
		PersistProductResponse persistProductResponse = productWebServiceImpl.persistProduct(paramPersistProduct, paramMetaData);
		return persistProductResponse.getReturn();
	}

	public Product refresh(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		RefreshProduct paramRefresProduct = new RefreshProduct();
		paramRefresProduct.setProduct(paramProduct);
		RefreshProductResponse refresProductResponse = productWebServiceImpl.refreshProduct(paramRefresProduct, paramMetaData);
		return refresProductResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Product paramProduct) throws GNUOpenBusinessServiceException_Exception {
		RemoveProduct paramRemoveProduct = new RemoveProduct();
		paramRemoveProduct.setProduct(paramProduct);
		productWebServiceImpl.removeProduct(paramRemoveProduct, paramMetaData);
	}
}
