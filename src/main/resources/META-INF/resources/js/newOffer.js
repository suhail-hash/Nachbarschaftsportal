

var map = L.map('map').setView([52.40743027842663, 7.9850803742691205], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

var pinIcon_orange = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_orange.png',
        iconSize:     [50, 50],
        iconAnchor:   [25, 50],
        popupAnchor:  [-3, -50]
    }
});

var pinIcon_green = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_orange_light.png',
        iconSize:     [50, 50],
        iconAnchor:   [25, 50],
        popupAnchor:  [-3, -50]
    }
});


var addressPin = new pinIcon_green();
var cityPin = new pinIcon_orange();

var markers = L.layerGroup();

document.getElementById("refreshAddress").addEventListener("click", checkForCompleteAddress);
document.getElementById("confirmDeleteBtn").addEventListener("click", confirmDelete);


document.onload = checkForCompleteAddress();

if(document.getElementById("offerId").value > 0){
    setMarkers(
        [
            document.getElementById("latitudeAddress").value,
            document.getElementById("longitudeAddress").value,
        ],
        [
            document.getElementById("latitudeCity").value,
            document.getElementById("longitudeCity").value,
        ]
    );
}

function checkForCompleteAddress(){

    var city = document.getElementById("city").value;
    var plz = document.getElementById("plz").value;
    var street = document.getElementById("street").value;

    if(city !== "" && plz !== "" && street !== ""){
        getGeos(city,plz, street);
    }
}


async function getGeos(city, plz, street){

    var streetString = street.replace(" ", "+");

    const urlAddress = "https://nominatim.openstreetmap.org/search?street=" + streetString + "&city=" + city + "&country=DE&postalcode=" + plz + "&format=json";
    const urlCity = "https://nominatim.openstreetmap.org/search?postalcode=" + plz + "&city=" + city + "&country=DE&featuretype=settlement&format=json";

    try {
        const responseAddress = await fetch(urlAddress);
        const resultAddress = await responseAddress.json();
        console.log(resultAddress);

        await sleep(200);

        const responseCity = await fetch(urlCity);
        const resultCity = await responseCity.json();
        console.log(resultCity);

        setMarkers([resultAddress[0].lat, resultAddress[0].lon], [resultCity[0].lat, resultCity[0].lon]);

     }catch (error) {
        console.error(error.message);
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function setMarkers(resultAddress, resultCity){

    markers.clearLayers();

    var markerAddress = L.marker([resultAddress[0], resultAddress[1]], {icon: addressPin});
    markers.addLayer(markerAddress);
    var markerCity = L.marker([resultCity[0], resultCity[1]], {icon: cityPin});
    markers.addLayer(markerCity);

    markers.addTo(map);

    map.setView([resultCity[0], resultCity[1]], 13);

    document.getElementById("longitudeCity").value = resultCity[1];
    document.getElementById("latitudeCity").value = resultCity[0];
    document.getElementById("longitudeAddress").value = resultAddress[1];
    document.getElementById("latitudeAddress").value = resultAddress[0];


}

async function confirmDelete() {

    const offerId = document.body.dataset.offerId;

    console.log("Id: " + offerId);
    const response = await fetch(`/offers/${offerId}`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"}
    });

    if (response.status === 204) {
        window.location.href = "/profile";
    } else {
        console.log("Löschen hat nicht funktioniert");
    }
}