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
    console.log("a new order is placed.");
    console.log("append order to order list.");
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

