# AlexiGuitarTuner – hibajavítási követelmények

Dátum: 2026-10-25. Állapot: tervezett munka, az itt leírt javítások még nincsenek implementálva.

Ez a dokumentum a projekt Markdown-dokumentációja, forráskódja, tesztjei és mellékelt Room-adatbázisa alapján készült analízis végrehajtható követelményeit tartalmazza. A lépésenkénti végrehajtási terv a [tasks.md](tasks.md). A repository útmutatója az [AGENTS.md](AGENTS.md), a korábbi Compose-migráció történeti leírása a [COMPOSE_MIGRATION.md](COMPOSE_MIGRATION.md).

## Kiindulási állapot és bizonyítékok

- Egy alkalmazásmodul, Kotlin, egy `ComponentActivity`, Jetpack Compose, Material 3 és öt navigációs célpont.
- Funkciók: hangoló, metronóm, akkordtábla, húrvastagság-kalkulátor és beállítások; Hilt, Room, coroutines/Flow és a helyi TarsosDSP JAR használatával.
- A mellékelt adatbázisban 6 hangszertípus, 9 hangolás, 108 hangmagasság, 72 akkord és 108 akkordfogás található. Az ukuleléhez nincs akkordadat; ez jelenleg tartalmi korlát, nem javítandó programhiba.
- Az analízis során ténylegesen lefutott: `.\gradlew.bat :app:testDebugUnitTest :app:lintDebug --offline`. Eredmény: 39 sikeres unit teszt, 0 hiba/kihagyás; lint: 0 hiba, 61 figyelmeztetés.
- A forrásban 36 eszközteszt található: 23 DAO-, 10 képernyő- és 3 activity-szintű teszt. Ezeket az analízis során nem futtattuk újra. A Compose-migráció dokumentumában szereplő korábbi sikeres futások történeti adatok.
- A referenciahang sztereó mintasorának külön számításos vizsgálatában a kért 440 Hz helyett 880 Hz adódott. Fizikai eszközös hangmérés nem történt.
- A mellékelt adatbázis kizárólag memóriabeli másolatán hat azonos frekvenciájú húr mentése egyetlen kapcsolatrekordot eredményezett. Az eredeti adatbázis nem változott.
- A metronóm ütemközhibája a ciklus képletéből következik. A hallható kattintás minősége, a hardveres időzítési szórás és az audio-életciklus versenyhelyzetei külön eszközös ellenőrzést igényelnek.

Prioritások: **P1** = központi funkció vagy felhasználói adatok helyessége; **P2** = állapotkezelés, életciklus, használhatóság és a javítások tartósságát biztosító tesztelés. A prioritás nem helyettesíti a tasks.md függőségi sorrendjét.

A forráslinkek a `app/src/main/java/com/ihegyi/alexiguitartuner/` alatti fájlokra mutatnak, kivéve ahol más útvonal szerepel.

## REQ-01 – A referenciahang frekvenciája és PCM-formátuma helyes legyen

**Prioritás:** P1. **Minősítés:** számítással megerősített funkcionális hiba.

**Érintett forrás:** [PitchGenerationRepositoryImpl.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/feat_tuner/data/PitchGenerationRepositoryImpl.kt), `PitchGenerationRepository`, `TunerViewModel`.

**Probléma:** a sztereó kimenet egycsatornásként léptetett mintasort kap, ezért a csatornánkénti frekvencia kétszereződik. A minimális puffer bájtban kapott mérete és a `ShortArray` elemszáma sincs világosan elválasztva.

**Követelmények és elfogadási feltételek:**

- A javítás elsődleges megoldása mono, 44 100 Hz-es, 16 bites PCM-kimenet; a fázis hangmintánként egyszer lépjen előre. Sztereó csak indokolt esetben maradjon, azonos minták csatornánkénti helyes összefűzésével.
- A puffer bájtmérete, mintaszáma és képkockaszáma külön és konzisztensen legyen kezelve; a hibás minimális pufferméret és sikertelen inicializálás kezelhető hibát eredményezzen.
- A tényleges implementáció által előállított PCM-mintákon 82,41 Hz, 329,6276 Hz és 440 Hz esetén a mért relatív frekvenciahiba legfeljebb 0,1% legyen. A teszt módszere és felbontása legyen megfelelő ehhez a tűréshez.
- A fázis több egymás utáni puffer között is folytonos legyen. A leállítás, frekvenciaváltás és az azonos gombbal történő ki-/bekapcsolás maradjon működőképes.
- Az erőforrások biztonságos használatát a REQ-09 szerint kell megoldani; fizikai eszközön is igazolni kell, hogy nincs oktávhiba.

