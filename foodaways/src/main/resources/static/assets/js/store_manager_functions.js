    // Open the modal
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