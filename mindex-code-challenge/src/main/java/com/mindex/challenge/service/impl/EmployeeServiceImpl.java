package com.mindex.challenge.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure get(String id){
        ReportingStructure reporting = new ReportingStructure();
        Employee employee = read(id);
        reporting.setEmployee(employee);
        reporting.setNumberOfReports(getNumReports(employee));
        return reporting;
    }

    private int getNumReports(Employee employee){
        int numReports = 0;
        if(employee.getDirectReports() != null){
            for (Employee reportingEmployee : employee.getDirectReports()){
                numReports++;
                reportingEmployee = employeeRepository.findByEmployeeId(reportingEmployee.getEmployeeId());
                numReports += getNumReports(reportingEmployee);
            }
        }
        return numReports;
    }
}
