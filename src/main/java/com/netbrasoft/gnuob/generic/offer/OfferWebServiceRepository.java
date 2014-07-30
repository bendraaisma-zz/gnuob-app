package com.netbrasoft.gnuob.generic.offer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.CountOffer;
import com.netbrasoft.gnuob.CountOfferResponse;
import com.netbrasoft.gnuob.FindOffer;
import com.netbrasoft.gnuob.FindOfferById;
import com.netbrasoft.gnuob.FindOfferByIdResponse;
import com.netbrasoft.gnuob.FindOfferResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeOffer;
import com.netbrasoft.gnuob.MergeOfferResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.Offer;
import com.netbrasoft.gnuob.OfferWebServiceImpl;
import com.netbrasoft.gnuob.OfferWebServiceImplService;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistOffer;
import com.netbrasoft.gnuob.PersistOfferResponse;
import com.netbrasoft.gnuob.RefreshOffer;
import com.netbrasoft.gnuob.RefreshOfferResponse;
import com.netbrasoft.gnuob.RemoveOffer;

@Repository("OfferWebServiceRepository")
public class OfferWebServiceRepository {

	private static final String GNUOB_OFFER_WEB_SERVICE = System.getProperty("gnuob.offer-service.url", "http://localhost:8080/gnuob-soap/OfferWebServiceImpl?wsdl");
	private OfferWebServiceImpl offerWebServiceImpl;

	public OfferWebServiceRepository() {
		try {
			OfferWebServiceImplService offerWebServiceImplService = new OfferWebServiceImplService(new URL(GNUOB_OFFER_WEB_SERVICE));
			offerWebServiceImpl = offerWebServiceImplService.getOfferWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		CountOffer paramCountOffer = new CountOffer();
		paramCountOffer.setOffer(paramOffer);
		CountOfferResponse countOfferResponse = offerWebServiceImpl.countOffer(paramCountOffer, paramMetaData);
		return countOfferResponse.getReturn();
	}

	public Offer find(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		FindOfferById paramFindOfferById = new FindOfferById();
		paramFindOfferById.setOffer(paramOffer);
		FindOfferByIdResponse findOfferByIdResponse = offerWebServiceImpl.findOfferById(paramFindOfferById, paramMetaData);
		return findOfferByIdResponse.getReturn();

	}

	public List<Offer> find(MetaData paramMetaData, Offer paramOffer, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindOffer paramFindOffer = new FindOffer();
		paramFindOffer.setOffer(paramOffer);
		paramFindOffer.setPaging(paramPaging);
		paramFindOffer.setOrderBy(paramOrderBy);
		FindOfferResponse findOfferResponse = offerWebServiceImpl.findOffer(paramFindOffer, paramMetaData);
		return findOfferResponse.getReturn();
	}

	public Offer merge(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		MergeOffer paramMergeOffer = new MergeOffer();
		paramMergeOffer.setOffer(paramOffer);
		MergeOfferResponse mergeOfferResponse = offerWebServiceImpl.mergeOffer(paramMergeOffer, paramMetaData);
		return mergeOfferResponse.getReturn();
	}

	public Offer persist(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		PersistOffer paramPersistOffer = new PersistOffer();
		paramPersistOffer.setOffer(paramOffer);
		PersistOfferResponse persistOfferResponse = offerWebServiceImpl.persistOffer(paramPersistOffer, paramMetaData);
		return persistOfferResponse.getReturn();
	}

	public Offer refresh(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		RefreshOffer paramRefresOffer = new RefreshOffer();
		paramRefresOffer.setOffer(paramOffer);
		RefreshOfferResponse refresOfferResponse = offerWebServiceImpl.refreshOffer(paramRefresOffer, paramMetaData);
		return refresOfferResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Offer paramOffer) throws GNUOpenBusinessServiceException_Exception {
		RemoveOffer paramRemoveOffer = new RemoveOffer();
		paramRemoveOffer.setOffer(paramOffer);
		offerWebServiceImpl.removeOffer(paramRemoveOffer, paramMetaData);
	}
}
