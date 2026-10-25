# AlexiGuitarTuner – lépésenkénti hibajavítási terv

Dátum: 2026-10-25. Állapot: megtervezve, az implementáció még nem kezdődött el.

A feladatok a [requirements.md](requirements.md) követelményeit hajtják végre. Minden implementációs lépés kezdetben `- [ ]`; csak elkészült és a leírt módon ellenőrzött lépés állítható `- [x]` állapotúra. Sikertelen vagy eszköz hiányában nem futtatott ellenőrzés maradjon üres. A feladat mellé rövid eredmény/hivatkozás írható; a részletes futási adatok a dokumentum végén rögzíthetők.

## Végrehajtási sorrend és döntések

- A következő fázisok sorrendben követik egymást; egy fázison belül az azonosító sorrendje az alapértelmezett. A terv nem igényel párhuzamos ügynökmunkát.
- Először a mérhető audiohibák javítása következik, majd az adatmodell és a migráció; a hangolásszerkesztő csak ezután áll át az új tárolásra.
- A referenciahang alapmegoldása mono PCM. A metronóm monoton időalapból számolt határidőket használ. A hangoláskapcsolat azonosítója a hangolás és a húrsorszám; a frekvencia ismétlődhet.
- A v2 adatmodell a húrsorszám mellett explicit beépített/saját jelölést ad a hangolásoknak, hogy a törlés ne az azonosítók felső határára épüljön. A v1→v2 átalakítás és az asset frissítése egy közös, ellenőrzött migrációs szabályt kövessen.
- A beállításmentés egyszeri, megvárható művelet lesz; a megfigyelések száma nem növekedhet dialógusnyitásonként. Az állapotlisták új értékként frissülnek.
- A hangfelismerés a legközelebbi támogatott hangot és centeltérést adja; az alapértelmezett pontos tartomány ±5 cent. A megjelenítési szöveg a UI-ban készül.
- A hangoló audiofolyamata képernyőhöz kötött; a metronóm foreground service-ként háttérben tovább működik. Ezt a két életciklust külön kell kezelni.
- A fázisok célzott tesztjeit a javítás közben kell lefuttatni. A teljes build/unit/lint/eszközteszt-csomag a végső ellenőrzéshez tartozik; változatlan, már sikeres ellenőrzéseket nem kell indokolatlanul ismételni.

## 00 – Kiindulási állapot és regressziós esetek rögzítése

Követelmények: REQ-01–REQ-10. Ezt a fázist az implementáció elején kell végrehajtani; a dokumentumok létrehozása önmagában nem teljesíti.

- [ ] T00.01 – Olvasd el a gyökér `AGENTS.md` fájlt, majd a `.codex/AGENTS.md`, `requirements.md`, `tasks.md` és `COMPOSE_MIGRATION.md` dokumentumokat; ellenőrizd a munkafa állapotát és őrizd meg az esetleges felhasználói változtatásokat.
- [ ] T00.02 – Ellenőrizd a JDK 17, Android SDK és Gradle wrapper használhatóságát; egyszer futtasd az aktuális unit/lint alapellenőrzést és rögzítsd a tényleges eredményt. Hasonlítsd a követelményekben szereplő 39 tesztes/61 lintfigyelmeztetéses analízisállapothoz, az eltéréseket magyarázd meg.
- [ ] T00.03 – Készíts regressziós esetlistát a frekvenciakétszerezésről, a 120 BPM/negyed és 200 BPM/tizenhatod időzítéséről, a hat azonos hangú húrról, a sorrendről, a dialógus újranyitásáról és a vesszős számbevitelről.
- [ ] T00.04 – Az audio-életciklus kockázatait külön reprodukciós feladatként rögzítsd: gyors start/stop, indítás közbeni kilépés, engedélyvesztés és szolgáltatásleállítás. Ne állítsd, hogy eszközön már igazolt hibák, amíg nincs ilyen eredmény.
- [ ] T00.05 – Rögzítsd az adatbázis v1 verzióját, az asset rekorddarabszámait és a megőrzendő adatok körét. A séma/export/fixture létrehozása előtt ne módosíts adatbázis-entitást vagy assetet.

