package com.gespyme.domain.facade;

import com.gespyme.commons.model.employee.EmployeeModelApi;

public interface EmployeeFacade {
    EmployeeModelApi getEmployeeById(String employeeId);
}
