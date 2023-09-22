package com.apedano.waiter.service;

import com.apedano.restaurant.api.model.KitchenOrderDto;
import com.apedano.restaurant.api.model.NewOrder;
import com.apedano.restaurant.api.model.OrderDto;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import static com.apedano.restaurant.common.ResturantUtility.sleepRandomSeconds;


@ApplicationScoped
@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WaiterService {

    private static final int MIN_DURATION = 2;
    private static final int MAX_DURATION = 7;
    @Inject
    @Channel("new-order")
    Emitter<NewOrder> newOrderEmitter;

    @Inject
    @Channel("dish-served")
    Emitter<KitchenOrderDto> dishServedEmitter;

    @Inject
    @Channel("order-updates")
    Multi<OrderDto> orderUpdates;

    public void createOrder(NewOrder newOrder) {
        log.info("Creating order:{}", newOrder);
        newOrderEmitter.send(newOrder);
        log.info("New order message sent to new-order channel");
    }

    @Incoming("kitchen-incoming")
    public void serveDish(KitchenOrderDto kitchenOrderDto) {
        log.info("Serving: {}", kitchenOrderDto);
        sleepRandomSeconds(MIN_DURATION, MAX_DURATION);
        log.info("Served: {}", kitchenOrderDto);
        dishServedEmitter.send(kitchenOrderDto);
    }

    public Multi<OrderDto> getOrderUpdates() {
        return orderUpdates;
    }

    public String orderIdByTable(String tableNumber) {
        return null;
    }


}
