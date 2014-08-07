package com.netbrasoft.gnuob.generic.category;

import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.netbrasoft.gnuob.api.Category;
import com.netbrasoft.gnuob.api.Content;
import com.netbrasoft.gnuob.api.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.api.MetaData;
import com.netbrasoft.gnuob.api.OrderBy;
import com.netbrasoft.gnuob.api.Paging;
import com.netbrasoft.gnuob.api.SubCategory;
import com.netbrasoft.gnuob.api.category.CategoryWebServiceRepository;
import com.netbrasoft.gnuob.generic.utils.Utils;

@RunWith(Arquillian.class)
public class CategoryWebServiceRepositoryTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return Utils.createDeployment();
	}

	private MetaData paramMetaData = null;
	private CategoryWebServiceRepository categoryWebServiceRepository;

	@Test
	public void createNewCategoryAndFindByCategoryName() throws GNUOpenBusinessServiceException_Exception {
		Category paramCategorySport = getParamCategorySport();

		paramCategorySport = categoryWebServiceRepository.persist(paramMetaData, paramCategorySport);
		paramCategorySport.getSubCategories().clear();

		Paging paging = new Paging();
		paging.setFirst(0);
		paging.setMax(2);

		List<Category> categories = categoryWebServiceRepository.find(paramMetaData, paramCategorySport, paging, OrderBy.NONE);

		Assert.assertFalse(categories.isEmpty());

		paramCategorySport = categories.get(0);

		Assert.assertTrue(paramCategorySport.getSubCategories().size() == 4);
	}

	@Test
	public void createNewCategoryAndOrderSubCategoriesByPostion() throws GNUOpenBusinessServiceException_Exception {
		SubCategory subCategoryWinterSport;
		SubCategory subCategoryZomerSport;
		SubCategory subCategoryLenteSport;
		SubCategory subCategoryHerfstSport;
		Category paramCategorySport = getParamCategorySport();

		paramCategorySport = categoryWebServiceRepository.persist(paramMetaData, paramCategorySport);

		Iterator<SubCategory> iterable = paramCategorySport.getSubCategories().iterator();

		Assert.assertEquals("ZomerSport", iterable.next().getName());
		Assert.assertEquals("HerfstSport", iterable.next().getName());
		Assert.assertEquals("WinterSport", iterable.next().getName());
		Assert.assertEquals("LenteSport", iterable.next().getName());

		iterable = paramCategorySport.getSubCategories().iterator();

		subCategoryZomerSport = iterable.next();
		subCategoryHerfstSport = iterable.next();
		subCategoryWinterSport = iterable.next();
		subCategoryLenteSport = iterable.next();

		paramCategorySport.getSubCategories().clear();

		paramCategorySport.getSubCategories().add(subCategoryLenteSport);
		paramCategorySport.getSubCategories().add(subCategoryWinterSport);
		paramCategorySport.getSubCategories().add(subCategoryHerfstSport);
		paramCategorySport.getSubCategories().add(subCategoryZomerSport);

		paramCategorySport = categoryWebServiceRepository.merge(paramMetaData, paramCategorySport);

		iterable = paramCategorySport.getSubCategories().iterator();

		Assert.assertEquals("LenteSport", iterable.next().getName());
		Assert.assertEquals("WinterSport", iterable.next().getName());
		Assert.assertEquals("HerfstSport", iterable.next().getName());
		Assert.assertEquals("ZomerSport", iterable.next().getName());
	}

	private Category getParamCategorySport() {
		Category paramCategorySport = new Category();
		SubCategory subCategoryWinterSport = new SubCategory();
		SubCategory subCategoryZomerSport = new SubCategory();
		SubCategory subCategoryLenteSport = new SubCategory();
		SubCategory subCategoryHerfstSport = new SubCategory();

		Content content = new Content();
		content.setFormat("pdf");
		content.setName("Document");
		content.setContent(new DataHandler(new String("dfd").getBytes(), "application/octet-stream"));

		paramCategorySport.setName("Sport");
		paramCategorySport.setDescription("Sport artikelen");
		paramCategorySport.getContents().add(content);

		subCategoryWinterSport.setName("WinterSport");
		subCategoryWinterSport.setDescription("WinterSport");

		subCategoryZomerSport.setName("ZomerSport");
		subCategoryZomerSport.setDescription("ZomerSport");

		subCategoryLenteSport.setName("LenteSport");
		subCategoryLenteSport.setDescription("LenteSport");

		subCategoryHerfstSport.setName("HerfstSport");
		subCategoryHerfstSport.setDescription("HerfstSport");

		paramCategorySport.getSubCategories().add(subCategoryZomerSport);
		paramCategorySport.getSubCategories().add(subCategoryHerfstSport);
		paramCategorySport.getSubCategories().add(subCategoryWinterSport);
		paramCategorySport.getSubCategories().add(subCategoryLenteSport);
		return paramCategorySport;
	}

	@Before
	public void init() {
		paramMetaData = Utils.paramMetaData();
		categoryWebServiceRepository = new CategoryWebServiceRepository();
	}
}
