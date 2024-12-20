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

document.addEventListener('DOMContentLoaded', function() {
   document.querySelectorAll('.reject').forEach((rejectBtn) =>{
        rejectBtn.addEventListener('click', () => {
        const orderId = parseInt(rejectBtn.getAttribute('data-order-id'), 10);
        const orderProcess = {orderId : orderId,
        orderStatus : 'ORDER_DECLINED'};
        fetch(`/store-manager/reject-order/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body : JSON.stringify(orderProcess)
        })
        .then((res) => {
            if(!res.ok){
                console.log('error occurred.');
                console.log(res);
            }
            return res.json();
        })
        .then((res) => {
//            this.data.remove();
            console.log(res);
        })
        .catch((error) => {
            console.log(error);
        });
        });
    })
});


document.addEventListener('DOMContentLoaded', function() {
   document.querySelectorAll('.accept').forEach((acceptBtn) =>{
        acceptBtn.addEventListener('click', () => {
        const orderId = parseInt(acceptBtn.getAttribute('data-order-id'), 10);
        const orderProcess = {orderId : orderId,
        orderStatus : 'ORDER_IN_PROGRESS'};
        fetch(`/store-manager/accept-order/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body : JSON.stringify(orderProcess)
        })
        .then((res) => {
            if(!res.ok){
                console.log('error occurred.');
                console.log(res);
            }
            return res.json();
        })
        .then((res) => {
//            this.data.remove();
            console.log(res);
        })
        .catch((error) => {
            console.log(error);
        });
        });
    })
});

document.addEventListener('DOMContentLoaded', function() {
   document.querySelectorAll('.complete').forEach((completeBtn) =>{
        completeBtn.addEventListener('click', () => {
        const orderId = parseInt(completeBtn.getAttribute('data-order-id'), 10);
        const orderProcess = {orderId : orderId,
        orderStatus : 'ORDER_COMPLETED'};
        fetch(`/store-manager/accept-order/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body : JSON.stringify(orderProcess)
        })
        .then((res) => {
            if(!res.ok){
                console.log('error occurred.');
                console.log(res);
            }
            return res.json();
        })
        .then((res) => {
//            this.data.remove();
            console.log(res);
        })
        .catch((error) => {
            console.log(error);
        });
        });
    })
});