function successNotification(message) {

    const notification = document.getElementById("notification");

    notification.textContent = message;
    notification.classList.remove("hidden");

    notification.classList.add("visible");
    setTimeout(() => {
        notification.classList.remove("visible");
        notification.classList.add("hidden");
    }, 2500);
}
