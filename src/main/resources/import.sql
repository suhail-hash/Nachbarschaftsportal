-- Zwei Testnutzer (E-Mails muessen zu Keycloak-Accounts passen!)
-- Identitaet: email, firstName, lastName (Pflicht) | Profil: displayName, Adresse (city/plz Pflicht, street optional)
INSERT INTO users (id, email, displayName, firstName, lastName, city, plz, street)
VALUES (1332, 'max.muster@test.de', 'Nachbar 4711', 'Max', 'Muster', 'Osnabrück', '49076', 'Caprivistraße 30A');

INSERT INTO users (id, email, displayName, firstName, lastName, city, plz, street)
VALUES (1333, 'erika.beispiel@test.de', 'Gartenfreund', 'Erika', 'Beispiel', 'Osnabrück', '49076', NULL);

INSERT INTO users (id, email, displayName, firstName, lastName, city, plz, street)
VALUES (1234, 'lena.schneider@example.com', 'LenaS', 'Lena', 'Schneider', 'Bramsche', '49565',
        'Beispielweg 12');

INSERT INTO users (id, email, displayName, firstName, lastName, city, plz, street)
VALUES (2000, 'jonas.becker@example.com', 'JonasB', 'Jonas', 'Becker', 'Osnabrück', '49090',
        'Musterweg 7');
-- Freundschaft: ACCEPTED, damit fullView (realer Name, Strasse) demonstrierbar ist
INSERT INTO friendships (id, requesterId, addresseeId, status, createdAt, respondedAt)
VALUES (2001, 1333, 1332, 'ACCEPTED', '2026-07-06T09:15:00Z', '2026-07-06T10:00:00Z');
-- Chat zwischen den beiden
INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9001, 1333, 1332, 'Hallo Nachbar!', '2026-07-07 10:00:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9002, 1332, 1333, 'Hallo zurück!', '2026-07-07 10:05:00+00');

INSERT INTO users (id, email, displayName, firstName, lastName, city, plz, street)
VALUES (1001, 'max.mueller@test.de', 'TechMax', 'Max', 'Müller', 'Osnabrück', '49074', 'Hasestraße 12'),
       (1002, 'anna.schmidt@test.de', 'AnnaS', 'Anna', 'Schmidt', 'Osnabrück', '49080', 'Lotter Straße 45'),
       (1003, 'lukas.weber@test.de', 'LukiW', 'Lukas', 'Weber', 'Bramsche', '49565', 'Malgartener Straße 8'),
       (1004, 'sarah.klein@test.de', 'SarahK', 'Sarah', 'Klein', 'Bramsche', '49565', 'Osnabrücker Straße 22'),
       (1005, 'tobias.fischer@test.de', 'TobiF', 'Tobias', 'Fischer', 'Osnabrück', '49082', 'Iburger Straße 67'),
       (1006, 'test1@test.de', 'TomT', 'Tom', 'Taber', 'Osnabrück', '49076', 'Barbarastraße 22'),
       (1007, 'test2@test.de', 'LisaT', 'Lisa', 'Thomas', 'Osnabrück', '49076', 'Sedanstraße 332'),
       (1008, 'test3@test.de', 'FraukeT', 'Frauke', 'Tallen', 'Osnabrück', '49076', 'Am Natruper Steinbruch 5'),
       (1009, 'test4@test.de', 'TimT', 'Tim', 'Tester', 'Osnabrück', '49076', 'Sedanstraße 330');


INSERT INTO offers (id, title, status, description, category, categorySubject, ownerId, privacy, longitudeAddress,
                    latitudeAddress, latitudeCity, longitudeCity, price, createdat, imageName, addition)
