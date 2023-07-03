package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Generating reporting structure for employeeId [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            LOG.error("Cannot find employeeId [{}]", employeeId);
            throw new RuntimeException("Invalid employeeId");
        }

        ReportingStructure reportingStructure = new ReportingStructure();

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(getReports(employee));

        return reportingStructure;
    }


    private int getReports(Employee employeeWithNullReports) {
        int total = 0;
        // Employee parameter has directReports with only employeeId and null for all other fields
        // including direct reports. Must find employee by employeeId to get non-null list of directReports
        Employee employee = employeeRepository.findByEmployeeId(employeeWithNullReports.getEmployeeId());
        List<Employee> employeeReports = employee.getDirectReports();
        if (employeeReports == null) {
            return total;
        }
        else {
            for (Employee directReport: employeeReports) {
                total += 1 + getReports(directReport);
            }
            return total;
        }

    }
}
