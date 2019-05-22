package com.almundo.callcenter.callstrategy;


import com.almundo.callcenter.object.employee.Director;
import com.almundo.callcenter.object.employee.Employee;
import com.almundo.callcenter.object.employee.Operator;
import com.almundo.callcenter.object.employee.Supervisor;
import com.almundo.callcenter.util.EmployeeState;
import com.almundo.callcenter.util.EmployeeType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultAttendStrategyTest {

    private AttendStrategy attendStrategy;

    public DefaultAttendStrategyTest() {
        this.attendStrategy = new DefaultAttendStrategy();
    }

    @Test
    public void testAssignToOperator() {
        Employee operator = new Operator();
        Employee supervisor = new Supervisor();
        Employee director = new Director();
        List<Employee> employeeList = Arrays.asList(operator, supervisor, director);

        Optional<Employee> employee = this.attendStrategy.findEmployee(employeeList);

        assertEquals(EmployeeType.OPERATOR, employee.get().getType());
    }

    @Test
    public void testAssignToSupervisor() {
        Employee operator = mock(Employee.class);
        when(operator.getState()).thenReturn(EmployeeState.BUSY);
        Employee supervisor = new Supervisor();
        Employee director = new Director();
        List<Employee> employeeList = Arrays.asList(operator, supervisor, director);

        Optional<Employee> employee = this.attendStrategy.findEmployee(employeeList);

        assertEquals(EmployeeType.SUPERVISOR, employee.get().getType());
    }

    @Test
    public void testAssignToDirector() {
        Employee operator = mockBusyEmployee(EmployeeType.OPERATOR);
        Employee supervisor = mockBusyEmployee(EmployeeType.SUPERVISOR);
        Employee director = new Director();
        List<Employee> employeeList = Arrays.asList(operator, supervisor, director);

        Optional<Employee> employee = this.attendStrategy.findEmployee(employeeList);

        assertEquals(EmployeeType.DIRECTOR, employee.get().getType());
    }

    @Test
    public void testAssignToNone() {
        Employee operator = mockBusyEmployee(EmployeeType.OPERATOR);
        Employee supervisor = mockBusyEmployee(EmployeeType.SUPERVISOR);
        Employee director = mockBusyEmployee(EmployeeType.DIRECTOR);
        List<Employee> employeeList = Arrays.asList(operator, supervisor, director);

        Optional<Employee> employee = this.attendStrategy.findEmployee(employeeList);

        assertFalse(employee.isPresent());
    }

    private static Employee mockBusyEmployee(EmployeeType employeeType) {
        Employee employee = mock(Employee.class);
        when(employee.getType()).thenReturn(employeeType);
        when(employee.getState()).thenReturn(EmployeeState.BUSY);
        return employee;
    }

}
