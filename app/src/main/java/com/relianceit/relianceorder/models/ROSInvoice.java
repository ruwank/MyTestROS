package com.relianceit.relianceorder.models;

import java.util.ArrayList;

/**
 * Created by Suresh on 5/14/15.
 */
public class ROSInvoice extends ROSNewOrder{

    public ArrayList<String> getProductNames() {
        ArrayList<String> names = new ArrayList<>();

        if (this.Products != null && this.Products.size() > 0) {
            for (int i = 0; i < this.Products.size(); i++) {
                ROSNewOrderItem orderItem = this.Products.get(i);
                String name = orderItem.getProductDescription();
                if (!names.contains(name)) {
                    names.add(name);
                }
            }
        }

        return names;
    }

    public ArrayList<ROSProduct> getProductsForReturns() {
        ArrayList<ROSProduct> products = new ArrayList<>();

        ArrayList<String> names = new ArrayList<>();

        if (this.Products != null && this.Products.size() > 0) {
            for (int i = 0; i < this.Products.size(); i++) {
                ROSNewOrderItem foundItem = this.Products.get(i);
                String name = foundItem.getProductDescription();
                if (!names.contains(name)) {
                    names.add(name);

                    ROSProduct product = new ROSProduct();
                    product.setProductDescription(foundItem.getProductDescription());
                    product.setProductBatchCode(foundItem.getProductBatchCode());
                    product.setProductCode(foundItem.getProductCode());
                    product.setUnitPrice(foundItem.getUnitPrice());
                    product.setQuntityInStock(foundItem.getQtyOrdered() + foundItem.getQtyBonus());
                    product.setBrandCode(foundItem.getBrandCode());
                    product.setBrandName(foundItem.getBrandName());
                    product.setProductUserCode(foundItem.getProductUserCode());

                    products.add(product);
                }
            }
        }

        return products;
    }

    public ArrayList<String> getBatchNames(String productName) {
        ArrayList<String> batches = new ArrayList<>();

        if (this.Products != null && this.Products.size() > 0) {
            for (int i = 0; i < this.Products.size(); i++) {
                ROSNewOrderItem orderItem = this.Products.get(i);
                String name = orderItem.getProductDescription();
                if (productName.equalsIgnoreCase(name)) {
                    String batch = orderItem.getProductBatchCode();
                    if (!batches.contains(batch)) {
                        batches.add(batch);
                    }
                }
            }
        }

        return batches;
    }

    public ArrayList<ROSProduct> getBatchesForReturns(String productName) {

        ArrayList<ROSProduct> products = new ArrayList<>();
        ArrayList<String> batches = new ArrayList<>();

        if (this.Products != null && this.Products.size() > 0) {
            for (int i = 0; i < this.Products.size(); i++) {
                ROSNewOrderItem foundItem = this.Products.get(i);
                String name = foundItem.getProductDescription();
                if (productName.equalsIgnoreCase(name)) {
                    String batch = foundItem.getProductBatchCode();
                    if (!batches.contains(batch)) {
                        batches.add(batch);

                        ROSProduct product = new ROSProduct();
                        product.setProductDescription(foundItem.getProductDescription());
                        product.setProductBatchCode(foundItem.getProductBatchCode());
                        product.setProductCode(foundItem.getProductCode());
                        product.setUnitPrice(foundItem.getUnitPrice());
                        product.setQuntityInStock(foundItem.getQtyOrdered() + foundItem.getQtyBonus());
                        product.setBrandCode(foundItem.getBrandCode());
                        product.setBrandName(foundItem.getBrandName());
                        product.setProductUserCode(foundItem.getProductUserCode());

                        products.add(product);
                    }
                }
            }
        }

        return products;
    }

    public ROSProduct getProduct(String productName, String batchName) {

        ROSNewOrderItem foundItem = null;

        if (this.Products != null && this.Products.size() > 0) {
            for (int i = 0; i < this.Products.size(); i++) {
                ROSNewOrderItem orderItem = this.Products.get(i);
                String name = orderItem.getProductDescription();
                String batch = orderItem.getProductBatchCode();
                if (productName.equalsIgnoreCase(name) && batchName.equalsIgnoreCase(batch)) {
                    foundItem = orderItem;
                    break;
                }
            }
        }

        if (foundItem != null) {
            ROSProduct product = new ROSProduct();
            product.setProductDescription(foundItem.getProductDescription());
            product.setProductBatchCode(foundItem.getProductBatchCode());
            product.setProductCode(foundItem.getProductCode());
            product.setUnitPrice(foundItem.getUnitPrice());
            product.setQuntityInStock(foundItem.getQtyOrdered() + foundItem.getQtyBonus());
            product.setBrandCode(foundItem.getBrandCode());
            product.setBrandName(foundItem.getBrandName());
            product.setProductUserCode(foundItem.getProductUserCode());

            return product;
        }

        return null;
    }

}
