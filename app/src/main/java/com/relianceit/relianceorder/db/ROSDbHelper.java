package com.relianceit.relianceorder.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSProduct;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.models.ROSReturnOrderItem;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.models.ROSVisit;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;

/**
 * Created by Suresh on 4/28/15.
 */
public class ROSDbHelper extends SQLiteOpenHelper {

    public static final String TAG = ROSDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "ROS.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String DECIMAL_TYPE = " DECIMAL(10,5)";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CUSTOMER = "CREATE TABLE " + ROSDbConstants.Customer.TABLE_NAME +
            "(" +
            ROSDbConstants.Customer._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.Customer.CL_NAME_CUSTOMER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_SHOP_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_TELEPHONE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_TOWN + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_TOWN_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_ADDRESS1 + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_ADDRESS2 + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_ADDRESS3 + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_OUTSTANDING + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.Customer.CL_NAME_CREDIT_LIMIT + DECIMAL_TYPE +
            ")";

    private static final String SQL_CREATE_NEW_ORDER = "CREATE TABLE " + ROSDbConstants.NewOrder.TABLE_NAME +
            "(" +
            ROSDbConstants.NewOrder._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.NewOrder.CL_NAME_ORDER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS + INT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_DISCOUNT + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_LATITUDE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_LONGITUDE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR + INT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH + INT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_NEW_ORDER_ITEM = "CREATE TABLE " + ROSDbConstants.NewOrderItem.TABLE_NAME +
            "(" +
            ROSDbConstants.NewOrderItem._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.NewOrderItem.CL_NAME_ITEM_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_ORDER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_BATCH_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_QUANTITY + INT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_PRICE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_DISCOUNT + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_FREE_ISSUES + INT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_ITEM_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_SUPP_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_BRAND_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_BRAND_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_USER_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.NewOrderItem.CL_NAME_LOCATION_CODE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_RETURN_ORDER = "CREATE TABLE " + ROSDbConstants.ReturnOrder.TABLE_NAME +
            "(" +
            ROSDbConstants.ReturnOrder._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS + INT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_RETURN_ORDER_ITEM = "CREATE TABLE " + ROSDbConstants.ReturnOrderItem.TABLE_NAME +
            "(" +
            ROSDbConstants.ReturnOrderItem._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.ReturnOrderItem.CL_NAME_ITEM_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_ORDER_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_ID + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_BATCH_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_QUANTITY + INT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_PRICE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_DISCOUNT + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_FREE_ISSUES + INT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_ITEM_VALUE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_SUPP_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_LOCATION_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_BRAND_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_USER_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.ReturnOrderItem.CL_NAME_AGEN_CODE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_STOCK = "CREATE TABLE " + ROSDbConstants.Stock.TABLE_NAME +
            "(" +
            ROSDbConstants.Stock._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_BATCH_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_BRAND_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_AGENCY_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_STATUS + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_AGENT_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_BRAND_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_COMP_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_UNIT_PRICE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_SUPP_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Stock.CL_NAME_LOCATION_CODE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_PRODUCT = "CREATE TABLE " + ROSDbConstants.Product.TABLE_NAME +
            "(" +
            ROSDbConstants.Product._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.Product.CL_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_BATCH_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_BRAND_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_AGENCY_NAME + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_ALLOCATED_QTY + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_AGENT_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_BRAND_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_PRODUCT_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_COMP_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_DISTRIB_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_UNIT_PRICE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_PRODUCT_USER_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Product.CL_NAME_SUPP_CODE + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_VISIT = "CREATE TABLE " + ROSDbConstants.Visit.TABLE_NAME +
            "(" +
            ROSDbConstants.Visit._ID + " INTEGER PRIMARY KEY," +
            ROSDbConstants.Visit.CL_NAME_CUST_CODE + TEXT_TYPE + COMMA_SEP +
            ROSDbConstants.Visit.CL_NAME_STATUS + INT_TYPE + COMMA_SEP +
            ROSDbConstants.Visit.CL_NAME_LATITUDE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.Visit.CL_NAME_LONGITUDE + DECIMAL_TYPE + COMMA_SEP +
            ROSDbConstants.Visit.CL_NAME_ADDED_DATE + TEXT_TYPE +
            ")";

    private static final String SQL_DELETE_CUSTOMER =
            "DROP TABLE IF EXISTS " + ROSDbConstants.Customer.TABLE_NAME;
    private static final String SQL_DELETE_NEW_ORDER =
            "DROP TABLE IF EXISTS " + ROSDbConstants.NewOrder.TABLE_NAME;
    private static final String SQL_DELETE_NEW_ORDER_ITEM =
            "DROP TABLE IF EXISTS " + ROSDbConstants.NewOrderItem.TABLE_NAME;
    private static final String SQL_DELETE_RETURN_ORDER =
            "DROP TABLE IF EXISTS " + ROSDbConstants.ReturnOrder.TABLE_NAME;
    private static final String SQL_DELETE_RETURN_ORDER_ITEM =
            "DROP TABLE IF EXISTS " + ROSDbConstants.ReturnOrderItem.TABLE_NAME;
    private static final String SQL_DELETE_STOCK =
            "DROP TABLE IF EXISTS " + ROSDbConstants.Stock.TABLE_NAME;
    private static final String SQL_DELETE_PRODUCT =
            "DROP TABLE IF EXISTS " + ROSDbConstants.Product.TABLE_NAME;
    private static final String SQL_DELETE_VISIT =
            "DROP TABLE IF EXISTS " + ROSDbConstants.Visit.TABLE_NAME;

