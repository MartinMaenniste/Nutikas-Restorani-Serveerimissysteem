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

#Programmi jooksutamine
mvn spring-boot:run
```

## Dockeri kasutamine
Projektile on lisatud Dockerfile. Selle kasutamiseks on vaja töötavat dockeri konteinerite jooksutamise süsteemi.<br/>
Esmalt on vaja luua projektist Dockerfile abil docker image fail. Seejärel saab seda jooksutada näiteks vaikimisi kasutataval pordil 8080.<br/>
Docker võib vajada lisaõigusi käskude jooksutamiseks (näiteks Linux süsteemis sudo kasutajat).<br/>
Terminalis saab programmi kasutada läbi dockeri:<br/>
```
# Dockeri olemasolu kontrollimine
docker --version

# Kui programm pole veel alla laetud
git clone https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimiss-steem.git
cd https://github.com/MartinMaenniste/Nutikas-Restorani-Serveerimissysteem.git

# dockerapp asemel saab valida endale sobiva image faili nime
docker build -t dockerapp .
# Image faili saab kontrollida
docker images
# Pordil 8080 jooksutamiseks
docker run -p 8080:8080 dockerapp
```
Kuna projekt on veel arenguetapis, ei ole valmis ehitatud docker image faili. Seetõttu peab ehitama selle enne programmi kasutamist
