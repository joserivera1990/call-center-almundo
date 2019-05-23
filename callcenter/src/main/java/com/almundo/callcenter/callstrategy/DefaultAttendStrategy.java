package com.almundo.callcenter.callstrategy;

import com.almundo.callcenter.object.employee.Employee;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.almundo.callcenter.util.EmployeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.almundo.callcenter.util.EmployeeState.AVAILABLE;
import static com.almundo.callcenter.util.EmployeeType.DIRECTOR;
import static com.almundo.callcenter.util.EmployeeType.OPERATOR;
import static com.almundo.callcenter.util.EmployeeType.SUPERVISOR;

public class DefaultAttendStrategy implements AttendStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAttendStrategy.class);

    /*
     * If an Operator is available must return it.
     * If no Operator is available then an available supervisor must return it.
     * If no supervisor is available then an available Director must attend it.
     * If no employee is available then must return to Option none.
     * @param employeeList employee List to choose the available depending of the logic
     */
    @Override
    public Optional<Employee> findEmployee(Collection<Employee> employeeList) {

        List<Employee> availableEmployees = findAvailableEmployees(employeeList);

        Optional<Employee> employee = getEmployeeAvailableFromType(availableEmployees, OPERATOR);

        if (!employee.isPresent()) {
            employee = getEmployeeAvailableFromType(availableEmployees, SUPERVISOR);

            if (!employee.isPresent()) {
                employee = getEmployeeAvailableFromType(availableEmployees, DIRECTOR);
                if (!employee.isPresent()) {
                    return employee;
                }
            }
        }

        logger.info("Employee of type " + employee.get() + " is available");
        return employee;
    }

    private Optional<Employee> getEmployeeAvailableFromType(List<Employee> availableEmployees, EmployeeType type) {
        Optional<Employee> employee =
                availableEmployees.stream()
                        .filter(e -> e.getType() == type)
                        .peek(available -> logger.info("Available " + type + " found"))
                        .findAny();

        if (!employee.isPresent()) {
                logger.info("No available " + type + " found");
        }
        return employee;
    }


    private List<Employee> findAvailableEmployees(Collection<Employee> employeeList) {

        List<Employee> availableEmployees =
                employeeList.stream()
                        .filter(employee -> employee.getState() == AVAILABLE)
                        .collect(Collectors.toList());

        logger.info("Available employees: " + availableEmployees.size());
        return Collections.unmodifiableList(availableEmployees);
    }
}
