const styleCarousel = () => {
    let isFirst = true;
    const container = document.querySelector('.carousel-inner');
    container.innerHTML = '';
    axios.get('http://localhost:8080/carousel-items')
        .then((res) => {
            console.log(res.data.carousel_items);
            for (let item of res.data.carousel_items) {
                const itemElement = document.createElement('div');
                itemElement.className = 'carousel-item';
                if (isFirst) {
                    itemElement.classList.add('active'); // Mark the first item as active
                    isFirst = false;
                }
                itemElement.innerHTML = `
                    <div class="carousel-image-wrap">
                        <img src="assets/images/${item.imageOfProduct}" class="img-fluid carousel-image" alt="">
                    </div>
                    <div class="carousel-caption">
                        <div class="d-flex align-items-center">
                            <h4 class="hero-text">${item.productName}</h4>
                            <span class="price-tag ms-4"><small>R</small>${item.productPrice}</span>
                        </div>
                    </div>
                `;
                container.appendChild(itemElement);
            }
        })
        .catch((e) => {
            console.error(e);
        });
};
styleCarousel();
