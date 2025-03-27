package com.example.expnecemgmt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.expnecemgmt.services.ExpenseService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalExpenses", expenseService.getTotalExpenses());
        response.put("monthlyBudget", expenseService.getMonthlyBudget());
        response.put("recentTransactions", expenseService.getRecentTransactions());
        return ResponseEntity.ok(response);
    }
}
