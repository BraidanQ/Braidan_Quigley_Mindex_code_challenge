package com.mindex.challenge.data;

import java.util.Date;

public class Compensation{
    private String employeeId;
    private int salary;
    private Date effectivDate;

    public Compensation(){
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getEffectivDate() {
        return effectivDate;
    }

    public void setEffectivDate(Date effectivDate) {
        this.effectivDate = effectivDate;
    }
}