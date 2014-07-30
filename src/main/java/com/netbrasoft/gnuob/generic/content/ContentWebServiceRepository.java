package com.netbrasoft.gnuob.generic.content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.Content;
import com.netbrasoft.gnuob.ContentWebServiceImpl;
import com.netbrasoft.gnuob.ContentWebServiceImplService;
import com.netbrasoft.gnuob.CountContent;
import com.netbrasoft.gnuob.CountContentResponse;
import com.netbrasoft.gnuob.FindContent;
import com.netbrasoft.gnuob.FindContentById;
import com.netbrasoft.gnuob.FindContentByIdResponse;
import com.netbrasoft.gnuob.FindContentResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeContent;
import com.netbrasoft.gnuob.MergeContentResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistContent;
import com.netbrasoft.gnuob.PersistContentResponse;
import com.netbrasoft.gnuob.RefreshContent;
import com.netbrasoft.gnuob.RefreshContentResponse;
import com.netbrasoft.gnuob.RemoveContent;

@Repository("ContentWebServiceRepository")
public class ContentWebServiceRepository {

	private static final String GNUOB_CONTENT_WEB_SERVICE = System.getProperty("gnuob.content-service.url", "http://localhost:8080/gnuob-soap/ContentWebServiceImpl?wsdl");
	private ContentWebServiceImpl contentWebServiceImpl;

	public ContentWebServiceRepository() {
		try {
			ContentWebServiceImplService contentWebServiceImplService = new ContentWebServiceImplService(new URL(GNUOB_CONTENT_WEB_SERVICE));
			contentWebServiceImpl = contentWebServiceImplService.getContentWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		CountContent paramCountContent = new CountContent();
		paramCountContent.setContent(paramContent);
		CountContentResponse countContentResponse = contentWebServiceImpl.countContent(paramCountContent, paramMetaData);
		return countContentResponse.getReturn();
	}

	public Content find(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		FindContentById paramFindContentById = new FindContentById();
		paramFindContentById.setContent(paramContent);
		FindContentByIdResponse findContentByIdResponse = contentWebServiceImpl.findContentById(paramFindContentById, paramMetaData);
		return findContentByIdResponse.getReturn();

	}

	public List<Content> find(MetaData paramMetaData, Content paramContent, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindContent paramFindContent = new FindContent();
		paramFindContent.setContent(paramContent);
		paramFindContent.setPaging(paramPaging);
		paramFindContent.setOrderBy(paramOrderBy);
		FindContentResponse findContentResponse = contentWebServiceImpl.findContent(paramFindContent, paramMetaData);
		return findContentResponse.getReturn();
	}

	public Content merge(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		MergeContent paramMergeContent = new MergeContent();
		paramMergeContent.setContent(paramContent);
		MergeContentResponse mergeContentResponse = contentWebServiceImpl.mergeContent(paramMergeContent, paramMetaData);
		return mergeContentResponse.getReturn();
	}

	public Content persist(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		PersistContent paramPersistContent = new PersistContent();
		paramPersistContent.setContent(paramContent);
		PersistContentResponse persistContentResponse = contentWebServiceImpl.persistContent(paramPersistContent, paramMetaData);
		return persistContentResponse.getReturn();
	}

	public Content refresh(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		RefreshContent paramRefresContent = new RefreshContent();
		paramRefresContent.setContent(paramContent);
		RefreshContentResponse refresContentResponse = contentWebServiceImpl.refreshContent(paramRefresContent, paramMetaData);
		return refresContentResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Content paramContent) throws GNUOpenBusinessServiceException_Exception {
		RemoveContent paramRemoveContent = new RemoveContent();
		paramRemoveContent.setContent(paramContent);
		contentWebServiceImpl.removeContent(paramRemoveContent, paramMetaData);
	}
}
