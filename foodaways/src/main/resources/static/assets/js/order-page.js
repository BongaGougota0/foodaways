let cart = [];

function addToCart(product) {
    const existingProduct = cart.find(item => item.id === product.id);
    if (existingProduct) {
        existingProduct.quantity += 1;
    } else {
        cart.push({
            ...product,
            quantity: 1
        });
    }

    localStorage.setItem('cart', JSON.stringify(cart));
    console.log(`Added ${product.name} to cart`);
    alert(`${product.name} added to cart`);
}

function loadCart() {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
        cart = JSON.parse(storedCart);
    }
}

function calculateTotalItemsInCart() {
    // Load the cart from localStorage
    const storedCart = localStorage.getItem('cart');
    let totalItems = 0;
    if (storedCart) {
        const cart = JSON.parse(storedCart);
        totalItems = cart.reduce((total, product) => total + product.quantity, 0);
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
