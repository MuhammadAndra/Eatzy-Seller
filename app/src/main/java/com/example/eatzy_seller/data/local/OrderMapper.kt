package com.example.eatzy_seller.data.local

import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderState

//API -> Room Entity (Model -> Entity)
fun OrderState.toEntity(): OrderStateEntity {
    return OrderStateEntity(
        order_id = order_id,
        order_status = order_status,
        order_time = order_time,
        estimation_time = estimation_time,
        total_price = total_price,
        items = items
    )
}

fun OrderItem.toEntity(orderId: Int): OrderItemEntity {
    return OrderItemEntity(
        order_id = orderId,
        menu_name = menu_name,
        item_details = item_details,
        menu_image = menu_image,
        menu_price = menu_price,
        quantity = quantity,
        add_on = add_on
    )
}

//Room Entity -> API Model / UI Model
fun OrderStateEntity.toModel(): OrderState {
    val groupItems = OrderItem.groupWithQuantity(items)
    return OrderState(
        order_id = order_id,
        order_status = order_status,
        order_time = order_time,
        estimation_time = estimation_time,
        total_price = total_price,
        items = groupItems
    )
}

fun OrderStateEntity.toOrderList(): OrderList {
    val groupItems = OrderItem.groupWithQuantity(this.items)
    return OrderList(
        order_id = order_id,
        order_time = order_time,
        total_price = total_price,
        items = groupItems
    )
}

fun OrderItemEntity.toModel(): OrderItem {
    return OrderItem(
        order_id = order_id,
        menu_name = menu_name,
        item_details = item_details,
        menu_image = menu_image,
        menu_price = menu_price,
        quantity = quantity,
        add_on = add_on
    )
}

