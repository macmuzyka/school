function successNotification(message) {

    const notification = document.getElementById("notification");

    notification.textContent = message;
    if (message === "Error adding grade") {
        notification.style.backgroundColor = "#A62929FF"
    }

    notification.classList.remove("hidden");
    notification.classList.add("visible");
    setTimeout(() => {
        notification.classList.remove("visible");
        notification.classList.add("hidden");
    }, 2500);
}
