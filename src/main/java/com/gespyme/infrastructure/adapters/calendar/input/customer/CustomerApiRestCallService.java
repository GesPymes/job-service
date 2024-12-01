package com.gespyme.infrastructure.adapters.calendar.input.customer;

import com.gespyme.application.appointment.port.output.facade.CustomerFacade;
import com.gespyme.commons.model.customer.CustomerModelApi;
import com.gespyme.rest.RestCallService;
import com.gespyme.rest.RestRequest;
import java.util.List;
import java.util.Map;
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

  public List<CustomerModelApi> getCustomers(String customerName) {
    return restService.performGetCallForList(
        RestRequest.builder()
            .server(customerEndpoint)
            .path("/customer")
            .headers(Map.of("Content-Type", "application/json"))
            .queryParams(Map.of("name", customerName))
            .build(),
        typeReference);
  }
}
