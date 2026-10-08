# Nachbarschaftsportal

In dieser ReadMe finden Sie alle Informationen, um die Anwendung __Nachbarschaftsportal__ in Betrieb zu nehmen.

## Voraussetzungen

+ Docker 2025.06+
+ JDK 25+
+ Maven 3.9+


## Anwendung vorbereiten / starten

Um die Anwendung in Betrieb zu nehmen, müssen folgende Befehle in der Kommandozeile ausgeführt werden:

(stellen Sie sicher das Sie im Verzeichnis "Nachbarschaftsportal" sind)

Code Kompilieren:
```
./mvnw compile
```

Quarkus mit DEV-Services starten
```
./mvnw quarkus:dev
```

Je nach Hardware und System kann dies unterschiedlich lange dauern.

Sobal Sie das Quarkus-Logo in der Konsole sehen können (ASCII-Art) ist die Software einsatzbereit


## Anwendung testen

Um die im Programmcode hinterlegten Tests zu starten, geben Sie Folgendes ein

```
./mvnw test
```

Nach dem Durchlauf sollten 29 Test als erfolgreich angezeigt werden.



### Endpunkt manuel testen

Um die API Endpunkt selbst zu testen, starten sie wie oben beschrieben die Anwendung.

Nun rufen Sie die integrierte Swagger UI auf

```
http://localhost:8080/q/swagger-ui/
```

Da die Endpunkte vor Zugriff geschützt sind, müssen Sie sich zunächst autorisieren.

Klicken Sie dazu auf die Schaltfläche __Authorize__ und geben sie folgende Daten ein:

+ Username: __lena.schneider@example.com__ oder __jonas.becker@example.com__
+ Password: Demo#2026
+ Client_ID: nachbarschaftsportal
+ Client_Secret: **********

Danach bestätigen sie mit __Authorize__.

Nun haben Sie Zugriff auf alle Endpunkte, da die beiden genannten User Admin-Rechte haben



## Anwendung verwenden

Um die Anwendung zu nutzen, rufen Sie folgenden URL im Browser auf:

```
http://localhost:8080/
```

Sie werden nun automatisch an /home weitergeleitet.

Nun befinden Sie sich auf der Startseite des Nachbarschaftsportals

### Funktionen (unangemeldet)

Damit sich potenzielle Nutzer bereits einen Überblick über das Angebot des Nachbarschaftsportals machen können, gibt es auch Funktionen für nicht registrierte Verwender.

So können diese durch die eingabe einer Postleitzahl, sehen welche Anzeigen in ihrer Nähe verfügbar sind.

__Demo__: nutzen Sie die Postleitzahl __49076__. Hier finden Sie eine representative Auswahl.

Alle weiteren Funktionen verlangen einen registrierten Nutzer.

### Funktionen (angemeldet)

Sobald Sie sich angemeldet haben, steht Ihnen der volle Umfang der Anwendung zur Verfügung.

__Demo:__ Folgende User wurden mit umfangreichen Testdaten ausgestattet:

```
Username: test1@test.de
Passwort: Demo#2026

Username: test2@test.de
Passwort: Demo#2026

Username: test3@test.de
Passwort: Demo#2026
```

+ __Home__

Die Startseite zeigt nun die Adresse des angemeldeten Benutzers und Anzeigen im näheren Umkreis

+ __Freunde__

Hier sehen Sie die aktuellen Freunde und Freundschaftsanfragen.

__Demo__: Öffnen Sie mit folgender URL ein Profil mit dem Sie noch nicht befreundet sind.

```
http://localhost:8080/profile/1009
```
Diesen können Sie eine Freundschaftsanfrage schicken. 
Nehmen Sie diese an, indem Sie sich als test4@test.de (PW:Demo#2026) an und nehmen Sie die ANfrage im Reiter "Freunde" an.

+ __Postfach__

Hier sehen Sie die bereits verschickten Nachrichten. Wählen Sie einen Chat um zu schreiben.

Wenn Sie einen Chat mit einem neuen Freund starten wollen, wählen Sie die drei Punkte neben seinem Namen im Reiter "Freunde". Nach der ersten Nachricht ist der Chat auch im Postfach verfügbar.

+ __Mein Konto > Mein Profil__

Hier finden Sie Ihre Daten und Ihre eigenen Anzeigen. Von hier aus können Sie ihre Anzeigen auch bearbeiten.

Zum Bearbeiten klicken Sie auf den Butten in der jeweiligen Anzeige. Danach kommen Sie zu Bearbeitungsseite. Dort können Sie alle Aspekte bearbeiten.

+ __Mein Konto > Einstellungen__

Hier können Sie Ihre Daten ändern oder das Profil löschen

+ __Mein Konto > Abmelden__

Klicken zum Abmelden

+ __Anzeigen finden__

Hier können Sie nach Anzeigen suchen. Suchkriterien:
 1. Art
2. Kategorie
3. Adresse (die des Users ist vorgeblendet, bei Änderung auf __Standort aktualisieren__ klicken)
4. Radius um die Adresse wählen 
5. __Suchen__ anklicken 

__Demo__: Kaufen, Haus & Garten, 49076, min. 5 km > 3 Anzeigen

Anschließend können Sie eine Anzeige öffnen (gleichnamiger Button) oder zum Profil des Anbieters gelangen. 

Wenn Sie Anzeige geöffnet haben sehen Sie detailierte Informationen und können Kontakt mit dem Anbieter aufnehmen

+ __Anzeigen erstellen__

Hier können Sie eine neue Anzeige erstellen (Beim Ändern der Adresse bitte auf __Adresse aktualisieren__ klicken)

## Anwendung beenden

Dücken Sie die Tastenkombination __STRG + C__ im Kommandozeilenfenster in dem Sie die ANwendung gestartet haben.  