VALUES (100, 'Renault Twingo', 'AVAILABLE',
        'Gepflegter Kleinwagen aus zweiter Hand. Ideal für Stadtfahrten und kurze Strecken. TÜV noch gültig, sparsam im Verbrauch und zuverlässig im Alltag. Kleine Gebrauchsspuren vorhanden, technisch einwandfrei.',
        'FOR_SALE', 'VEHICLES', 1234, 'PUBLIC', 7.986, 52.405, 52.405, 7.986, 1500.00, '2026-07-10 12:00:00+00',
        '2b0ff0bd-4b85-40b4-90e1-d7fe5c7d4bb0.jpg', 'VB'),
       (101, 'Mountainbike', 'NOT_AVAILABLE',
        'Stabiles Mountainbike mit Aluminiumrahmen und guter Federung. Perfekt für Gelände und Waldwege geeignet. Bremsen und Schaltung funktionieren einwandfrei, sofort einsatzbereit.',
        'FOR_SALE', 'SPORTS_AND_LEISURE', 1234, 'PUBLIC', 7.987, 52.404, 52.405, 7.986, 350.00,
        '2026-07-11 10:00:00+00', '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (102, 'Wohnung 2 Zimmer', 'AVAILABLE',
        'Helle 2-Zimmer-Wohnung mit Balkon und moderner Einbauküche. Gute Anbindung an öffentliche Verkehrsmittel und Einkaufsmöglichkeiten in der Nähe. Ideal für Singles oder Paare.',
        'FOR_RENT', 'HOME_AND_GARDEN', 1234, 'PUBLIC', 7.985, 52.406, 52.405, 7.986, 650.00, '2026-07-09 09:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'mtl.'),
       (103, 'Suche Laptop', 'AVAILABLE',
        'Ich suche einen gebrauchten Laptop für Office und gelegentliches Streaming. Sollte zuverlässig laufen und nicht zu alt sein. Angebote mit kurzer Beschreibung bitte senden.',
        'WANTED', 'ELECTRONICS', 1005, 'PUBLIC', 7.984, 52.405, 52.405, 7.986, 400.00, '2026-07-08 14:20:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (104, 'E-Bike Verleih', 'AVAILABLE',
        'Modernes E-Bike zur Vermietung. Perfekt für Ausflüge in der Umgebung oder tägliche Fahrten. Akku hält lange und ist schnell geladen. Flexible Mietdauer möglich.',
        'FOR_RENT', 'SPORTS_AND_LEISURE', 1234, 'PUBLIC', 7.988, 52.405, 52.405, 7.986, 25.00, '2026-07-12 08:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'pro Tag'),
       (105, 'VW Golf', 'AVAILABLE',
        'Zuverlässiger VW Golf mit guter Ausstattung und gepflegtem Innenraum. Regelmäßig gewartet und technisch in gutem Zustand. Ideal als Alltagsfahrzeug oder für Pendler.',
        'FOR_SALE', 'VEHICLES', 1234, 'PUBLIC', 7.970, 52.280, 52.279, 7.960, 4200.00, '2026-07-07 11:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (106, 'WG Zimmer', 'AVAILABLE',
        'Gemütliches Zimmer in einer freundlichen WG. Küche und Bad werden gemeinsam genutzt. Gute Lage mit schneller Verbindung zur Innenstadt und zur Universität.',
        'FOR_RENT', 'HOME_AND_GARDEN', 1002, 'PUBLIC', 7.950, 52.275, 52.279, 7.960, 320.00, '2026-07-06 16:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'mtl.'),
       (107, 'Suche Sofa', 'AVAILABLE',
        'Ich suche ein gebrauchtes Sofa in gutem Zustand. Farbe und Stil sind zweitrangig, wichtig ist der Komfort. Abholung in Osnabrück möglich.',
        'WANTED', 'HOME_AND_GARDEN', 1002, 'PUBLIC', 7.960, 52.290, 52.279, 7.960, 150.00, '2026-07-05 18:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (108, 'Gaming PC', 'AVAILABLE',
        'Leistungsstarker Gaming-PC mit aktueller Hardware. Läuft alle modernen Spiele flüssig und eignet sich auch für Streaming oder Videobearbeitung. Keine Mängel.',
        'FOR_SALE', 'ELECTRONICS', 1234, 'PUBLIC', 7.955, 52.300, 52.279, 7.960, 900.00, '2026-07-04 20:15:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (109, 'Werkzeug Set', 'AVAILABLE',
        'Umfangreiches Werkzeugset zur Miete. Enthält alles für Heimwerkerprojekte und kleinere Reparaturen. Gut sortiert und in stabilem Koffer verstaut.',
        'FOR_RENT', 'HOME_AND_GARDEN', 1004, 'PUBLIC', 7.965, 52.285, 52.279, 7.960, 15.00, '2026-07-03 07:45:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'pro Tag');


INSERT INTO offers (id, title, status, description, category, categorySubject, ownerId, privacy, longitudeAddress,
                    latitudeAddress, latitudeCity, longitudeCity, price, createdat, imageName, addition)
VALUES (110, 'IKEA Kleiderschrank', 'AVAILABLE',
        'Gut erhaltener Kleiderschrank mit viel Stauraum. Der Schrank bietet mehrere Einlegeböden sowie eine Kleiderstange und eignet sich ideal für Schlafzimmer oder Gästezimmer. Leichte Gebrauchsspuren vorhanden, aber voll funktionstüchtig.',
        'FOR_SALE', 'HOME_AND_GARDEN', 1006, 'PUBLIC', 8.020, 52.285, 52.284, 8.023, 120.00, '2026-07-13 10:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (111, 'Schreibtisch mieten', 'AVAILABLE',
        'Stabiler Schreibtisch zur kurzfristigen oder längerfristigen Miete. Ideal für Homeoffice oder Studium. Die Oberfläche ist gepflegt und bietet ausreichend Platz für Laptop und Unterlagen. Flexible Abholung und Rückgabe nach Absprache möglich.',
        'FOR_RENT', 'HOME_AND_GARDEN', 1006, 'PUBLIC', 8.024, 52.283, 52.284, 8.023, 20.00, '2026-07-13 11:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'pro Woche'),
       (112, 'Suche Fahrrad', 'AVAILABLE',
        'Ich suche ein gebrauchtes Fahrrad für den täglichen Weg zur Arbeit. Es sollte verkehrssicher sein und über funktionierende Bremsen und Licht verfügen. Kleine Gebrauchsspuren sind kein Problem, wichtig ist die Zuverlässigkeit im Alltag.',
        'WANTED', 'SPORTS_AND_LEISURE', 1006, 'PUBLIC', 8.018, 52.286, 52.284, 8.023, 200.00, '2026-07-13 12:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (114, 'Sony Fernseher 42 Zoll', 'NOT_AVAILABLE',
        'Verkaufe einen gut erhaltenen Fernseher mit 42 Zoll Bildschirmdiagonale. Das Gerät bietet ein klares Bild und mehrere Anschlussmöglichkeiten für externe Geräte. Ideal für Wohnzimmer oder Schlafzimmer geeignet.',
        'FOR_SALE', 'ELECTRONICS', 1006, 'PUBLIC', 8.025, 52.282, 52.284, 8.023, 180.00, '2026-07-13 13:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (115, 'Kaffeemaschine', 'AVAILABLE',
        'Kompakte Kaffeemaschine für Filterkaffee. Einfach zu bedienen und zuverlässig im täglichen Gebrauch. Perfekt für kleine Haushalte oder Büros. Das Gerät wurde regelmäßig gereinigt und funktioniert einwandfrei.',
        'FOR_SALE', 'ELECTRONICS', 1006, 'PUBLIC', 8.021, 52.284, 52.284, 8.023, 25.00, '2026-07-13 14:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (116, 'Gartenstuhl Set', 'AVAILABLE',
        'Set aus vier stabilen Gartenstühlen aus Kunststoff. Wetterfest und leicht zu reinigen. Ideal für Balkon, Terrasse oder Garten. Die Stühle sind stapelbar und platzsparend zu lagern.',
        'FOR_SALE', 'HOME_AND_GARDEN', 1007, 'PUBLIC', 8.019, 52.287, 52.284, 8.023, 60.00, '2026-07-14 09:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (117, 'Bohrmaschine mieten', 'AVAILABLE',
        'Leistungsstarke Bohrmaschine zur Miete für Heimwerkerprojekte. Geeignet für Holz, Metall und Mauerwerk. Das Gerät ist einfach zu bedienen und wird mit passendem Zubehör übergeben.',
        'FOR_RENT', 'HOME_AND_GARDEN', 1007, 'PUBLIC', 8.023, 52.281, 52.284, 8.023, 10.00, '2026-07-14 10:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'pro Tag'),
       (118, 'Suche Bücherregal', 'AVAILABLE',
        'Ich suche ein stabiles Bücherregal für mein Wohnzimmer. Es sollte mehrere Fächer haben und sich gut in eine helle Einrichtung einfügen. Gebrauchsspuren sind in Ordnung, solange es funktional ist.',
        'WANTED', 'HOME_AND_GARDEN', 1007, 'PUBLIC', 8.026, 52.283, 52.284, 8.023, 80.00, '2026-07-14 11:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (119, 'Mikrowelle', 'NOT_AVAILABLE',
        'Funktionierende Mikrowelle in gutem Zustand abzugeben. Das Gerät verfügt über mehrere Leistungsstufen und eine Auftaufunktion. Ideal für Küche oder Büro geeignet.',
        'FOR_SALE', 'ELECTRONICS', 1007, 'PUBLIC', 8.017, 52.285, 52.284, 8.023, 40.00, '2026-07-14 12:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (210, 'Laufschuhe Größe 42', 'AVAILABLE',
        'Gut erhaltene Laufschuhe mit angenehmer Dämpfung. Ideal für Einsteiger oder gelegentliche Joggingrunden. Die Schuhe wurden nur wenig getragen und sind sauber.',
        'FOR_SALE', 'SPORTS_AND_LEISURE', 1007, 'PUBLIC', 8.022, 52.286, 52.284, 8.023, 35.00, '2026-07-14 13:00:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (211, 'Sideboard Holz', 'AVAILABLE',
        'Modernes Sideboard aus Holz mit viel Stauraum. Die Kombination aus Schubladen und Fächern bietet Platz für verschiedene Gegenstände. Optisch zeitlos und gut kombinierbar mit anderen Möbeln.',
        'FOR_SALE', 'HOME_AND_GARDEN', 1008, 'PUBLIC', 8.021, 52.282, 52.284, 8.023, 140.00, '2026-07-15 09:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (212, 'Anhänger mieten', 'AVAILABLE',
        'Kleiner PKW-Anhänger zur Miete für Transporte aller Art. Ideal für Umzüge, Gartenabfälle oder größere Einkäufe. Einfach anzukoppeln und zuverlässig im Einsatz.',
        'FOR_RENT', 'VEHICLES', 1008, 'PUBLIC', 8.024, 52.285, 52.284, 8.023, 15.00, '2026-07-15 10:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'pro Tag'),
       (213, 'Suche Esstisch', 'AVAILABLE',
        'Ich suche einen Esstisch für 4 bis 6 Personen. Der Tisch sollte stabil sein und optisch in eine moderne Wohnung passen. Kleine Gebrauchsspuren sind kein Problem.',
        'WANTED', 'HOME_AND_GARDEN', 1008, 'PUBLIC', 8.018, 52.283, 52.284, 8.023, 150.00, '2026-07-15 11:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (214, 'Bluetooth Lautsprecher', 'NOT_AVAILABLE',
        'Tragbarer Bluetooth-Lautsprecher mit gutem Klang und langer Akkulaufzeit. Ideal für unterwegs oder zu Hause. Das Gerät ist voll funktionsfähig und weist nur leichte Gebrauchsspuren auf.',
        'FOR_SALE', 'ELECTRONICS', 1008, 'PUBLIC', 8.026, 52.286, 52.284, 8.023, 55.00, '2026-07-15 12:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),
       (215, 'Bürostuhl', 'AVAILABLE',
        'Ergonomischer Bürostuhl mit verstellbarer Sitzhöhe und Rückenlehne. Bietet guten Komfort auch bei längerem Sitzen. Der Stuhl ist in einem gepflegten Zustand und sofort einsatzbereit.',
        'FOR_SALE', 'HOME_AND_GARDEN', 1008, 'PUBLIC', 8.020, 52.284, 52.284, 8.023, 70.00, '2026-07-15 13:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

       (216, 'Zelt (2 Personen)', 'AVAILABLE',
        'Dieses praktische 2-Personen-Zelt ist der ideale Begleiter für Campingausflüge, Festivals oder Trekkingtouren. Es bietet ausreichend Platz für zwei Personen und überzeugt durch seinen schnellen Aufbau sowie sein geringes Gewicht. Länge: ca. 200–220 cm / Breite: ca. 120–140 cm / Höhe: ca. 100–110 cm',
        'FOR_SALE', 'SPORTS_AND_LEISURE', 1009, 'PUBLIC', 8.020, 52.284, 52.284, 8.023, 35.00, '2026-07-15 13:30:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

       (900001, 'Suche Bohrmaschine', 'AVAILABLE',
        'Ich suche eine funktionierende Bohrmaschine für kleinere Arbeiten in der Wohnung. Ein älteres Modell ist ebenfalls in Ordnung, solange es zuverlässig funktioniert. Zubehör oder passende Bohrer können gerne mit angeboten werden.',
        'WANTED', 'HOME_AND_GARDEN', 2000, 'PUBLIC', 8.015, 52.305, 52.305, 8.015, 40.00, '2026-07-13 09:15:00+00',
        '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

        (900002, 'Suche gebrauchtes Fahrrad', 'AVAILABLE',
         'Gesucht wird ein einfaches und verkehrssicheres Fahrrad für den täglichen Weg zur Arbeit. Das Aussehen ist nicht wichtig, Bremsen, Beleuchtung und Schaltung sollten jedoch zuverlässig funktionieren.',
         'WANTED', 'SPORTS_AND_LEISURE', 2000, 'PUBLIC', 8.011, 52.308, 52.305, 8.015, 120.00, '2026-07-14 11:30:00+00',
         '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

        (900003, 'Suche Bücherregal', 'AVAILABLE',
         'Ich suche ein stabiles Bücherregal mit mehreren Fächern. Kleine Gebrauchsspuren sind kein Problem. Das Regal sollte möglichst bereits aufgebaut sein und kann im Raum Osnabrück abgeholt werden.',
         'WANTED', 'HOME_AND_GARDEN', 2000, 'PUBLIC', 8.019, 52.303, 52.305, 8.015, 60.00, '2026-07-15 16:20:00+00',
         '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

        (900004, 'Suche Monitor', 'AVAILABLE',
         'Für meinen Arbeitsplatz suche ich einen gebrauchten Monitor mit HDMI-Anschluss. Eine Bildschirmgröße ab 24 Zoll wäre ideal. Das Gerät sollte vollständig funktionieren und keine sichtbaren Bildfehler haben.',
         'WANTED', 'ELECTRONICS', 2000, 'PUBLIC', 8.008, 52.301, 52.305, 8.015, 100.00, '2026-07-16 13:45:00+00',
         '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB'),

        (900005, 'Suche Autokindersitz', 'AVAILABLE',
         'Ich suche einen gut erhaltenen Autokindersitz für ein Kleinkind. Der Sitz sollte unfallfrei, vollständig und sauber sein. Angebote aus Osnabrück und der näheren Umgebung werden bevorzugt.',
         'WANTED', 'VEHICLES', 2000, 'PUBLIC', 8.022, 52.307, 52.305, 8.015, 80.00, '2026-07-17 10:10:00+00',
         '38ca94a2-9015-43d7-a50d-527e53de8f52.png', 'VB');

INSERT INTO friendships (id, requesterId, addresseeId, status, createdAt, respondedAt)
VALUES (2002, 1006, 1007, 'ACCEPTED', '2026-07-10T08:15:00Z', '2026-07-10T09:00:00Z'),
       (2003, 1007, 1008, 'ACCEPTED', '2026-07-11T10:30:00Z', '2026-07-11T11:05:00Z'),
       (2004, 1006, 1008, 'ACCEPTED', '2026-07-12T14:45:00Z', '2026-07-12T15:20:00Z');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9003, 1006, 1007, 'Hallo, ich habe deine Anzeige gesehen. Vielen Dank für die schnelle Rückmeldung!',
        '2026-07-13 09:15:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9004, 1007, 1006, 'Gerne, kein Problem. Wenn du noch Fragen hast, melde dich einfach.',
        '2026-07-13 09:20:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9005, 1007, 1008, 'Hallo, ich wollte kurz wegen der Anzeige nachfragen. Ist der Artikel noch verfügbar?',
        '2026-07-14 15:30:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9006, 1008, 1007, 'Hallo, ja der Artikel ist noch verfügbar. Eine Abholung wäre nach Absprache möglich.',
        '2026-07-14 15:40:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9007, 1006, 1008,
        'Hallo, ich habe gesehen, dass du auch in der Umgebung aktiv bist. Viel Erfolg mit deinen Anzeigen!',
        '2026-07-15 11:00:00+00');

INSERT INTO messages (id, senderId, recipientId, text, sentAt)
VALUES (9008, 1008, 1006, 'Danke dir! Ich wünsche dir ebenfalls viel Erfolg und einen schönen Tag.',
        '2026-07-15 11:10:00+00');