## 01 – Az éles logika teszteléséhez szükséges alapok

Követelmények: REQ-01, REQ-02, REQ-04, REQ-09, REQ-10. Függőség: 00. fázis.

- [ ] T01.01 – Válaszd le a PCM-mintaképzést az `AudioTrack` hardveres meghajtásáról úgy, hogy ugyanazt a mintaképzőt használja a production repository és a frekvenciateszt; a teszt ne másolja le az algoritmust.
- [ ] T01.02 – Vezess be a metronómhoz injektálható monoton időalapot, ütemezőt és hangkimeneti határt, a szükséges legkisebb módosítással. A virtuális idő és a kiadott ütéskezdetek legyenek tesztből megfigyelhetők.
- [ ] T01.03 – Az érintett settings/tuning fake repositoryk egyszeri `flow { emit(...) }` értékei helyett adj valóban változó állapotfolyamokat; legyen megfigyelhető az aktív gyűjtések száma és a megszakítás.
- [ ] T01.04 – Készíts vezérelhető tesztkimenetet az audioindítás, pufferírás, késleltetett inicializálás és felszabadítás eseményeihez. A tesztek ne igényeljenek mikrofont vagy tényleges hanglejátszást.
- [ ] T01.05 – Javítsd a várt kivételt csak `catch` ágban ellenőrző teszteket: akkor is bukjanak, ha nincs kivétel. Ellenőrizd a módosított tesztek sorrendfüggetlenségét és a production/fake szerződés egyezését.

## 02 – A referenciahang frekvenciájának javítása

Követelmények: REQ-01, REQ-10; az életciklus teljességét a 10. fázis zárja le. Függőség: 01. fázis.

- [ ] T02.01 – Írj az éles PCM-mintaképzőt használó regressziós tesztet 82,41 Hz, 329,6276 Hz és 440 Hz kimenetére; a régi hibánál mutasd ki az oktáveltérést.
- [ ] T02.02 – Állítsd át a referenciahangot mono 44 100 Hz/PCM 16 bit formátumra; a fázist mintánként egyszer léptesd. Ha sztereó szükséges, dokumentáld az okát és helyesen töltsd mindkét csatornát.
- [ ] T02.03 – Válaszd külön a bájtban megadott minimális pufferméretet és a `ShortArray` elemszámát; ellenőrizd a kimeneti formátumhoz szükséges igazítást és a hibás inicializálás kezelését.
- [ ] T02.04 – Őrizd meg a fázist egymást követő pufferek között; kezeld a részleges/sikertelen írást anélkül, hogy elcsúszna a mintasor vagy beragadna a lejátszó állapota.
- [ ] T02.05 – Ellenőrizd automatizáltan a legfeljebb 0,1%-os relatív frekvenciahibát és a pufferhatárok folytonosságát; a mérőeljárás felbontását is rögzítsd.
- [ ] T02.06 – Ellenőrizd a frekvenciaváltást, az azonos referenciagombbal történő ki-/bekapcsolást, a Stop műveletet és a mikrofonengedély nélküli használatot; futtasd a kapcsolódó célzott unit teszteket.

## 03 – A metronóm időzítésének és ütésállapotának javítása

Követelmények: REQ-02, REQ-07, REQ-10. Függőség: 01–02. fázis.

