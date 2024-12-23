"use strict";
var stompClient = null;
var storeIdVar = null;

function connect(storeId){
    storeIdVar = storeId
    var socket = new SockJS("/foodaways");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);

}

function onConnected() {
    stompClient.subscribe(`/store-manager/new-orders/${storeIdVar}`, onMessageReceived);
      stompClient.send(
        "/store-manager/activate-store",
        {},
        JSON.stringify({ store: storeIdVar, status: "ONLINE" })
      );
    console.log("subscribed to "+`${storeIdVar}`);
}

function onMessageReceived(payload) {
  var order = JSON.parse(payload.body);
  if(order.orderStatus === 'ORDER_PLACED'){
    addOrderRow(order);
  }
}

function onError(error) {
    console.log("Could not connect to WebSocket!");
}

function openModal() {
    document.getElementById('productModal').style.display = 'flex';
}
// Close the modal
function closeModal() {
    document.getElementById('productModal').style.display = 'none';
}
// Close modal when clicking outside of it
window.onclick = function (event) {
    if (event.target === document.getElementById('productModal')) {
        closeModal();
    }
}


function addOrderRow(order) {
    const container = document.querySelector("#orders-container");
    // Create order row div elemeent
    const orderRow = document.createElement("div");
    orderRow.classList.add("order-data");
    // Create column 1
    const firstColumn = document.createElement("div");
    firstColumn.classList.add("column");
    const orderId = document.createElement("h3");
    orderId.textContent = `Order ID: ${order.orderId}`;
    firstColumn.appendChild(orderId);
    // Create column 2
    const middleColumn = document.createElement("div");
    middleColumn.classList.add("column", "middle");
    const orderDetails = document.createElement("p");
    orderDetails.textContent = `Details: ${order.orderItems}`;
    middleColumn.appendChild(orderDetails);
    // Create column 3
    const lastColumn = document.createElement("div");
    lastColumn.classList.add("column");
    const orderStatus = document.createElement("p");
    orderStatus.textContent = `Status: ${order.orderStatus}`;
    lastColumn.appendChild(orderStatus);
    // Append columns to order row - one order
    orderRow.appendChild(firstColumn);
    orderRow.appendChild(middleColumn);
    orderRow.appendChild(lastColumn);
    // Append order row to container.
    container.prepend(orderRow);
}

const ORDER_ACTIONS = {
    reject: {
        selector: '.reject',
        endpoint: '/store-manager/reject-order/',
        status: 'ORDER_DECLINED'
    },
    accept: {
        selector: '.accept',
        endpoint: '/store-manager/accept-order/',
        status: 'ORDER_IN_PROGRESS'
    },
    complete: {
        selector: '.complete',
        endpoint: '/store-manager/accept-order/',
        status: 'ORDER_COMPLETED'
    }
};

async function processOrderStatus(orderId, endpoint, orderStatus) {
    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ orderId, orderStatus })
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log('Response:', data);
        return data;
    } catch (error) {
        console.error('Error processing order:', error);
        throw error;
    }
}

function setupOrderActionListeners() {
    Object.values(ORDER_ACTIONS).forEach(action => {
        document.querySelectorAll(action.selector).forEach(button => {
            button.addEventListener('click', async () => {
                try {
                    const orderId = parseInt(button.getAttribute('data-order-id'), 10);
                    if (isNaN(orderId)) {
                        throw new Error('Invalid order ID');
                    }
                    await processOrderStatus(orderId, action.endpoint, action.status);
                     button.closest('.order-item')?.remove();
                } catch (error) {
                    console.error('Failed to process order:', error);
                }
            });
        });
    });
}

document.addEventListener('DOMContentLoaded', setupOrderActionListeners);