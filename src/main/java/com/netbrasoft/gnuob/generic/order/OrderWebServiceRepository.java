package com.netbrasoft.gnuob.generic.order;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.CountOrder;
import com.netbrasoft.gnuob.CountOrderResponse;
import com.netbrasoft.gnuob.FindOrder;
import com.netbrasoft.gnuob.FindOrderById;
import com.netbrasoft.gnuob.FindOrderByIdResponse;
import com.netbrasoft.gnuob.FindOrderResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeOrder;
import com.netbrasoft.gnuob.MergeOrderResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.Order;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.OrderWebServiceImpl;
import com.netbrasoft.gnuob.OrderWebServiceImplService;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistOrder;
import com.netbrasoft.gnuob.PersistOrderResponse;
import com.netbrasoft.gnuob.RefreshOrder;
import com.netbrasoft.gnuob.RefreshOrderResponse;
import com.netbrasoft.gnuob.RemoveOrder;

@Repository("OrderWebServiceRepository")
public class OrderWebServiceRepository {

	private static final String GNUOB_ORDER_WEB_SERVICE = System.getProperty("gnuob.order-service.url", "http://localhost:8080/gnuob-soap/OrderWebServiceImpl?wsdl");
	private OrderWebServiceImpl orderWebServiceImpl;

	public OrderWebServiceRepository() {
		try {
			OrderWebServiceImplService orderWebServiceImplService = new OrderWebServiceImplService(new URL(GNUOB_ORDER_WEB_SERVICE));
			orderWebServiceImpl = orderWebServiceImplService.getOrderWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		CountOrder paramCountOrder = new CountOrder();
		paramCountOrder.setOrder(paramOrder);
		CountOrderResponse countOrderResponse = orderWebServiceImpl.countOrder(paramCountOrder, paramMetaData);
		return countOrderResponse.getReturn();
	}

	public Order find(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		FindOrderById paramFindOrderById = new FindOrderById();
		paramFindOrderById.setOrder(paramOrder);
		FindOrderByIdResponse findOrderByIdResponse = orderWebServiceImpl.findOrderById(paramFindOrderById, paramMetaData);
		return findOrderByIdResponse.getReturn();

	}

	public List<Order> find(MetaData paramMetaData, Order paramOrder, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindOrder paramFindOrder = new FindOrder();
		paramFindOrder.setOrder(paramOrder);
		paramFindOrder.setPaging(paramPaging);
		paramFindOrder.setOrderBy(paramOrderBy);
		FindOrderResponse findOrderResponse = orderWebServiceImpl.findOrder(paramFindOrder, paramMetaData);
		return findOrderResponse.getReturn();
	}

	public Order merge(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		MergeOrder paramMergeOrder = new MergeOrder();
		paramMergeOrder.setOrder(paramOrder);
		MergeOrderResponse mergeOrderResponse = orderWebServiceImpl.mergeOrder(paramMergeOrder, paramMetaData);
		return mergeOrderResponse.getReturn();
	}

	public Order persist(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		PersistOrder paramPersistOrder = new PersistOrder();
		paramPersistOrder.setOrder(paramOrder);
		PersistOrderResponse persistOrderResponse = orderWebServiceImpl.persistOrder(paramPersistOrder, paramMetaData);
		return persistOrderResponse.getReturn();
	}

	public Order refresh(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		RefreshOrder paramRefreshOrder = new RefreshOrder();
		paramRefreshOrder.setOrder(paramOrder);
		RefreshOrderResponse refreshOrderResponse = orderWebServiceImpl.refreshOrder(paramRefreshOrder, paramMetaData);
		return refreshOrderResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		RemoveOrder paramRemoveOrder = new RemoveOrder();
		paramRemoveOrder.setOrder(paramOrder);
		orderWebServiceImpl.removeOrder(paramRemoveOrder, paramMetaData);
	}
}
