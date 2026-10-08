// home.js

var map = L.map('map').setView([52.273768553807436, 8.047917873309096], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);


var pinIcon_orange = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_orange.png',
        iconSize: [50, 50],
        iconAnchor: [25, 50],
        popupAnchor: [-3, -50]
    }
});


var addressPin = new pinIcon_orange();

var markers = L.layerGroup();
var circles = L.layerGroup();


document.getElementById("searchButton").addEventListener("click", searchPublicOffers);

function searchPublicOffers() {

    let plz = document.getElementById("searchPLZ").value;
    if (plz != "") {
        getGeos(plz, 2);
    }
}

async function getGeos(plz, radius) {


    const urlAddress = "https://nominatim.openstreetmap.org/search?&country=DE&postalcode=" + plz + "&format=json";

    try {
        const responseAddress = await fetch(urlAddress);
        const resultAddress = await responseAddress.json();
        console.log(resultAddress);


        longitudePoint = resultAddress[0].lon;
        latitudePoint = resultAddress[0].lat;

        addToMap(latitudePoint, longitudePoint, radius);

        searchOffers(latitudePoint, longitudePoint, radius, false);

    } catch (error) {
        console.error(error.message);
    }
}


async function getGeosForUser(radius) {

    let street = document.getElementById("street").innerHTML;
    let plz = document.getElementById("plz").innerHTML;
    let city = document.getElementById("city").innerHTML;

    var streetString = street.replace(" ", "+");

    const urlAddress = "https://nominatim.openstreetmap.org/search?street=" + streetString + "&city=" + city + "&country=DE&postalcode=" + plz + "&format=json";


    try {
        const responseAddress = await fetch(urlAddress);
        const resultAddress = await responseAddress.json();
        console.log(resultAddress);


        longitudePoint = resultAddress[0].lon;
        latitudePoint = resultAddress[0].lat;

        addToMap(latitudePoint, longitudePoint, radius);

        searchOffers(latitudePoint, longitudePoint, radius, true);

    } catch (error) {
        console.error(error.message);
    }
}


function addToMap(latitudePoint, longitudePoint, radius){

    markers.clearLayers();
    circles.clearLayers();

    var markerAddress = L.marker([latitudePoint, longitudePoint], {icon: addressPin});
    markers.addLayer(markerAddress);

    markers.addTo(map);


    var circle = L.circle([latitudePoint, longitudePoint], {
        color: 'orange',
        fillColor: '#f97316',
        fillOpacity: 0.25,
        radius: radius * 1000
    });

    circles.addLayer(circle);

    map.setView([latitudePoint, longitudePoint], radiusToZoom(radius));

    console.log("Radius:" + radius);

    circles.addTo(map);
}

function radiusToZoom(radiusKm) {
    const maxZoom = 14;
    console.log(Math.floor(maxZoom - Math.log2(radiusKm)));
    return Math.floor(maxZoom - Math.log2(radiusKm));
}

async function searchOffers(latitude, longitude, radius, login) {
    const data = {
        longitude: longitude,
        latitude: latitude,
        category: null,
        categorySubject: null,
        radius: radius
    };

    const response = await fetch("offers/search", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        console.error("Fehler beim Laden");
        return;
    }

    const offers = await response.json();

    console.log(offers);
    if(login){
        renderOffersUser(offers)
    }
    else{
        renderOffers(offers)
    }
}

