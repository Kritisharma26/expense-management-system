package com.example.expnecemgmt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.expnecemgmt.models.Expense;
import com.example.expnecemgmt.repositories.ExpenseRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public double getTotalExpenses() {
        return expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public long getPendingApprovals() { 
        return expenseRepository.getPendingApprovals(); // Revert back to the previous method
    }

    public double getMonthlyBudget() {
        return 5000.0;
    }

    public List<Expense> getRecentTransactions() {
        return expenseRepository.findAll()
            .stream()
            .filter(e -> e.getDate() != null)
            .sorted(Comparator.comparing(Expense::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(5)
            .collect(Collectors.toList());
    }
    
    public boolean approveExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        if (!"APPROVED".equals(expense.getStatus())) {
            expense.setStatus("APPROVED");
            expenseRepository.save(expense);
            return true; // Successfully approved
        }
        return false; // Already approved
    }
    
    public boolean rejectExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        if (!"REJECTED".equals(expense.getStatus())) {
            expense.setStatus("REJECTED");
            expenseRepository.save(expense);
            return true; // Successfully rejected
        }
        return false; // Already rejected
    }
    
    
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