- [ ] T03.01 – Írj virtuális idejű tesztet a jelenlegi 50 ms-os ütemközhibára, külön 120 BPM/negyed és 200 BPM/tizenhatod esetben.
- [ ] T03.02 – Implementáld a monoton következő határidőből számolt ütemezést; a hanghossz külön levonását távolítsd el, a kerekítési maradékot és a feldolgozás idejét kezeld drift nélkül.
- [ ] T03.03 – A kattintás ne álljon le közvetlenül az indítás után; a hanghossz ne tolja el a következő ütés kezdetét. A néma ütés hagyja ki a hangkimenetet.
- [ ] T03.04 – A metronóm listaváltoztatásait alakítsd új listát és új állapotot előállító műveletté. Szüntesd meg a megosztott, módosítható kezdeti ütéslistát és az `update` mellékhatásait.
- [ ] T03.05 – Tedd konzisztenssé a lejátszó állapotának kiolvasását, az aktuális ütés indexét és a 3–8 elem közötti hozzáadást/törlést. Lejátszás közben se legyen érvénytelen index vagy észrevétlenül elmaradó módosítás.
- [ ] T03.06 – Határozd meg és teszteld a BPM-/ritmusváltás érvényesülését lejátszás közben; a következő ütés kezelése ne okozzon több párhuzamos ciklust vagy véletlen dupla ütést.
- [ ] T03.07 – Futtass determinisztikus tesztmátrixot 40/100/120/200 BPM-mel és mindhárom ritmussal, továbbá 60 másodperces virtuális idővel; ellenőrizd a legfeljebb 1 ms-os határidőeltérést.
- [ ] T03.08 – Ellenőrizd a hangsúlyciklust, a néma ütést, a lista minimumát/maximumát és a start/stop ismételhetőségét az éles logikán; futtasd a kapcsolódó célzott unit teszteket.

## 04 – Az adatmodell és az adatmegőrző v1→v2 migráció

Követelmények: REQ-03, REQ-05, REQ-10. Függőség: 00. fázis; az adatbázist érintő további javítások előtt végrehajtandó.

- [ ] T04.01 – Még a modellváltás előtt kapcsold be a Room sémaexportját, rögzítsd a jelenlegi v1 sémát és őrizd meg az eredeti v1 assetet izolált migrációteszt-fixture-ként. A fő asset maradjon változatlan, amíg a migráció el nem készül.
- [ ] T04.02 – Vizsgáld meg a v1 kapcsolatok érvényességét, a beépített hangolások tényleges húrsorrendjét és az akkordfogások irányát; rögzíts explicit megfeleltetést gitárhoz, basszushoz és ukuleléhez.
- [ ] T04.03 – Vezesd be a húrsorszámos kapcsolatot egyedi `(tuningId, stringNumber)` azonosítással és ismételhető frekvenciával; add hozzá a szükséges rendezéseket/indexeket és a hangolások explicit beépített/saját jelölését.
- [ ] T04.04 – Az érvényességvizsgálat alapján alakítsd ki a kapcsolatok integritásvédelmét. Idegen kulcs bevezetése ne dobjon el régi rekordot; az esetleges eltérő adatok kezelését dokumentáld és teszteld.
- [ ] T04.05 – Implementáld az explicit v1→v2 migrációt úgy, hogy a meglévő hangolásazonosítók, nevek, frekvenciák, hangszerkapcsolatok, beállítások, kalkulátorsorok és akkordadatok megmaradjanak.
- [ ] T04.06 – A beépített hangolásokhoz a rögzített fizikai sorrendet használd; a régi saját hangolásokhoz determinisztikus sorrendet rendelj. Dokumentáld a korábban elveszett ismétlődések/sorrend visszaállíthatatlanságát, és tartsd betölthetőnek a hiányos régi hangolásokat kitalált pótlás nélkül.
- [ ] T04.07 – Emeld a sémaverziót v2-re, regisztráld a migrációt és távolítsd el a destruktív migrációs visszaesést. Ellenőrizd, hogy hiányzó migráció esetén az alkalmazás nem törli újra létrehozással a felhasználói adatbázist.
- [ ] T04.08 – Reprodukálható módon állítsd elő a v2 fő assetet a megőrzött v1 adatokból, a migrációval azonos szabályokkal; ellenőrizd a verziót, sémát és megőrzött adatokat. Ne módosíts felhasználói adatbázist.
- [ ] T04.09 – Készíts valós Room-migrációtesztet saját hangolással, módosított beállításokkal és kalkulátorsorokkal; ellenőrizd az adatok, kapcsolatok és azonosítók megőrzését v1→v2 frissítéskor.
- [ ] T04.10 – Külön teszteld a friss telepítés v2 assetből történő indítását és a frissített v1 adatbázis megnyitását; a teljes új séma validálódjon és a DAO-k működjenek.

