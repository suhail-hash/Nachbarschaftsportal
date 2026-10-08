async function loadRequests() {
    const response = await fetch("/friendships/requests");
    if (!response.ok) return;
    const list = await response.json();

    const ul = document.getElementById("pendingList");
    ul.innerHTML = "";
    for (const f of list) {
        ul.appendChild(requestItem(f));
    }
    document.getElementById("noRequests").hidden = list.length > 0;
}

async function loadFriends() {
    const response = await fetch("/friendships");
    if (!response.ok) return;
    const list = await response.json();

    const ul = document.getElementById("friendsList");
    ul.innerHTML = "";
    for (const f of list) {
        ul.appendChild(friendItem(f));
    }
    document.getElementById("noFriends").hidden = list.length > 0;
}

// ---------- Aktionen ----------

async function sendRequest() {
    const addresseeId = Number(document.getElementById("addresseeInput").value);
    if (!addresseeId) {
        alert("Bitte eine User-ID eingeben");
        return;
    }

    const response = await fetch("/friendships", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ addresseeId: addresseeId })
    });

    if (response.ok) {
        document.getElementById("addresseeInput").value = "";
        alert("Anfrage gesendet");
    } else {
        alert("Anfrage nicht möglich (existiert schon / User nicht gefunden / selbst?)");
    }
}

async function respond(friendshipId, accept) {
    const action = accept ? "accept" : "decline";
    const response = await fetch(`/friendships/${friendshipId}/${action}`, {
        method: "PUT"
    });

    if (response.ok) {
        await loadRequests();
        await loadFriends();
    } else {
        alert("Aktion fehlgeschlagen");
    }
}

// ---------- Listeneinträge bauen ----------
function requestItem(f) {
    const li = document.createElement("li");
    li.className = "list-group-item d-flex justify-content-between align-items-center";

    const name = document.createElement("span");
    name.textContent = f.partnerLabel;

    const actions = document.createElement("div");
    actions.className = "d-flex gap-2";

    const accept = document.createElement("button");
    accept.className = "btn btn-primary btn-sm rounded-pill";
    accept.textContent = "Annehmen";
    accept.addEventListener("click", () => respond(f.friendshipId, true));

    const decline = document.createElement("button");
    decline.className = "btn btn-outline-secondary btn-sm rounded-pill";
    decline.textContent = "Ablehnen";
    decline.addEventListener("click", () => respond(f.friendshipId, false));

    actions.append(accept, decline);
    li.append(name, actions);
    return li;
}

function friendItem(f) {
    const li = document.createElement("li");
    li.className = "list-group-item d-flex justify-content-between align-items-center";

    const name = document.createElement("span");
    name.className = "fw-semibold";
    name.textContent = f.partnerLabel;

    const dd = document.createElement("div");
    dd.className = "dropdown";
    dd.innerHTML = `
        <button class="btn btn-light btn-sm rounded-pill" type="button"
                data-bs-toggle="dropdown" aria-expanded="false" aria-label="Aktionen">⋮</button>
        <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="#">Chat</a></li>
            <li><a class="dropdown-item" href="#">Profil ansehen</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><button class="dropdown-item text-danger" type="button">Entfernen</button></li>
        </ul>`;

    const [chatLink, profileLink] = dd.querySelectorAll("a.dropdown-item");
    chatLink.href = "/chat/" + f.partnerId;
    profileLink.href = "/profile/" + f.partnerId;

    dd.querySelector("button.text-danger")
        .addEventListener("click", () => openUnfriendModal(f));

    li.append(name, dd);
    return li;
}

let unfriendTarget = null;

function openUnfriendModal(f) {
    unfriendTarget = f;
    document.getElementById("unfriendName").textContent = f.partnerLabel;
    bootstrap.Modal.getOrCreateInstance(document.getElementById("unfriendModal")).show();
}

document.getElementById("confirmUnfriendBtn")
    .addEventListener("click", async () => {
        if (!unfriendTarget) return;

        const response = await fetch(`/friendships/${unfriendTarget.friendshipId}`, {
            method: "DELETE"
        });

        bootstrap.Modal.getOrCreateInstance(document.getElementById("unfriendModal")).hide();
        unfriendTarget = null;

        if (response.ok) {
            await loadFriends();
        } else {
            alert("Entfernen fehlgeschlagen");
        }
    });
// --- Init ---
loadRequests();
loadFriends();