let map = L.map('map').setView([52.40743027842663, 7.9850803742691205], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

let pinIcon_orange = L.Icon.extend({
    options: {
        iconUrl: '/img/pin_orange.png',
        iconSize:     [50, 50],
        iconAnchor:   [25, 50],
        popupAnchor:  [-3, -50]
    }
});

let addressPin = new pinIcon_orange();

let markers = L.layerGroup();

let markerAddress = L.marker([latitude, longitude], {icon: addressPin});
markers.addLayer(markerAddress);

markers.addTo(map);

map.setView([latitude, longitude], 13);