## REQ-02 – A metronóm tartsa a beállított tempót

**Prioritás:** P1. **Minősítés:** a forrásból kimutatható időzítési hiba; a hang megszakításának hatása eszközön ellenőrizendő.

**Érintett forrás:** [MetronomeRepositoryImpl.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/feat_metronome/data/MetronomeRepositoryImpl.kt), `MetronomeState`, `Beat`, `Rhythm`, `MetronomeService`.

**Probléma:** a ciklus a saját végrehajtási idején túl további 50 ms-ot levon; 120 BPM negyedeknél 500 helyett körülbelül 450 ms-os ciklust céloz. A hangot az indítás után azonnal leállítja.

**Követelmények és elfogadási feltételek:**

- Az ütések kezdete közötti célidő `60 000 / (BPM × ritmusosztás)` ms legyen, ahol a ritmusosztás negyednél 1, nyolcadnál 2, tizenhatodnál 4.
- Az időzítés monoton időalapból és következő határidőből számoljon; a feldolgozási idő és a kerekítés ne okozzon folyamatosan halmozódó eltérést. A hang hosszát nem szabad az ütemközből még egyszer levonni.
- A kattintás ne álljon le közvetlenül az indítás után. A hanghossz kezelése a következő ütést se tolja el; a néma ütés ténylegesen néma legyen.
- Determinisztikus teszt ellenőrizze a 40, 100, 120 és 200 BPM értékeket mindhárom ritmussal. Példák: 120 BPM/negyed = 500 ms; 200 BPM/tizenhatod = 75 ms.
- Injektált virtuális idővel 60 másodperces vizsgálatban az ütemkezdések eltérése az ideális határidőktől legfeljebb 1 ms legyen; a teszt ne valódi várakozásokkal mérjen.
- A tempó- és ritmusváltás működése lejátszás közben legyen meghatározott és tesztelt. A 3–8 ütéses korlát maradjon meg; hozzáadás, törlés és hangsúlyváltás közben ne legyen érvénytelen listaindex vagy több lejátszó ciklus.

## REQ-03 – Minden húr külön, sorrenddel együtt tárolható legyen

**Prioritás:** P1. **Minősítés:** adatbázis-másolaton megerősített adatmodellezési hiba.

**Érintett forrás:** [PitchTuningCrossRef.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/commons/domain/entities/PitchTuningCrossRef.kt), `PitchTuningCrossRefDAO`, `PitchDAO`, `CreateTuningViewModel`, `ButtonGenerationRepositoryImpl`, `ChordDiagram`, adatbázisasset.

**Probléma:** a `(tuningId, frequency)` kulcs összevonja az azonos hangú húrokat. A modell nem tárol húrsorszámot, a frekvencia szerinti rendezés pedig nem képes minden hangolás fizikai húrsorrendjét leírni.

**Követelmények és elfogadási feltételek:**

- A hangolás minden húrja rendelkezzen a hangoláson belül egyedi húrsorszámmal; az elsődleges azonosítás ne a frekvencia egyediségétől függjön. Azonos frekvencia több húrhoz is tartozhasson.
- Új hangolás mentésekor a húrok száma feleljen meg a kiválasztott hangszernek, a sorrend egyezzen a szerkesztőben megadottal. A mentési bemenetet a művelet elején rögzíteni kell.
- Hat A4-re állított gitárhúr mentése, újraolvasása és a hangoló megnyitása után hat húr és hat referenciagomb maradjon meg.
- Nem monoton frekvenciasorrendű hangolás is változatlan sorrendben térjen vissza. A DAO-k explicit húrsorszám szerinti rendezést használjanak.
- Az akkordrajz és feliratai ugyanazt a fizikai húrsorrendet használják; ne egy külön frekvencia szerinti rendezés párosítsa össze a húrokat a fogásokkal. A beépített gitár/basszus sorrendet és az ukulele visszahajló hangolását külön ellenőrizni kell.
- A módosítás csak a REQ-05 szerinti, tesztelt adatmegőrző migrációval vezethető be.

