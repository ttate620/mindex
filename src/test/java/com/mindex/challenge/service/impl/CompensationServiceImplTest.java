package com.mindex.challenge.service.impl;


import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationIdUrl;
    private String compensationUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        compensationUrl = "http://localhost:" + port + "/compensation/";
    }

    @Test
    public void testCreateRead() throws ParseException {

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        String date_str = "2022/02/01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = formatter.parse(date_str);

        Compensation testComp = new Compensation();
        testComp.setEmployee(createdEmployee);
        testComp.setEffectiveDate(date);
        testComp.setSalary(new BigDecimal(123000.00));

        // Check create
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testComp, Compensation.class).getBody();

        assertNotNull(createdCompensation);

        // Check read
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();

        assertNotNull(readCompensation);

        // Check what is returned by read and create are equal. Must check equivalence this way because the object ID's will be different but the contents will be the same.
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }



    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
    }

}
