package com.gespyme.infrastructure.adapters.job.output.facade;

import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.employee.EmployeeModelApi;
import com.gespyme.domain.facade.EmployeeFacade;
import com.gespyme.rest.RestCallService;
import com.gespyme.rest.RestRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeFacadeImpl implements EmployeeFacade {

  @Value("${employee.endpoint}")
  private String employeeEndpoint;

  private final RestCallService<EmployeeModelApi, EmployeeModelApi> restCallService;

  @Override
  public EmployeeModelApi getEmployeeById(String employeeId) {
    return restCallService
        .performGetCall(
            RestRequest.builder()
                .server(employeeEndpoint)
                .path("/employee/{employeeId}")
                .headers(Map.of("Content-Type", "application/json"))
                .pathParams(Map.of("employeeId", employeeId))
                .build(),
            EmployeeModelApi.class)
        .orElseThrow(() -> new NotFoundException("Employee not found"));
  }
}
