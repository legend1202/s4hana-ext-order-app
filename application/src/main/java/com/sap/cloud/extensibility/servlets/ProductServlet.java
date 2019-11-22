package com.sap.cloud.extensibility.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
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
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.Product;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultProductMasterService;

@WebServlet("/")
public class ProductServlet extends HttpServlet {

	private static final String WEBSALEBLE_PRODUCTS = "websaleble_products";

	private static final String WEBSALEBLE_COUNT = "websaleble_count";

	private static final String ERRORPAGE_HTML = "errorpage.html";

	private static final String PRODUCTS_HTML = "products.html";

	private static final String APPLICATION_PROPERTIES = "application.properties";

	private static final String PRODUCTS = "products";

	private static final String COUNT = "count";

	private static final String S4CLD_PRODUCTGROUP = "s4cld.productgroup";

	private static final long serialVersionUID = 3735198720506536939L;

	private static final Logger LOGGER = CloudLoggerFactory.getLogger(OrderServlet.class);

	@Inject
	private DefaultProductMasterService defaultProductMasterService;

	List<Product> allProducts;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PropertiesConfiguration config = new PropertiesConfiguration();

		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());

		WebContext context = new WebContext(request, response, request.getServletContext());

		List<Product> webSalebleProducts = null;

		List<Product> nonWebSalebleProducts = null;

		try {

			config.load(APPLICATION_PROPERTIES);

			String productGroup = config.getString(S4CLD_PRODUCTGROUP);

			// defaultProductMasterService.getProductByKey(productId).execute();

			allProducts = findByProductGroup(productGroup);

			context.setVariable(COUNT, allProducts.size());

			webSalebleProducts = filterWebSalebleProducts(allProducts);

			nonWebSalebleProducts = filterNonWebSalebleProducts(allProducts);

			context.setVariable(PRODUCTS, nonWebSalebleProducts);

			context.setVariable(WEBSALEBLE_COUNT, webSalebleProducts.size());

			context.setVariable(WEBSALEBLE_PRODUCTS, webSalebleProducts);

			engine.process(PRODUCTS_HTML, context, response.getWriter());

		} catch (ConfigurationException e) {

			LOGGER.error("Exception occured while loading the properties file.." + e);

			engine.process(ERRORPAGE_HTML, context, response.getWriter());

		} catch (Exception e) {

			LOGGER.error("Exception occured while fetching the products and the exception is :" + e);

			engine.process(ERRORPAGE_HTML, context, response.getWriter());

		}
	}

	private List<Product> filterNonWebSalebleProducts(List<Product> prodlist) {

		List<Product> nonWebSalebleProducts = new ArrayList<>();

		for (Product cp : prodlist) {
			if (!(boolean) cp.getCustomField("YY1_SaleableProduct_PRD")) {

				nonWebSalebleProducts.add(cp);
			}
		}

		return nonWebSalebleProducts;
	}

	private List<Product> filterWebSalebleProducts(List<Product> prodlist) {

		List<Product> webSalebleProducts = new ArrayList<>();

		for (Product cp : prodlist) {

			if ((boolean) cp.getCustomField("YY1_SaleableProduct_PRD")) {

				webSalebleProducts.add(cp);
			}
		}

		return webSalebleProducts;
	}

	public List<Product> findByProductGroup(String productGroup) throws Exception {

		List<Product> productsList = null;
		try {

			LOGGER.info("productGroup :: " + productGroup);

			productsList = defaultProductMasterService.getAllProduct().filter(Product.PRODUCT_GROUP.eq(productGroup))
					.execute();

		} catch (ODataException e) {

			LOGGER.error("ODATA Exception occured in Product Service " + "while fetching the records :: ", e);

			throw new Exception(e);
		}
		return productsList;

	}

}