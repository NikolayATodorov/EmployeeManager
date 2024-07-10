package com.nitdrv.employeemanager.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmployeeProject getEmployeeProjectSample1() {
        return new EmployeeProject().id(1L);
    }

    public static EmployeeProject getEmployeeProjectSample2() {
        return new EmployeeProject().id(2L);
    }

    public static EmployeeProject getEmployeeProjectRandomSampleGenerator() {
        return new EmployeeProject().id(longCount.incrementAndGet());
    }
}
