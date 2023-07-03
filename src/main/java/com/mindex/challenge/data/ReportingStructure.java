package com.mindex.challenge.data;

public class ReportingStructure {

    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public Employee getEmployee() { return this.employee; }

    public int getNumberOfReports() { return this.numberOfReports; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    public void setNumberOfReports(int numberOfReports) { this.numberOfReports = numberOfReports; }
}
