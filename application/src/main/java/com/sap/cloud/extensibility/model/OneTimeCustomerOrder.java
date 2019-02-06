package com.sap.cloud.extensibility.model;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder

/**
 * Sets the resource.
 *
 * @param resource the new resource
 */
@Data

/**
 * Instantiates a new one time customer order.
 *
 * @param id 
 * @param orderId
 * @param lastName
 * @param firstName
 * @param phoneNumber
 * @param shippingAddress
 * @param bankAccount
 * @param servicePath
 * @param resource
 * @param erpConfigContext
 */
@AllArgsConstructor

/* 
 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject#toString()
 */
@ToString(doNotUseGetters = true, callSuper = true)

/* 
 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject#hashCode()
 */
@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
@JsonAdapter(com.sap.cloud.sdk.s4hana.datamodel.odata.adapter.ODataVdmEntityAdapterFactory.class)
public class OneTimeCustomerOrder extends VdmEntity<OneTimeCustomerOrder> {

	/** The Constants */
	private static final String SAP_UUID = "SAP_UUID";

	private static final String PHONE_NUMBER = "PhoneNumber";

	private static final String ORDER_ID = "OrderID";

	private static final String BANK_ACCOUNT = "BankAccount";

	private static final String LAST_NAME = "LastName";

	private static final String SHIPPING_ADDRESS = "ShippingAddress";

	private static final String FIRST_NAME = "FirstName";

	/** The id. */
	@SerializedName(SAP_UUID)
	@JsonProperty(SAP_UUID)
	private UUID id;

	/** The order id. */
	@SerializedName(ORDER_ID)
	@JsonProperty(ORDER_ID)
	private String orderId;

	/** The last name. */
	@SerializedName(LAST_NAME)
	@JsonProperty(LAST_NAME)
	@Nullable
	private String lastName;

	/** The first name. */
	@SerializedName(FIRST_NAME)
	@JsonProperty(FIRST_NAME)
	@Nullable
	private String firstName;

	/** The phone number. */
	@SerializedName(PHONE_NUMBER)
	@JsonProperty(PHONE_NUMBER)
	@Nullable
	private String phoneNumber;

	/** The shipping address. */
	@SerializedName(SHIPPING_ADDRESS)
	@JsonProperty(SHIPPING_ADDRESS)
	@Nullable
	private String shippingAddress;

	/** The bank account. */
	@SerializedName(BANK_ACCOUNT)
	@JsonProperty(BANK_ACCOUNT)
	@Nullable
	private String bankAccount;

	/** The service path. */
	@JsonIgnore
	private String servicePath;

	/** The resource. */
	@JsonIgnore
	private String resource;
	
	/** The erp config context. */
	@JsonIgnore
	@Nullable
	private transient ErpConfigContext erpConfigContext;
	
	/**
	 * Instantiates a new one time customer order.
	 * Loads service path and resource from application properties for Custom business object
	 * for One Time Customer.
	 */
	public OneTimeCustomerOrder() {

		PropertiesConfiguration config = new PropertiesConfiguration();

		try {

			config.load("application.properties");

			servicePath = config.getString("s4cld.onetimecustomerrecord_servicepath");

			resource = config.getString("s4cld.onetimecustomerrecord_resource");

		} catch (Exception e) {

		}

	}
	
	 /* 
 	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject#toMap()
 	 */
 	@Override
	    protected Map<String, Object> toMap() {
	        final Map<String, Object> values = super.toMap();
	        values.put(SHIPPING_ADDRESS, getShippingAddress());
	        values.put(FIRST_NAME, getFirstName());
	        values.put(LAST_NAME, getLastName());
	        values.put(BANK_ACCOUNT, getBankAccount());
	        values.put(ORDER_ID, getOrderId());
	        values.put(PHONE_NUMBER, getPhoneNumber());
	        return values;
	    }
	 
	    /* 
    	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject#fromMap(java.util.Map)
    	 */
    	@Override
	    protected void fromMap(final Map<String, Object> inputValues) {
	        final Map<String, Object> values = Maps.newHashMap(inputValues);
	        if (values.containsKey(SHIPPING_ADDRESS)) {
	            final Object value = values.remove(SHIPPING_ADDRESS);
	            if ((value == null)||(!value.equals(getShippingAddress()))) {
	                setShippingAddress(((String) value));
	            }
	        }
	        if (values.containsKey(SAP_UUID)) {
	            final Object value = values.remove(SAP_UUID);
	            if ((value == null)||(!value.equals(getId()))) {
	                setId(((UUID) value));
	            }
	        }
	        if (values.containsKey(FIRST_NAME)) {
	            final Object value = values.remove(FIRST_NAME);
	            if ((value == null)||(!value.equals(getFirstName()))) {
	                setFirstName(((String) value));
	            }
	        }
	        if (values.containsKey(LAST_NAME)) {
	            final Object value = values.remove(LAST_NAME);
	            if ((value == null)||(!value.equals(getLastName()))) {
	                setLastName(((String) value));
	            }
	        }
	        if (values.containsKey(BANK_ACCOUNT)) {
	            final Object value = values.remove(BANK_ACCOUNT);
	            if ((value == null)||(!value.equals(getBankAccount()))) {
	                setBankAccount(((String) value));
	            }
	        }
	        if (values.containsKey(ORDER_ID)) {
	            final Object value = values.remove(ORDER_ID);
	            if ((value == null)||(!value.equals(getOrderId()))) {
	                setOrderId(((String) value));
	            }
	        }
	        if (values.containsKey(PHONE_NUMBER)) {
	            final Object value = values.remove(PHONE_NUMBER);
	            if ((value == null)||(!value.equals(getPhoneNumber()))) {
	                setPhoneNumber(((String) value));
	            }
	        }
	        super.fromMap(values);
	    }


	/*
	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity#getEndpointUrl()
	 */
	@Override
	protected String getEndpointUrl() {
		return servicePath;
	}

	/* 
	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity#getEntityCollection()
	 */
	@Override
	protected String getEntityCollection() {
		return resource;
	}

	/* 
	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmEntity#setErpConfigContext(com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext)
	 */
	@Override
	public void setErpConfigContext(ErpConfigContext arg0) {
		this.erpConfigContext = arg0;
	}

	/* 
	 * @see com.sap.cloud.sdk.s4hana.datamodel.odata.helper.VdmObject#getType()
	 */
	@Override
	public Class<OneTimeCustomerOrder> getType() {
		return OneTimeCustomerOrder.class;
	}

}
