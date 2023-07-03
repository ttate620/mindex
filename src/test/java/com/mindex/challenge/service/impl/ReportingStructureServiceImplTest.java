package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String reportUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportUrl = "http://localhost:" + port + "/report/{id}";
    }

    // Test for an employee with no direct reports
    @Test
    public void testReadNoReports() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();

        // Make sure nothing was returned as null
        assertNotNull(reportingStructure);

        // Make sure the employee created with the employee endpoint is the same as the one stored in the reporting structure
        assertEmployeeEquivalence(createdEmployee, reportingStructure.getEmployee());

        // Make sure the number of direct reports is correct
        assertEquals(0, reportingStructure.getNumberOfReports());
    }

    // Test for an employee with only on direct report
    @Test
    public void testReadFewReports() {
        /* The structure of this test's employees
                    1
                   /
                  2
       */

        Employee testEmp1 = new Employee();
        testEmp1.setFirstName("Mic");
        testEmp1.setLastName("Jagger");
        testEmp1.setDepartment("Musician");
        testEmp1.setPosition("Harmonica");

        Employee testEmp2 = new Employee();
        testEmp2.setFirstName("Keith");
        testEmp2.setLastName("Richards");
        testEmp2.setDepartment("Musician");
        testEmp2.setPosition("Guitar");

        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmp2, Employee.class).getBody();

        List<Employee> directReportsList = new ArrayList();
        directReportsList.add(createdEmployee2);
        testEmp1.setDirectReports(directReportsList);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmp1, Employee.class).getBody();

        ReportingStructure reportingStructure1 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        ReportingStructure reportingStructure2 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee2.getEmployeeId()).getBody();

        // Make sure nothing was returned as null
        assertNotNull(reportingStructure1);
        assertNotNull(reportingStructure2);

        // Make sure the employee created with the employee endpoint is the same as the one stored in the reporting structure
        assertEmployeeEquivalence(createdEmployee1, reportingStructure1.getEmployee());
        assertEmployeeEquivalence(createdEmployee2, reportingStructure2.getEmployee());

        // Make sure the number of direct reports is correct
        assertEquals(1, reportingStructure1.getNumberOfReports());
        assertEquals(0, reportingStructure2.getNumberOfReports());
    }

    @Test
    public void testReadNestedReports() {
        /* The structure of this test's employees
                    1
                   / \
                  2   3
                     / \
                    4   5
                         \
                          6
       */
        Employee testEmp6 = new Employee();
        testEmp6.setFirstName("Keith");
        testEmp6.setLastName("Richards");
        testEmp6.setDepartment("Musician");
        testEmp6.setPosition("Guitar");

        Employee createdEmployee6 = restTemplate.postForEntity(employeeUrl, testEmp6, Employee.class).getBody();

        Employee testEmp5 = new Employee();
        testEmp5.setFirstName("Keith");
        testEmp5.setLastName("Richards");
        testEmp5.setDepartment("Musician");
        testEmp5.setPosition("Guitar");

        List<Employee> directReportsList5 = new ArrayList();
        directReportsList5.add(createdEmployee6);
        testEmp5.setDirectReports(directReportsList5);

        Employee createdEmployee5 = restTemplate.postForEntity(employeeUrl, testEmp5, Employee.class).getBody();

        Employee testEmp4 = new Employee();
        testEmp4.setFirstName("Brian");
        testEmp4.setLastName("Jones");
        testEmp4.setDepartment("Musician");
        testEmp4.setPosition("Guitar");

        Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmp4, Employee.class).getBody();

        Employee testEmp3 = new Employee();
        testEmp3.setFirstName("Charlie");
        testEmp3.setLastName("Watts");
        testEmp3.setDepartment("Musician");
        testEmp3.setPosition("Drum");

        List<Employee> directReportsList3 = new ArrayList();
        directReportsList3.add(createdEmployee4);
        directReportsList3.add(createdEmployee5);
        testEmp3.setDirectReports(directReportsList3);

        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmp3, Employee.class).getBody();

        Employee testEmp2 = new Employee();
        testEmp2.setFirstName("Ronnie");
        testEmp2.setLastName("Wood");
        testEmp2.setDepartment("Musician");
        testEmp2.setPosition("Bass");

        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmp2, Employee.class).getBody();

        Employee testEmp1 = new Employee();
        testEmp1.setFirstName("Bill");
        testEmp1.setLastName("Wyman");
        testEmp1.setDepartment("Musician");
        testEmp1.setPosition("Bass");

        List<Employee> directReportsList1 = new ArrayList();
        directReportsList1.add(createdEmployee2);
        directReportsList1.add(createdEmployee3);
        testEmp1.setDirectReports(directReportsList1);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmp1, Employee.class).getBody();


        ReportingStructure reportingStructure1 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        ReportingStructure reportingStructure2 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee2.getEmployeeId()).getBody();
        ReportingStructure reportingStructure3 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee3.getEmployeeId()).getBody();
        ReportingStructure reportingStructure4 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee4.getEmployeeId()).getBody();
        ReportingStructure reportingStructure5 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee5.getEmployeeId()).getBody();
        ReportingStructure reportingStructure6 = restTemplate.getForEntity(reportUrl, ReportingStructure.class, createdEmployee6.getEmployeeId()).getBody();

        // Testing

        // Make sure nothing was returned as null
        assertNotNull(reportingStructure1);
        assertNotNull(reportingStructure2);
        assertNotNull(reportingStructure3);
        assertNotNull(reportingStructure4);
        assertNotNull(reportingStructure5);
        assertNotNull(reportingStructure6);

        // Make sure the employee created with the employee endpoint is the same as the one stored in the reporting structure
        assertEmployeeEquivalence(createdEmployee1, reportingStructure1.getEmployee());
        assertEmployeeEquivalence(createdEmployee2, reportingStructure2.getEmployee());
        assertEmployeeEquivalence(createdEmployee3, reportingStructure3.getEmployee());
        assertEmployeeEquivalence(createdEmployee4, reportingStructure4.getEmployee());
        assertEmployeeEquivalence(createdEmployee5, reportingStructure5.getEmployee());
        assertEmployeeEquivalence(createdEmployee6, reportingStructure6.getEmployee());

        // Make sure the number of direct reports is correct
        assertEquals(5, reportingStructure1.getNumberOfReports());
        assertEquals(0, reportingStructure2.getNumberOfReports());
        assertEquals(3, reportingStructure3.getNumberOfReports());
        assertEquals(0, reportingStructure4.getNumberOfReports());
        assertEquals(1, reportingStructure5.getNumberOfReports());
        assertEquals(0, reportingStructure6.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
