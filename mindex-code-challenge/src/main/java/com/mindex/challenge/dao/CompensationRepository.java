package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.data.Compensation;

//compensation should get its own repository becasue it stores data that is different from that of an employee.
//so unless compensation is attached to the employee class it should likely have its own repository
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    Compensation findByEmployeeId(String employeeId);
}
