package com.example.documentapi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${APP_ORIGIN:http://localhost:3000}")
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentRepository repository;
    DocumentController(DocumentRepository repository) { this.repository = repository; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    DocumentRecord create(@RequestBody ExtractedDocument input) {
        DocumentRecord document = new DocumentRecord();
        document.supplier = input.supplier();
        document.invoiceNumber = input.invoiceNumber();
        document.invoiceDate = input.invoiceDate();
        document.total = input.total();
        document.reviewReason = missingRequiredField(input) ? "Faltan campos obligatorios" : null;
        document.status = document.reviewReason == null ? ProcessingStatus.PROCESSED : ProcessingStatus.REQUIRES_REVIEW;
        return repository.save(document);
    }

    @GetMapping
    List<DocumentRecord> all() { return repository.findAll(); }

    private boolean missingRequiredField(ExtractedDocument input) {
        return input.supplier() == null || input.supplier().isBlank() || input.invoiceNumber() == null || input.invoiceNumber().isBlank() || input.invoiceDate() == null || input.total() == null || input.total().compareTo(BigDecimal.ZERO) <= 0;
    }

    record ExtractedDocument(String supplier, String invoiceNumber, LocalDate invoiceDate, BigDecimal total) { }
}