## 05 – Hangolásmentés, húrsorrend és tranzakciók

Követelmények: REQ-03, REQ-05, REQ-10. Függőség: 04. fázis.

- [ ] T05.01 – A hangolás beszúrása adja vissza a saját létrehozott azonosítóját; a mentési útból távolítsd el a „legutolsó hangolás” külön lekérdezésére épülő hozzárendelést.
- [ ] T05.02 – A hangolás és az összes sorszámozott húr mentését helyezd egy repository-/DAO-szintű tranzakcióba. A bemenetet induláskor másold le, validáld a húrszámot, és kezeld a sikert/hibát a UI felé.
- [ ] T05.03 – A hangoló referenciagombjai és az érintett DAO-lekérdezések explicit húrsorszám szerint töltsenek; ne vonják össze az azonos frekvenciájú húrokat.
- [ ] T05.04 – Az akkorddiagram hangfeliratait és fogáspozícióit ugyanahhoz a fizikai húrsorrendhez igazítsd; távolítsd el a sorrendet felülíró frekvencia szerinti rendezést, és őrizd meg a beépített fogások helyességét.
- [ ] T05.05 – A saját hangolások törlése használja az explicit saját/beépített jelölést; a kapcsolatok törlésével és az alapértelmezett hangolás rendezésével együtt legyen atomi. Őrizd meg az eddigi törlési megerősítést.
- [ ] T05.06 – A kalkulátorsor törlését/átszámozását és új sorszámának kiosztását tedd tranzakciósan biztonságossá; párhuzamos hozzáadás ne írjon felül másik sort. Ellenőrizd, hogy a UI nem köt szerkesztési állapotot másik húrhoz az átszámozás után.
- [ ] T05.07 – Teszteld valódi Room-on a hat azonos hangú húr és a nem monoton sorrend oda-vissza mentését; Compose/E2E teszt ellenőrizze a megmaradó hat referenciagombot és az akkordrajzhoz tartozó feliratokat.
- [ ] T05.08 – Mesterséges mentési hibával ellenőrizd a tranzakció visszagörgetését, párhuzamos hangolásmentéssel az azonosítók elkülönülését, továbbá a saját hangolások törlésének integritását.

## 06 – A beállítások és a hangolásválasztó reaktív működése

Követelmények: REQ-04, REQ-05, REQ-10. Függőség: 01., 04–05. fázis.

- [ ] T06.01 – Írj változó Flow-forrásokkal regressziós tesztet arra, hogy a `SelectTuningViewModel` a kezdeti érték után új beállítás-, hangszer- és hangoláslista-változást is feldolgoz.
- [ ] T06.02 – Cseréld le az egymásba ágyazott tartós `collect` hívásokat egy megszakítható, legfrissebb kiválasztáshoz igazodó adatfolyamra; ne hagyj korábbi kiválasztásból visszaérő eredményt felülírni újabbat.
- [ ] T06.03 – Az inicializálás legyen idempotens, a megfigyelés életciklusa egyértelmű. Ismételt dialógusnyitás ne adjon hozzá új, szükségtelenül életben maradó gyűjtőt.
- [ ] T06.04 – A beállításmentést alakítsd egyszeri, megvárható műveletté; a hangnevek és jelölési beállítások összetartozó módosítása egy tranzakcióban történjen.
- [ ] T06.05 – A kiválasztott hangolás és a jelölési kapcsolók mentése csak a saját módosítandó mezőit érintse, vagy konzisztens legfrissebb állapotból történjen. Ne lehessen elavult beállításmásolattal felülírni a másik művelet eredményét.
- [ ] T06.06 – A route/dialógus mutassa a folyamatban lévő mentést és annak eredményét; kezeld a hibát, akadályozd meg a véletlen dupla mentést és őrizd meg a Mégse mentés nélküli működését.
- [ ] T06.07 – Teszteld a dialógus ismételt megnyitását/bezárását és a figyelők számát, a külső hangoláslista-változást, valamint az aktuális mentett választás visszatöltését.
- [ ] T06.08 – Teszteld a kapcsolóváltás + hangolásválasztás sorrendjeit, gyors egymásutáni mentést, mentés utáni navigációt és activity-újralétrehozást; a megfelelő beállítások maradjanak meg.

