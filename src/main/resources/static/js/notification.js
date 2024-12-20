function getDocumentContainer() {
    return document.getElementById('notification-container')
}

function fileUploadNotification(message, type, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    notification.innerText = message;
    if (message === "File uploaded successfully") {
        notification.className = `notification ` + type + '-success';
    } else {
        notification.className = `notification ` + type + '-error';
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function seedingGradesNotification(message, type, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    notification.className = `notification ` + type;
    notification.innerText = message;
    if (message === "Seeding Done!") {
        notification.style.backgroundColor = 'limegreen'
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function studentNotification(message, type, duration = 10000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');


    if (message !== "OK") {
        notification.textContent = message;
        notification.className = 'notification ' + type + '-error'
    } else {
        notification.textContent = "Student updated";
        notification.className = 'notification ' + type
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function studentDeleteNotification(message, type, duration = 10000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');


    if (message !== "OK") {
        notification.textContent = message;
        notification.className = 'notification ' + type + '-error'
    } else {
        notification.textContent = "Student removed";
        notification.className = 'notification ' + type
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}


function studentUponInsertNotificationNotification(message, type, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    notification.className = 'notification ' + type
    if (message !== "OK") {
        notification.textContent = message;
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function studentAddedNotification(message, type, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message === "OK") {
        notification.className = 'notification ' + type
        notification.textContent = "New student added";
    } else {
        notification.className = 'notification ' + type + '-error'
        notification.textContent = message;
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function studentDuplicateNotification(message, type, duration = 5000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message !== "OK") {
        notification.className = 'notification ' + type + '-error';
        notification.textContent = message;
    } else {
        notification.className = 'notification ' + type;
        notification.textContent = "Student duplicate/s removed";
    }


    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function studentInsertErrorNotification(message, type, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message !== "OK") {
        notification.className = 'notification ' + type + '-error';
        notification.textContent = message;
    } else {
        notification.className = 'notification ' + type;
        notification.textContent = "Student updated";
    }

    notificationContainer.appendChild(notification);

    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}


function gradeNotification(message, duration = 5000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message === "OK") {
        notification.className = `notification grade-success`;
        notification.innerText = "Grade added";
    } else {
        notification.className = `notification grade-error`;
        notification.innerText = "Error adding grade: " + message;
    }

    notificationContainer.appendChild(notification);
    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
    refreshPage(duration)
}

function feedbackNotification(message, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message === "OK") {
        notification.className = `notification feedback-consumed`;
        notification.innerText = "Feedback submitted";
    } else {
        notification.className = `notification grade-error`;
        notification.innerText = "Error adding grade: " + message;
    }

    notificationContainer.appendChild(notification);
    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function seedingProgressNotification(message, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    notification.className = `notification feedback-consumed`;
    notification.innerText = message;

    notificationContainer.appendChild(notification);
    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

//TODO:
function alertNotification(message, duration = 15000) {
    const notificationContainer = getDocumentContainer()
    const notification = document.createElement('div');

    if (message === "OK") {
        notification.className = `notification feedback-consumed`;
        notification.innerText = "Grade added";
    } else {
        notification.className = `notification grade-error`;
        notification.innerText = "Error adding grade: " + message;
    }

    notificationContainer.appendChild(notification);
    setTimeout(() => notification.classList.add('visible'), 100);

    setTimeout(() => {
        notification.classList.remove('visible');
        setTimeout(() => notification.remove(), 1000);
    }, duration);
}

function refreshPage(duration) {
    setTimeout(() => location.reload(), duration + 1000)
}