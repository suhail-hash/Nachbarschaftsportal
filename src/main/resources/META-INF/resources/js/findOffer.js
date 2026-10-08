
let latitudePoint = 52.40743027842663;
let longitudePoint = 7.9850803742691205

let ready = false;


var map = L.map('map').setView([latitudePoint, longitudePoint], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

var pinIcon_red = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_red.png',
        iconSize:     [50, 50],
        iconAnchor:   [25, 50],
        popupAnchor:  [-3, -50]
    }
});

var pinIcon_blue = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_orange.png',
        iconSize:     [50, 50],
        iconAnchor:   [25, 50],
        popupAnchor:  [-3, -50]
    }
});



var addressPin = new pinIcon_blue();

var markers = L.layerGroup();
var circles = L.layerGroup();

document.getElementById("offerRadius").addEventListener("change", updateRange);
document.getElementById("refreshAddress").addEventListener("click", checkForCompleteAddress);
document.getElementById("searchButton").addEventListener("click", startSearch);

window.onload = checkForCompleteAddress;

function checkForCompleteAddress(){

    var city = document.getElementById("cityFind").value;
    var plz = document.getElementById("plzFind").value;
    var street = document.getElementById("streetFind").value;

    if(plz !== ""){
        getGeos(city,plz, street);
        ready = true;
    }
}

function updateRange(){


    circles.clearLayers();

    let radius = document.getElementById("offerRadius").value;

    let radiusField = document.getElementById("offerRadiusValue");

    radiusField.innerHTML = radius;

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

async function getGeos(city, plz, street){

    var streetString = street.replace(" ", "+");

    const urlAddress = "https://nominatim.openstreetmap.org/search?street=" + streetString + "&city=" + city + "&country=DE&postalcode=" + plz + "&format=json";

    try {
        const responseAddress = await fetch(urlAddress);
        const resultAddress = await responseAddress.json();
        console.log(resultAddress);

        markers.clearLayers();

        var markerAddress = L.marker([resultAddress[0].lat, resultAddress[0].lon], {icon: addressPin});
        markers.addLayer(markerAddress);

        markers.addTo(map);

        longitudePoint = resultAddress[0].lon;
        latitudePoint = resultAddress[0].lat;

        updateRange();

    }catch (error) {
        console.error(error.message);
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function radiusToZoom(radiusKm) {
    const maxZoom = 14;
    console.log(Math.floor(maxZoom - Math.log2(radiusKm)));
    return Math.floor(maxZoom - Math.log2(radiusKm));
}

function startSearch(){

    let radius = document.getElementById("offerRadius").value;
    let category = document.getElementById("category").value;
    let categorySubject = document.getElementById("categorySubject").value;

    if(ready){
        var myWindow = window.open("/listOffer?cat=" + category + "&catsub=" + categorySubject + "&lat=" + latitudePoint + "&lon=" + longitudePoint + "&rad=" + radius, "_self");
    }
    else{
       alert("Bitte Adresse suchen/bestätigen.")
    }

}
