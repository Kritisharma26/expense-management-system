package com.example.expnecemgmt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private double amount;
    private String status;
    private String description; // ✅ Added description field

    @Temporal(TemporalType.DATE)
    private Date date;
}
