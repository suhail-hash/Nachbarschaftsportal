document
    .getElementById("setupForm")
    .addEventListener("submit", createProfile);

async function createProfile(event) {
    event.preventDefault();

    const street = document.getElementById("street").value.trim();

    const dto = {
        displayName: document.getElementById("displayName").value.trim(),
        address: {
            city: document.getElementById("city").value.trim(),
            plz: document.getElementById("plz").value.trim(),
            street: street === "" ? null : street
        }
    };

    try {
        const response = await fetch("/users", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            window.location.href = "/home?welcome=1";
        } else if (response.status === 409) {
            // Profil existiert schon -> einfach weiter
            window.location.href = "/home";
        } else {
            const msg = await response.text();
            alert("Profil konnte nicht erstellt werden: " + msg);
        }
    } catch (error) {
        console.error(error);
        alert("Server konnte nicht erreicht werden.");
    }
}