let cart = [];

function addToCart(product) {
    console.log(product);
    var item = {
    productId : product.productId,
    productName : product.productName,
    imageOfProduct : product.imageOfProduct,
    productMenuItems : product.menuItems,
    productCount : product.productName += 1
    }

    console.log(item);
    if(cartItems == null || cartItems == Undefined){
        var x = JSON.parse(localStorage.getItem('productCart')) || [] ;
        x.push(item);
        localStorage.setItem('productCart', JSON.stringify(x));
    }

    const existingItem = cart.find(p => p.productId === item.productId);
    if (existingItem) {
        existingItem.productCount += 1;
    } else {
        cart.push(item);
    }

    localStorage.setItem('productCart', JSON.stringify(cart));
    console.log(`Added ${item.productName} to cart`);
    alert(`${item.productName} added to cart`);
}

function loadCart() {
    const storedCart = localStorage.getItem('productCart');
    if (storedCart) {
        cart = JSON.parse(storedCart);
    }
}

function calculateTotalItemsInCart() {
    // Load the cart from localStorage
    const storedCart = localStorage.getItem('productCart');
    let totalItems = 0;
    if (storedCart) {
        const cart = JSON.parse(storedCart);
        totalItems = cart.reduce((total, product) => total + product.productCount, 0);
    }
    return totalItems;
}


function displayCartCount() {
    const totalItems = calculateTotalItemsInCart();
    const cartCountElement = document.getElementById('cart-count');
    cartCountElement.innerText = totalItems;
}

window.onload = displayCartCount;
window.onload = loadCart;

