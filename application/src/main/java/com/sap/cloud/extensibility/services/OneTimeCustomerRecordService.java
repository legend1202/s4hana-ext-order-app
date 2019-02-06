package com.sap.cloud.extensibility.services;

import org.slf4j.Logger;

import com.sap.cloud.extensibility.model.OneTimeCustomerOrder;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

public class OneTimeCustomerRecordService {

	private static final Logger LOGGER = CloudLoggerFactory.getLogger(OneTimeCustomerRecordService.class);

	public OneTimeCustomerRecordService() {

	}

	/**
	 * 
	 * @param otcOrder
	 * @return otcOrderCreated
	 * @throws Exception
	 */
	public OneTimeCustomerOrder create(OneTimeCustomerOrder otcOrder) throws Exception {
		
		OneTimeCustomerOrder otcOrderCreated = null;
		
		try {

			if (otcOrder != null) {

				OneTimeCustomerRecordCreateFluentHelper orderHelper = new OneTimeCustomerRecordCreateFluentHelper(
						otcOrder);

				otcOrderCreated = orderHelper.execute();

				LOGGER.info("Created customer Id:: " + otcOrderCreated.getId());

			}

		} catch (ODataException e) {
			
			LOGGER.error("Exception occured while creating One Time Customer Record :: ", e);
			
			throw new Exception(e);
		}

		return otcOrderCreated;
	}

}