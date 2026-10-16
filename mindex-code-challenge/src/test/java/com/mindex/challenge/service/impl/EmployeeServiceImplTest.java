package com.mindex.challenge.service.impl;

import java.util.ArrayList;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    //testing method for the various outcomes of the reporting structures getter
    @Test
    public void testGetReportingStructure(){
        Employee testEmployee1 = new Employee();
        Employee testEmployee2 = new Employee();
        Employee testEmployee3 = new Employee();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        ArrayList list = new ArrayList();
        list.add(createdEmployee2);
        list.add(createdEmployee3);
        testEmployee1.setDirectReports(list);
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        //for an employee with 2 reports
        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity("http://localhost:"+ port +"/reportingStructure/{id}", ReportingStructure.class, createdEmployee1.getEmployeeId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdEmployee1.getEmployeeId(), response.getBody().getEmployee().getEmployeeId());
        assertEquals(2, response.getBody().getNumberOfReports());

        //for an employee with no reports
        response = restTemplate.getForEntity("http://localhost:"+ port +"/reportingStructure/{id}", ReportingStructure.class, createdEmployee2.getEmployeeId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdEmployee2.getEmployeeId(), response.getBody().getEmployee().getEmployeeId());
        assertEquals(0, response.getBody().getNumberOfReports());

        //for an non existant employee
        response = restTemplate.getForEntity("http://localhost:"+ port +"/reportingStructure/{id}", ReportingStructure.class, new Employee().getEmployeeId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    } 
}
