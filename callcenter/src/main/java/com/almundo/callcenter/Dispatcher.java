package com.almundo.callcenter;

import com.almundo.callcenter.callstrategy.AttendStrategy;
import com.almundo.callcenter.callstrategy.DefaultAttendStrategy;
import com.almundo.callcenter.object.Call;
import com.almundo.callcenter.object.employee.Employee;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.almundo.callcenter.util.Constant.MAX_THREADS;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private Boolean active;

    private ExecutorService executorService;

    private BlockingQueue<Employee> employees;

    private BlockingQueue<Call> incomingCalls;

    private AttendStrategy attendStrategy;

    public Dispatcher(List<Employee> employees) {
        this(employees, new DefaultAttendStrategy());
    }

    public Dispatcher(List<Employee> employees, AttendStrategy callAttendStrategy) {
        this.employees = new LinkedBlockingQueue(employees);
        this.attendStrategy = callAttendStrategy;
        this.incomingCalls = new LinkedBlockingQueue<>();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public synchronized void dispatchCall(Call call) {
        logger.info("Dispatch new call of " + call);
        this.incomingCalls.add(call);
    }

    /**
     * It starts the employee threads and it allows the dispatcher run method to execute
     */
    public synchronized void start() {
        this.active = true;
        this.employees.forEach(employee -> this.executorService.execute(employee));
    }

    /**
     * Stops the employee threads and the dispatcher run method immediately
     */
    public synchronized void stop() {
        this.active = false;
        this.executorService.shutdown();
    }

    public synchronized Boolean isActive() {
        return active;
    }

    /**
     * This is the method that runs on the thread.
     * If the incoming calls queue is not empty, then it searches for and available employee to take the call.
     * Calls will queue up until some workers becomes available.
     */
    @Override
    public void run() {

        while (isActive()) {
            if (this.incomingCalls.isEmpty()) {
                continue;
            } else {
                Optional<Employee> employee = this.attendStrategy.findEmployee(this.employees);
                if (!employee.isPresent()) {
                    continue;
                }
                Call call = this.incomingCalls.poll();
                employee.get().attend(call);
            }
        }
    }

}
