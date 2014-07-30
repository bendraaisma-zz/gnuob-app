package com.netbrasoft.gnuob.generic.category;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.Category;
import com.netbrasoft.gnuob.CategoryWebServiceImpl;
import com.netbrasoft.gnuob.CategoryWebServiceImplService;
import com.netbrasoft.gnuob.CountCategory;
import com.netbrasoft.gnuob.CountCategoryResponse;
import com.netbrasoft.gnuob.FindCategory;
import com.netbrasoft.gnuob.FindCategoryById;
import com.netbrasoft.gnuob.FindCategoryByIdResponse;
import com.netbrasoft.gnuob.FindCategoryResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeCategory;
import com.netbrasoft.gnuob.MergeCategoryResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistCategory;
import com.netbrasoft.gnuob.PersistCategoryResponse;
import com.netbrasoft.gnuob.RefreshCategory;
import com.netbrasoft.gnuob.RefreshCategoryResponse;
import com.netbrasoft.gnuob.RemoveCategory;

@Repository("CategoryWebServiceRepository")
public class CategoryWebServiceRepository {

	private static final String GNUOB_CATEGORY_WEB_SERVICE = System.getProperty("gnuob.category-service.url", "http://localhost:8080/gnuob-soap/CategoryWebServiceImpl?wsdl");
	private CategoryWebServiceImpl categoryWebServiceImpl = null;

	public CategoryWebServiceRepository() {
		try {
			CategoryWebServiceImplService categoryWebServiceImplService = new CategoryWebServiceImplService(new URL(GNUOB_CATEGORY_WEB_SERVICE));
			categoryWebServiceImpl = categoryWebServiceImplService.getCategoryWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		CountCategory paramCountCategory = new CountCategory();
		paramCountCategory.setCategory(paramCategory);
		CountCategoryResponse countCategoryResponse = categoryWebServiceImpl.countCategory(paramCountCategory, paramMetaData);
		return countCategoryResponse.getReturn();
	}

	public Category find(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		FindCategoryById paramFindCategoryById = new FindCategoryById();
		paramFindCategoryById.setCategory(paramCategory);
		FindCategoryByIdResponse findCategoryByIdResponse = categoryWebServiceImpl.findCategoryById(paramFindCategoryById, paramMetaData);
		return findCategoryByIdResponse.getReturn();

	}

	public List<Category> find(MetaData paramMetaData, Category paramCategory, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindCategory paramFindCategory = new FindCategory();
		paramFindCategory.setCategory(paramCategory);
		paramFindCategory.setPaging(paramPaging);
		paramFindCategory.setOrderBy(paramOrderBy);
		FindCategoryResponse findCategoryResponse = categoryWebServiceImpl.findCategory(paramFindCategory, paramMetaData);

		ArrayList<Category> findCategoryReturn = new ArrayList<Category>();

		for (Object object : findCategoryResponse.getReturn()) {
			findCategoryReturn.add((Category) object);
		}

		return findCategoryReturn;
	}

	public Category merge(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		MergeCategory paramMergeCategory = new MergeCategory();
		paramMergeCategory.setCategory(paramCategory);
		MergeCategoryResponse mergeCategoryResponse = categoryWebServiceImpl.mergeCategory(paramMergeCategory, paramMetaData);
		return mergeCategoryResponse.getReturn();
	}

	public Category persist(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		PersistCategory paramPersistCategory = new PersistCategory();
		paramPersistCategory.setCategory(paramCategory);
		PersistCategoryResponse persistCategoryResponse = categoryWebServiceImpl.persistCategory(paramPersistCategory, paramMetaData);
		return persistCategoryResponse.getReturn();
	}

	public Category refresh(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		RefreshCategory paramRefresCategory = new RefreshCategory();
		paramRefresCategory.setCategory(paramCategory);
		RefreshCategoryResponse refreshCategoryResponse = categoryWebServiceImpl.refreshCategory(paramRefresCategory, paramMetaData);
		return refreshCategoryResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Category paramCategory) throws GNUOpenBusinessServiceException_Exception {
		RemoveCategory paramRemoveCategory = new RemoveCategory();
		paramRemoveCategory.setCategory(paramCategory);
		categoryWebServiceImpl.removeCategory(paramRemoveCategory, paramMetaData);
	}
}
