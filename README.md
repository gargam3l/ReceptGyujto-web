# ReceptGyujto-web

Szükséges előkövetelmények: 
1. Oracle Database Express feltelpítése 
2. Oracle database express homapage-en adminként a hr felhasználó unlock-olása, hr jelszó megadása, ill CREATE TABLE plusz jogosultság megadása
3. Apache tomcat telepítése - 8084-es porton netbeans beállítás
4. Legfrissebb JRE verzió telepítése - ellenőrzés java.com/verify oldallal
5. Security kivétel hozzáadása Java applethez - vezérlőpult - java- security - Edit site list - Add - http://localhost:8084/
6. Ellenőrzés, hogy a webes project (ReceptGyujto-web) megfelelő útvonallal éri el az applet projektet(ReceptGyujto-AppletLogic), mint függőségét - Project properties - Build/Packaging alatt
7. Ellenőrzés, hogy a webes project (ReceptGyujto-web) megfelelő útvonallal éri el a JDBC jart, mint függőségét - Project properties - Libraries alatt
8. Web projekt (ReceptGyujto-web) futtatása internet explorer böngészőt használva
