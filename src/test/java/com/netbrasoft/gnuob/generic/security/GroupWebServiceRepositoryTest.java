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
import com.netbrasoft.gnuob.api.Group;
import com.netbrasoft.gnuob.api.MetaData;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.Paging;
import com.netbrasoft.gnuob.api.security.GroupWebServiceRepository;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class GroupWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private MetaData paramMetaData = null;
	private GroupWebServiceRepository groupWebServiceRepository;
	private Group paramGroupWithNoAccess = null;

	@Test
	public void countNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		long countNewGroupWithNoAccess = groupWebServiceRepository.count(paramMetaData, paramGroupWithNoAccess);

		Assert.assertEquals(1, countNewGroupWithNoAccess);
	}

	public void createNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {

		paramGroupWithNoAccess = new Group();
		paramGroupWithNoAccess.setName(UUID.randomUUID().toString());
		paramGroupWithNoAccess.setDescription(UUID.randomUUID().toString());
		// paramGroupWithNoAccess.setRule(Rule.NONE_ACCESS);

		paramGroupWithNoAccess = groupWebServiceRepository.persist(paramMetaData, paramGroupWithNoAccess);

		Assert.assertTrue(paramGroupWithNoAccess.getId() > 0);
	}

	@Test
	public void findNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		List<Group> findGroupWithNoAccessList = groupWebServiceRepository.find(paramMetaData, paramGroupWithNoAccess, paging, OrderBy.NONE);

		Assert.assertEquals(1, findGroupWithNoAccessList.size());
	}

	@Test
	public void findNewGroupWithNoAccessById() throws GNUOpenBusinessServiceException_Exception {
		Group group = groupWebServiceRepository.find(paramMetaData, paramGroupWithNoAccess);

		Assert.assertEquals(paramGroupWithNoAccess.getId(), group.getId());
	}

	@Before
	public void init() throws GNUOpenBusinessServiceException_Exception {
		paramMetaData = Utils.paramMetaData();
		groupWebServiceRepository = new GroupWebServiceRepository();
		createNewGroupWithNoAccess();
	}

	@Test
	public void mergeNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramGroupWithNoAccess.getDescription();

		paramGroupWithNoAccess.setDescription(UUID.randomUUID().toString());
		paramGroupWithNoAccess = groupWebServiceRepository.merge(paramMetaData, paramGroupWithNoAccess);

		Assert.assertTrue(paramGroupWithNoAccess.getId() > 0);
		Assert.assertFalse(paramGroupWithNoAccess.getDescription().equals(descripton));
	}

	// @Test
	public void refreshNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramGroupWithNoAccess.getDescription();

		paramGroupWithNoAccess.setDescription(UUID.randomUUID().toString());
		paramGroupWithNoAccess = groupWebServiceRepository.refresh(paramMetaData, paramGroupWithNoAccess);

		Assert.assertTrue(paramGroupWithNoAccess.getId() > 0);
		Assert.assertTrue(paramGroupWithNoAccess.getDescription().equals(descripton));
	}

	// @After
	public void removeNewGroupWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		paramGroupWithNoAccess = groupWebServiceRepository.find(paramMetaData, paramGroupWithNoAccess, paging, OrderBy.NONE).get(0);

		groupWebServiceRepository.remove(paramMetaData, paramGroupWithNoAccess);
	}

}
