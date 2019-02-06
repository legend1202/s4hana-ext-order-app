package com.sap.cloud.extensibility.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.sap.cloud.extensibility.config.TemplateEngineUtil;
import com.sap.cloud.extensibility.model.OneTimeCustomerOrder;
import com.sap.cloud.extensibility.services.OneTimeCustomerRecordService;
import com.sap.cloud.extensibility.services.SalesOrderService;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.salesorder.SalesOrder;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.salesorder.SalesOrderItem;

@WebServlet("/orderconfirm")
public class OrderConfirmServlet extends HttpServlet {

	private static final long serialVersionUID = -6311730511204678803L;

	private static final Logger LOGGER = CloudLoggerFactory.getLogger(OrderConfirmServlet.class);

	private static final String ERRORPAGE_HTML = "errorpage.html";

	@Inject
	private SalesOrder salesorder;

	@Inject
	private SalesOrderItem salesOrderItem;

	@Inject
	private SalesOrderService salesOrderService;

	@Inject
	private OneTimeCustomerRecordService oneTimeCustomerRecordService;

	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean successful = true;
		
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());

		WebContext context = new WebContext(request, response, request.getServletContext());

		try {

			OneTimeCustomerOrder otcOrder = new OneTimeCustomerOrder();

			LOGGER.info("request get parameter-->" + request.getParameter("firstName"));

			setSalesOrderDetails(request);

			salesorder.setCustomerPurchaseOrderDate(Calendar.getInstance());

			// the result contains the newly created Sales Order number
			SalesOrder soResult = salesOrderService.create(salesorder);

			LOGGER.info("soResult::" + soResult.toString());

			otcOrder = setOneTimeCustomerRecord(request);
			// ... which we also // write into the custom business object
			otcOrder.setOrderId(soResult.getSalesOrder());

			otcOrder = oneTimeCustomerRecordService.create(otcOrder);

			LOGGER.info("soResult::" + otcOrder.getId());

			if (successful) {
				
				context.setVariable("createdSalesOrderId", soResult.getSalesOrder());
				context.setVariable("createdOtcRecordId", otcOrder.getId());
				engine.process("order-confirmation.html", context, response.getWriter());
				
			} else {
				
				context.setVariable("productId", request.getParameter("productId"));
				
				engine.process("order.html", context, response.getWriter());
			}

		} catch (Exception e) {

			LOGGER.info("Exception occured while submitting the Post request for customer order creation ::" + e);
			
			engine.process(ERRORPAGE_HTML, context, response.getWriter());
		}
	}

	private OneTimeCustomerOrder setOneTimeCustomerRecord(HttpServletRequest request) {

		OneTimeCustomerOrder otcOrder = new OneTimeCustomerOrder();
		// otcOrder.setOrderId(soResult.getSalesOrder());
		otcOrder.setFirstName(request.getParameter("firstName"));
		otcOrder.setLastName(request.getParameter("lastName"));
		otcOrder.setPhoneNumber(request.getParameter("phoneNumber"));
		otcOrder.setBankAccount(request.getParameter("bankAccount"));
		otcOrder.setShippingAddress(request.getParameter("shippingAddress"));
		otcOrder.setErpConfigContext(new ErpConfigContext("ErpQueryEndpoint"));
		return otcOrder;

	}

	private void setSalesOrderDetails(HttpServletRequest request) throws ConfigurationException {

		PropertiesConfiguration config = new PropertiesConfiguration();

		config.load("application.properties");

		// Note in this sample we pre-define most of the sales order with static values
		// due to simplicity...

		salesorder.setSalesOrderType(config.getString("s4cld.salesordertype"));
		salesorder.setDistributionChannel(config.getString("s4cld.distrubutionchannel"));
		salesorder.setOrganizationDivision(config.getString("s4cld.organizationdivision"));

		// since we are working with anonymous external users. the sales order is
		// created under a one time customer
		salesorder.setSoldToParty(config.getString("s4cld.onetimebusinesspartner"));

		salesorder.setPurchaseOrderByCustomer("Web Order " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

		salesOrderItem.setMaterial(request.getParameter("productId"));

		salesOrderItem.setRequestedQuantity(new BigDecimal(1.0));

		salesorder.addItem(salesOrderItem);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
