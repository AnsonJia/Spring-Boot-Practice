package com.codewithmosh.store.entities;

// Represents the valid order statuses for orders
public enum OrderStatus {//Using an enum restricts status to  predefined values only
    PENDING,
    PAID,
    FAILED,
    CANCELED
}
