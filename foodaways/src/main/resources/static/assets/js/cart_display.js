document.addEventListener('DOMContentLoaded', function() {

    function updateCartDisplay() {
        const cartItems = document.querySelector('#cartItems');
        cartItems.innerHTML = '';

        // Get cart from localStorage
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        let total = 0;

        cart.forEach(product => {
            const itemElement = document.createElement('div');
            itemElement.className = 'cart-item';
            itemElement.innerHTML = `
                <img src="${product.productImagePath}" alt="${product.productName}" class="product-image">
                <div class="product-details">
                    <h3>${product.productName}</h3>
                    <p>R${product.productPrice}</p>
                </div>
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateQuantity(${product.productId}, -1)">-</button>
                    <span>${product.productCount}</span>
                    <button class="quantity-btn" onclick="updateQuantity(${product.productId}, 1)">+</button>
                </div>
            `;
            console.log(itemElement);
            cartItems.appendChild(itemElement);

            total += product.productPrice * product.productCount;
        });

        document.querySelector('#totalPrice').textContent = total.toFixed(2);
    }

    function updateQuantity(productId, change) {
        const cart = JSON.parse(localStorage.getItem('cart')) || [];
        const productIndex = cart.findIndex(p => p.productId === productId);

        if (productIndex !== -1) {
            cart[productIndex].productCount += change;

            if (cart[productIndex].productCount <= 0) {
                cart.splice(productIndex, 1);
            }

            localStorage.setItem('cart', JSON.stringify(cart));
            updateCartDisplay();
        }
    }

});