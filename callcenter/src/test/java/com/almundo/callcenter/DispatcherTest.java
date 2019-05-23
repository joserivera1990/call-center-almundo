package com.almundo.callcenter;

import com.almundo.callcenter.object.Call;
import com.almundo.callcenter.object.employee.Director;
import com.almundo.callcenter.object.employee.Employee;
import com.almundo.callcenter.object.employee.Operator;
import com.almundo.callcenter.object.employee.Supervisor;
import com.almundo.callcenter.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatcherTest {

    private static final int TEN_CALLS = 10;
    private static final int TWENTY_CALLS = 20;


    private static final int MIN_CALL_DURATION = 5;

    private static final int MAX_CALL_DURATION = 10;

    @Test
    public void testShouldGetTenCallsAndTenAttendedCalls() throws InterruptedException {
        List<Employee> employeeList = buildEmployeeList();
        Dispatcher dispatcher = new Dispatcher(employeeList);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        executeDispatchCall(dispatcher, TEN_CALLS);

        executorService.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);
        assertEquals(TEN_CALLS, employeeList.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
    }

    @Test
    public void testShouldGetTwentyCallsAndTenAttendedCalls() throws InterruptedException {
        List<Employee> employeeList = buildEmployeeList();
        Dispatcher dispatcher = new Dispatcher(employeeList);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        executeDispatchCall(dispatcher, TWENTY_CALLS);

        executorService.awaitTermination(MAX_CALL_DURATION * 3, TimeUnit.SECONDS);
        assertEquals(TWENTY_CALLS, employeeList.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
    }

    private void executeDispatchCall(Dispatcher dispatcher, int amountCalls) {
        buildCallList(amountCalls).stream().forEach(call -> {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });
    }

    private static List<Employee> buildEmployeeList() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Operator());
        employees.add(new Supervisor());
        employees.add(new Supervisor());
        employees.add(new Director());
        return employees;
    }

    private static List<Call> buildCallList(int amount) {

        List<Call> calls = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            calls.add(Util.buildRandomCall(MIN_CALL_DURATION, MAX_CALL_DURATION));
        }
        return calls;
    }

}
