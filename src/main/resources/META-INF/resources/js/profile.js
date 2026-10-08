const userId = Number(document.body.dataset.profilUserId);

async function loadUser() {
    console.log("ProfilUserId: " + document.body.dataset.profilUserId);
    console.log("ProfilUserId: " + userId);

    try {

        const response = await fetch(`/users/${userId}`);

        if (!response.ok) {
            document.getElementById("notFound").hidden = false;
            document.querySelector("main .card").hidden = true;
            document.getElementById("offersLoading").hidden = true;
            return;
        }

        const user = await response.json();

        renderUser(user);

        // Placeholder until offers exist.
        document.getElementById("offersLoading").hidden = true;
        document.getElementById("offersContainer").hidden = false;

    } catch (err) {

        console.error(err);

    }
}
const addFriendBtn = document.getElementById("addFriendBtn");

if (addFriendBtn) {
    addFriendBtn.addEventListener("click", async () => {
        const response = await fetch("/friendships", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ addresseeId: userId })
        });

        if (response.ok) {
            addFriendBtn.textContent = "Anfrage gesendet ✓";
            addFriendBtn.disabled = true;
        } else {
            alert("Anfrage nicht möglich (schon befreundet oder angefragt?)");
        }
    });
}

async function checkFriendship() {
    if (!addFriendBtn) return;
    const response = await fetch("/friendships");
    if (!response.ok) return;
    const friends = await response.json();
    if (friends.some(f => f.partnerId === userId)) {
        addFriendBtn.textContent = "Ihr seid befreundet ✓";
        addFriendBtn.disabled = true;
    }
}

function renderUser(user) {
    const fullName =
        [user.firstName, user.lastName].filter(Boolean).join(" ");

    document.getElementById("displayNameInfo").textContent = user.displayName ?? "—";
    document.getElementById("nameInfo").textContent = fullName || "—";
    document.getElementById("cityText").textContent = user.address?.city ?? "—";
    document.getElementById("plzText").textContent = user.address?.plz ?? "—";
    document.getElementById("streetText").textContent = user.address?.street ?? "Nicht sichtbar";
}

loadUser();
checkFriendship();