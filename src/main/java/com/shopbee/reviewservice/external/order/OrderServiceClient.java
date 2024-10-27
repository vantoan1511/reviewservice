package com.shopbee.reviewservice.external.order;

import com.shopbee.reviewservice.external.order.dto.Order;
import com.shopbee.reviewservice.external.order.filter.FilterCriteria;
import com.shopbee.reviewservice.shared.exception.mapper.ExternalServiceExceptionMapper;
import com.shopbee.reviewservice.shared.page.PageRequest;
import com.shopbee.reviewservice.shared.page.PagedResponse;
import com.shopbee.reviewservice.shared.sort.SortCriteria;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("orders")
@RegisterRestClient(configKey = "orderservice")
@RegisterClientHeaders
@RegisterProvider(ExternalServiceExceptionMapper.class)
public interface OrderServiceClient {

    @GET
    PagedResponse<Order> getOrders(@BeanParam @Valid FilterCriteria filterCriteria,
                                   @BeanParam @Valid PageRequest pageRequest,
                                   @BeanParam @Valid SortCriteria sortCriteria);
}
