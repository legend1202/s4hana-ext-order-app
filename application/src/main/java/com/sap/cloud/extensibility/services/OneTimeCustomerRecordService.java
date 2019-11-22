package com.sap.cloud.extensibility.services;

import javax.ejb.Stateless;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;

import com.sap.cloud.extensibility.model.OneTimeCustomerOrder;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

@Stateless
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

		PropertiesConfiguration config = new PropertiesConfiguration();

		config.load("application.properties");

		try {

			if (otcOrder != null) {

				OneTimeCustomerRecordCreateFluentHelper orderHelper = new OneTimeCustomerRecordCreateFluentHelper(
						config.getString("s4cld.onetimecustomerrecord_servicepath"), otcOrder);

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