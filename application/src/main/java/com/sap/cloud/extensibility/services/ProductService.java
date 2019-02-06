package com.sap.cloud.extensibility.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.sap.cloud.extensibility.model.CustomProduct;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.Product;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultProductMasterService;

/**
 * The Class ProductService.
 */
public class ProductService {

	/** The default product master service. */
	@Inject
	private DefaultProductMasterService defaultProductMasterService;

	/** The Constant logger. */
	private static final Logger logger = CloudLoggerFactory.getLogger(ProductService.class);

	/**
	 * Find by id.
	 *
	 * @param productId
	 * @return the product
	 * @throws Exception
	 * 
	 */
	public Product findById(String productId) throws Exception {
		Product product = null;
		try {
			logger.info("ProductId :: " + productId);

			product = defaultProductMasterService.getProductByKey(productId).execute();

			logger.info("ProductIdDetails :: " + product.toString());

		} catch (ODataException e) {

			logger.error("ODATA Exception occured in Product Service "
					+ "while fetching the product for given product Id:: ", e);

			throw new Exception(e);
		}
		return product;
	}

	/**
	 * Find by product group.
	 *
	 * @param productGroup
	 * @return List of Products
	 * @throws Exception
	 * 
	 */
	public List<Product> findByProductGroup(String productGroup) throws Exception {

		List<Product> productsList = null;
		try {

			logger.info("productGroup :: " + productGroup);

			productsList = defaultProductMasterService.getAllProduct().filter(Product.PRODUCT_GROUP.eq(productGroup))
					.execute();

		} catch (ODataException e) {

			logger.error("ODATA Exception occured in Product Service " + "while fetching the records :: ", e);

			throw new Exception(e);
		}
		return productsList;

	}
	
	public List<CustomProduct> findCustomerProductsByProductGroup(String productGroup) throws Exception {

		List<Product> productsList = null;
		
		List<CustomProduct> customProductsList = new ArrayList<CustomProduct>();
		
		try {

			logger.info("productGroup :: " + productGroup);

			productsList = defaultProductMasterService.getAllProduct().filter(Product.PRODUCT_GROUP.eq(productGroup)).
					execute();
			
			logger.info("productsList size::"+productsList.size());
			
			for(Product p: productsList ) {
				
				logger.info("YY1_WebSaleable_PRD"+p.getCustomField("YY1_SaleableProduct_PRD"));
				
				customProductsList.add(CustomProduct.of(p));
				
			}
			for(CustomProduct cp: customProductsList) {
				logger.info("cp websaleble::"+cp.getCustomWebSaleble());
			}
			
			logger.info("customProductsList :: " + customProductsList);

		} catch (ODataException e) {

			logger.error("ODATA Exception occured in Product Service " + "while fetching the records :: ", e);

			throw new Exception(e);
		}
		return customProductsList;

	}


}