package com.example.expnecemgmt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.expnecemgmt.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Restore this method if it was originally present
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.status = 'PENDING'")
    long getPendingApprovals();
}