function renderOffers(offers) {

    const offerContainer = document.getElementById("offerList");
    offerContainer.innerHTML = "";

    offers.forEach(offer => {

        const categoryLabel = getCategory(offer.category)

        const card = document.createElement("div");
        card.className = "card mb-4 shadow-sm";

        card.innerHTML = `
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <div class="mb-2">
                            <span class="badge text-bg-primary">${categoryLabel}</span>
                            <span class="badge text-bg-light"></span>
                        </div>
                        <h5 class="card-title">${offer.title}</h5>

                        <p class="card-text text-truncate">
                            ${offer.description}
                        </p>
                        <p class="card-text fw-bold fs-5">
                            ${new Intl.NumberFormat("de-DE", {
                                style: "currency",
                                currency: "EUR"
                            }).format(offer.price)}
                            <span class="fw-light">
                                ${offer.addition ?? ""}
                            </span>
                        </p>

                        <a href="/showOffer/${offer.id}" class="btn btn-outline-primary btn-sm">Anzeige öffnen</a>
                    </div>
                    <div class="col-6 d-flex justify-content-end">
                        <img src="/img/offer/${offer.imageName}" alt="Anzeigen Bild" class="offer-image-details rounded" style="max-height: 180px">
                    </div>   
                </div>
            </div>
        `;

        offerContainer.appendChild(card);
    });

    if(offers.length === 0){


        const cardEmpty = document.createElement("div");
        cardEmpty.className = "card mb-4 shadow-sm";

        cardEmpty.innerHTML = `
            <div class="mb-1 text-center">
                <div class="mb-3 offer-image card-img-top text-center btn-primary">
                    <img  src="/img/empty_icon.png" alt="Keine Anzeigen" class="offer-image">
                </div>
                <p class="card-text mb-3 text-body-secondary fw-bold">
                    Keine Anzeigen in deiner Nähe. </p>
            </div>
        `;

        offerContainer.appendChild(cardEmpty);
    }
}

function renderOffersUser(offers) {

    const offerContainer = document.getElementById("offerList");
    offerContainer.innerHTML = "";

    offers.forEach(offer => {

        const categoryLabel = getCategory(offer.category)

        const card = document.createElement("div");
        card.className = "card mb-4 shadow-sm";

        card.innerHTML = `
            <div class="card-body">
                <div class="row">
                    <div class="col-6">
                        <div class="mb-2">
                            <span class="badge text-bg-primary">${categoryLabel}</span>
                            <span class="badge text-bg-light"></span>
                        </div>
                        <h5 class="card-title">${offer.title}</h5>

                        <p class="card-text text-truncate">
                            ${offer.description}
                        </p>
                        <p class="card-text fw-bold fs-5">
                            ${new Intl.NumberFormat("de-DE", {
            style: "currency",
            currency: "EUR"
        }).format(offer.price)}
                            <span class="fw-light">
                                ${offer.addition ?? ""}
                            </span>
                        </p>
                          
                                <div class="mt-3">
                                    <a href="/showOffer/${offer.id}" class="btn btn-primary btn-sm">Anzeige öffnen</a>
                                    <a href="/profile/${offer.ownerId}" class="btn btn-outline-primary btn-sm">Profil anzeigen</a>
                                </div>
                    </div>
                    <div class="col-6 d-flex justify-content-end">
                        <img src="/img/offer/${offer.imageName}" alt="Anzeigen Bild" class="offer-image-details rounded" style="max-height: 180px">
                    </div>   
                </div>
            </div>
        `;

        offerContainer.appendChild(card);
    });

    if(offers.length === 0){


        const cardEmpty = document.createElement("div");
        cardEmpty.className = "card mb-4 shadow-sm";

        cardEmpty.innerHTML = `
            <div class="mb-1 text-center">
                <div class="mb-3 offer-image card-img-top text-center btn-primary">
                    <img  src="/img/empty_icon.png" alt="Keine Anzeigen" class="offer-image">
                </div>
                <p class="card-text mb-3 text-body-secondary fw-bold">
                    Keine Anzeigen in deiner Nähe. </p>
            </div>
        `;

        offerContainer.appendChild(cardEmpty);
    }
}

function getCategory(rawCategory) {

    switch (rawCategory) {
        case "FOR_SALE":
            return "Zu verkaufen"
        case "FOR_RENT":
            return "Zu verleihen"
        case "WANTED":
            return "Gesucht"
    }

}

//clean the path to avoid modal on reloading
//https://www.geeksforgeeks.org/bootstrap/bootstrap-5-modal-getorcreateinstance-method/
if (new URLSearchParams(location.search).get("welcome")) {
    bootstrap.Modal.getOrCreateInstance(
        document.getElementById("welcomeModal")
    ).show();
    history.replaceState(null, "", "/home");
}