package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportingStructureServiceImplTest {

    @MockBean
    EmployeeRepository employeeRepository;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Test
    public void testComputeReportingStructure() {
        Employee junior = new Employee();
        junior.setEmployeeId("c");

        Employee boss = new Employee();
        boss.setEmployeeId("b");
        boss.setDirectReports(Collections.singletonList(junior));

        Employee ceo = new Employee();
        ceo.setEmployeeId("a");
        ceo.setDirectReports(Collections.singletonList(boss));

        Mockito.when(employeeRepository.findByEmployeeId(junior.getEmployeeId())).thenReturn(junior);
        Mockito.when(employeeRepository.findByEmployeeId(boss.getEmployeeId())).thenReturn(boss);
        Mockito.when(employeeRepository.findByEmployeeId(ceo.getEmployeeId())).thenReturn(ceo);

        ReportingStructure juniorReportingStructure = reportingStructureService.computeReportingStructure(junior.getEmployeeId());
        ReportingStructure bossReportingStructure = reportingStructureService.computeReportingStructure(boss.getEmployeeId());
        ReportingStructure ceoReportingStructure = reportingStructureService.computeReportingStructure(ceo.getEmployeeId());

        assertAll("Reporting structure value checks",
                () -> assertEquals(0, juniorReportingStructure.getNumberOfReports()),
                () -> assertEquals(junior.getEmployeeId(), juniorReportingStructure.getEmployee()),
                () -> assertEquals(1, bossReportingStructure.getNumberOfReports()),
                () -> assertEquals(boss.getEmployeeId(), bossReportingStructure.getEmployee()),
                () -> assertEquals(2, ceoReportingStructure.getNumberOfReports()),
                () -> assertEquals(ceo.getEmployeeId(), ceoReportingStructure.getEmployee()));
    }
}
