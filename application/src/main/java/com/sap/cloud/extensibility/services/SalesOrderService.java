package com.sap.cloud.extensibility.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.salesorder.SalesOrder;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.salesorder.SalesOrderCreateFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultSalesOrderService;

@Stateless
public class SalesOrderService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = CloudLoggerFactory.getLogger(SalesOrderService.class);

	/** The process sales order service. */

	@Inject
	DefaultSalesOrderService processSalesOrderService;

	/**
	 * Creates SalesOrder
	 *
	 * @param salesOrder
	 * @return created sales order
	 * @throws RuntimeException
	 */
	public SalesOrder create(SalesOrder salesOrder) throws Exception {

		SalesOrder so = null;

		try {
			if (salesOrder != null) {

				LOGGER.info("SalesOrder :: " + salesOrder.toString());

				SalesOrderCreateFluentHelper salesHelper = processSalesOrderService.createSalesOrder(salesOrder);

				so = salesHelper.execute();

			}

		} catch (ODataException e) {

			LOGGER.error("Exception occured while creating a sales Order and the exception is :: ", e);

			throw new Exception(e);
		}
		
		return so;
	}
}
