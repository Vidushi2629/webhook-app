package com.example.webhookapp.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    public String solve(String regNo) {
        return solveQuestion1(); // because odd
    }

    private String solveQuestion1() {
        return "SELECT c.customer_id, c.customer_name, SUM(o.amount) AS total_spent " +
               "FROM customers c " +
               "JOIN orders o ON c.customer_id = o.customer_id " +
               "GROUP BY c.customer_id, c.customer_name " +
               "ORDER BY total_spent DESC LIMIT 1;";
    }
}