## REQ-04 – A beállítások és a hangolásválasztó Flow-kezelése legyen kiszámítható

**Prioritás:** P2. **Minősítés:** a forrásban azonosítható gyűjtési és mentési probléma.

**Érintett forrás:** [SelectTuningViewModel.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/commons/presentation/viewmodel/SelectTuningViewModel.kt), [UserSettingsViewModel.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/commons/presentation/viewmodel/UserSettingsViewModel.kt), `TuningDialogs`, `UserSettingsRoute`, beállításrepository/DAO.

**Követelmények és elfogadási feltételek:**

- A külső Flow következő értékének feldolgozását ne akadályozza meg belső, végtelen `collect`. A kapcsolódó adatokat megszakítható, reaktív lánc kapcsolja össze, például `combine`/`flatMapLatest` használatával.
- A dialógus ismételt megnyitása ne indítson egymás mellett újabb, felhalmozódó figyelőket. Egy ViewModelben legfeljebb egy azonos célú aktív megfigyelési lánc legyen, a már szükségtelen munka álljon le.
- A hangolásválasztó újranyitásakor az aktuálisan mentett hangszer és hangolás jelenjen meg; a hangoláslista változásai is frissüljenek. A Mégse ne mentsen választást.
- Egy mentési kérés egyszeri, befejeződő művelet legyen, ne újabb folyamatos beállításfigyelés.
- A jelölési beállítás és a kiválasztott hangolás mentése ne írja felül egymás frissebb értékeit; a kapcsolóállapotok ne vesszenek el egy hangolás kiválasztása miatt.
- A mentés befejezése és hibája megfigyelhető legyen. A UI ne tekintsen sikeresnek egy csak elindított, de még el nem végzett mentést, és ne hagyjon kezeletlen háttérkivételt.
- Ismételt megnyitás/bezárás, gyors választásváltás, mentés utáni visszalépés és activity-újralétrehozás tesztelve legyen.

## REQ-05 – Az adatbázis változásai őrizzék meg a felhasználói adatokat

**Prioritás:** P1. **Minősítés:** jelenlegi migrációs kockázat és tranzakcióhiány; nem igazolt, már megtörtént adatvesztés.

**Érintett forrás:** [DatabaseModule.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/commons/di/DatabaseModule.kt), `AppDatabase`, érintett entitások/DAO-k, `UserSettingsRepositoryImpl`, `SGCRepositoryImpl`, Room- és migrációtesztek.

**Követelmények és elfogadási feltételek:**

- A jelenlegi v1 sémát és assetet a változtatás előtt reprodukálható migrációteszt-alapként meg kell őrizni. A Room sémaexportját be kell kapcsolni, a sémákat verziókezelni kell.
- A húrsorszámos modellhez explicit v1→v2 migráció készüljön. Az alkalmazás ne használjon destruktív migrációs visszaesést a felhasználói adatokat tartalmazó adatbázison.
- A migráció őrizze meg a hangolások azonosítóit, neveit, hangszerkapcsolatait, minden még létező frekvenciakapcsolatot, a beállításokat, a kalkulátor sorait és az akkordadatokat.
- A beépített hangolások fizikai sorrendjét explicit, ellenőrzött megfeleltetés állítsa elő. Régi saját hangolásnál a korábban nem tárolt sorrend és az összevont duplikátumok nem állíthatók biztosan vissza: determinisztikus sorrendet kell választani és a korlátot dokumentálni; hiányzó húrokat nem szabad kitalált adatokkal feltölteni. A hiányos régi hangolás továbbra is legyen betölthető.
- A friss telepítés és a meglévő v1 adatbázis frissítése egyaránt a helyes v2 sémát nyissa meg. Az új asset előállítása legyen reprodukálható, a régi asset maradjon izolált tesztfixture.
- A hangolás és húrai együtt, egy tranzakcióban kerüljenek mentésre. A beszúrás által visszaadott azonosítót kell használni a „legutolsó hangolás” külön lekérdezése helyett.
- A saját hangolások törlése, a kapcsolódó sorok eltávolítása és az alapértelmezett hangolás rendezése alkosson egy tranzakciót. A beépített elemek védelme ne kizárólag a `tuningId > 9` feltételezésen múljon.
- A hangnevek/beállítások összetartozó mentése, valamint a kalkulátorsor törlése/átszámozása legyen atomi. A párhuzamos új sorok létrehozása ne használhasson azonos, felülírást okozó sorszámot.
- A kapcsolatérvényesség és a szükséges indexek legyenek meghatározva. Idegen kulcs bevezetése előtt a régi és a mellékelt adatok érvényességét ellenőrizni kell; hibás adatot nem szabad csendben eldobni.
- Valós Room-adatbázison ellenőrzött migráció, párhuzamos mentés és mesterséges mentési hiba igazolja, hogy nincs részleges mentés vagy adatkeveredés. Teszt nem használhatja vagy törölheti a felhasználó adatbázisát.

