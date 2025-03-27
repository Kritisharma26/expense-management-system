package com.example.expnecemgmt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.expnecemgmt.services.ExpenseService;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ExpenseService expenseService;

    // ✅ Approve an expense
    @PostMapping("/approve")
    public ResponseEntity<String> approveExpense(@RequestParam Long expenseId) {
        boolean success = expenseService.approveExpense(expenseId);
        if (success) {
            return ResponseEntity.ok("Expense approved successfully!");
        } else {
            return ResponseEntity.badRequest().body("Expense not found or already approved.");
        }
    }

    // ✅ Reject an expense
    @PostMapping("/reject")
    public ResponseEntity<String> rejectExpense(@RequestParam Long expenseId) {
        boolean success = expenseService.rejectExpense(expenseId);
        if (success) {
            return ResponseEntity.ok("Expense rejected successfully!");
        } else {
            return ResponseEntity.badRequest().body("Expense not found or already rejected.");
        }
    }
}
