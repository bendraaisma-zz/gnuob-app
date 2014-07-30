package com.netbrasoft.gnuob.generic.security;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.CountSite;
import com.netbrasoft.gnuob.CountSiteResponse;
import com.netbrasoft.gnuob.FindSite;
import com.netbrasoft.gnuob.FindSiteById;
import com.netbrasoft.gnuob.FindSiteByIdResponse;
import com.netbrasoft.gnuob.FindSiteResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeSite;
import com.netbrasoft.gnuob.MergeSiteResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistSite;
import com.netbrasoft.gnuob.PersistSiteResponse;
import com.netbrasoft.gnuob.RefreshSite;
import com.netbrasoft.gnuob.RefreshSiteResponse;
import com.netbrasoft.gnuob.RemoveSite;
import com.netbrasoft.gnuob.Site;
import com.netbrasoft.gnuob.SiteWebServiceImpl;
import com.netbrasoft.gnuob.SiteWebServiceImplService;

@Repository("SiteWebServiceRepository")
public class SiteWebServiceRepository {

	private static final String GNUOB_SITE_WEB_SERVICE = System.getProperty("gnuob.site-service.url", "http://localhost:8080/gnuob-soap/SiteWebServiceImpl?wsdl");
	private SiteWebServiceImpl siteWebServiceImpl;

	public SiteWebServiceRepository() {
		try {
			SiteWebServiceImplService siteWebServiceImplService = new SiteWebServiceImplService(new URL(GNUOB_SITE_WEB_SERVICE));
			siteWebServiceImpl = siteWebServiceImplService.getSiteWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		CountSite paramCountSite = new CountSite();
		paramCountSite.setSite(paramSite);
		CountSiteResponse countSiteResponse = siteWebServiceImpl.countSite(paramCountSite, paramMetaData);
		return countSiteResponse.getReturn();
	}

	public Site find(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		FindSiteById paramFindSiteById = new FindSiteById();
		paramFindSiteById.setSite(paramSite);
		FindSiteByIdResponse findSiteByIdResponse = siteWebServiceImpl.findSiteById(paramFindSiteById, paramMetaData);
		return findSiteByIdResponse.getReturn();

	}

	public List<Site> find(MetaData paramMetaData, Site paramSite, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindSite paramFindSite = new FindSite();
		paramFindSite.setSite(paramSite);
		paramFindSite.setPaging(paramPaging);
		paramFindSite.setOrderBy(paramOrderBy);
		FindSiteResponse findSiteResponse = siteWebServiceImpl.findSite(paramFindSite, paramMetaData);
		return findSiteResponse.getReturn();
	}

	public Site merge(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		MergeSite paramMergeSite = new MergeSite();
		paramMergeSite.setSite(paramSite);
		MergeSiteResponse mergeSiteResponse = siteWebServiceImpl.mergeSite(paramMergeSite, paramMetaData);
		return mergeSiteResponse.getReturn();
	}

	public Site persist(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		PersistSite paramPersistSite = new PersistSite();
		paramPersistSite.setSite(paramSite);
		PersistSiteResponse persistSiteResponse = siteWebServiceImpl.persistSite(paramPersistSite, paramMetaData);
		return persistSiteResponse.getReturn();
	}

	public Site refresh(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		RefreshSite paramRefresSite = new RefreshSite();
		paramRefresSite.setSite(paramSite);
		RefreshSiteResponse refresSiteResponse = siteWebServiceImpl.refreshSite(paramRefresSite, paramMetaData);
		return refresSiteResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Site paramSite) throws GNUOpenBusinessServiceException_Exception {
		RemoveSite paramRemoveSite = new RemoveSite();
		paramRemoveSite.setSite(paramSite);
		siteWebServiceImpl.removeSite(paramRemoveSite, paramMetaData);
	}
}
