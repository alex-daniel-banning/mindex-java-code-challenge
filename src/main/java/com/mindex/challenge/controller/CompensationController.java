package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    CompensationService compensationService;

    @PostMapping("/employee/{id}/compensation")
    public Compensation create(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        boolean compensationEmployeeIdsMatch = compensation.getEmployee() == null || compensation.getEmployee().equals(id);
        if (!compensationEmployeeIdsMatch) {
            throw new IllegalArgumentException("Employee id in url path must match employee id in request object");
        }

        compensation.setEmployee(id);
        return compensationService.create(compensation);
    }

    @GetMapping("/employee/{id}/compensation")
    public ResponseEntity<Compensation> read(@PathVariable String id) {
        LOG.debug("Received get request for compensation of employee with id [{}]", id);

        try {
            Compensation compensation = compensationService.read(id);
            return ResponseEntity.ok(compensation);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