    public ROSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CUSTOMER);
        db.execSQL(SQL_CREATE_NEW_ORDER);
        db.execSQL(SQL_CREATE_NEW_ORDER_ITEM);
        db.execSQL(SQL_CREATE_RETURN_ORDER);
        db.execSQL(SQL_CREATE_RETURN_ORDER_ITEM);
        db.execSQL(SQL_CREATE_STOCK);
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_VISIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_NEW_ORDER_ITEM);
        db.execSQL(SQL_DELETE_RETURN_ORDER_ITEM);
        db.execSQL(SQL_DELETE_NEW_ORDER);
        db.execSQL(SQL_DELETE_RETURN_ORDER);
        db.execSQL(SQL_DELETE_STOCK);
        db.execSQL(SQL_DELETE_CUSTOMER);
        db.execSQL(SQL_DELETE_PRODUCT);
        db.execSQL(SQL_DELETE_VISIT);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public SQLiteDatabase getReadableDb(Context context) {
        ROSDbHelper dbHelper = new ROSDbHelper(context);
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDb(Context context) {
        ROSDbHelper dbHelper = new ROSDbHelper(context);
        return dbHelper.getWritableDatabase();
    }

    /*
    Customer section
     */
    public ArrayList<ROSCustomer> getCustomers(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_ALL_CUSTOMERS = "SELECT * FROM " + ROSDbConstants.Customer.TABLE_NAME +
                " ORDER BY " + ROSDbConstants.Customer.CL_NAME_FIRST_NAME + ";";
        Cursor c = db.rawQuery(SQL_SELECT_ALL_CUSTOMERS, null);

        ArrayList<ROSCustomer> cusList = new ArrayList<ROSCustomer>();

        if (c != null) {
            while (c.moveToNext()){
                ROSCustomer cus = new ROSCustomer();
                cus.setAddress1(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_ADDRESS1)));
                cus.setAddress2(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_ADDRESS2)));
                cus.setAddress3(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_ADDRESS2)));
                cus.setCustomerId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_CUSTOMER_ID)));
                cus.setEmail(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_EMAIL)));
                cus.setFirstName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_FIRST_NAME)));
                cus.setLastName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_LAST_NAME)));
                cus.setShopName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_SHOP_NAME)));
                cus.setTelephone(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_TELEPHONE)));
                cus.setTown(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_TOWN)));
                cus.setTownId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_TOWN_ID)));
                cus.setOutstanding(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_OUTSTANDING)));
                cus.setCreditLimit(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Customer.CL_NAME_CREDIT_LIMIT)));

                cusList.add(cus);
            }

            c.close();
            db.close();
        }

        return  cusList;
    }

    public void updateCustomerOutstanding(Context context, String customerId, double value) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_UPDATE_CUSTOMER = "UPDATE " + ROSDbConstants.Customer.TABLE_NAME +
                " SET " + ROSDbConstants.Customer.CL_NAME_OUTSTANDING + " = " + value +
                " WHERE " + ROSDbConstants.Customer.CL_NAME_CUSTOMER_ID + " = '" + customerId + "';";
        db.execSQL(SQL_UPDATE_CUSTOMER);
        db.close();
    }

    public void clearCustomerTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_CUSTOMER = "DELETE FROM " + ROSDbConstants.Customer.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_CUSTOMER);
        db.close();
    }

    public void insertCustomer(Context context, ROSCustomer customer) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();
        values.put(ROSDbConstants.Customer.CL_NAME_CUSTOMER_ID, customer.getCustomerId());
        values.put(ROSDbConstants.Customer.CL_NAME_FIRST_NAME, customer.getFirstName());
        values.put(ROSDbConstants.Customer.CL_NAME_LAST_NAME, customer.getLastName());
        values.put(ROSDbConstants.Customer.CL_NAME_EMAIL, customer.getEmail());
        values.put(ROSDbConstants.Customer.CL_NAME_TELEPHONE, customer.getTelephone());
        values.put(ROSDbConstants.Customer.CL_NAME_SHOP_NAME, customer.getShopName());
        values.put(ROSDbConstants.Customer.CL_NAME_TOWN, customer.getTown());
        values.put(ROSDbConstants.Customer.CL_NAME_TOWN_ID, customer.getTownId());
        values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS1, customer.getAddress1());
        values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS2, customer.getAddress2());
        values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS3, customer.getAddress3());
        values.put(ROSDbConstants.Customer.CL_NAME_OUTSTANDING, customer.getOutstanding());
        values.put(ROSDbConstants.Customer.CL_NAME_CREDIT_LIMIT, customer.getCreditLimit());

        db.insert(ROSDbConstants.Customer.TABLE_NAME, null, values);
        db.close();
    }

    public void insertCustomers(Context context, ArrayList<ROSCustomer>customers) {
        SQLiteDatabase db = getWritableDb(context);

        for (int i = 0; i < customers.size(); i++) {
            ROSCustomer customer = customers.get(i);

            ContentValues values = new ContentValues();
            values.put(ROSDbConstants.Customer.CL_NAME_CUSTOMER_ID, customer.getCustomerId());
            values.put(ROSDbConstants.Customer.CL_NAME_FIRST_NAME, customer.getFirstName());
            values.put(ROSDbConstants.Customer.CL_NAME_LAST_NAME, customer.getLastName());
            values.put(ROSDbConstants.Customer.CL_NAME_EMAIL, customer.getEmail());
            values.put(ROSDbConstants.Customer.CL_NAME_TELEPHONE, customer.getTelephone());
            values.put(ROSDbConstants.Customer.CL_NAME_SHOP_NAME, customer.getShopName());
            values.put(ROSDbConstants.Customer.CL_NAME_TOWN, customer.getTown());
            values.put(ROSDbConstants.Customer.CL_NAME_TOWN_ID, customer.getTownId());
            values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS1, customer.getAddress1());
            values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS2, customer.getAddress2());
            values.put(ROSDbConstants.Customer.CL_NAME_ADDRESS3, customer.getAddress3());
            values.put(ROSDbConstants.Customer.CL_NAME_OUTSTANDING, customer.getOutstanding());
            values.put(ROSDbConstants.Customer.CL_NAME_CREDIT_LIMIT, customer.getCreditLimit());

            db.insert(ROSDbConstants.Customer.TABLE_NAME, null, values);
        }

        db.close();
    }

    /*
    New Order section
     */
    public ArrayList<ROSNewOrder> getNewOrders(Context context, String customerId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID + " = '" + customerId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        ArrayList<ROSNewOrder> orderList = new ArrayList<ROSNewOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSNewOrder order = new ROSNewOrder();
                order.setCustCode(customerId);
                order.setSalesOrdNum(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE)));
                order.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LATITUDE)));
                order.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS)));
                order.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR)));
                order.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        return  orderList;
    }

    public ArrayList<ROSNewOrder> getNewOrders(Context context, String customerId, String startDateStr, String endDateStr) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID + "= '" + customerId + "' AND ("
                + ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE + " BETWEEN '" + startDateStr + "' AND '" + endDateStr + "');";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        ArrayList<ROSNewOrder> orderList = new ArrayList<ROSNewOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSNewOrder order = new ROSNewOrder();
                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID)));
                order.setSalesOrdNum(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE)));
                order.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LATITUDE)));
                order.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS)));
                order.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR)));
                order.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        return  orderList;
    }

    public ArrayList<ROSNewOrder> getNewOrdersPending(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING + ";";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        ArrayList<ROSNewOrder> orderList = new ArrayList<ROSNewOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSNewOrder order = new ROSNewOrder();
                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID)));
                order.setSalesOrdNum(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE)));
                order.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LATITUDE)));
                order.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS)));
                order.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR)));
                order.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        for (int i = 0; i < orderList.size(); i++) {
            ROSNewOrder order = orderList.get(i);
            order.setProducts(getNewOrderItems(context, order.getSalesOrdNum()));
        }

        return  orderList;
    }

    public ArrayList<ROSNewOrder> getNewOrdersPending(Context context, String customerId, String startDateStr, String endDateStr) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING +
                " AND " + ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID + " = '" + customerId +
                "' AND (" + ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE + " BETWEEN '" + startDateStr + "' AND '" + endDateStr + "');";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        ArrayList<ROSNewOrder> orderList = new ArrayList<ROSNewOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSNewOrder order = new ROSNewOrder();
                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID)));
                order.setSalesOrdNum(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE)));
                order.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LATITUDE)));
                order.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS)));
                order.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR)));
                order.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        for (int i = 0; i < orderList.size(); i++) {
            ROSNewOrder order = orderList.get(i);
            order.setProducts(getNewOrderItems(context, order.getSalesOrdNum()));
        }

        return  orderList;
    }

    public int getNewOrderCountPending(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING + ";";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        int count = 0;

        if (c != null) {
            count = c.getCount();
            c.close();
        }

        db.close();

        return  count;
    }

    public ROSNewOrder getNewOrder(Context context, String orderId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDER = "SELECT * FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDER, null);

        ROSNewOrder order = null;

        if (c != null) {

            c.moveToFirst();
            order = new ROSNewOrder();
            order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID)));
            order.setSalesOrdNum(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_ID)));
            order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE)));
            order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT)));
            order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE)));
            order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE)));
            order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE)));
            order.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LATITUDE)));
            order.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE)));
            order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS)));
            order.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR)));
            order.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH)));

            c.close();
            db.close();
        }

        if (order != null) {
            order.setProducts(getNewOrderItems(context, orderId));
        }

        return order;
    }

    public void updateNewOrderStatusToSynced(Context context, String orderId) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_UPDATE_ORDER_STATUS = "UPDATE " + ROSDbConstants.NewOrder.TABLE_NAME +
                " SET " + ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.SYNCED +
                " WHERE " + ROSDbConstants.NewOrder.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        db.execSQL(SQL_UPDATE_ORDER_STATUS);
        db.close();
    }

    public String insertNewOrder(Context context, ROSNewOrder order) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();
        values.put(ROSDbConstants.NewOrder.CL_NAME_CUSTOMER_ID, order.getCustCode());
        values.put(ROSDbConstants.NewOrder.CL_NAME_ORDER_DATE, order.getAddedDate());
        values.put(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT, order.getOVDiscount());
        values.put(ROSDbConstants.NewOrder.CL_NAME_DISCOUNT_VALUE, order.getDiscountValue());
        values.put(ROSDbConstants.NewOrder.CL_NAME_GROSS_VALUE, order.getGrossValue());
        values.put(ROSDbConstants.NewOrder.CL_NAME_ORDER_VALUE, order.getOrderValue());
        values.put(ROSDbConstants.NewOrder.CL_NAME_LATITUDE, order.getLatitude());
        values.put(ROSDbConstants.NewOrder.CL_NAME_LONGITUDE, order.getLongitude());
        values.put(ROSDbConstants.NewOrder.CL_NAME_ORDER_STATUS, Constants.OrderStatus.PENDING);
        values.put(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_YEAR, order.getAccountYear());
        values.put(ROSDbConstants.NewOrder.CL_NAME_ACCOUNT_MONTH, order.getAccountMonth());

        long orderId = db.insert(ROSDbConstants.NewOrder.TABLE_NAME, null, values);
        db.close();

        if (orderId == -1) {
            return null;
        }

        db = getWritableDb(context);
        String orderIdStr = "" + orderId;
        final String SQL_UPDATE_NEW_ORDER = "UPDATE " + ROSDbConstants.NewOrder.TABLE_NAME +
                " SET " + ROSDbConstants.NewOrder.CL_NAME_ORDER_ID + " = '" + orderIdStr +
                "' WHERE " + ROSDbConstants.NewOrder._ID + " = " + orderId + ";";
        db.execSQL(SQL_UPDATE_NEW_ORDER);
        db.close();

        //inserting order items
        boolean success = insertNewOrderItems(context, orderIdStr, order.getProducts());
        if (!success) {
            db = getWritableDb(context);
            final String SQL_DELETE_NEW_ORDER = "DELETE FROM " + ROSDbConstants.NewOrder.TABLE_NAME +
                    " WHERE " + ROSDbConstants.NewOrder._ID + " = " + orderId + ";";
            db.execSQL(SQL_DELETE_NEW_ORDER);
            db.close();

            return null;
        } else {
            for (int i = 0; i < order.getProducts().size(); i++) {
                ROSNewOrderItem orderItem = order.getProducts().get(i);
                ROSStock stock = new ROSStock();
                stock.setProductDescription(orderItem.getProductDescription());
                stock.setProductBatchCode(orderItem.getProductBatchCode());
                stock.setProductCode(orderItem.getProductCode());

                updateStock(context, stock, orderItem.getQtyBonus() + orderItem.getQtyOrdered());
            }
        }

        return orderIdStr;
    }

    public void clearNewOrderTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_NEW_ORDER = "DELETE FROM " + ROSDbConstants.NewOrder.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_NEW_ORDER);
        db.close();
    }

    /*
    New Order Item section
     */
    public ArrayList<ROSNewOrderItem> getNewOrderItems(Context context, String orderId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDER_ITEMS = "SELECT * FROM " + ROSDbConstants.NewOrderItem.TABLE_NAME +
                " WHERE " + ROSDbConstants.NewOrderItem.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDER_ITEMS, null);

        ArrayList<ROSNewOrderItem> orderItemList = new ArrayList<ROSNewOrderItem>();
        if (c != null) {
            while (c.moveToNext()){
                ROSNewOrderItem orderItem = new ROSNewOrderItem();

                orderItem.setItemId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_ITEM_ID)));
                orderItem.setOrderId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_ORDER_ID)));
                orderItem.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_ID)));
                orderItem.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_NAME)));
                orderItem.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_BATCH_NAME)));
                orderItem.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_PRICE)));
                orderItem.setProdDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_DISCOUNT)));
                orderItem.setEffPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_ITEM_VALUE)));
                orderItem.setQtyOrdered(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_QUANTITY)));
                orderItem.setQtyBonus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_FREE_ISSUES)));
                orderItem.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_SUPP_CODE)));
                orderItem.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_BRAND_NAME)));
                orderItem.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_BRAND_CODE)));
                orderItem.setStockLocationCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_LOCATION_CODE)));
                orderItem.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_USER_CODE)));
                orderItemList.add(orderItem);
            }

            c.close();
            db.close();
        }

        return orderItemList;
    }

    protected boolean insertNewOrderItems(Context context, String orderId, ArrayList<ROSNewOrderItem> items) {

        SQLiteDatabase db = getWritableDb(context);
        boolean success = true;

        for (int i = 0; i < items.size(); i++) {

            ROSNewOrderItem orderItem = items.get(i);

            ContentValues values = new ContentValues();

            values.put(ROSDbConstants.NewOrderItem.CL_NAME_ORDER_ID, orderId);
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_ID, orderItem.getProductCode());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_NAME, orderItem.getProductDescription());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_BATCH_NAME, orderItem.getProductBatchCode());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_PRICE, orderItem.getUnitPrice());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_DISCOUNT, orderItem.getProdDiscount());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_ITEM_VALUE, orderItem.getEffPrice());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_QUANTITY, orderItem.getQtyOrdered());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_FREE_ISSUES, orderItem.getQtyBonus());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_SUPP_CODE, orderItem.getSuppCode());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_BRAND_NAME, orderItem.getBrandName());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_BRAND_CODE, orderItem.getBrandCode());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_LOCATION_CODE, orderItem.getStockLocationCode());
            values.put(ROSDbConstants.NewOrderItem.CL_NAME_PRODUCT_USER_CODE, orderItem.getProductUserCode());

            long itemId = db.insert(ROSDbConstants.NewOrderItem.TABLE_NAME, null, values);
            if (itemId == -1) {
                success = false;
                break;
            }
        }
        db.close();

        if (!success) {
            deleteNewOrderItems(context, orderId);
        }

        return success;
    }

    protected void deleteNewOrderItems(Context context, String orderId) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_NEW_ORDER_ITEMS = "DELETE FROM " + ROSDbConstants.NewOrderItem.TABLE_NAME
                + " WHERE " + ROSDbConstants.NewOrderItem.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        db.execSQL(SQL_DELETE_NEW_ORDER_ITEMS);
        db.close();
    }

    public void clearNewOrderItemTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_NEW_ORDER_ITEMS = "DELETE FROM " + ROSDbConstants.NewOrderItem.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_NEW_ORDER_ITEMS);
        db.close();
    }

    /*
    Return Order section
     */
    public ArrayList<ROSReturnOrder> getReturnOrders(Context context, String customerId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_RETURN_ORDERS = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID + " = '" + customerId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_RETURN_ORDERS, null);

        ArrayList<ROSReturnOrder> orderList = new ArrayList<ROSReturnOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSReturnOrder order = new ROSReturnOrder();

                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID)));
                order.setReturnNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE)));
                order.setInvoiceNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS)));

                orderList.add(order);
            }

            c.close();
        }
        db.close();

        return  orderList;
    }

    public ArrayList<ROSReturnOrder> getReturnOrders(Context context, String customerId, String startDateStr, String endDateStr) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID + " = '" + customerId + "' AND ("
                + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE + " BETWEEN '" + startDateStr + "' AND '" + endDateStr + "');";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        ArrayList<ROSReturnOrder> orderList = new ArrayList<ROSReturnOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSReturnOrder order = new ROSReturnOrder();

                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID)));
                order.setReturnNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE)));
                order.setInvoiceNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS)));

                orderList.add(order);
            }

            c.close();
        }
        db.close();

        return  orderList;
    }

    public ArrayList<ROSReturnOrder> getReturnOrdersPending(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_RETURN_ORDERS = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING + ";";
        Cursor c = db.rawQuery(SQL_SELECT_RETURN_ORDERS, null);

        ArrayList<ROSReturnOrder> orderList = new ArrayList<ROSReturnOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSReturnOrder order = new ROSReturnOrder();
                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID)));
                order.setReturnNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE)));
                order.setInvoiceNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        for (int i = 0; i < orderList.size(); i++) {
            ROSReturnOrder order = orderList.get(i);
            order.setProducts(getReturnOrderItems(context, order.getReturnNumb()));
        }

        return  orderList;
    }

    public ArrayList<ROSReturnOrder> getReturnOrdersPending(Context context, String customerId, String startDateStr, String endDateStr) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_RETURN_ORDERS = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING +
                " AND " + ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID + " = '" + customerId +
                "' AND (" + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE + " BETWEEN '" + startDateStr + "' AND '" + endDateStr + "');";
        Cursor c = db.rawQuery(SQL_SELECT_RETURN_ORDERS, null);

        ArrayList<ROSReturnOrder> orderList = new ArrayList<ROSReturnOrder>();

        if (c != null) {
            while (c.moveToNext()){
                ROSReturnOrder order = new ROSReturnOrder();
                order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID)));
                order.setReturnNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID)));
                order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE)));
                order.setInvoiceNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO)));
                order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT)));
                order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE)));
                order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE)));
                order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE)));
                order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS)));
                orderList.add(order);
            }

            c.close();
        }

        db.close();

        for (int i = 0; i < orderList.size(); i++) {
            ROSReturnOrder order = orderList.get(i);
            order.setProducts(getReturnOrderItems(context, order.getReturnNumb()));
        }

        return  orderList;
    }

    public int getReturnOrderCountPending(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.PENDING + ";";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        int count = 0;

        if (c != null) {
            count = c.getCount();
            c.close();
        }

        db.close();

        return  count;
    }

    public ROSReturnOrder getReturnOrder(Context context, String orderId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_RETURN_ORDER = "SELECT * FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_RETURN_ORDER, null);

        ROSReturnOrder order = null;

        if (c != null) {

            c.moveToFirst();
            order = new ROSReturnOrder();
            order.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID)));
            order.setReturnNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID)));
            order.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE)));
            order.setInvoiceNumb(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO)));
            order.setOVDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT)));
            order.setDiscountValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE)));
            order.setGrossValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE)));
            order.setOrderValue(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE)));
            order.setOrderStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS)));

            c.close();
            db.close();
        }

        if (order != null) {
            order.setProducts(getReturnOrderItems(context, orderId));
        }

        return order;
    }

    public void updateReturnOrderStatusToSynced(Context context, String orderId) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_UPDATE_ORDER_STATUS = "UPDATE " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " SET " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS + " = " + Constants.OrderStatus.SYNCED +
                " WHERE " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        db.execSQL(SQL_UPDATE_ORDER_STATUS);
        db.close();
    }

    public String insertReturnOrder(Context context, ROSReturnOrder order) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_CUSTOMER_ID, order.getCustCode());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_DATE, order.getAddedDate());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT, order.getOVDiscount());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_DISCOUNT_VALUE, order.getDiscountValue());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_GROSS_VALUE, order.getGrossValue());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_VALUE, order.getOrderValue());
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_ORDER_STATUS, Constants.OrderStatus.PENDING);
        values.put(ROSDbConstants.ReturnOrder.CL_NAME_INVOICE_NO, order.getInvoiceNumb());

        long orderId = db.insert(ROSDbConstants.ReturnOrder.TABLE_NAME, null, values);
        db.close();

        if (orderId == -1) {
            return null;
        }

        db = getWritableDb(context);
        String orderIdStr = "" + orderId;
        final String SQL_UPDATE_RETURN_ORDER = "UPDATE " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                " SET " + ROSDbConstants.ReturnOrder.CL_NAME_ORDER_ID + " = '" + orderIdStr +
                "' WHERE " + ROSDbConstants.ReturnOrder._ID + " = " + orderId + ";";
        db.execSQL(SQL_UPDATE_RETURN_ORDER);
        db.close();

        //inserting order items
        boolean success = insertReturnOrderItems(context, orderIdStr, order.getProducts());
        if (!success) {
            db = getWritableDb(context);
            final String SQL_DELETE_RETURN_ORDER = "DELETE FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME +
                    " WHERE " + ROSDbConstants.ReturnOrder._ID + " = " + orderId + ";";
            db.execSQL(SQL_DELETE_RETURN_ORDER);
            db.close();

            return null;
        }

        return orderIdStr;
    }

    public void clearReturnOrderTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_RETURN_ORDER = "DELETE FROM " + ROSDbConstants.ReturnOrder.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_RETURN_ORDER);
        db.close();
    }

    /*
    Return Order Item section
     */
    public ArrayList<ROSReturnOrderItem> getReturnOrderItems(Context context, String orderId) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_RETURN_ORDER_ITEMS = "SELECT * FROM " + ROSDbConstants.ReturnOrderItem.TABLE_NAME +
                " WHERE " + ROSDbConstants.ReturnOrderItem.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        Cursor c = db.rawQuery(SQL_SELECT_RETURN_ORDER_ITEMS, null);

        ArrayList<ROSReturnOrderItem> orderItemList = new ArrayList<ROSReturnOrderItem>();
        if (c != null) {
            while (c.moveToNext()){
                ROSReturnOrderItem orderItem = new ROSReturnOrderItem();

                orderItem.setItemId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_ITEM_ID)));
                orderItem.setOrderId(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_ORDER_ID)));
                orderItem.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_ID)));
                orderItem.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_NAME)));
                orderItem.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_BATCH_NAME)));
                orderItem.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_PRICE)));
                orderItem.setProdDiscount(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_DISCOUNT)));
                orderItem.setEffPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_ITEM_VALUE)));
                orderItem.setQtyOrdered(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_QUANTITY)));
                orderItem.setQtyBonus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_FREE_ISSUES)));
                orderItem.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_SUPP_CODE)));
                orderItem.setStockLocationCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_LOCATION_CODE)));
                orderItem.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_AGEN_CODE)));
                orderItem.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_BRAND_CODE)));
                orderItem.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_USER_CODE)));

                orderItemList.add(orderItem);
            }

            c.close();
            db.close();
        }

        return orderItemList;
    }

    protected boolean insertReturnOrderItems(Context context, String orderId, ArrayList<ROSReturnOrderItem> items) {

        SQLiteDatabase db = getWritableDb(context);
        boolean success = true;

        for (int i = 0; i < items.size(); i++) {

            ROSReturnOrderItem orderItem = items.get(i);

            ContentValues values = new ContentValues();

            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_ORDER_ID, orderId);
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_ID, orderItem.getProductCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_NAME, orderItem.getProductDescription());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_BATCH_NAME, orderItem.getProductBatchCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_PRICE, orderItem.getUnitPrice());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_DISCOUNT, orderItem.getProdDiscount());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_ITEM_VALUE, orderItem.getEffPrice());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_QUANTITY, orderItem.getQtyOrdered());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_FREE_ISSUES, orderItem.getQtyBonus());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_SUPP_CODE, orderItem.getSuppCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_LOCATION_CODE, orderItem.getStockLocationCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_AGEN_CODE, orderItem.getAgenCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_BRAND_CODE, orderItem.getBrandCode());
            values.put(ROSDbConstants.ReturnOrderItem.CL_NAME_PRODUCT_USER_CODE, orderItem.getProductUserCode());

            long itemId = db.insert(ROSDbConstants.ReturnOrderItem.TABLE_NAME, null, values);
            if (itemId == -1) {
                success = false;
                break;
            }
        }
        db.close();

        if (!success) {
            deleteReturnOrderItems(context, orderId);
        }

        return success;
    }

    protected void deleteReturnOrderItems(Context context, String orderId) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_RETURN_ORDER_ITEMS = "DELETE FROM " + ROSDbConstants.ReturnOrderItem.TABLE_NAME
                + " WHERE " + ROSDbConstants.ReturnOrderItem.CL_NAME_ORDER_ID + " = '" + orderId + "';";
        db.execSQL(SQL_DELETE_RETURN_ORDER_ITEMS);
        db.close();
    }

    public void clearReturnOrderItemTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_RETURN_ORDER_ITEMS = "DELETE FROM " + ROSDbConstants.ReturnOrderItem.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_RETURN_ORDER_ITEMS);
        db.close();
    }

    /*
    Stock section
     */
    public ArrayList<String> getProductNamesForSale(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_STOCK = "SELECT DISTINCT " + ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME + " FROM " + ROSDbConstants.Stock.TABLE_NAME +
                " WHERE "+ ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY + " > 0 AND " + ROSDbConstants.Stock.CL_NAME_STATUS +" = 0;";
        Cursor c = db.rawQuery(SQL_SELECT_STOCK, null);

        ArrayList<String> productNames = new ArrayList<String>();

        if (c != null) {
            while (c.moveToNext()) {
                productNames.add(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME)));
            }
            c.close();
            db.close();
        }

        return productNames;
    }

    public ArrayList<String> getBatchNamesForSale(Context context, String productName) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_STOCK = "SELECT DISTINCT " + ROSDbConstants.Stock.CL_NAME_BATCH_NAME + " FROM " + ROSDbConstants.Stock.TABLE_NAME +
                " WHERE " + ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME + "='" + productName + "' AND " + ROSDbConstants.Stock.CL_NAME_STATUS + " = 0;";
        Cursor c = db.rawQuery(SQL_SELECT_STOCK, null);

        ArrayList<String> batchNames = new ArrayList<String>();

        if (c != null) {
            while (c.moveToNext()) {
                batchNames.add(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BATCH_NAME)));
            }
            c.close();
            db.close();
        }

        return batchNames;
    }

    public ROSStock getStockForSale(Context context, String productName, String batchName) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_STOCK = "SELECT * FROM " + ROSDbConstants.Stock.TABLE_NAME +
                " WHERE " + ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME + "='" + productName +
                "' AND " + ROSDbConstants.Stock.CL_NAME_BATCH_NAME + "='" + batchName +"';";
        Cursor c = db.rawQuery(SQL_SELECT_STOCK, null);

        ROSStock stock = null;

        if (c != null) {
            c.moveToFirst();
            stock = new ROSStock();

            stock.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME)));
            stock.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BATCH_NAME)));
            stock.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_NAME)));
            stock.setAgenName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME)));
            stock.setQuntityInStock(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY)));
            stock.setAvailableQuantity(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY)));
            stock.setStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_STATUS)));
            stock.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENT_CODE)));
            stock.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_CODE)));
            stock.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE)));
            stock.setCompCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_COMP_CODE)));
            stock.setDistributorCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE)));
            stock.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE)));
            stock.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_SUPP_CODE)));
            stock.setStockLocationCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE)));
            stock.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR)));
            stock.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH)));
            stock.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE)));

            c.close();
        }
        db.close();

        return stock;
    }

    public ArrayList<ROSStock> getStocks(Context context) {

        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_STOCK = "SELECT * FROM " + ROSDbConstants.Stock.TABLE_NAME + ";";
        Cursor c = db.rawQuery(SQL_SELECT_STOCK, null);

        ArrayList<ROSStock> stockList = new ArrayList<ROSStock>();

        if (c != null) {
            while (c.moveToNext()) {
                ROSStock stock = new ROSStock();
                stock.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME)));
                stock.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BATCH_NAME)));
                stock.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_NAME)));
                stock.setAgenName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME)));
                stock.setQuntityInStock(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY)));
                stock.setAvailableQuantity(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY)));
                stock.setStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_STATUS)));
                stock.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENT_CODE)));
                stock.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_CODE)));
                stock.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE)));
                stock.setCompCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_COMP_CODE)));
                stock.setDistributorCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE)));
                stock.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE)));
                stock.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_SUPP_CODE)));
                stock.setStockLocationCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE)));
                stock.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR)));
                stock.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH)));
                stock.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE)));

                stockList.add(stock);
            }
            c.close();
            db.close();
        }

        return stockList;
    }

    public ArrayList<ROSStock> getStocks(Context context, int status) {

        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_STOCK = "SELECT * FROM " + ROSDbConstants.Stock.TABLE_NAME +
                " WHERE " + ROSDbConstants.Stock.CL_NAME_STATUS + " = " + status + ";";
        Cursor c = db.rawQuery(SQL_SELECT_STOCK, null);

        ArrayList<ROSStock> stockList = new ArrayList<ROSStock>();

        if (c != null) {
            while (c.moveToNext()) {
                ROSStock stock = new ROSStock();

                stock.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME)));
                stock.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BATCH_NAME)));
                stock.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_NAME)));
                stock.setAgenName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME)));
                stock.setQuntityInStock(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY)));
                stock.setAvailableQuantity(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY)));
                stock.setStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_STATUS)));
                stock.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_AGENT_CODE)));
                stock.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_BRAND_CODE)));
                stock.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE)));
                stock.setCompCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_COMP_CODE)));
                stock.setDistributorCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE)));
                stock.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE)));
                stock.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_SUPP_CODE)));
                stock.setStockLocationCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE)));
                stock.setAccountYear(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR)));
                stock.setAccountMonth(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH)));
                stock.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE)));

                stockList.add(stock);
            }
            c.close();
            db.close();
        }

        return stockList;
    }

    public void insertStocks(Context context, ArrayList<ROSStock> stocks) {
        SQLiteDatabase db = getWritableDb(context);

        for (int i = 0; i < stocks.size(); i++) {
            ROSStock stock = stocks.get(i);

            ContentValues values = new ContentValues();

            values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME, stock.getProductDescription());
            values.put(ROSDbConstants.Stock.CL_NAME_BATCH_NAME, stock.getProductBatchCode());
            values.put(ROSDbConstants.Stock.CL_NAME_BRAND_NAME, stock.getBrandName());
            values.put(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME, stock.getAgenName());
            values.put(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY, stock.getQuntityInStock());
            values.put(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY, stock.getAvailableQuantity());
            values.put(ROSDbConstants.Stock.CL_NAME_STATUS, stock.getStatus());
            values.put(ROSDbConstants.Stock.CL_NAME_AGENT_CODE, stock.getAgenCode());
            values.put(ROSDbConstants.Stock.CL_NAME_BRAND_CODE, stock.getBrandCode());
            values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE, stock.getProductCode());
            values.put(ROSDbConstants.Stock.CL_NAME_COMP_CODE, stock.getCompCode());
            values.put(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE, stock.getDistributorCode());
            values.put(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE, stock.getUnitPrice());
            values.put(ROSDbConstants.Stock.CL_NAME_SUPP_CODE, stock.getSuppCode());
            values.put(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE, stock.getStockLocationCode());
            values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR, stock.getAccountYear());
            values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH, stock.getAccountMonth());
            values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE, stock.getProductUserCode());

            db.insert(ROSDbConstants.Stock.TABLE_NAME, null, values);
        }

        db.close();
    }

    public void insertStock(Context context, ROSStock stock) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();

        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME, stock.getProductDescription());
        values.put(ROSDbConstants.Stock.CL_NAME_BATCH_NAME, stock.getProductBatchCode());
        values.put(ROSDbConstants.Stock.CL_NAME_BRAND_NAME, stock.getBrandName());
        values.put(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME, stock.getAgenName());
        values.put(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY, stock.getQuntityInStock());
        values.put(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY, stock.getAvailableQuantity());
        values.put(ROSDbConstants.Stock.CL_NAME_STATUS, stock.getStatus());
        values.put(ROSDbConstants.Stock.CL_NAME_AGENT_CODE, stock.getAgenCode());
        values.put(ROSDbConstants.Stock.CL_NAME_BRAND_CODE, stock.getBrandCode());
        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE, stock.getProductCode());
        values.put(ROSDbConstants.Stock.CL_NAME_COMP_CODE, stock.getCompCode());
        values.put(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE, stock.getDistributorCode());
        values.put(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE, stock.getUnitPrice());
        values.put(ROSDbConstants.Stock.CL_NAME_SUPP_CODE, stock.getSuppCode());
        values.put(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE, stock.getStockLocationCode());
        values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR, stock.getAccountYear());
        values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH, stock.getAccountMonth());
        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE, stock.getProductUserCode());

        db.insert(ROSDbConstants.Stock.TABLE_NAME, null, values);
        db.close();
    }

    private void updateStock(Context context, ROSStock stock, int removedQuantity) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_UPDATE_STOCK = "UPDATE " + ROSDbConstants.Stock.TABLE_NAME +
                " SET " + ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY + " = (" + ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY + "-" + removedQuantity + ")" +
                " WHERE " + ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME + " = '" + stock.getProductDescription() +
                "' AND " + ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE + " = '" + stock.getProductCode() +
                "' AND " + ROSDbConstants.Stock.CL_NAME_BATCH_NAME + " = '" + stock.getProductBatchCode() + "';";
        db.execSQL(SQL_UPDATE_STOCK);
        db.close();
    }

    private void insertReturnStock(Context context, ROSStock stock) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();

        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_NAME, stock.getProductDescription());
        values.put(ROSDbConstants.Stock.CL_NAME_BATCH_NAME, stock.getProductBatchCode());
        values.put(ROSDbConstants.Stock.CL_NAME_BRAND_NAME, stock.getBrandName());
        values.put(ROSDbConstants.Stock.CL_NAME_AGENCY_NAME, stock.getAgenName());
        values.put(ROSDbConstants.Stock.CL_NAME_ALLOCATED_QTY, stock.getQuntityInStock());
        values.put(ROSDbConstants.Stock.CL_NAME_AVAILABLE_QTY, stock.getAvailableQuantity());
        values.put(ROSDbConstants.Stock.CL_NAME_STATUS, stock.getStatus());
        values.put(ROSDbConstants.Stock.CL_NAME_AGENT_CODE, stock.getAgenCode());
        values.put(ROSDbConstants.Stock.CL_NAME_BRAND_CODE, stock.getBrandCode());
        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_CODE, stock.getProductCode());
        values.put(ROSDbConstants.Stock.CL_NAME_COMP_CODE, stock.getCompCode());
        values.put(ROSDbConstants.Stock.CL_NAME_DISTRIB_CODE, stock.getDistributorCode());
        values.put(ROSDbConstants.Stock.CL_NAME_UNIT_PRICE, stock.getUnitPrice());
        values.put(ROSDbConstants.Stock.CL_NAME_SUPP_CODE, stock.getSuppCode());
        values.put(ROSDbConstants.Stock.CL_NAME_LOCATION_CODE, stock.getStockLocationCode());
        values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_YEAR, stock.getAccountYear());
        values.put(ROSDbConstants.Stock.CL_NAME_ACCOUNT_MONTH, stock.getAccountMonth());
        values.put(ROSDbConstants.Stock.CL_NAME_PRODUCT_USER_CODE, stock.getProductUserCode());

        db.insert(ROSDbConstants.Stock.TABLE_NAME, null, values);
        db.close();
    }

    public void clearStockTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_STOCK = "DELETE FROM " + ROSDbConstants.Stock.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_STOCK);
        db.close();
    }

    /*
    Product section
     */
    public ArrayList<ROSProduct> getProducts(Context context) {

        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_PRODUCT = "SELECT * FROM " + ROSDbConstants.Product.TABLE_NAME + ";";
        Cursor c = db.rawQuery(SQL_SELECT_PRODUCT, null);

        ArrayList<ROSProduct> productList = new ArrayList<ROSProduct>();

        if (c != null) {
            while (c.moveToNext()) {
                ROSProduct product = new ROSProduct();

                product.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_NAME)));
                product.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BATCH_NAME)));
                product.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BRAND_NAME)));
                product.setAgenName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_AGENCY_NAME)));
                product.setQuntityInStock(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_ALLOCATED_QTY)));
                product.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_AGENT_CODE)));
                product.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BRAND_CODE)));
                product.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_CODE)));
                product.setCompCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_COMP_CODE)));
                product.setDistributorCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_DISTRIB_CODE)));
                product.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_UNIT_PRICE)));
                product.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_SUPP_CODE)));
                product.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_USER_CODE)));

                productList.add(product);
            }
            c.close();
            db.close();
        }

        return productList;
    }

    public ArrayList<String> getProductNamesForReturns(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_PRODUCT_NAMES = "SELECT DISTINCT " + ROSDbConstants.Product.CL_NAME_PRODUCT_NAME + " FROM " + ROSDbConstants.Product.TABLE_NAME + ";";
        Cursor c = db.rawQuery(SQL_SELECT_PRODUCT_NAMES, null);

        ArrayList<String> productNames = new ArrayList<String>();

        if (c != null) {
            while (c.moveToNext()) {
                productNames.add(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_NAME)));
            }
            c.close();
            db.close();
        }

        return productNames;
    }

    public ArrayList<String> getBatchNamesForReturns(Context context, String productName) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_PRODUCT_BATCHES = "SELECT DISTINCT " + ROSDbConstants.Product.CL_NAME_BATCH_NAME + " FROM " + ROSDbConstants.Product.TABLE_NAME +
                " WHERE " + ROSDbConstants.Product.CL_NAME_PRODUCT_NAME + "='" + productName + "';";
        Cursor c = db.rawQuery(SQL_SELECT_PRODUCT_BATCHES, null);

        ArrayList<String> batchNames = new ArrayList<String>();

        if (c != null) {
            while (c.moveToNext()) {
                batchNames.add(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BATCH_NAME)));
            }
            c.close();
            db.close();
        }

        return batchNames;
    }

    public ROSProduct getProductForReturns(Context context, String productName, String batchName) {

        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_PRODUCT = "SELECT * FROM " + ROSDbConstants.Product.TABLE_NAME +
                " WHERE " + ROSDbConstants.Product.CL_NAME_PRODUCT_NAME + "='" + productName +
                "' AND " + ROSDbConstants.Product.CL_NAME_BATCH_NAME + "='" + batchName +"';";
        Cursor c = db.rawQuery(SQL_SELECT_PRODUCT, null);

        ROSProduct product = null;

        if (c != null) {
            c.moveToFirst();
            product = new ROSProduct();

            product.setProductDescription(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_NAME)));
            product.setProductBatchCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BATCH_NAME)));
            product.setBrandName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BRAND_NAME)));
            product.setAgenName(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_AGENCY_NAME)));
            product.setQuntityInStock(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_ALLOCATED_QTY)));
            product.setAgenCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_AGENT_CODE)));
            product.setBrandCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_BRAND_CODE)));
            product.setProductCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_CODE)));
            product.setCompCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_COMP_CODE)));
            product.setDistributorCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_DISTRIB_CODE)));
            product.setUnitPrice(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_UNIT_PRICE)));
            product.setSuppCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_SUPP_CODE)));
            product.setProductUserCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Product.CL_NAME_PRODUCT_USER_CODE)));

            c.close();
        }
        db.close();

        return product;
    }

    public void insertProducts(Context context, ArrayList<ROSProduct> products) {
        SQLiteDatabase db = getWritableDb(context);

        for (int i = 0; i < products.size(); i++) {
            ROSProduct product = products.get(i);
            ContentValues values = new ContentValues();

            values.put(ROSDbConstants.Product.CL_NAME_PRODUCT_NAME, product.getProductDescription());
            values.put(ROSDbConstants.Product.CL_NAME_BATCH_NAME, product.getProductBatchCode());
            values.put(ROSDbConstants.Product.CL_NAME_BRAND_NAME, product.getBrandName());
            values.put(ROSDbConstants.Product.CL_NAME_AGENCY_NAME, product.getAgenName());
            values.put(ROSDbConstants.Product.CL_NAME_ALLOCATED_QTY, product.getQuntityInStock());
            values.put(ROSDbConstants.Product.CL_NAME_AGENT_CODE, product.getAgenCode());
            values.put(ROSDbConstants.Product.CL_NAME_BRAND_CODE, product.getBrandCode());
            values.put(ROSDbConstants.Product.CL_NAME_PRODUCT_CODE, product.getProductCode());
            values.put(ROSDbConstants.Product.CL_NAME_COMP_CODE, product.getCompCode());
            values.put(ROSDbConstants.Product.CL_NAME_DISTRIB_CODE, product.getDistributorCode());
            values.put(ROSDbConstants.Product.CL_NAME_UNIT_PRICE, product.getUnitPrice());
            values.put(ROSDbConstants.Product.CL_NAME_SUPP_CODE, product.getSuppCode());
            values.put(ROSDbConstants.Product.CL_NAME_PRODUCT_USER_CODE, product.getProductUserCode());

            db.insert(ROSDbConstants.Product.TABLE_NAME, null, values);
        }

        db.close();
    }

    public void clearProductTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_PRODUCT = "DELETE FROM " + ROSDbConstants.Product.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_PRODUCT);
        db.close();
    }

    /*
    Visit section
     */
    public void insertVisit(Context context, ROSVisit visit) {
        SQLiteDatabase db = getWritableDb(context);

        ContentValues values = new ContentValues();

        values.put(ROSDbConstants.Visit.CL_NAME_CUST_CODE, visit.getCustCode());
        values.put(ROSDbConstants.Visit.CL_NAME_ADDED_DATE, visit.getAddedDate());
        values.put(ROSDbConstants.Visit.CL_NAME_STATUS, visit.getVisitStatus());
        values.put(ROSDbConstants.Visit.CL_NAME_LATITUDE, visit.getLatitude());
        values.put(ROSDbConstants.Visit.CL_NAME_LONGITUDE, visit.getLongitude());

        db.insert(ROSDbConstants.Visit.TABLE_NAME, null, values);

        db.close();
    }

    public ArrayList<ROSVisit> getPendingVisits(Context context) {

        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_ALL_CUSTOMERS = "SELECT * FROM " + ROSDbConstants.Visit.TABLE_NAME + ";";
        Cursor c = db.rawQuery(SQL_SELECT_ALL_CUSTOMERS, null);

        ArrayList<ROSVisit> visitList = new ArrayList<ROSVisit>();

        if (c != null) {
            while (c.moveToNext()){

                ROSVisit visit = new ROSVisit();

                visit.setCustCode(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Visit.CL_NAME_CUST_CODE)));
                visit.setAddedDate(c.getString(c.getColumnIndexOrThrow(ROSDbConstants.Visit.CL_NAME_ADDED_DATE)));
                visit.setVisitId(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Visit._ID)));
                visit.setVisitStatus(c.getInt(c.getColumnIndexOrThrow(ROSDbConstants.Visit.CL_NAME_STATUS)));
                visit.setLatitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Visit.CL_NAME_LATITUDE)));
                visit.setLongitude(c.getDouble(c.getColumnIndexOrThrow(ROSDbConstants.Visit.CL_NAME_LONGITUDE)));

                visitList.add(visit);
            }

            c.close();
            db.close();
        }

        return  visitList;
    }

    public void deleteVisit(Context context, int visitId) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_NEW_ORDER_ITEMS = "DELETE FROM " + ROSDbConstants.Visit.TABLE_NAME
                + " WHERE " + ROSDbConstants.Visit._ID + " = " + visitId + ";";
        db.execSQL(SQL_DELETE_NEW_ORDER_ITEMS);
        db.close();
    }

    public int getVisitCountPending(Context context) {
        SQLiteDatabase db = getReadableDb(context);

        final String SQL_SELECT_NEW_ORDERS = "SELECT * FROM " + ROSDbConstants.Visit.TABLE_NAME +
                " WHERE " + ROSDbConstants.Visit.CL_NAME_STATUS + " = " + 0 + ";";
        Cursor c = db.rawQuery(SQL_SELECT_NEW_ORDERS, null);

        int count = 0;

        if (c != null) {
            count = c.getCount();
            c.close();
        }

        db.close();

        return  count;
    }

    public void clearVisitTable(Context context) {
        SQLiteDatabase db = getWritableDb(context);

        final String SQL_DELETE_PRODUCT = "DELETE FROM " + ROSDbConstants.Visit.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_PRODUCT);
        db.close();
    }


    /*
    Test methods
     */
    private void addCustomers(Context context) {
        for (int i = 0; i < 20; i++) {
            ROSCustomer customer = new ROSCustomer();
            customer.setCustomerId("" + (i+1)*5);
            customer.setFirstName("Temp customer " + (i+1));
            customer.setLastName("");
            customer.setShopName("Shop " + (i+1));
            customer.setTown("Town " + (i+1));
            customer.setTownId("" + (i+1)*5);
            customer.setOutstanding((i+1)*1000);

            insertCustomer(context, customer);
        }
    }

    private void addStocks(Context context) {
        for (int i = 0; i < 50; i++) {
            ROSStock stock = new ROSStock();
            stock.setProductDescription("Product " + (i + 1));
            stock.setProductBatchCode("Batch " + (i + 1));
            stock.setBrandName("Brand " + (i + 1));
            stock.setAgenName("Agency " + (i + 1));
            stock.setQuntityInStock((i + 1) * 100);
            stock.setAvailableQuantity((i + 1) * 100);

            insertStock(context, stock);
        }
    }

    public void addProducts(Context context) {

        ArrayList<ROSProduct> products = new ArrayList<ROSProduct>();

        for (int i = 0; i < 200; i++) {
            ROSProduct product = new ROSProduct();
            product.setProductDescription("Product " + (i + 1));
            product.setProductBatchCode("Batch " + (i + 1));
            product.setBrandName("Brand " + (i+1));
            product.setAgenName("Agency " + (i + 1));
            products.add(product);
        }

        insertProducts(context, products);
    }
}
