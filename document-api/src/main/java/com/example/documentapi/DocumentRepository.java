package com.example.documentapi;

import org.springframework.data.jpa.repository.JpaRepository;

interface DocumentRepository extends JpaRepository<DocumentRecord, Long> { }