## REQ-06 – Az elhangolt húr is kapjon értelmezhető visszajelzést

**Prioritás:** P2. **Minősítés:** megfigyelhető algoritmikus korlát és nem reaktív hangnévlista.

**Érintett forrás:** [PitchDetectionRepositoryImpl.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/feat_tuner/data/PitchDetectionRepositoryImpl.kt), `TunerViewModel`, `TunerScreen`, `PitchDAO`, jelölési beállítások.

**Követelmények és elfogadási feltételek:**

- Érvényes, támogatott tartománybeli frekvenciánál a legközelebbi hangot kell meghatározni a jelenlegi ±0,4 Hz-es névkeresési kapu helyett.
- Az eltérés zenei mértékegységben, centben számolódjon: `1200 × log2(mért frekvencia / referenciafrekvencia)`. Az alacsony/magas/pontos állapot alapértelmezett határa ±5 cent; a határértékek befoglalása legyen egyértelmű és tesztelt.
- Félhangfelező határnál determinisztikus legyen a választás, például az alacsonyabb hang nyerjen. A támogatott tartományon kívüli érték ne kapjon félrevezető, levágott hangnevet.
- Nulla, negatív, nem véges vagy a detektor által nem hangként jelölt eredmény „nincs érvényes hang” állapotot adjon, ne negatív Hz-et vagy régi mérésből megmaradt pontos hangot.
- A sharp/flat és angol/német jelölés módosítása újraindítás nélkül frissítse a felismert hang, a referenciagombok és az érintett hangválasztók feliratait. A gyorsítótár ne tartson régi neveket.
- Valós gitárhangokon, több oktávban, legalább ±10/±25 cent eltérésekkel is legyen visszajelzés; külön tesztelje a hangválasztást és a pontossági állapotot.
- A mérési állapot strukturált adat legyen; a lokalizált „alacsonyabb/magasabb” szöveg a UI-ban készüljön el, ne a repository angol szövegéből.

## REQ-07 – A UI-állapotok módosítása mindig észlelhető és izolált legyen

**Prioritás:** P2. **Minősítés:** a forrásban látható technikai adósság és elmaradó Flow-emisszió.

**Érintett forrás:** `MetronomeState`, `MetronomeRepositoryImpl`, `CreateTuningUIState`, `CreateTuningViewModel`, `MetronomeRoute`, `CreateTuningSheet`.

**Követelmények és elfogadási feltételek:**

- A megfigyelt állapotokban a listák kívülről ne legyenek módosíthatók. Ütés-/hanglista változtatásakor új lista és új állapotérték készüljön; a korábbi állapot változatlan maradjon.
- A `MutableStateFlow.update` függvénye ne végezzen a korábbi értéket módosító mellékhatást. A kezdeti állapot ne osszon meg módosítható listát külön ViewModel-/repository-példányok között.
- A létrehozó dialógus kezdeti húrszáma és későbbi hangszer-/hangválasztása is konzisztens legyen; üres adatforrás kezelése ne `first()`/`!!` hibával történjen.
- A UI a normál állapotfigyelésből frissüljön; az érintett kézi `revision` számlálók és kikényszerített újrakomponálások eltávolíthatók legyenek.
- Állapotfolyam-teszt igazolja az új emissziót, a régi érték változatlanságát és a példányok elkülönülését. Compose-teszt ellenőrizze a tényleges felületi frissülést.

