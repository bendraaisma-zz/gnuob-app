package com.netbrasoft.gnuob.generic.security;

import java.util.List;
import java.util.UUID;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.netbrasoft.gnuob.api.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.api.MetaData;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.Paging;
import com.netbrasoft.gnuob.api.Site;
import com.netbrasoft.gnuob.api.security.SiteWebServiceRepository;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class SiteWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private MetaData paramMetaData = null;
	private SiteWebServiceRepository siteWebServiceRepository;
	private Site paramSite = null;

	@Test
	public void countNewSiteWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		long countNewSiteWithNoAccess = siteWebServiceRepository.count(paramMetaData, paramSite);

		Assert.assertEquals(1, countNewSiteWithNoAccess);
	}

	public void createNewSite() throws GNUOpenBusinessServiceException_Exception {

		paramSite = new Site();
		paramSite.setName(UUID.randomUUID().toString());
		paramSite.setDescription(UUID.randomUUID().toString());

		paramSite = siteWebServiceRepository.persist(paramMetaData, paramSite);

		Assert.assertTrue(paramSite.getId() > 0);
	}

	@Test
	public void findNewSite() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		List<Site> findSiteWithNoAccessList = siteWebServiceRepository.find(paramMetaData, paramSite, paging, OrderBy.NONE);

		Assert.assertEquals(1, findSiteWithNoAccessList.size());
	}

	@Test
	public void findNewSiteById() throws GNUOpenBusinessServiceException_Exception {
		Site site = siteWebServiceRepository.find(paramMetaData, paramSite);

		Assert.assertEquals(paramSite.getId(), site.getId());
	}

	@Before
	public void init() throws GNUOpenBusinessServiceException_Exception {
		paramMetaData = Utils.paramMetaData();
		siteWebServiceRepository = new SiteWebServiceRepository();
		createNewSite();
	}

	@Test
	public void mergeNewSite() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramSite.getDescription();

		paramSite.setDescription(UUID.randomUUID().toString());
		paramSite = siteWebServiceRepository.merge(paramMetaData, paramSite);

		Assert.assertTrue(paramSite.getId() > 0);
		Assert.assertFalse(paramSite.getDescription().equals(descripton));
	}

	// @Test
	public void refreshNewSite() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramSite.getDescription();

		paramSite.setDescription(UUID.randomUUID().toString());
		paramSite = siteWebServiceRepository.refresh(paramMetaData, paramSite);

		Assert.assertTrue(paramSite.getId() > 0);
		Assert.assertTrue(paramSite.getDescription().equals(descripton));
	}

	// @After
	public void removeNewSite() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		paramSite = siteWebServiceRepository.find(paramMetaData, paramSite, paging, OrderBy.NONE).get(0);

		siteWebServiceRepository.remove(paramMetaData, paramSite);
	}

}
