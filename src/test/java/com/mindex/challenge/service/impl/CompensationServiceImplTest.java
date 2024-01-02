package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    @LocalServerPort
    private int port;
    private final String EMPLOYEE = String.valueOf(UUID.randomUUID());

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    /* This will create a compensation object using the rest endpoint, end then another with a more recent effective date.
    *  It then checks if the get request for the employee returns the effective compensation. */
    @Test
    public void testCreateRead() {
        Compensation initialCompensation = new Compensation();
        initialCompensation.setEmployee(EMPLOYEE);
        initialCompensation.setSalary(100);
        initialCompensation.setEffectiveDate(localDateFromString("2010-01-01"));

        Compensation newCompensation = new Compensation();
        newCompensation.setEmployee(EMPLOYEE);
        newCompensation.setSalary(500);
        newCompensation.setEffectiveDate(localDateFromString("2012-01-01"));

        Compensation createdCompensation1 = restTemplate.postForEntity(compensationUrl, initialCompensation, Compensation.class, EMPLOYEE).getBody();
        assertCompensationEquivalence(createdCompensation1, initialCompensation);

        Compensation readCompensation1 = restTemplate.getForEntity(compensationUrl, Compensation.class, initialCompensation.getEmployee(), EMPLOYEE).getBody();
        assertCompensationEquivalence(readCompensation1, initialCompensation);

        Compensation createdCompensation2 = restTemplate.postForEntity(compensationUrl, newCompensation, Compensation.class, EMPLOYEE).getBody();
        assertCompensationEquivalence(createdCompensation2, newCompensation);

        Compensation readCompensation2 = restTemplate.getForEntity(compensationUrl, Compensation.class, EMPLOYEE).getBody();
        assertCompensationEquivalence(readCompensation2, newCompensation);
    }

    private LocalDate localDateFromString(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(string, formatter);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
    }
}
