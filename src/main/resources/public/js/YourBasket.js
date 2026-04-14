document.addEventListener("DOMContentLoaded", () => {
    const pickupSelect = document.getElementById("pickupDate");
    const confirmButton = document.getElementById("confirmButton");
    const basketItemCards = document.querySelectorAll(".basketItemsBox .basketItemCard");

    if (!pickupSelect) {
        return;
    }

    const basketHasItems =
        basketItemCards.length > 0 &&
        !Array.from(basketItemCards).some(card => {
            const heading = card.querySelector("h2");
            return heading && heading.textContent.trim() === "Your basket is empty";
        });

    pickupSelect.innerHTML = "";

    const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    function formatDisplayDate(date) {
        const day = date.getDate();
        const month = monthNames[date.getMonth()];
        return `${day} ${month}`;
    }

    function formatValueDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    for (let i = 0; i <= 3; i++) {
        const date = new Date(today);
        date.setDate(today.getDate() + i);

        const option = document.createElement("option");
        option.value = formatValueDate(date);

        if (i === 0) {
            option.textContent = `Today (${formatDisplayDate(date)})`;
            option.selected = true;
        } else {
            option.textContent = formatDisplayDate(date);
        }

        pickupSelect.appendChild(option);
    }

    if (!basketHasItems) {
        pickupSelect.disabled = true;

        if (confirmButton) {
            confirmButton.disabled = true;
        }
    }
});