package com.almundo.callcenter.object.employee;

import com.almundo.callcenter.object.Call;
import com.almundo.callcenter.util.EmployeeState;
import com.almundo.callcenter.util.EmployeeType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Employee implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Employee.class);

    private EmployeeState state;

    private String id;

    private BlockingQueue<Call> incomingCalls;

    private BlockingQueue<Call> attendedCalls;

    public Employee() {

        this.id = UUID.randomUUID().toString();
        this.state = EmployeeState.AVAILABLE;
        this.state = EmployeeState.AVAILABLE;
        this.incomingCalls = new LinkedBlockingQueue<>();
        this.attendedCalls = new LinkedBlockingQueue<>();
    }

    public abstract EmployeeType getType();

    public synchronized EmployeeState getState() {
        return this.state;
    }

    private synchronized void updateState(EmployeeState employeeState) {
        logger.info(this + " updated the state to " + employeeState);
        this.state = employeeState;
    }

    public synchronized List<Call> getAttendedCalls() {
        return new ArrayList<>(attendedCalls);
    }

    /**
     * Queues a call to be attended by the employee
     *
     * @param call call to be attended
     */
    public synchronized void attend(Call call) {
        logger.info(this + " queues a call " + call);
        this.incomingCalls.add(call);
    }

    /**
     * This is the method that runs on the thread.
     * If the incoming calls queue is not empty, then it changes its state from AVAILABLE to BUSY, takes the call
     * and when it finishes it changes its state from BUSY back to AVAILABLE. This allows a Thread Pool to decide
     * to dispatch another call to another employee.
     */
    @Override
    public void run() {

        logger.info(this + " starts to work");
        while (true) {
            if (!this.incomingCalls.isEmpty()) {
                Call call = this.incomingCalls.poll();
                this.updateState(EmployeeState.BUSY);
                logger.info(this + " starts working on a call of " + call);
                try {
                    call.start();
                } catch (InterruptedException e) {
                    logger.error(this + " was interrupted and could not finish call of " + call);
                } finally {
                    this.updateState(EmployeeState.AVAILABLE);
                }
                this.attendedCalls.add(call);
                logger.info(this + " finishes a call of " + call);
            }
        }
    }

    @Override
    public String toString() {

        return "Employee [id=" + id + ", type=" + getType() + ", state=" + state + "]";
    }
}