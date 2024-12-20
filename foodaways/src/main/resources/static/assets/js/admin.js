const btnAddStore = document.querySelector('#addStore');
btnAddStore.addEventListener('click', (e) => {
    e.preventDefault();
    const formDisplay = document.createElement('div');
    formDisplay.classList.add('modal');
    formDisplay.innerHTML =
     `
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h2>Add New Store</h2>
                    <form action="@{/store-manager/add-new-product}" method="POST"
                          enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="productName">Product Name</label>
                            <input type="text" id="productName" name="productName" required>
                        </div>
                        <div class="form-group">
                            <label for="menuItems">Menu Items</label>
                            <input type="text" id="menuItems" name="menuItems" rows="6" required>
                        </div>
                        <button type="submit" class="btn">Add Product</button>
                    </form>
                </div>
    `;
    formDisplay.style.display = `block`;
    const viewContainer = document.querySelector('#container');
    viewContainer.appendChild(formDisplay);
});