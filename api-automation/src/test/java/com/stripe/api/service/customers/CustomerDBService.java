package com.stripe.api.service.customers;

import com.stripe.utilities.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database service for customer-related queries.
 *
 * Responsibilities:
 * - Query customer records from DB for verification
 * - Provide reusable DB assertion helpers for customer tests
 */
public class CustomerDBService {

    private final DatabaseManager dbManager;

    public CustomerDBService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Checks if a customer exists in the database by Stripe customer ID.
     */
    public boolean customerExistsById(String customerId) {
        ResultSet rs = dbManager.executeQuery(
                "SELECT COUNT(*) AS count FROM customers WHERE stripe_customer_id = ?", customerId);
        try {
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check customer existence", e);
        }
        return false;
    }

    /**
     * Fetches the customer name stored in DB by Stripe customer ID.
     */
    public String getCustomerNameById(String customerId) {
        ResultSet rs = dbManager.executeQuery(
                "SELECT name FROM customers WHERE stripe_customer_id = ?", customerId);
        try {
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer name", e);
        }
        return null;
    }

    /**
     * Fetches the customer email stored in DB by Stripe customer ID.
     */
    public String getCustomerEmailById(String customerId) {
        ResultSet rs = dbManager.executeQuery(
                "SELECT email FROM customers WHERE stripe_customer_id = ?", customerId);
        try {
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer email", e);
        }
        return null;
    }

    /**
     * Fetches the metadata (order_id) for a customer from DB.
     */
    public String getCustomerMetadataOrderId(String customerId) {
        ResultSet rs = dbManager.executeQuery(
                "SELECT order_id FROM customer_metadata WHERE stripe_customer_id = ?", customerId);
        try {
            if (rs.next()) {
                return rs.getString("order_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer metadata", e);
        }
        return null;
    }
}

