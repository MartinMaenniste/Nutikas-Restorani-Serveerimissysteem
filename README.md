# Nutikas Restorani Serveerimissüsteem

## Projekti kirjeldus
Projekt haldab restoranikülastajate broneeringuid väljamõeldud restoranis. Kasutaja saab teha broneeringuid kasutades veebiliidest.<br/>
Broneeringut tehes on võimalik filtreerida kuupäeva+kellaaja ning inimeste arvu järgi. Täpsustada saab ka eelistatud tsooni restoranis - näiteks väljas/sees.<br/>
## Tehniline pool
Projekt kasutab veebiliidest, mille kaudu kasutaja saab teha broneeringuid. Veebiliides on loodud Java Spring Boot raamistiku kasutades, java programmeerimiskeeles. Lisaks on kasutatud *Thymeleaf*, et HTML dünaamiliselt luua.<br/>
Andmed on salvestatud postgresql andmebaasi. Andmebaas ja Spring rakendus jooksevad kahe eraldi docker konteineri sees, mida haldab docker compose.<bt/>
Andmebaasis on 3 tabelit, mis on defineeritud klassides Reservations.java, TableTypes.java ja TableAsClass.java
## Näide projekti kasutamisest
Projekti kasutamiseks on vaja:<br/>
 - Docker + Docker compose<br/>
Windowsi puhul on vaja docker desktop rakendust. Kui see avada, on võimalik docker compose käsku kasutada.
Linuxi puhul peab kasutama enda süsteemi paketihaldurit, et laadida docker compose (mille sõltuvus on docker). Näiteks apt ja RPM puhul peab paigaldama docker-compose-plugin paketi. https://docs.docker.com/compose/install/linux/<br/>
```
#Dockeri kontrollimine (näha on nii dockeri versiooni kui ka docker compose kohta infot)
docker info

#Repo allalaadimine
git clone https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimiss-steem.git
cd https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimissysteem.git

#Programmi jooksutamine (linux süsteemi puhul peab dockerit enamasti juurkasutajana jooksutama - sudo)
docker compose up
```
Kui programm on käivitatud, saab veebiliidest kasutada lehel localhost:8080<br/>
## Teadaolevad puudused
Projekt ei saanud täiesti valmis, järgnevad on teadaolevad vead, mille parandamiseks ei jätkunud aega:<br/>
 - Vormi täitmisel seotud kellaajaline piirang ei kontrolli kuupäeva (teoorias vältib minevikku reserveerimist, aga praktikas teeb programmi kasutamatuks. Vaja kuupäev enne täita ja seda kellaaja juures kontrollida).<br/>
 - Vormi täitmisel minimaalne kellaaeg on praegune ajahetk, võetuna kasutaja arvutist. Peale selle on problemaatiline ka asjaolu, et ei võeta arvesse restorani avamise aega. (Pigem väike muudatus - vaja ajatsoone ja minimaalseks väärtuseks maksimaalne kahest võimalusest.)<br/>
 - Arvulise aja moodustamisel kasutatavale java.util.Calendar muutujale on vaja lisada ajatsoon. Vaikimisi kasutatav on Eesti ajast 2 tundi maas.<br/>
 - Programm on disainitud vaid ühele kasutajale korraga. Kui samaaegselt mitu kasutajat prooviksid lauda reserveerida, läheks programm katki. (Selleks peab programmi targemini üles ehitama. Andmebaasis on vaid mitte muutuvad andmed ja *styling* muutujad seada koodis)<br/>
 ## Tegemata jäi
 Järgnevalt on loetletud mida ma planeerisin lisada, aga ei jõudnud:
 - Admin leht - On näha kõiki laudu. Hiirega laua kohale minnes on näha kõiki selle laua reserveeringuid. Peale vajutades avaneb eraldi leht, kus saab muuta/kustutada reserveeringuid.<br/>
 - Üldine programmi silumine - Veebilehtede väljanägemine; Programmi läbi testimine, vigade leidmine ja parandamine; Teadaolevate puuduste likvideerimine<br/>
 - Laudade kuvamine reservation.html lehel - targemini. (Plaan oli teha ruudustik, kus ruudud saavad olla lauad või muud ette defineeritud asjad - põrand, sein, uks jne. See võimaldab teha terviklikumat programmi, kuhu saab lisada loogika näiteks laudade paigutuse muutmise (nt. admin vaadet kasutades ja koodis laudade ehitamine) ja mitme laua korraga reserveerimise kohta.)
 - Testide lisamine. (Testide väga algeline faas on salvestatud old-files kausta - samal ajal otsustasin lisada andmebaasi projekti, aga tahtsin alles hoida ning hiljem muuta esimesi teste, mille pealt oleksin teinud rohkem teste. Testimine töötaks sarnaselt olemasolevatele testidele, aga kasutades teisi meetodeid. Samuti on võimalik otse andmebaasi teha päringuid.)
 ## Programmi loomisel tekkinud probleemid
Suurim probleem programmi loomisel oli ajapuudus. Kuna tegin seda ajateenistuse kõrvalt, teadsin ette, et väljaõppe tõttu terve esimene nädal ei ole võimalik arvutit avada. Teise nädala alguses sain teada ka, et suur osa teisest nädalast on kinni. Seetõttu pidin teadlikult tegema otsuseid, mida jõuan selle ajaga teha ja mida mitte. Proovisin teha programmi, mille kaudu ma näitan rohkem enda sügavamaid teadmisi programmeerimisest ja enda kiiret õppevõimet. Seetõttu jäid unarusse programmi ülesehitus/disain ja detailide silumine.<br>
Lühidalt kirjeldatud suuremad probleemid arendamisel:<br/>
 - Kasutasin esimest korda Spring raamistikku.<br/>
 - Kuna olen pikemat aega C++ kasutanud ja pole ammu Javat kasutanud, läks aega baasteadmiste meeldetuletamisele - süntaks, prügihalduriga programmi lihtsustused ja Javas olevad tööriistad<br/>
 - Pole pikalt veebirakendust arendanud, läks aega HTML, CSS baasteadmiste meeldetuletamiseks.<br/>
 - Kasutasin sisuliselt esimest korda projektihaldust maven/gradle. Ülikoolis puutusin sellega kokku, aga väga vähe.<br/>
 - Vaba aja olemuse tõttu oli koodi kirjutamine katkendlik ja tihti oli vaja kiirustada - ei olnud aega rahulikult läbi mõelda.<br/>
Probleemidele leidsin lahendusi internetist otsingumootoriga märksõnade otsimise kaudu. Kui ei olnud kindel, mida peaksin otsima või ei leidnud tavalise otsimise kaudu lahendust, kasutasin AI tööriista ChatGPT, et seletada mulle konteksti või tuua näiteid, et oskaksin kaasa mõelda ja infot juurde otsida. Veebis leitu ja AI vastuses nähtu aitasid luua terviklikuma pildi sellest, kuidas võiks lahendus välja näha. Seda oli keeruline dokumenteerida, sest proovisin kasutada tööriistu vaid metoodikast aru saamiseks ning seetõttu ei otsinud suuremaid lõike valmis koodi, vaid üksnes üksikuid ridu või funktsioone. Omandatud arusaama testimiseks proovisin koodi ikkagi ise kirjutada, pannes kokku erinevad osad, mis jäid meelde erinevatest allikatest. Suuremad koodijupid, mille ma kopeerisin on kommentaaridega täpsustatud.
