package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;


@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    //The create for compensation simply takes the passes compensation and saves it to the repository
    @Override
    public Compensation create(Compensation compensation){
        LOG.debug("Creating compensation [{}]", compensation);
        Compensation comp = compensationRepository.insert(compensation); // returns the returned compensation to ensure that it was properly saved
        return comp;
    }

    //the getter grabs the compensation from the repository and does a simple check to make sure it exists
    @Override
    public Compensation get(String id){
        LOG.debug("Creating compensation [{}]", id);
        Compensation compensation = compensationRepository.findByEmployeeId(id);
        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        return compensation;
    }
}