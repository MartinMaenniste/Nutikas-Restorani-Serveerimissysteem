# Nutikas Restorani Serveerimissüsteem

## Projekti kirjeldus
Projekt haldab restoranikülastajate broneeringuid väljamõeldud restoranis. Kasutaja saab teha broneeringuid kasutades veebiliidest.<br/>
Broneeringut tehes on võimalik filtreerida kuupäeva+kellaaja ning inimeste arvu järgi. Täpsustada saab ka eelistatud tsooni restoranis - näiteks väljas/sees.<br/>
## Tehniline pool
Projekt kasutab veebiliidest, mille kaudu kasutaja saab teha broneeringuid. Veebiliides on loodud Java Spring Boot raamistiku kasutades, java programmeerimiskeeles. Lisaks on kasutatud *Thymeleaf*, et HTML dünaamiliselt luua.<br/>
Lisatud on võimalus kompileerida ja käivitada programmi kasutades *maven* süsteemi. Selleks on pom.xml fail.<br/>
## Näide projekti kasutamisest
Projekti kasutamiseks on vaja:<br/>
 - Java JDK25<br/>
 - Maven<br/>
```
#Java olemasolu/versiooni kontrollimine
java --version

#Maven olemasolu kontrollimine
mvn --version

#Repo allalaadimine
git clone https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimiss-steem.git
cd https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimissysteem.git

#Kompileerimine - vaid esmakäivitamisel (või peale koodis muudatuste tegemist)
mvn compile

#Programmi jooksutamine
mvn spring-boot:run
```
