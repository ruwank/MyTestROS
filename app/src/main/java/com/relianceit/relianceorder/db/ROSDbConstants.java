package com.relianceit.relianceorder.db;

import android.provider.BaseColumns;

/**
 * Created by sura on 4/28/15.
 */
public final class ROSDbConstants {

    public ROSDbConstants() {
    }

    public static abstract class Customer implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        public static final String CL_NAME_CUSTOMER_ID = "customer_id";
        public static final String CL_NAME_FIRST_NAME = "first_name";
        public static final String CL_NAME_LAST_NAME = "last_name";
        public static final String CL_NAME_SHOP_NAME = "shop_name";
        public static final String CL_NAME_TELEPHONE = "telephone";
        public static final String CL_NAME_EMAIL = "email";
        public static final String CL_NAME_TOWN = "town";
        public static final String CL_NAME_TOWN_ID = "town_id";
        public static final String CL_NAME_ADDRESS1 = "address1";
        public static final String CL_NAME_ADDRESS2 = "address2";
        public static final String CL_NAME_ADDRESS3 = "address3";
        public static final String CL_NAME_OUTSTANDING = "outstanding";
        public static final String CL_NAME_CREDIT_LIMIT = "credit_limit";
    }

    public static abstract class NewOrder implements BaseColumns {
        public static final String TABLE_NAME = "new_order";
        public static final String CL_NAME_ORDER_ID = "order_id";
        public static final String CL_NAME_ORDER_STATUS = "order_status";
        public static final String CL_NAME_CUSTOMER_ID = "customer_id";
        public static final String CL_NAME_GROSS_VALUE = "gross_value";
        public static final String CL_NAME_DISCOUNT = "discount";
        public static final String CL_NAME_DISCOUNT_VALUE = "discount_value";
        public static final String CL_NAME_ORDER_VALUE = "order_value";
        public static final String CL_NAME_ORDER_DATE = "order_date";
        public static final String CL_NAME_LATITUDE = "latitude";
        public static final String CL_NAME_LONGITUDE = "longitude";
    }

    public static abstract class NewOrderItem implements BaseColumns {
        public static final String TABLE_NAME = "new_order_item";
        public static final String CL_NAME_ITEM_ID = "item_id";
        public static final String CL_NAME_ORDER_ID = "order_id";
        public static final String CL_NAME_PRODUCT_ID = "product_id";
        public static final String CL_NAME_PRODUCT_NAME = "product_name";
        public static final String CL_NAME_BATCH_NAME = "batch_name";
        public static final String CL_NAME_QUANTITY = "quantity";
        public static final String CL_NAME_PRICE = "price";
        public static final String CL_NAME_DISCOUNT = "discount";
        public static final String CL_NAME_FREE_ISSUES = "free_issues";
        public static final String CL_NAME_ITEM_VALUE = "item_value";
        public static final String CL_NAME_SUPP_CODE = "supp_code";
        public static final String CL_NAME_LOCATION_CODE = "location_code";
        public static final String CL_NAME_BRAND_NAME = "brand_name";
        public static final String CL_NAME_BRAND_CODE = "brand_code";
    }

    public static abstract class ReturnOrder implements BaseColumns {
        public static final String TABLE_NAME = "return_order";
        public static final String CL_NAME_ORDER_ID = "order_id";
        public static final String CL_NAME_ORDER_STATUS = "order_status";
        public static final String CL_NAME_CUSTOMER_ID = "customer_id";
        public static final String CL_NAME_GROSS_VALUE = "gross_value";
        public static final String CL_NAME_DISCOUNT = "discount";
        public static final String CL_NAME_DISCOUNT_VALUE = "discount_value";
        public static final String CL_NAME_ORDER_VALUE = "order_value";
        public static final String CL_NAME_ORDER_DATE = "order_date";
        public static final String CL_NAME_INVOICE_NO = "invoice_no";
    }

    public static abstract class ReturnOrderItem implements BaseColumns {
        public static final String TABLE_NAME = "return_order_item";
        public static final String CL_NAME_ITEM_ID = "item_id";
        public static final String CL_NAME_ORDER_ID = "order_id";
        public static final String CL_NAME_PRODUCT_ID = "product_id";
        public static final String CL_NAME_PRODUCT_NAME = "product_name";
        public static final String CL_NAME_BATCH_NAME = "batch_name";
        public static final String CL_NAME_QUANTITY = "quantity";
        public static final String CL_NAME_PRICE = "price";
        public static final String CL_NAME_DISCOUNT = "discount";
        public static final String CL_NAME_FREE_ISSUES = "free_issues";
        public static final String CL_NAME_ITEM_VALUE = "item_value";
        public static final String CL_NAME_SUPP_CODE = "supp_code";
        public static final String CL_NAME_LOCATION_CODE = "location_code";
        public static final String CL_NAME_BRAND_CODE = "brand_code";
        public static final String CL_NAME_AGEN_CODE = "agen_code";
    }

    public static abstract class Stock implements BaseColumns {
        public static final String TABLE_NAME = "stock";
        public static final String CL_NAME_PRODUCT_NAME = "product_name";
        public static final String CL_NAME_BATCH_NAME = "batch_name";
        public static final String CL_NAME_BRAND_NAME = "brand_name";
        public static final String CL_NAME_AGENCY_NAME = "agency_name";
        public static final String CL_NAME_ALLOCATED_QTY = "allocated_qty";
        public static final String CL_NAME_AVAILABLE_QTY = "available_qty";
        public static final String CL_NAME_STATUS = "status";
        public static final String CL_NAME_AGENT_CODE = "agent_code";
        public static final String CL_NAME_BRAND_CODE = "brand_code";
        public static final String CL_NAME_PRODUCT_CODE = "product_code";
        public static final String CL_NAME_COMP_CODE = "comp_code";
        public static final String CL_NAME_DISTRIB_CODE = "distrib_code";
        public static final String CL_NAME_UNIT_PRICE = "unit_price";
        public static final String CL_NAME_SUPP_CODE = "supp_code";
        public static final String CL_NAME_LOCATION_CODE = "location_code";
    }

    public static abstract class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String CL_NAME_PRODUCT_NAME = "product_name";
        public static final String CL_NAME_BATCH_NAME = "batch_name";
        public static final String CL_NAME_BRAND_NAME = "brand_name";
        public static final String CL_NAME_AGENCY_NAME = "agency_name";
        public static final String CL_NAME_ALLOCATED_QTY = "allocated_qty";
        public static final String CL_NAME_AGENT_CODE = "agent_code";
        public static final String CL_NAME_BRAND_CODE = "brand_code";
        public static final String CL_NAME_PRODUCT_CODE = "product_code";
        public static final String CL_NAME_COMP_CODE = "comp_code";
        public static final String CL_NAME_DISTRIB_CODE = "distrib_code";
        public static final String CL_NAME_UNIT_PRICE = "unit_price";
        public static final String CL_NAME_SUPP_CODE = "supp_code";
    }
}
