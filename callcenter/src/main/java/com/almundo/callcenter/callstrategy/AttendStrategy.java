package com.almundo.callcenter.callstrategy;

import com.almundo.callcenter.object.employee.Employee;

import java.util.Collection;
import java.util.Optional;

public interface AttendStrategy {

    Optional<Employee> findEmployee(Collection<Employee> employeeList);
}
