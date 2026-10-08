const userId = Number(document.body.dataset.userId);
const myEmail = document.body.dataset.email;

async function loadUser() {
    try {
        const response = await fetch(`/users/${userId}`);
        if (!response.ok) {
            console.error("Failed to load user");
            return;
        }
        const user = await response.json();

        document.getElementById("displayNameInput").value = user.displayName ?? "";
        document.getElementById("cityInput").value = user.address?.city ?? "";
        document.getElementById("plzInput").value = user.address?.plz ?? "";
        document.getElementById("streetInput").value = user.address?.street ?? "";
    } catch (err) {
        console.error("Error loading user:", err);
    }
}

document.getElementById("displayNameForm")
    .addEventListener("submit", updateDisplayName);

document.getElementById("addressForm")
    .addEventListener("submit", updateAddress);

async function updateDisplayName(event) {
    event.preventDefault();

    const displayName = document.getElementById("displayNameInput").value.trim();

    const response = await fetch(`/users/${userId}/display-name`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({displayName: displayName})
    });

    if (response.ok) {
        alert("Anzeigename gespeichert");
    } else {
        alert("Speichern fehlgeschlagen: " + await response.text());
    }
}

async function updateAddress(event) {
    event.preventDefault();

    const street = document.getElementById("streetInput").value.trim();
    const address = {
        city: document.getElementById("cityInput").value.trim(),
        plz: document.getElementById("plzInput").value.trim(),
        street: street === "" ? null : street
    };

    const response = await fetch(`/users/${userId}/address`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(address)
    });

    if (response.ok) {
        alert("Adresse gespeichert");
    } else {
        alert("Speichern fehlgeschlagen: " + await response.text());
    }
}

document.getElementById("confirmDeleteBtn")
    .addEventListener("click", confirmDelete);

async function confirmDelete() {
    const typed = document.getElementById("confirmEmailInput").value.trim();
    const errorBox = document.getElementById("deleteError");

    if (typed.toLowerCase() !== myEmail.toLowerCase()) {
        errorBox.textContent = "E-Mail stimmt nicht überein.";
        errorBox.hidden = false;
        return;
    }
    errorBox.hidden = true;

    const response = await fetch(`/users/${userId}`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({confirmEmail: typed})
    });

    if (response.status === 204) {
        const modalEl = document.getElementById("deleteModal");
        bootstrap.Modal.getOrCreateInstance(modalEl).hide();
        window.location.href = "/logout";
    } else {
        errorBox.textContent = "Löschen fehlgeschlagen: " + await response.text();
        errorBox.hidden = false;
    }
}

loadUser();