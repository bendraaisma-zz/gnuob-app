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

import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.Group;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.Rule;
import com.netbrasoft.gnuob.Site;
import com.netbrasoft.gnuob.User;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class UserWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private MetaData paramMetaData = null;
	private UserWebServiceRepository userWebServiceRepository;
	private User paramUserWithNoAccess = null;

	@Test
	public void countNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		long countNewUserWithNoAccess = userWebServiceRepository.count(paramMetaData, paramUserWithNoAccess);

		Assert.assertEquals(1, countNewUserWithNoAccess);
	}

	public void createNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		paramUserWithNoAccess = new User();

		paramUserWithNoAccess.setName(UUID.randomUUID().toString());
		paramUserWithNoAccess.setPassword(UUID.randomUUID().toString());
		paramUserWithNoAccess.setRule(Rule.NO_ACCESS);

		Group group = new Group();
		group.setName(UUID.randomUUID().toString());
		group.setDescription(UUID.randomUUID().toString());
		group.setRule(Rule.READ_ACCESS);

		Site site = new Site();
		site.setName(UUID.randomUUID().toString());
		site.setDescription(UUID.randomUUID().toString());

		paramUserWithNoAccess.getSites().add(site);
		paramUserWithNoAccess.getGroups().add(group);

		paramUserWithNoAccess = userWebServiceRepository.persist(paramMetaData, paramUserWithNoAccess);

		Assert.assertTrue(paramUserWithNoAccess.getId() > 0);
	}

	@Test
	public void findNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		List<User> findUserWithNoAccessList = userWebServiceRepository.find(paramMetaData, paramUserWithNoAccess, paging, OrderBy.NONE);

		Assert.assertEquals(1, findUserWithNoAccessList.size());
	}

	@Test
	public void findNewUserWithNoAccessById() throws GNUOpenBusinessServiceException_Exception {
		User user = userWebServiceRepository.find(paramMetaData, paramUserWithNoAccess);

		Assert.assertEquals(paramUserWithNoAccess.getId(), user.getId());
	}

	@Before
	public void init() throws GNUOpenBusinessServiceException_Exception {
		paramMetaData = Utils.paramMetaData();
		userWebServiceRepository = new UserWebServiceRepository();
		createNewUserWithNoAccess();
	}

	@Test
	public void mergeNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramUserWithNoAccess.getDescription();

		paramUserWithNoAccess.setDescription(UUID.randomUUID().toString());
		paramUserWithNoAccess = userWebServiceRepository.merge(paramMetaData, paramUserWithNoAccess);

		Assert.assertTrue(paramUserWithNoAccess.getId() > 0);
		Assert.assertFalse(paramUserWithNoAccess.getDescription().equals(descripton));
	}

	// @Test
	public void refreshNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		String descripton = paramUserWithNoAccess.getDescription();

		paramUserWithNoAccess.setDescription(UUID.randomUUID().toString());
		paramUserWithNoAccess = userWebServiceRepository.refresh(paramMetaData, paramUserWithNoAccess);

		Assert.assertTrue(paramUserWithNoAccess.getId() > 0);
		Assert.assertTrue(paramUserWithNoAccess.getDescription().equals(descripton));
	}

	// @After
	public void removeNewUserWithNoAccess() throws GNUOpenBusinessServiceException_Exception {
		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(1);

		paramUserWithNoAccess = userWebServiceRepository.find(paramMetaData, paramUserWithNoAccess, paging, OrderBy.NONE).get(0);

		userWebServiceRepository.remove(paramMetaData, paramUserWithNoAccess);
	}

}
