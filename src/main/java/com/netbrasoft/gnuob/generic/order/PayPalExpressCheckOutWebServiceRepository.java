package com.netbrasoft.gnuob.generic.order;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.DoCheckout;
import com.netbrasoft.gnuob.DoCheckoutDetails;
import com.netbrasoft.gnuob.DoCheckoutDetailsResponse;
import com.netbrasoft.gnuob.DoCheckoutPayment;
import com.netbrasoft.gnuob.DoCheckoutPaymentResponse;
import com.netbrasoft.gnuob.DoCheckoutResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.Order;
import com.netbrasoft.gnuob.PayPalExpressCheckOutWebServiceImpl;
import com.netbrasoft.gnuob.PayPalExpressCheckOutWebServiceImplService;

@Repository("PayPalExpressCheckOutWebServiceRepository")
public class PayPalExpressCheckOutWebServiceRepository {

	private static final String GNUOB_PAY_PAL_EXPRESS_CHECK_OUT_WEB_SERVICE = System.getProperty("gnuob.paypal-service.url", "http://localhost:8080/gnuob-soap/PayPalExpressCheckOutWebServiceImpl?wsdl");
	private PayPalExpressCheckOutWebServiceImpl payPalExpressCheckOutWebServiceImpl = null;

	public PayPalExpressCheckOutWebServiceRepository() {
		try {
			PayPalExpressCheckOutWebServiceImplService payPalExpressCheckOutWebServiceImplService = new PayPalExpressCheckOutWebServiceImplService(new URL(GNUOB_PAY_PAL_EXPRESS_CHECK_OUT_WEB_SERVICE));
			payPalExpressCheckOutWebServiceImpl = payPalExpressCheckOutWebServiceImplService.getPayPalExpressCheckOutWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public Order doCheckout(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {

		DoCheckout paramDoCheckout = new DoCheckout();
		paramDoCheckout.setOrder(paramOrder);
		DoCheckoutResponse doCheckoutResponse = payPalExpressCheckOutWebServiceImpl.doCheckout(paramDoCheckout, paramMetaData);
		return doCheckoutResponse.getReturn();

	}

	public Order doCheckoutDetails(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		DoCheckoutDetails paramDoCheckoutDetails = new DoCheckoutDetails();
		paramDoCheckoutDetails.setOrder(paramOrder);
		DoCheckoutDetailsResponse doCheckoutDetailsResponse = payPalExpressCheckOutWebServiceImpl.doCheckoutDetails(paramDoCheckoutDetails, paramMetaData);
		return doCheckoutDetailsResponse.getReturn();

	}

	public Order doCheckoutPayment(MetaData paramMetaData, Order paramOrder) throws GNUOpenBusinessServiceException_Exception {
		DoCheckoutPayment paramDoCheckoutPayment = new DoCheckoutPayment();
		paramDoCheckoutPayment.setOrder(paramOrder);
		DoCheckoutPaymentResponse doCheckoutPaymentResponse = payPalExpressCheckOutWebServiceImpl.doCheckoutPayment(paramDoCheckoutPayment, paramMetaData);
		return doCheckoutPaymentResponse.getReturn();
	}

}
