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
    console.log("subscribed to"+`storeIdVar`);
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);
  if(message.status === 'ONLINE'){
  console.log("We are now online.");
  }
}

function onError(error) {
    console.log("Could not connect to WebSocket!");
}


