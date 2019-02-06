package com.sap.cloud.extensibility.model;

import com.sap.cloud.extensibility.utils.MapperUtils;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.Product;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.productmaster.ProductField;

public class CustomProduct extends Product{
	
	public static final String WEBSALEBLE_CUSTOM_FIELD = "YY1_SaleableProduct_PRD";
	
	public static final ProductField<Boolean> WEBSALEBLE = Product.field(WEBSALEBLE_CUSTOM_FIELD, Boolean.class);
	
	public static CustomProduct of(Product product) {
		return MapperUtils.map(product, CustomProduct.class);
	}
	
	public Boolean getCustomWebSaleble() {
		Boolean stringValue = getCustomField(WEBSALEBLE);
		return stringValue;
	}
	
	
}
