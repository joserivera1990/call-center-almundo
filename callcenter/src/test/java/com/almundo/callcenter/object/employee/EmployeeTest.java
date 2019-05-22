package com.almundo.callcenter.object.employee;

import com.almundo.callcenter.util.EmployeeState;
import com.almundo.callcenter.util.Util;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EmployeeTest {

    @Test
    public void testEmployeeAttendWhileAvailable() throws InterruptedException {
        Employee employee = new Operator();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(employee);
        employee.attend(Util.buildRandomCall(0, 1));

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(1, employee.getAttendedCalls().size());
    }

    @Test
    public void testEmployeeQueueTheCalls() throws InterruptedException {
        Employee employee = new Director();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(employee);
        assertEquals(EmployeeState.AVAILABLE, employee.getState());
        TimeUnit.SECONDS.sleep(1);
        employee.attend(Util.buildRandomCall(2, 3));
        employee.attend(Util.buildRandomCall(0, 1));
        TimeUnit.SECONDS.sleep(1);
        assertEquals(EmployeeState.BUSY, employee.getState());

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, employee.getAttendedCalls().size());
    }

}
