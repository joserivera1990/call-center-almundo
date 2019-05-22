package com.almundo.callcenter.callstrategy;

import com.almundo.callcenter.object.employee.Employee;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<Employee> availableEmployees =
                employeeList.stream()
                        .filter(employee -> employee.getState() == AVAILABLE)
                        .collect(Collectors.toList());

        logger.info("Available employees: " + availableEmployees.size());

        Optional<Employee> employee =
                availableEmployees.stream()
                        .filter(e -> e.getType() == OPERATOR)
                        .findAny();

        if (!employee.isPresent()) {
            logger.info("No available operators found");

            employee = availableEmployees.stream()
                    .filter(e -> e.getType() == SUPERVISOR)
                    .findAny();

            if (!employee.isPresent()) {
                logger.info("No available supervisors found");

                employee = availableEmployees.stream()
                        .filter(e -> e.getType() == DIRECTOR)
                        .findAny();

                if (!employee.isPresent()) {
                    logger.info("No available directors found");
                    return employee;
                }
            }
        }
        logger.info("Employee of type " + employee.get() + " is available");
        return employee;
    }
}
