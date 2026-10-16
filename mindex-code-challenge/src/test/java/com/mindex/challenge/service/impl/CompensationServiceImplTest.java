package com.mindex.challenge.service.impl;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationGetUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationGetUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateCompensation() {

        Compensation testComp = new Compensation();
        testComp.setEmployeeId(UUID.randomUUID().toString());
        testComp.setSalary(40000);
        testComp.setEffectivDate(new Date());

        // Create checks
        ResponseEntity<Compensation> createResponse = restTemplate.postForEntity(compensationUrl, testComp, Compensation.class);

        System.out.println("POST status: " + createResponse.getStatusCode());
        System.out.println("POST body: " + createResponse.getBody());

        assertEquals(createResponse.getStatusCode(),HttpStatus.OK);
        assertNotNull(createResponse.getBody().getEmployeeId());
        assertEquals(testComp.getEffectivDate(), createResponse.getBody().getEffectivDate());
        assertEquals(testComp.getEmployeeId(), createResponse.getBody().getEmployeeId());
        assertEquals(testComp.getSalary(), createResponse.getBody().getSalary());

        // get checks
        ResponseEntity<Compensation> getResponse = restTemplate.getForEntity(compensationGetUrl, Compensation.class, createResponse.getBody().getEmployeeId());

        assertEquals(getResponse.getStatusCode(),HttpStatus.OK);
        assertNotNull(getResponse.getBody().getEmployeeId());
        assertEquals(testComp.getEffectivDate(), getResponse.getBody().getEffectivDate());
        assertEquals(testComp.getEmployeeId(), getResponse.getBody().getEmployeeId());
        assertEquals(testComp.getSalary(), getResponse.getBody().getSalary());

        getResponse = restTemplate.getForEntity(compensationGetUrl, Compensation.class, "not an id");

        assertEquals(getResponse.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
        assertNotNull(getResponse.getBody());
        assertNull(getResponse.getBody().getEmployeeId());
    }
}
