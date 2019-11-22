package com.sap.cloud.extensibility.servlets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.sap.cloud.extensibility.config.TemplateEngineUtil;
import com.sap.cloud.extensibility.model.OneTimeCustomerOrder;
import com.sap.cloud.extensibility.services.ProductService;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.Product;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.ProductDescription;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

	private static final String N_A = "n/a";

	private static final String EN = "EN";

	private static final String ORDER_HTML = "order.html";

	private static final String OTC_RECORD = "otcRecord";

	private static final String EN_DESCRIPTION = "en_description";

	private static final String PRODUCT = "product";

	private static final String PRODUCT_ID = "productId";

	@Inject
	private ProductService productService;

	private static final long serialVersionUID = 123456789L;

	private static final Logger LOGGER = CloudLoggerFactory.getLogger(OrderServlet.class);

	private static final String ERRORPAGE_HTML = "errorpage.html";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());

		WebContext context = new WebContext(request, response, request.getServletContext());

		Product product = null;

		try {
			String productId = request.getParameter(PRODUCT_ID);

			if (productId != null) {

				product = productService.findById(productId);
			}

			context.setVariable(PRODUCT, product);

			context.setVariable(EN_DESCRIPTION, getDescription(product));

			context.setVariable(OTC_RECORD, new OneTimeCustomerOrder());

			engine.process(ORDER_HTML, context, response.getWriter());

		} catch (Exception e) {

			LOGGER.info("Exception occured while fetching the products and the exception is :" + e);

			engine.process(ERRORPAGE_HTML, context, response.getWriter());

		}
	}

	// helper method to retrieve the english description
	public String getDescription(Product product) throws ODataException {

		if (product != null) {
			List<ProductDescription> prodDesc_list = product.getDescriptionOrFetch();
			for (ProductDescription productDescription : prodDesc_list) {
				if (EN.equalsIgnoreCase(productDescription.getLanguage())) {
					return productDescription.getProductDescription();
				}
			}
		}
		return N_A;
	}
}
