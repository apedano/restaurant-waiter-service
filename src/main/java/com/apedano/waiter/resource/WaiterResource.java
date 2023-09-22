package com.apedano.waiter.resource;

import com.apedano.restaurant.api.OrderResourceApi;
import com.apedano.restaurant.api.model.NewOrder;
import com.apedano.restaurant.api.model.OrderDto;
import com.apedano.restaurant.api.model.OrderedDishes;
import com.apedano.waiter.service.WaiterService;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.net.URI;
import java.util.List;

@Path("/api/restaurant/waiters/")
@Slf4j
public class WaiterResource {

    @Inject
    WaiterService waiterService;

//    @RestClient
//    OrderResourceApi orderResourceApi;

//    private final OrderResourceApi orderResourceApi;
//
//    public WaiterResource() {
//        orderResourceApi = QuarkusRestClientBuilder.newBuilder()
//                .baseUri(URI.create("http://localhost:8080/api"))
//                .build(OrderResourceApi.class);
//    }

    @POST
    @Path("create-order")
    @Transactional
    public void create(NewOrder newOrder) {
        log.info("Create order called for table:{}", newOrder);
        waiterService.createOrder(newOrder);
    }

//    @POST
//    @Path("{orderId}/add-dishes")
//    public void addDishesToOrder(OrderedDishes orderedDishes) {
//        log.info("Invoked addDishesToOrder with: {}", orderedDishes);
//        orderResourceApi.addDishesToOrder(orderedDishes);
//    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Path("/order-updates")
    @RestStreamElementType("text/plain")
    public Multi<OrderDto> orderUpdates() {
        return waiterService.getOrderUpdates();
    }


}