## REQ-08 – A számbevitel és a felhasználói szövegek legyenek következetesek

**Prioritás:** P2. **Minősítés:** a forrásban azonosított lokalizációs és hordozhatósági probléma.

**Érintett forrás:** [SGCScreen.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/feat_sgc/presentation/ui/SGCScreen.kt), `CalculateStringGaugeUseCase`, `MetronomeScreen`, `MetronomeService`, `strings.xml`, érintett tesztek.

**Követelmények és elfogadási feltételek:**

- A kalkulátor egyszerű tizedes számként fogadja el a pontot és a vesszőt is (`25.5`, `25,5`). Az ezreselválasztós vagy vegyes elválasztós kétértelmű bevitel legyen elutasítva; a teljes bemenetet kell ellenőrizni.
- Csak pozitív, véges menzúra és húrfeszítés menthető. Az ismeretlen hangnév, üres vagy hibás érték lokalizálható visszajelzést adjon és ne mentsen részleges változtatást.
- A számszerű számítás maradjon független a megjelenítési lokalizációtól. Az eredmény a kiválasztott eszköznyelv szabályai szerint jelenjen meg; a jelenlegi inch/pound mértékegységek és a sima acélhúrra vonatkozó becslési korlát maradjon világos.
- A felhasználónak megjelenő értesítésszövegek, hibák, ütéshangsúlyok és ritmusnevek Android string resource-ból származzanak; ne nyers enumnevek vagy repository-kivételszövegek jelenjenek meg.
- A kalkulátor súgója a tényleges „Calculate” műveletet írja le; az elavult „refresh button” megfogalmazás javítandó.
- Magyar és angol locale alatt is legyen parser-/formázóteszt. A numerikus tesztek numerikus eredményt ellenőrizzenek; a globális locale-t változtató teszt állítsa vissza az eredeti értéket.
- A meglévő akadálymentes leírások és szemantikai tesztazonosítók működjenek tovább. Teljes magyar felületfordítás nem része ennek a javításnak.

## REQ-09 – Az audiofolyamatok és a szolgáltatás biztonságosan álljanak le

**Prioritás:** P2. **Minősítés:** a forrásban látható életciklus-/erőforrás-kockázat; a versenyhelyzetek reprodukciója szükséges.

**Érintett forrás:** `PitchDetectionRepositoryImpl`, `PitchGenerationRepositoryImpl`, `TunerRoute`, `MetronomeRepositoryImpl`, [MetronomeService.kt](../app/src/main/java/com/ihegyi/alexiguitartuner/feat_metronome/data/service/MetronomeService.kt).

**Követelmények és elfogadási feltételek:**

- Rögzítésből, referenciagenerálásból és metronómlejátszásból funkciónként legfeljebb egy aktív feldolgozó fusson; az ismételt indítás/leállítás legyen biztonságos.
- A hangoló leállítása az indításra váró munkát is törölje akkor is, ha az `audioDispatcher` még nem jött létre. A képernyőről kilépés vagy engedélyvesztés után ne indulhasson késleltetve mikrofonrögzítés.
- A feldolgozó és az erőforrás felszabadítása legyen összehangolt: ne írhasson coroutine egy már felszabadított `AudioTrack`-re. Újraindítás után az erőforrás ismét használható legyen, a UI-szálat ne blokkolja várakozás.
- Az audioinicializálás és -írás hibája ne hagyjon beragadt lejátszási állapotot vagy kezeletlen háttérkivételt.
- A hangoló mikrofonkezelése maradjon engedély- és életciklusfüggő. A referenciahang mikrofonengedély nélkül is működjön.
- A metronóm szándékosan fusson tovább háttérben foreground service-ként; a hangoló képernyőhöz kötött leállítását nem szabad automatikusan rá alkalmazni. A szolgáltatás Stop művelete és megszűnése azonban állítsa le a lejátszót és rendezze a foglalt hangerőforrást/értesítést.
- A `res/navigation/bottom_nav.xml` azonosítói és a `MainActivity` értesítési navigációja maradjon kompatibilis, vagy kapjon azonos működést igazoló explicit migrációt.
- Automatizált versenyhelyzetteszt, valamint API 28 és egy modern API-szintű eszköz/emulátor ellenőrizze az indítás/leállítás, háttérbe helyezés, engedélymegtagadás, újranyitás és értesítésből történő visszatérés eseteit. A tényleges hangminőséget és mikrofonműködést fizikai eszközön is ellenőrizni kell.

