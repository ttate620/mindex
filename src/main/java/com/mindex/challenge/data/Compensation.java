package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Compensation {

    private Employee employee;

    private BigDecimal salary;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date effectiveDate;

    public Employee getEmployee() {
        return this.employee;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public Date getEffectiveDate() { return this.effectiveDate; }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


}
