package net.mymenu.service;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import net.mymenu.models.Order;
import net.mymenu.models.order.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService {

    public Preference createPreference(Order order) {
        try {
            List<OrderItem> orderItems = Optional.ofNullable(order.getOrderItems()).orElse(List.of());
            List<PreferenceItemRequest> items = createItemsWithNested(orderItems);
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .build();

            PreferenceClient preferenceClient = new PreferenceClient();
            return preferenceClient.create(preferenceRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error on creating preference", e);
        }
    }

    private List<PreferenceItemRequest> createItemsWithNested(List<OrderItem> orderItems) {
        List<PreferenceItemRequest> preferenceItems = new ArrayList<>();

        for (OrderItem item : orderItems) {
            String url = item.getImage() != null
                    ? item.getImage().getUrl()
                    : null;

            preferenceItems.add(PreferenceItemRequest.builder()
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .quantity(item.getQuantity())
                    .unitPrice(BigDecimal.valueOf(item.getUnitPrice()))
                    .categoryId(item.getCategory())
                    .currencyId("BRL")
                    .pictureUrl(url)
                    .build());

            if (item.getOrderItems() != null && !item.getOrderItems().isEmpty()) {
                preferenceItems.addAll(createItemsWithNested(item.getOrderItems()));
            }
        }

        return preferenceItems;
    }
}