## REQ-10 – A tesztek a javított éles logikát ellenőrizzék

**Prioritás:** P2, minden javítás elfogadásának feltétele. **Minősítés:** a tesztforrások alapján azonosított lefedettségi hiány.

**Érintett forrás:** `app/src/test`, `app/src/androidTest`, build-/tesztkonfiguráció, projekt-dokumentáció.

**Követelmények és elfogadási feltételek:**

- A hangfelismerés frekvencia-/hangnévlogikáját ne a production kódot lemásoló fake tesztelje. Az éles implementáció által használt algoritmust, a valódi állapotkezelést és az injektált eszköz-/időabsztrakciókat kell ellenőrizni.
- A fake repositoryk küldjenek későbbi Flow-változásokat is, ahol az éles Room-forrás ezt teszi; egyetlen `flow { emit(...) }` ne rejtse el a megfigyelési hibákat.
- A frekvenciakimenet, időzítés, azonos hangú húrok, húrsorrend, migráció, tranzakció-visszagörgetés, ismételt dialógusnyitás, jelölésfrissítés, locale-kezelés és audioleállítás kapjon célzott regressziós tesztet.
- A hibát váró tesztek akkor is bukjanak meg, ha a várt kivétel egyáltalán nem következik be. A közös módosítható állapot és a gép alapértelmezett locale-ja ne befolyásolja a futások sorrendfüggetlenségét.
- Megmaradjon a képernyők, navigáció, újralétrehozás, hangolásmentés és értesítési útvonal end-to-end lefedettsége; a képernyőtesztek állapotra szinkronizáljanak, ne önkényes alvásokra.
- A végső ellenőrzés tartalmazza az `assembleDebug`, `testDebugUnitTest`, `lintDebug`, valamint rendelkezésre álló megfelelő eszközön a `connectedDebugAndroidTest` futtatását. A lint ne legyen kikapcsolva; az érintett kódban ne maradjon új, feloldatlan figyelmeztetés. Az összes régi dependency-/resource-figyelmeztetés megszüntetése nem cél.
- Az eredményeket dátummal, tesztszámmal, környezettel és korlátokkal kell rögzíteni. Nem futtatott vagy környezeti okból elbukó ellenőrzés nem jelölhető sikeresnek.
- A README, a repository útmutató és a tasks.md a megvalósult állapotot tükrözze. A Compose-migráció korábbi ellenőrzési adatait meg kell őrizni, az új eredmények külön kerüljenek rögzítésre.

## Tervezési keretek és kizárások

- Megmarad az egyetlen Android-modul, a Compose/Material 3 felület, a meglévő funkciók és a Hilt/Room tesztinfrastruktúra. Nem cél a teljes domainréteg platformfüggetlen újraírása vagy új UI-architektúra bevezetése.
- A ViewModel-, repository-, service- és adatbázis-módosítások a fenti konkrét javítások részeként indokoltak; a korábbi Compose-migráció backendmegőrzési megkötése a történeti migrációra vonatkozott.
- Nincs automatikus függőség-/SDK-frissítés, új akkordadatbázis, új mértékegységrendszer, teljes fordítás vagy általános kódformázás.
- A helyi TarsosDSP JAR-t és a felhasználó meglévő módosításait meg kell őrizni. A `build/`, `app/build/`, `.gradle/` és `.kotlin/` generált tartalma nem szerkesztendő kézzel.
- A mostani dokumentációs munka nem hajtja végre a javításokat. A tasks.md jelölőnégyzetei az implementáció során, az adott feladat eredményének ellenőrzése után pipálhatók ki.
