package com.gespyme.domain.facade;

import com.gespyme.commons.model.employee.EmployeeModelApi;
import java.util.List;

public interface EmployeeFacade {
  EmployeeModelApi getEmployeeById(String employeeId);
  List<EmployeeModelApi> getEmployees(String employeeId, String employeeLastName);
}
