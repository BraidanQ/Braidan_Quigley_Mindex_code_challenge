package com.mindex.challenge.service.impl;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
    }

    @Test
    public void testCreateCompensation() {
        
        Compensation testComp = new Compensation();
        testComp.setEmployeeId(UUID.randomUUID().toString());
        testComp.setSalary(40000);
        testComp.setEffectivDate(new Date());

        // Create checks
        ResponseEntity<Compensation> response = restTemplate.postForEntity(compensationUrl, testComp, Compensation.class);

        assertNotNull(response.getBody().getEmployeeId());
        assertEquals(testComp.getEffectivDate(), response.getBody().getEffectivDate());
        assertEquals(testComp.getEmployeeId(), response.getBody().getEmployeeId());
        assertEquals(testComp.getSalary(), response.getBody().getSalary());
    }
}
