package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure computeReportingStructure(String employeeId) {
        LOG.debug("Computing report structure for employee with id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        int numberOfDirectReports = calculateDirectReports(employee);

        return new ReportingStructure(employeeId, numberOfDirectReports);
    }

    private int calculateDirectReports(Employee employee) {
        if (employee == null || employee.getDirectReports() == null) {
            return 0;
        }
        int directReportCount = employee.getDirectReports().size();

        List<String> directReportIds = employee.getDirectReports()
                .stream()
                .map(Employee::getEmployeeId)
                .collect(Collectors.toList());

        for (String directReportId : directReportIds) {
            Employee directReport = employeeRepository.findByEmployeeId(directReportId);
            directReportCount += calculateDirectReports(directReport);
        }

        return directReportCount;
    }
}
