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
import com.sap.cloud.extensibility.model.CustomProduct;
import com.sap.cloud.extensibility.services.ProductService;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;

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
	ProductService productService;

	List<CustomProduct> allProducts;
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PropertiesConfiguration config = new PropertiesConfiguration();
		
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
		
		WebContext context = new WebContext(request, response, request.getServletContext());
		
		List<CustomProduct> webSalebleProducts = null;
		
		List<CustomProduct> nonWebSalebleProducts = null;
		
		try {

			config.load(APPLICATION_PROPERTIES);

			String productGroup = config.getString(S4CLD_PRODUCTGROUP);

			allProducts = productService.findCustomerProductsByProductGroup(productGroup);
			
			context.setVariable(COUNT, allProducts.size());
			
			
			webSalebleProducts = filterWebSalebleProducts(allProducts);
			
			nonWebSalebleProducts = filterNonWebSalebleProducts(allProducts);
			
			context.setVariable(PRODUCTS, nonWebSalebleProducts);
			
			context.setVariable(WEBSALEBLE_COUNT, webSalebleProducts.size());
			context.setVariable(WEBSALEBLE_PRODUCTS, webSalebleProducts);
			
			engine.process(PRODUCTS_HTML, context, response.getWriter());

		} catch (ConfigurationException e) {
			
			LOGGER.error("Exception occured while loading the properties file.."+ e);
			
			engine.process(ERRORPAGE_HTML, context, response.getWriter());

		} catch (Exception e) {
			
			LOGGER.error("Exception occured while fetching the products and the exception is :"+ e);
			
			engine.process(ERRORPAGE_HTML, context, response.getWriter());

		}
	}


	
	private List<CustomProduct> filterNonWebSalebleProducts(List<CustomProduct> prodlist) {
		
		List<CustomProduct> nonWebSalebleProducts = new ArrayList<>();
		
		for(CustomProduct cp: prodlist) {
			
			if(!cp.getCustomWebSaleble()) {
				
				nonWebSalebleProducts.add(cp);
			}
		}
		
		return nonWebSalebleProducts;
	}


	private List<CustomProduct> filterWebSalebleProducts(List<CustomProduct> prodlist) {
		
		List<CustomProduct> webSalebleProducts = new ArrayList<>();
		
		for(CustomProduct cp: prodlist) {
			
			if(cp.getCustomWebSaleble()) {
				
				webSalebleProducts.add(cp);
			}
		}
		
		return webSalebleProducts;
	}

}
