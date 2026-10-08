async function loadChats() {
    const res = await fetch('/messages/chats');
    if (!res.ok) {
        const hint = document.getElementById('empty-hint');
        hint.textContent = 'Fehler beim Laden.';
        hint.hidden = false;
        return;
    }
    const partners = await res.json();

    if (partners.length === 0) {
        document.getElementById('empty-hint').hidden = false;
        return;
    }

    const list = document.getElementById('chat-list');
    for (const p of partners) {
        list.appendChild(chatItem(p));
    }
}

function chatItem(p) {
    const a = document.createElement('a');
    a.href = '/chat/' + p.userId;
    a.className = 'list-group-item list-group-item-action py-3 fw-semibold';

    const name = document.createElement('span');
    name.className = 'chat-arrow';
    name.textContent = p.labelName;          // Nutzerdaten -> textContent

    const arrow = document.createElement('span');
    arrow.className = 'text-body-secondary';
    arrow.setAttribute('aria-hidden', 'true');
    arrow.textContent = '›';

    a.append(name, arrow);
    return a;
}
loadChats();