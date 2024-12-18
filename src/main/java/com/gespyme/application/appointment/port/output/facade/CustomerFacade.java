package com.gespyme.application.appointment.port.output.facade;

import com.gespyme.commons.model.customer.CustomerModelApi;
import java.util.List;

public interface CustomerFacade {
  List<CustomerModelApi> getCustomers(String customerName, String customerLastName);
}