## 07 – Hangfelismerési visszajelzés és jelölésfrissítés

Követelmények: REQ-06, REQ-10. Függőség: 01., 05–06. fázis.

- [ ] T07.01 – A production repository által használt hangazonosító algoritmust tedd közvetlenül tesztelhetővé; a másolt algoritmust tesztelő `PitchDetectionRepositoryTest` eseteket irányítsd át az éles logikára.
- [ ] T07.02 – A ±0,4 Hz-es névkeresési kapu helyett határozd meg a legközelebbi támogatott hangot logaritmikus/cent alapú távolsággal; a hangok közötti felezőnél az alacsonyabb hang legyen a determinisztikus választás.
- [ ] T07.03 – Vezesd be a strukturált mérési eredményt a referenciahanggal, mért frekvenciával, centeltéréssel és alacsony/magas/pontos állapottal; a pontos tartomány legyen befoglalóan −5…+5 cent.
- [ ] T07.04 – Kezeld a detektor nem hangként jelölt, nulla, negatív, nem véges és támogatott tartományon kívüli eredményeit; a képernyő mutasson egyértelmű mérés nélküli állapotot.
- [ ] T07.05 – A hangnévlista és az érintett referenciagombok/választók kövessék a jelölési beállítások változását; szüntesd meg az újraindításig elavult neveket tároló gyorsítótárat.
- [ ] T07.06 – Az új eredményt jelenítsd meg a hangolóban Android string resource-okkal és akadálymentes leírással; a repository ne állítson elő angol „Above/Below” feliratokat.
- [ ] T07.07 – Teszteld az E2/A4 és további oktávok hangjait ±10/±25 cent eltéréssel, a ±5 cent határokat, a félhangfelezőket, a tartomány széleit és a hibás méréseket.
- [ ] T07.08 – Compose-/integrációs teszt igazolja a sharp/flat és angol/német jelölés azonnali frissülését a felismert hangon, referenciagombokon és nyitott hangválasztón; futtasd a kapcsolódó célzott teszteket.

## 08 – A megmaradt módosítható UI-állapotok rendezése

Követelmények: REQ-07, REQ-10. Függőség: 03., 05–07. fázis.

- [ ] T08.01 – A `CreateTuningUIState` listáját és módosításait alakítsd új értéket előállító állapotfrissítéssé; külön ViewModel-példányok ne használjanak közös módosítható kezdeti listát vagy hangobjektumot.
- [ ] T08.02 – A létrehozó inicializálásakor és hangszer-/hangváltáskor egy konzisztens új állapotban jelenjen meg a helyes húrszám és hanglista; kezeld az üres adatforrást és a még be nem töltött állapotot.
- [ ] T08.03 – Távolítsd el a `MetronomeRoute` és `CreateTuningSheet` kézi `revision` számlálóit és az azokhoz kötött újrakomponálási kerülőutakat, miután az állapotemissziók már helyesek.
- [ ] T08.04 – Állapotfolyam-teszttel igazold minden releváns módosítás új emisszióját, a korábban kiadott érték változatlanságát és két külön példány egymástól független működését.
- [ ] T08.05 – Compose-teszttel ellenőrizd az ütés hozzáadását/törlését/hangsúlyváltását és a hangolásszerkesztő hangszer-/hangváltását mesterséges újrarajzolási jelzés nélkül; ellenőrizd az újralétrehozási viselkedést is.

## 09 – Tizedesbevitel, formázás és felhasználói szövegek

