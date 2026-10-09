package com.example.documentapi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class DocumentRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    public String supplier;
    public String invoiceNumber;
    public LocalDate invoiceDate;
    public BigDecimal total;
    @Enumerated(EnumType.STRING) public ProcessingStatus status;
    @Column(length = 1000) public String reviewReason;
    public OffsetDateTime createdAt = OffsetDateTime.now();
}
