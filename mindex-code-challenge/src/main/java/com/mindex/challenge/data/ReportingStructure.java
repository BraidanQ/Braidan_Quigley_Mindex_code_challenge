package com.mindex.challenge.data;

public class ReportingStructure{
    private int numberOfReports;//number of reports is an int becasue a the number of reporting people would be a number
    //employee is a employee type because it would make sense to want to see the full employee data while adressing the number or reports
    private Employee employee;

    public ReportingStructure() {
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}