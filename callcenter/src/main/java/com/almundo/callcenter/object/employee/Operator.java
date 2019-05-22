package com.almundo.callcenter.object.employee;

import com.almundo.callcenter.util.EmployeeType;

public class Operator extends Employee {

    @Override
    public EmployeeType getType() {

        return EmployeeType.OPERATOR;

    }

}