// const myId = Number(document.body.dataset.userId);
const partnerId = Number(document.body.dataset.partnerId);         // von Qute injiziert
let offerId = new URLSearchParams(location.search).get('offerId'); // optional, vom Offer-Button

async function loadPartnerName() {
    const res = await fetch('/users/' + partnerId);
    if (!res.ok) return;
    const user = await res.json();

    const fullName = [user.firstName, user.lastName].filter(Boolean).join(' ');
    const name = fullName ? `${fullName} (${user.displayName})` : user.displayName;

    const link = document.getElementById('partnerName');
    link.textContent = name;
    link.href = '/profile/' + partnerId;
}

async function loadMessages() {
    const res = await fetch('/messages/chat/' + partnerId);
    if (!res.ok) return;
    const messages = await res.json();

    const box = document.getElementById('messages');
    box.innerHTML = '';
    for (const m of messages) {
        const div = document.createElement('div');
        div.className = 'msg ' + (m.senderId === partnerId ? 'theirs' : 'mine');
        div.textContent = m.text;


        if (m.offerId) {
            const link = document.createElement('small');
            link.innerHTML = `<a href="/showOffer/${m.offerId}" class="offer-link">Anzeige öffnen ↗</a>`;
            div.appendChild(link);
        }

        const time = document.createElement('small');
        time.textContent = new Date(m.sentAt).toLocaleString('de-DE', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
        div.appendChild(time);

        box.appendChild(div);
    }
}

async function sendMessage() {
    const input = document.getElementById('textInput');
    if (!input.value.trim()) return;

    const res = await fetch('/messages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            recipientId: partnerId,
            text: input.value,
            offerId: offerId ? Number(offerId) : null
        })
    });

    if (res.ok) {
        input.value = '';
        offerId = null;
        await loadMessages();
    } else {
        alert(await res.text());
    }
}

document.getElementById('sendForm')
    .addEventListener('submit', (event) => {
        event.preventDefault();
        sendMessage();
    });

loadPartnerName();
loadMessages();
setInterval(loadMessages, 3000);        // Polling, bewusste Entscheidung statt WebSockets