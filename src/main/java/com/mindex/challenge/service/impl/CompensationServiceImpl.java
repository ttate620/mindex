package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    CompensationRepository compensationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        // Don't allow api users to create multiple compensations for the same employee
        Compensation comp = compensationRepository.findByEmployeeEmployeeId(compensation.getEmployee().getEmployeeId());
        if (comp != null) {
            LOG.error("Compensation for employeeId [{}] already exists: ", compensation.getEmployee().getEmployeeId());
            throw new RuntimeException("Compensation for employee already exists");
        }
        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employee with id [{}]", employeeId);

        Compensation compensation = compensationRepository.findByEmployeeEmployeeId(employeeId);

        if (compensation == null) {
            LOG.error("Cannot find compensation for employeeId [{}]", employeeId);
            throw new RuntimeException("Invalid employeeId");
        }
        return compensation;
    }
}
