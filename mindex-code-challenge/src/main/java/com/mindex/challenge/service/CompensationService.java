package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

//interface is in case alternate implemenation of compensation is to be created in the future
public interface CompensationService {
    Compensation create(Compensation compensation);
    Compensation get(String id);
}