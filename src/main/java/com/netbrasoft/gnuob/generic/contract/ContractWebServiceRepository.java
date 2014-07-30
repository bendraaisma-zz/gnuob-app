package com.netbrasoft.gnuob.generic.contract;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netbrasoft.gnuob.Contract;
import com.netbrasoft.gnuob.ContractWebServiceImpl;
import com.netbrasoft.gnuob.ContractWebServiceImplService;
import com.netbrasoft.gnuob.CountContract;
import com.netbrasoft.gnuob.CountContractResponse;
import com.netbrasoft.gnuob.FindContract;
import com.netbrasoft.gnuob.FindContractById;
import com.netbrasoft.gnuob.FindContractByIdResponse;
import com.netbrasoft.gnuob.FindContractResponse;
import com.netbrasoft.gnuob.GNUOpenBusinessServiceException_Exception;
import com.netbrasoft.gnuob.MergeContract;
import com.netbrasoft.gnuob.MergeContractResponse;
import com.netbrasoft.gnuob.MetaData;
import com.netbrasoft.gnuob.OrderBy;
import com.netbrasoft.gnuob.Paging;
import com.netbrasoft.gnuob.PersistContract;
import com.netbrasoft.gnuob.PersistContractResponse;
import com.netbrasoft.gnuob.RefreshContract;
import com.netbrasoft.gnuob.RefreshContractResponse;
import com.netbrasoft.gnuob.RemoveContract;

@Repository("ContractWebServiceRepository")
public class ContractWebServiceRepository {

	private static final String GNUOB_CONTRACT_WEB_SERVICE = System.getProperty("gnuob.contract-service.url", "http://localhost:8080/gnuob-soap/ContractWebServiceImpl?wsdl");
	private ContractWebServiceImpl contractWebServiceImpl;

	public ContractWebServiceRepository() {
		try {
			ContractWebServiceImplService contractWebServiceImplService = new ContractWebServiceImplService(new URL(GNUOB_CONTRACT_WEB_SERVICE));
			contractWebServiceImpl = contractWebServiceImplService.getContractWebServiceImplPort();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public long count(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		CountContract paramCountContract = new CountContract();
		paramCountContract.setContract(paramContract);
		CountContractResponse countContractResponse = contractWebServiceImpl.countContract(paramCountContract, paramMetaData);
		return countContractResponse.getReturn();
	}

	public Contract find(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		FindContractById paramFindContractById = new FindContractById();
		paramFindContractById.setContract(paramContract);
		FindContractByIdResponse findContractByIdResponse = contractWebServiceImpl.findContractById(paramFindContractById, paramMetaData);
		return findContractByIdResponse.getReturn();

	}

	public List<Contract> find(MetaData paramMetaData, Contract paramContract, Paging paramPaging, OrderBy paramOrderBy) throws GNUOpenBusinessServiceException_Exception {
		FindContract paramFindContract = new FindContract();
		paramFindContract.setContract(paramContract);
		paramFindContract.setPaging(paramPaging);
		paramFindContract.setOrderBy(paramOrderBy);
		FindContractResponse findContractResponse = contractWebServiceImpl.findContract(paramFindContract, paramMetaData);
		return findContractResponse.getReturn();
	}

	public Contract merge(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		MergeContract paramMergeContract = new MergeContract();
		paramMergeContract.setContract(paramContract);
		MergeContractResponse mergeContractResponse = contractWebServiceImpl.mergeContract(paramMergeContract, paramMetaData);
		return mergeContractResponse.getReturn();
	}

	public Contract persist(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		PersistContract paramPersistContract = new PersistContract();
		paramPersistContract.setContract(paramContract);
		PersistContractResponse persistContractResponse = contractWebServiceImpl.persistContract(paramPersistContract, paramMetaData);
		return persistContractResponse.getReturn();
	}

	public Contract refresh(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		RefreshContract paramRefresContract = new RefreshContract();
		paramRefresContract.setContract(paramContract);
		RefreshContractResponse refresContractResponse = contractWebServiceImpl.refreshContract(paramRefresContract, paramMetaData);
		return refresContractResponse.getReturn();
	}

	public void remove(MetaData paramMetaData, Contract paramContract) throws GNUOpenBusinessServiceException_Exception {
		RemoveContract paramRemoveContract = new RemoveContract();
		paramRemoveContract.setContract(paramContract);
		contractWebServiceImpl.removeContract(paramRemoveContract, paramMetaData);
	}
}
