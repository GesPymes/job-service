package com.gespyme.infrastructure.facade.customer;

import com.gespyme.application.appointment.port.output.facade.CustomerFacade;
import com.gespyme.commons.model.customer.CustomerModelApi;
import com.gespyme.rest.RestCallService;
import com.gespyme.rest.RestRequest;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerApiRestCallService implements CustomerFacade {

  private final RestCallService<CustomerModelApi, CustomerModelApi> restService;

  @Value("${customer.endpoint}")
  private String customerEndpoint;

  ParameterizedTypeReference<List<CustomerModelApi>> typeReference =
      new ParameterizedTypeReference<List<CustomerModelApi>>() {};

  public List<CustomerModelApi> getCustomers(String customerName, String customerLastName) {
    return restService.performGetCallForList(
        RestRequest.builder()
            .server(customerEndpoint)
            .path("/customer")
            .headers(Map.of("Content-Type", "application/json"))
            .queryParams(getMap(customerName,customerLastName))
            .build(),
        typeReference);
  }

  private Map<String,String> getMap(String customerName, String customerLastName) {
    Map<String,String> params = new HashMap<>();
    if(Objects.nonNull(customerName)) {
      params.put("customerName", customerName);
    }
    if(Objects.nonNull(customerLastName)) {
      params.put("customerLastName", customerLastName);
    }
    return params;
  }
}
