package com.gespyme.infrastructure.facade.employee;

import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.employee.EmployeeModelApi;
import com.gespyme.domain.facade.EmployeeFacade;
import com.gespyme.rest.RestCallService;
import com.gespyme.rest.RestRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeFacadeImpl implements EmployeeFacade {

  @Value("${employee.endpoint}")
  private String employeeEndpoint;

  private final RestCallService<EmployeeModelApi, EmployeeModelApi> restCallService;

  ParameterizedTypeReference<List<EmployeeModelApi>> typeReference =
          new ParameterizedTypeReference<List<EmployeeModelApi>>() {};

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

  @Override
  public List<EmployeeModelApi> getEmployees(String employeeName, String employeeLastName) {
    return restCallService.performGetCallForList(
            RestRequest.builder()
                    .server(employeeEndpoint)
                    .path("/employee")
                    .headers(Map.of("Content-Type", "application/json"))
                    .queryParams(getMap(employeeName, employeeLastName))
                    .build(),
            typeReference);
  }

  private Map<String,String> getMap(String employeeName, String employeeLastName) {
    Map<String,String> params = new HashMap<>();
    if(Objects.nonNull(employeeName)) {
      params.put("name", employeeName);
    }
    if(Objects.nonNull(employeeLastName)) {
      params.put("lastName", employeeLastName);
    }
    return params;
  }

}
