package com.netbrasoft.gnuob.generic.customer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.CountCustomer;
import com.netbrasoft.gnuob.CountCustomerResponse;
import com.netbrasoft.gnuob.Customer;
import com.netbrasoft.gnuob.CustomerWebServiceImpl;
import com.netbrasoft.gnuob.CustomerWebServiceImplService;
import com.netbrasoft.gnuob.FindCustomer;
import com.netbrasoft.gnuob.FindCustomerById;
import com.netbrasoft.gnuob.FindCustomerByIdResponse;
import com.netbrasoft.gnuob.FindCustomerResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeCustomer;
import com.netbrasoft.gnuob.MergeCustomerResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistCustomer;
import com.netbrasoft.gnuob.PersistCustomerResponse;
import com.netbrasoft.gnuob.RefreshCustomer;
import com.netbrasoft.gnuob.RefreshCustomerResponse;
import com.netbrasoft.gnuob.RemoveCustomer;

@Repository("CustomerWebServiceRepository")
public class CustomerWebServiceRepository {

	private static final String GNUOB_CUSTOMER_WEB_SERVICE = System.getProperty("gnuob.customer-service.url", "http://localhost:8080/gnuob-soap/CustomerWebServiceImpl?wsdl");
	private CustomerWebServiceImpl customerWebServiceImpl;

	public CustomerWebServiceRepository() {
		try {
			CustomerWebServiceImplService customerWebServiceImplService = new CustomerWebServiceImplService(new URL(GNUOB_CUSTOMER_WEB_SERVICE));
			customerWebServiceImpl = customerWebServiceImplService.getCustomerWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		CountCustomer paramCountCustomer = new CountCustomer();
		paramCountCustomer.setCustomer(paramCustomer);
		CountCustomerResponse countCustomerResponse = customerWebServiceImpl.countCustomer(paramCountCustomer, paramMetaData);
		return countCustomerResponse.getReturn();
	}

	public Customer find(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		FindCustomerById paramFindCustomerById = new FindCustomerById();
		paramFindCustomerById.setCustomer(paramCustomer);
		FindCustomerByIdResponse findCustomerByIdResponse = customerWebServiceImpl.findCustomerById(paramFindCustomerById, paramMetaData);
		return findCustomerByIdResponse.getReturn();

	}

	public List<Customer> find(MetaData paramMetaData, Customer paramCustomer, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindCustomer paramFindCustomer = new FindCustomer();
		paramFindCustomer.setCustomer(paramCustomer);
		paramFindCustomer.setPaging(paramPaging);
		paramFindCustomer.setOrderBy(paramOrderBy);
		FindCustomerResponse findCustomerResponse = customerWebServiceImpl.findCustomer(paramFindCustomer, paramMetaData);
		return findCustomerResponse.getReturn();
	}

	public Customer merge(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		MergeCustomer paramMergeCustomer = new MergeCustomer();
		paramMergeCustomer.setCustomer(paramCustomer);
		MergeCustomerResponse mergeCustomerResponse = customerWebServiceImpl.mergeCustomer(paramMergeCustomer, paramMetaData);
		return mergeCustomerResponse.getReturn();
	}

	public Customer persist(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		PersistCustomer paramPersistCustomer = new PersistCustomer();
		paramPersistCustomer.setCustomer(paramCustomer);
		PersistCustomerResponse persistCustomerResponse = customerWebServiceImpl.persistCustomer(paramPersistCustomer, paramMetaData);
		return persistCustomerResponse.getReturn();
	}

	public Customer refresh(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		RefreshCustomer paramRefresCustomer = new RefreshCustomer();
		paramRefresCustomer.setCustomer(paramCustomer);
		RefreshCustomerResponse refresCustomerResponse = customerWebServiceImpl.refreshCustomer(paramRefresCustomer, paramMetaData);
		return refresCustomerResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Customer paramCustomer) throws GNUOpenBusinessServiceException_Exception {
		RemoveCustomer paramRemoveCustomer = new RemoveCustomer();
		paramRemoveCustomer.setCustomer(paramCustomer);
		customerWebServiceImpl.removeCustomer(paramRemoveCustomer, paramMetaData);
	}
}
