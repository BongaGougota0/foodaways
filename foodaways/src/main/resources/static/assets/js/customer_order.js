// Product DTO class to match Java structure
class ProductDto {
    constructor(productId, menuItems, productName, productImagePath, imageOfProduct, 
                productCategory, productPrice, productAverageRating, productCount, storeId) {
        this.productId = productId;
        this.menuItems = menuItems;
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.imageOfProduct = imageOfProduct;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productAverageRating = productAverageRating;
        this.productCount = productCount;
        this.storeId = storeId;
    }
}

// Cart management functions
const CartManager = {
    cart: [],
    // Add product to cart
    addToCart: function(productData) {
        const existingProduct = this.cart.find(item => item.productId === productData.productId);
        if (existingProduct) {
            existingProduct.productCount += 1;
        } else {
            this.cart.push({...productData, productCount: 1});
        }
        
        this.updateCartUI();
        this.saveCartToLocalStorage();
    },
    
    // Save cart to localStorage
    saveCartToLocalStorage: function() {
        localStorage.setItem('cart', JSON.stringify(this.cart));
    },
    
    // Load cart from localStorage
    loadCartFromLocalStorage: function() {
        const savedCart = localStorage.getItem('cart');
        if (savedCart) {
            this.cart = JSON.parse(savedCart);
            this.updateCartUI();
        }
    },
    
    // Update cart UI
    updateCartUI: function() {
        // Update cart count in UI
        const cartCountElement = document.querySelector('.cart-count');
        if (cartCountElement) {
            const totalItems = this.cart.reduce((sum, item) => sum + item.productCount, 0);
            cartCountElement.textContent = totalItems;
        }
        // Dispatch custom event for cart update
        document.dispatchEvent(new CustomEvent('cartUpdated', { 
            detail: { cart: this.cart }
        }));
    }
};

document.addEventListener('DOMContentLoaded', function() {
    // Load cart from localStorage
    CartManager.loadCartFromLocalStorage();
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            // Get product details from the parent elements
            const menuThumb = this.closest('.menu-thumb');
            const productData = {
                productId: parseInt(menuThumb.querySelector('id').textContent),
                storeId: parseInt(menuThumb.querySelector('storeId').textContent),
                productName: menuThumb.querySelector('h4').textContent,
                productPrice: parseFloat(menuThumb.querySelector('.price-tag').textContent),
                productCategory: menuThumb.querySelector('.menu-tag').textContent,
                menuItems: menuThumb.querySelector('p').textContent,
                imageOfProduct: menuThumb.querySelector('img').getAttribute('src').split('/').pop(),
                productCount: 1
            };
            CartManager.addToCart(productData);
            showNotification('Product added to cart successfully!');
        });
    });
});

function updateCartDisplay() {
    const cartItems = document.querySelector('#cartItems');
    cartItems.innerHTML = '';

    // Get cart from localStorage
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    let total = 0;
    let totalItems = 0;

    cart.forEach(product => {
        const itemElement = document.createElement('div');
        itemElement.className = 'cart-item';
        itemElement.innerHTML = `
            <img src="/assets/images/${product.imageOfProduct}" alt="${product.productName}" class="product-image">
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
        CartManager.updateCartUI();
        cartItems.appendChild(itemElement);
        total += product.productPrice * product.productCount;
        totalItems += product.productCount;
    });
    document.querySelector('.cart-count').textContent = totalItems;
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

// Utility function to show notifications
function showNotification(message, bgColor = '#f3af24') {
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    Object.assign(notification.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        background: `${bgColor}`,
        color: 'black',
        padding: '15px',
        borderRadius: '4px',
        zIndex: '1000',
        animation: 'fadeIn 0.3s, fadeOut 0.3s 2.7s'
    });
    document.body.appendChild(notification);
    // Remove notification after 3 seconds
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

const style = document.createElement('style');
style.textContent = `
    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-20px); }
        to { opacity: 1; transform: translateY(0); }
    }
    @keyframes fadeOut {
        from { opacity: 1; transform: translateY(0); }
        to { opacity: 0; transform: translateY(-20px); }
    }
`;
document.head.appendChild(style);

const placeOrder = document.querySelector('#placeOrderButton');
placeOrder.addEventListener("click", function(e){
    e.preventDefault();
    placeOrderFromCart();
})

function placeOrderFromCart() {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    if (cart.length === 0) {
        alert('Cart is empty');
        return;
    }
    const firstStoreId = cart[0].storeId;
    const sameStore = cart.every(product => product.storeId === firstStoreId);

    if (!sameStore) {
        alert('Products must be from a single store. Please separate orders by store.');
        return;
    }
    const orderData = {
        orderItems: cart.map(item => ({
            productId: item.productId,
            productName: item.productName,
            productPrice: item.productPrice,
            productCount: item.productCount,
            storeId: item.storeId
        })),
        storeId: firstStoreId
    };

    fetch('/in/place-order', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Order submission failed');
        }
        return response.json();
    })
    .then(result => {
        localStorage.removeItem('cart');
        const cartCountElement = document.querySelector('.cart-count');
        updateCartDisplay();
        showNotification('Order placed successfully!', '#00FF00');
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification('Failed to place order. Please try again.');
    });
}