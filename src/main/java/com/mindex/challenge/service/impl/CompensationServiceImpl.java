package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        if (compensation.getEmployee() == null) {
            throw new IllegalArgumentException("Compensation must have a valid employeeId, must not be null");
        }

        LOG.debug("Creating compensation for employee with id [{}]", compensation.getEmployee());
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }

        List<Compensation> compensations = compensationRepository.findByEmployeeOrderByEffectiveDateDesc(employeeId);
        if (compensations.isEmpty()) {
            throw new NoSuchElementException("No compensation found for employee with id " + employeeId);
        }
        return compensations.get(0);
    }
}
