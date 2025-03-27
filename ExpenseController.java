package com.example.expnecemgmt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.expnecemgmt.services.ExpenseService;
import com.example.expnecemgmt.models.Expense;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/expenses", produces = "application/json")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/dashboard-data")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalExpenses", expenseService.getTotalExpenses());
        response.put("monthlyBudget", expenseService.getMonthlyBudget());
        response.put("recentTransactions", expenseService.getRecentTransactions());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending-approvals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPendingApprovals() {
        Map<String, Object> response = new HashMap<>();
        response.put("pendingApprovals", expenseService.getPendingApprovals());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(Map.of("message", "Expense added successfully!", "expense", savedExpense));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(Map.of("message", "Expense deleted successfully!"));
    }
}






