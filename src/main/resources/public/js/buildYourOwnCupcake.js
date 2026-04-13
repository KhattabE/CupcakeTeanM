const toppingInputs = document.querySelectorAll('input[name="topping"]');
const bottomInputs = document.querySelectorAll('input[name="bottom"]');
const quantityInput = document.getElementById('quantityInput');
const totalPrice = document.getElementById('totalPrice');
const minusButton = document.getElementById('minusButton');
const plusButton = document.getElementById('plusButton');

function updatePrice() {
    let toppingPrice = 0;
    let bottomPrice = 0;

    toppingInputs.forEach(input => {
        if (input.checked) {
            toppingPrice = Number(input.dataset.price);
        }
    });

    bottomInputs.forEach(input => {
        if (input.checked) {
            bottomPrice = Number(input.dataset.price);
        }
    });

    let quantity = Number(quantityInput.value);

    let total = (toppingPrice + bottomPrice) * quantity;
    totalPrice.textContent = "Price: " + total + " kr";
}

minusButton.addEventListener('click', function () {
    let current = Number(quantityInput.value);
    if (current > 1) {
        quantityInput.value = current - 1;
        updatePrice();
    }
});

plusButton.addEventListener('click', function () {
    let current = Number(quantityInput.value);
    quantityInput.value = current + 1;
    updatePrice();
});

toppingInputs.forEach(input => {
    input.addEventListener('change', updatePrice);
});

bottomInputs.forEach(input => {
    input.addEventListener('change', updatePrice);
});

quantityInput.addEventListener('input', updatePrice);

updatePrice();