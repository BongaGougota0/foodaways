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
    
    // Update cart UI (implement based on your UI needs)
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

// Utility function to show notifications
function showNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    Object.assign(notification.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        background: '#f3af24',
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