function fetchStoreOrders(storeId) {
    const eventSource = new EventSource(`http://localhost:8080/store-manager/new-orders/${storeId}`);
    const ordersContainer = document.getElementById('orders-container');
    eventSource.onmessage = function(event) {
        const order = JSON.parse(event.data);
        const orderDiv = document.createElement('div');
        orderDiv.innerHTML = `
            <div class="column">
                <h3>Order Info</h3>
                <p>Order #: ${order.orderId}</p>
                <p>Status: ${order.orderStatus}</p>
            </div>
            <div class="middle">
                <h3>Order Details</h3>
                <p>Product: ${order.orderItems}</p>
                <p>Quantity: </p>
                <p>Price: </p>
            </div>
            <div class="column">
                <h3>Customer Info</h3>
                <p>Phone: </p>
                <p>Delivery: </p>
                <div>
                    <button id="incoming-order" class="">Accept Order</button>
                </div>
                <!-- Dynamically change the text of this button on click -->
                <div id="order-status" class="">
                    <button id="order-button" class="">Order Status</button>
                </div>
            </div>
        `;
        ordersContainer.appendChild(orderDiv);
    };
    // Handle any errors from the EventSource
    eventSource.onerror = function(error) {
        console.error('Error receiving order updates:', error);
        eventSource.close();
    };
}

