package com.mindex.challenge.dto;

/* I put this in the dto package because it will not be persisted.
*  I also made it immutable because it logically didn't make sense to ever update it. */
public class ReportingStructure {

    private final String employee;
    private final Integer numberOfReports;

    public ReportingStructure() {
        this.employee = null;
        this.numberOfReports = null;
    };

    public ReportingStructure(final String employee, final Integer numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    public String getEmployee() {
        return employee;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }
}