Követelmények: REQ-08, REQ-10. Függőség: 05–08. fázis.

- [ ] T09.01 – Készíts közösen használt, teljes bemenetet ellenőrző tizedesparser-t: fogadja el a `25.5` és `25,5` alakot, de utasítsa el a vegyes/ezreselválasztós kétértelmű alakot, a nem véges és nem pozitív adatot.
- [ ] T09.02 – A kalkulátor használja a parser-t, az ismeretlen hangnevet és hibás számot mentés előtt kezelje; a perzisztálás eredményét várja meg, ne jelezzen idő előtt sikeres mentést.
- [ ] T09.03 – Válaszd külön a húrvastagság numerikus eredményét és annak lokalizált formázását; őrizd meg a jelenlegi képletet, mértékegységeket és a sima acélhúrra érvényes becslés leírását.
- [ ] T09.04 – Vidd string resource-ba a metronóm értesítéseit, az ütés-/ritmusfeliratokat és a megjelenített hibákat; a UI ne mutasson nyers enumnevet vagy backend-kivételszöveget. A kalkulátor súgójában a „refresh button” helyett a tényleges számítási művelet szerepeljen.
- [ ] T09.05 – Készíts magyar/angol locale mátrixot a parserhez és formázóhoz; a számítási tesztek numerikus értéket hasonlítsanak, a globális locale minden teszt után álljon vissza.
- [ ] T09.06 – Ellenőrizd Compose-teszttel a vesszős/pontos mentést, a hibás bevitelt és a visszajelzést; őrizd meg az akadálymentes leírásokat és szemantikai azonosítókat.

## 10 – Audio-életciklus és foreground service

Követelmények: REQ-09, REQ-01, REQ-02, REQ-10. Függőség: 02–03., 07–09. fázis.

- [ ] T10.01 – Vezérelt késleltetett inicializálással reprodukáld az indítás közbeni leállítás/képernyőelhagyás esetét; készíts tesztet arra, hogy a leállítás után nem indulhat el mikrofonrögzítés.
- [ ] T10.02 – A hangoló repositoryk indítását/leállítását egyértelműen tulajdonolt és összehangolt munkák kezeljék; töröld az indításra váró munkát akkor is, ha az audioobjektum még null.
- [ ] T10.03 – A PCM-író munka leállítása és az `AudioTrack` felszabadítása legyen rendezett; teszt igazolja, hogy a felszabadítás után nincs írás, és az újraindítás sikerül.
- [ ] T10.04 – Tedd biztonságossá az ismételt start/stop hívásokat és a sikertelen audioinicializálás/írás kezelését; funkciónként csak egy aktív feldolgozó maradjon és hiba után is lehessen újraindítani.
- [ ] T10.05 – Ellenőrizd a `TunerRoute` életciklus- és engedélykezelését: háttérbe helyezés, másik lapra navigálás és engedélyvesztés állítsa le a hangoló audiofolyamatait; a referenciahang mikrofonengedély nélkül is induljon.
- [ ] T10.06 – A metronóm szolgáltatása birtokolja és állítsa le a lejátszó munkát: Stop, szolgáltatásmegszűnés és ismételt START esetén ne maradjon árva coroutine vagy foglalt hangerőforrás. A megszüntetett hangkimenet szükség esetén újra létrehozható legyen.
- [ ] T10.07 – Rendezd a Stop utáni foreground értesítés és szolgáltatás lezárását, továbbá a null intenttel történő újraindulás állapotát; közben a szándékos háttérlejátszás maradjon működőképes.
- [ ] T10.08 – Tartsd meg a metronóm értesítésének navigációs kompatibilitását; activity-/szolgáltatásintegrációs teszt ellenőrizze a visszatérést és a Stop műveletet, a meglévő navigációs azonosítók megőrzésével.
- [ ] T10.09 – Futtasd a start/stop és életciklus teszteket API 28 és egy modern API-szintű eszközön/emulátoron; fizikai eszközön külön ellenőrizd a mikrofon tényleges leállását, a referenciahang frekvenciáját és a metronóm hallhatóságát/tempóját. A hiányzó eszközös ellenőrzést ne pipáld ki.

## 11 – Összesített ellenőrzés és dokumentáció lezárása

Követelmények: REQ-01–REQ-10. Függőség: a 00–10. fázis szükséges javításai.

- [ ] T11.01 – Ellenőrizd, hogy minden követelményhez tartozik megvalósítás és célzott bizonyíték; a production logikát másoló teszteket ne tekintsd regressziós bizonyítéknak.
- [ ] T11.02 – Futtasd a `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug` parancsot a teljes javított állapoton; jegyezd fel a tesztszámokat és a lint eredményét. Ne kapcsold ki a lintet, és az újonnan érintett figyelmeztetéseket kezeld.
- [ ] T11.03 – Ébren lévő, feloldott eszközön/emulátoron futtasd a `.\gradlew.bat :app:connectedDebugAndroidTest` parancsot. Több eszköznél állítsd az `ANDROID_SERIAL` változót; a tesztek izolált adatbázist használjanak.
- [ ] T11.04 – Ellenőrizd az öt lapot, navigációt, activity-újralétrehozást, azonos hangú saját hangolást, régi adatbázis frissítését, beállításmegőrzést, értesítésből visszatérést és a kis/fekvő ablakban való használhatóságot.
- [ ] T11.05 – Rögzítsd a fizikai eszközös frekvencia-/tempómérés módszerét, eredményét, eszközét és API-szintjét; a számításos/virtuális idejű tesztektől külön kezeld a hardveres tapasztalatokat és korlátokat.
- [ ] T11.06 – Frissítsd a README és a `.codex/AGENTS.md` ténylegesen megváltozott működés-/séma-/tesztinformációit. A `.codex/COMPOSE_MIGRATION.md` történeti eredményeit őrizd meg; az új javítások ellenőrzési adatait külön rögzítsd.
- [ ] T11.07 – Ellenőrizd a dokumentumlinkeket és a munkafa diffjét: ne maradjon véletlen változás, helyi SDK-útvonal vagy kézzel szerkesztett buildkimenet. Csak bizonyítékkal elkészült feladat legyen kipipálva.
- [ ] T11.08 – Készíts átadási összesítést a javításokról, migrációs korlátokról, ténylegesen lefutott tesztekről és esetleges nyitott ellenőrzésekről; befejezetlen eszközteszt mellett ne állíts teljes körű validációt.

## Követelmények és feladatok kapcsolata

| Követelmény | Elsődleges fázisok | Kiegészítő / végső ellenőrzés |
| --- | --- | --- |
| REQ-01 – referenciahang | 01, 02 | 10, 11 |
| REQ-02 – metronóm | 01, 03 | 10, 11 |
| REQ-03 – húrok és sorrend | 04, 05 | 08, 11 |
| REQ-04 – Flow és beállításmentés | 01, 06 | 07, 11 |
| REQ-05 – migráció és atomikusság | 04, 05, 06 | 09, 11 |
| REQ-06 – hangfelismerési visszajelzés | 07 | 09, 10, 11 |
| REQ-07 – UI-állapotok | 03, 08 | 11 |
| REQ-08 – számbevitel és szövegek | 09 | 11 |
| REQ-09 – audio-életciklus | 01, 10 | 02, 03, 11 |
| REQ-10 – regressziós bizonyítékok | valamennyi fázis | 11 |

## Eredménynapló

Implementációs eredmény még nincs. Az analízis kiindulási futásai a requirements.md elején szerepelnek; nem helyettesítik a javítások utáni ellenőrzést.

Minden tényleges ellenőrzéshez rögzítendő: dátum, feladatazonosító, parancs vagy mérési módszer, környezet/eszköz/API/locale, eredmény, riport útvonala és esetleges korlát. Környezeti hiba vagy hiányzó eszköz esetén az érintett feladat maradjon nyitott.
