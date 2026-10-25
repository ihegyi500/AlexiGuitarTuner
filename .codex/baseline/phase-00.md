# 00 – Kiindulási állapot és regressziós esetek

Dátum: 2026-10-25 (Europe/Budapest). Hatókör: kizárólag a tasks.md 00. fázisa, T00.01–T00.05. A későbbi hibajavítások és új regressziós tesztek még nem készültek el.

## Bizonyítékok és feladatok

| Feladat | Elvégzett munka | Bizonyíték |
| --- | --- | --- |
| T00.01 | A gyökér és a `.codex` repository-útmutatója, a requirements/tasks és a Compose-migrációs dokumentum elolvasva; kezdeti munkafa ellenőrizve. | A munkafa tiszta volt; alapcommit: `6f2d206ae0e6229babfa15830e34db0568550043`. A dokumentumok induló SHA-256 értékei a snapshotban szerepelnek. |
| T00.02 | JDK/SDK/wrapper ellenőrzése; a teljes unit/lint alapellenőrzés egyszeri, tényleges újrafuttatása. | [Normalizált Gradle-napló](phase-00-gradle.log), [gépi eredményösszesítés](phase-00.validation.json). |
| T00.03 | A hét szükséges regressziós eset rögzítve bemenettel, lépésekkel, jelenlegi bizonyítékkal és javítás utáni elvárással. | A lent szereplő RC00-01–RC00-07 esetek; két SQL-es reprodukció a snapshotban. |
| T00.04 | A négy audio-életciklus kockázathoz külön reprodukciós forgatókönyv készült, a bizonyítottsági korlát feltüntetésével. | AR00-01–AR00-04; egyik sem szerepel már igazolt hardveres hibaként. |
| T00.05 | A v1 adatbázis verziója, leltára, SQL-szerkezete, integritása, kapcsolatai és megőrzendő adatai rögzítve. | [Snapshot és fájl-ellenőrzőösszegek](phase-00.snapshot.json); az asset változatlan. |

A jegyzőkönyv a [követelményekhez](../requirements.md) és a [feladatlistához](../tasks.md) tartozik. A JSON-ban az időbélyegek UTC szerint szerepelnek. A napló gépfüggő abszolút útvonalai helyettesítőkre vannak normalizálva; a futási eredmények nem módosultak.

## Környezet és ténylegesen lefutott ellenőrzés

| Elem | Ellenőrzött állapot |
| --- | --- |
| Operációs rendszer | Windows 11, amd64 |
| Launcher és daemon JDK | JetBrains JBR 17.0.14+1-b1367.22; `JAVA_HOME/bin/java.exe` működik |
| Gradle wrapper | 9.3.1; a repository wrapperével a `--version` és a feladatok sikeresek |
| Alkalmazás buildkonfigurációja | AGP 9.1.1, Compose compiler plugin 2.2.10, KSP 2.3.12 |
| Android SDK | Compile platform 37.0 telepítve; a build fel tudta használni; min/target SDK 28/35 |
| Telepített további platformok | 34 és 35 |
| Telepített build-tools | 33.0.1, 35.0.0, 35.0.1, 36.0.0 |
| Verzióellenőrzés megjegyzése | A Gradle saját beágyazott Kotlin-verziója 2.2.21; ez külön adat az alkalmazás dokumentált/konfigurált Kotlin-/Compose-toolchainjétől. |

A repository gyökeréből futtatott parancsok:

```powershell
.\gradlew.bat --version
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug --offline --rerun-tasks --console=plain
```

A `--rerun-tasks` biztosította, hogy az alapellenőrzés ne csak egy régi, naprakésznek ítélt tesztjelentést használjon. Egyetlen unit/lint futás történt: **BUILD SUCCESSFUL**, kilépési kód 0, időtartam **2m 2s**, **41 végrehajtott feladat**. Az új unit XML-ek UTC-időbélyege 2026-10-25T19:21:43–19:21:44 közötti; a teljes értékek a gépi összesítésben vannak.

| Mérőszám | Korábbi analízis | Mostani újrafuttatás | Eltérés |
| --- | --- | --- | --- |
| Unit tesztek | 39 sikeres | 39 sikeres, 0 failure/error/skip, 8 tesztosztály | 0 |
| Lint hibák | 0 | 0 | 0 |
| Lint figyelmeztetések | 61 | 61 | 0 |

Nincs magyarázandó darabszámeltérés; a forrás és a buildkonfiguráció nem változott. A sikeres alapellenőrzés nem bizonyítja az ismert hibák hiányát, mert azok célzott tesztjei a későbbi fázisokban készülnek el.

A mostani lintfigyelmeztetések megoszlása:

| Kategória | Darab |
| --- | --- |
| UnusedResources | 42 |
| GradleDependency | 5 |
| NewerVersionAvailable | 8 |
| MonochromeLauncherIcon | 2 |
| ObsoleteSdkInt | 2 |
| OldTargetApi | 1 |
| DefaultLocale | 1 |
| Összesen | 61 |

A teljes újrafordítás ezen felül meglévő Room `QUERY_MISMATCH` figyelmeztetést jelzett a két PitchDAO joinnál, elavult `fallbackToDestructiveMigration()` hívást, valamint Gradle 10-zel nem kompatibilis elavult Gradle-funkciókat. Ezek nem további lint-hibák, és ebben a fázisban nem javítottuk őket.

Helyi részletes riportok: [unit HTML](../../app/build/reports/tests/testDebugUnitTest/index.html), [lint HTML](../../app/build/reports/lint-results-debug.html), [lint XML](../../app/build/reports/lint-results-debug.xml). Ezek buildkimenetek, egy későbbi futás felülírhatja őket; a fázis naplója, darabszámai és az XML-ek ellenőrzőösszegei külön megmaradnak.

Nem futott `connectedDebugAndroidTest`, önálló `assembleDebug` APK-feladat, fizikai hangmérés vagy új, későbbi fázishoz tartozó regressziós teszt. A lint AndroidTest-elemzése nem jelent eszközteszt-végrehajtást.

## Regressziós esetlista

A „jelenlegi bizonyíték” a mostani forrásra vagy külön jelzett memóriabeli SQL-re vonatkozik. A jövőbeli automatizált regressziós teszteket ez a lista meghatározza, de nem helyettesíti.

### RC00-01 – A referenciahang oktávhibája

- Követelmény/feladat: REQ-01; T01.01, T02.01–T02.06.
- Bemenet: 44 100 Hz, PCM 16 bit; először 440 Hz, majd 82,41 Hz és 329,6276 Hz kérés, több egymás utáni pufferrel.
- Reprodukció: a production mintaképzőt a későbbi leválasztás után valódi hangkimenet nélkül futtatni; a kimeneti formátum szerinti csatornabontás után a minták frekvenciáját megfelelő felbontással mérni.
- Jelenlegi bizonyíték: a [generátor](../../app/src/main/java/com/ihegyi/alexiguitartuner/feat_tuner/data/PitchGenerationRepositoryImpl.kt) sztereó, a fázis viszont minden tömbelemnél lép. Csatornánként minden második minta kerül felhasználásra: a forrásból levezetett kimenet 440 Hz helyett 880 Hz. Ebben a fázisban nem futott közvetlen AudioTrack- vagy fizikai mérés.
- Javítás utáni elvárás: mindhárom hang relatív hibája legfeljebb 0,1%, pufferhatáron folytonos fázis, működő váltás/Stop és mikrofonengedély nélküli referenciahang.

### RC00-02 – Metronóm, 120 BPM és negyedek

- Követelmény/feladat: REQ-02; T01.02, T03.01–T03.03, T03.07.
- Bemenet: 120 BPM, negyedritmus, legalább négy ütés; a hangkimenet rövid, vezérelt feldolgozási idővel működik.
- Reprodukció: a későbbi injektált virtuális idővel rögzíteni az ütéskezdeteket; összehasonlítani a szomszédos kezdőidőpontok különbségét és a 60 másodperces határidőket.
- Jelenlegi bizonyíték: a [metronóm](../../app/src/main/java/com/ihegyi/alexiguitartuner/feat_metronome/data/MetronomeRepositoryImpl.kt) képlete `time + (500 - time - 50)`, tehát a célciklus 450 ms, nem 500 ms, amíg a késleltetés pozitív. Ez statikus levezetés, nem megmért hangszóró-időzítés.
- Javítás utáni elvárás: 500 ms-os ütemköz; 60 másodperces virtuális vizsgálatban legfeljebb 1 ms határidőeltérés, halmozódó drift nélkül.

### RC00-03 – Metronóm, 200 BPM és tizenhatodok

- Követelmény/feladat: REQ-02; T03.01–T03.03, T03.07.
- Bemenet: 200 BPM, tizenhatodritmus, ritmusosztás 4.
- Reprodukció: az RC00-02 virtuális idejű ellenőrzését ezen a rövid ütemközön is végrehajtani; néma/hangsúlyos ütésekkel és lejátszás közbeni tempóváltással kiegészíteni.
- Jelenlegi bizonyíték: az ideális `60000 / (200 × 4) = 75 ms`; a forrás a ciklus feldolgozási idején túl 50 ms-ot levon, így 25 ms-os célciklust képez. Az azonnali `stopTone()` hallhatósági hatása külön eszközön ellenőrizendő.
- Javítás utáni elvárás: 75 ms-os ütemköz, folyamatos határidők, a következő ütést nem eltoló kattintás és valóban néma néma ütés.

### RC00-04 – Hat azonos hangú húr

- Követelmény/feladat: REQ-03, REQ-05; T04.03–T04.10, T05.02–T05.03, T05.07.
- Bemenet: új hathúros gitárhangolás, hat darab A4/440 Hz húr, külön fizikai húrsorszámokkal.
- Reprodukció: izolált adatbázisban hat beszúrás, hangolás újraolvasása, majd a kiválasztott hangolás megnyitása a hangolóban. A későbbi teszt a valódi Room-/repository-mentési utat is fedje le.
- Most ténylegesen végrehajtott rész: a v1 asset memóriabeli másolatán a production DAO-val egyező `INSERT OR REPLACE` hat alkalommal futott. **1 kapcsolat és 1 joinnal visszaolvasott hang** maradt; a 440 Hz-es Pitch sor létezik.
- Ok: a [kapcsolótábla](../../app/src/main/java/com/ihegyi/alexiguitartuner/commons/domain/entities/PitchTuningCrossRef.kt) kulcsa `(tuningId, frequency)`.
- Javítás utáni elvárás: 6 külön sorszámozott rekord, 6 visszaolvasott húr és 6 referenciagomb; újranyitás és újralétrehozás után is.

### RC00-05 – Nem monoton húrsorrend

- Követelmény/feladat: REQ-03; T04.02, T04.06, T05.03–T05.04, T05.07.
- Bemenet: saját négyhúros hangolás `A4, C4, E4, G4` sorrendben. Az ismétlődés hibájától függetlenül, négy külön frekvenciával kell vizsgálni.
- Most ténylegesen végrehajtott rész: memóriabeli assetmásolaton a megadott sorrendű beszúrás után a jelenlegi [PitchDAO](../../app/src/main/java/com/ihegyi/alexiguitartuner/commons/data/db/dao/PitchDAO.kt) joinja `C4, E4, G4, A4` sorrendet adott. `ORDER BY` nélkül ez csak megfigyelt visszatérési sorrend, nem SQL-garancia. A v1 modell eleve nem tárolja az eredeti húrsorszámot.
- Javítás utáni elvárás: pontosan a megadott `A4, C4, E4, G4` sorrend térjen vissza húrsorszám szerinti explicit rendezéssel. Külön ellenőrizendő a beépített gitár/basszus sorrend és az ukulele fizikai visszahajló hangolása; az akkordrajz felirata se végezzen önálló frekvenciarendezést.

### RC00-06 – A hangolásválasztó ismételt megnyitása

- Követelmény/feladat: REQ-04; T01.03, T06.01–T06.03, T06.07–T06.08.
- Előkészítés: a későbbi fake adjon több értéket kibocsátó settings-, instrument- és tuning-Flow-t; legyen számolható az aktív feliratkozás és annak leállítása.
- Lépések: Beállítások → hangolásválasztó → Mégse, ezt 30-szor ismételni; közben a mentett hangolást és a választható listát külső emisszióval megváltoztatni. Külön esetben választás és OK után újranyitni, illetve kapcsolóváltással együtt menteni.
- Jelenlegi bizonyíték: a [dialógus](../../app/src/main/java/com/ihegyi/alexiguitartuner/commons/presentation/ui/TuningDialogs.kt) megnyitása `initTuningList()` hívást indít; a [ViewModel](../../app/src/main/java/com/ihegyi/alexiguitartuner/commons/presentation/viewmodel/SelectTuningViewModel.kt) minden hívásra új `viewModelScope.launch` munkát hoz létre, belül tartósan egymásba ágyazott gyűjtéssel. A feliratkozásszámot és a 30-szoros UI-forgatókönyvet ebben a fázisban nem mértük meg.
- Javítás utáni elvárás: legfeljebb egy azonos célú aktív figyelési lánc, a szükségtelen munka leállítása; a legfrissebb mentett választás és listák látszanak; Mégse nem ment, hangolásmentés nem írja felül a frissebb kapcsolóértéket.

### RC00-07 – Tizedesvesszős bevitel

- Követelmény/feladat: REQ-08; T09.01–T09.03, T09.05–T09.06.
- Előkészítés: izolált kalkulátorsor érvényes E2 hanggal; magyar, majd angol locale.
- Lépések: menzúra `25,5`, feszítés `18,5`, Calculate; ismétlés `25.5` és `18.5` adatokkal. Negatív, nulla, üres, NaN/Infinity és vegyes `1,234.5` alak külön negatív eset.
- Jelenlegi bizonyíték: a [StringEditor](../../app/src/main/java/com/ihegyi/alexiguitartuner/feat_sgc/presentation/ui/SGCScreen.kt) `toDoubleOrNull()` hívással olvas; a vesszős egyszerű tizedes nem kerül normál számmá alakításra. A UI-forgatókönyv még nincs végrehajtva; az aktuális 39 unit teszt nem fedi ezt az éles bemeneti utat.
- Javítás utáni elvárás: pont és vessző ugyanarra a numerikus értékre vezet, az eredmény locale szerint formázott, hibás bemenet nem ment részleges adatot. A számítási teszt numerikus értéket ellenőriz, a locale-t minden változtató teszt visszaállítja.

## Audio-életciklus: külön reprodukálandó kockázatok

Mind a négy alábbi eset **rögzített, még nem végrehajtott reprodukciós forgatókönyv**. E fázis teljesítése a forgatókönyvek elkészítését jelenti; nem igazolja a hibát és nem zárja le a későbbi T01/T10 tesztfeladatokat.

### AR00-01 – Gyors start/stop

- Feladat: T01.04, T10.02–T10.04. Forrás: `PitchGenerationRepositoryImpl`, `PitchDetectionRepositoryImpl`, `MetronomeRepositoryImpl`.
- Előkészítés/lépések: vezérelhető inicializálás és írás mellett legalább 30 gyors start/stop ciklus; referenciagenerálásnál frekvenciaváltás is, metronómnál ismételt START kérés. Minden esemény rögzítse a munka és az audioobjektum azonosítóját.
- Ellenőrzés: funkciónként legfeljebb egy aktív feldolgozó, felszabadítás után nincs írás, ismételt Stop biztonságos, újraindítás működik, nincs beragadt playing állapot vagy kezeletlen kivétel.
- Kockázat alapja: külön coroutine scope-ok és megosztott audioobjektumok/állapotmezők; a munka megszakítása és az erőforrás felszabadítása nincs egyértelműen sorrendezve. Ebből önmagában nem állítunk már bekövetkezett hardveres hibát.

### AR00-02 – Kilépés az inicializálás befejezése előtt

- Feladat: T10.01–T10.02, T10.05. Forrás: `PitchDetectionRepositoryImpl.startAudioProcessing/stopAudioProcessing`, `TunerRoute.onDispose`.
- Lépések: az indítási coroutine-t az `audioDispatcher` létrehozása előtt megállítani egy tesztkapun; leállítást/képernyőelhagyást kiváltani; ezután az indítási kaput felengedni.
- Ellenőrzés: az elhagyott képernyő ne indítson később mikrofont, a függő munka is törlődjön; megmaradt aktív recorder száma 0.
- Kockázat alapja: a jelenlegi Stop csak nem null `audioDispatcher` mellett törli a scope gyerekmunkáit. A vezérelt versenyhelyzetteszt a későbbi fázisban készül el.

### AR00-03 – Mikrofonengedély elvesztése

- Feladat: T10.04–T10.05, T10.09. Forrás: `TunerRoute` életciklus- és engedélyellenőrzése, detektorrepository.
- Lépések: engedéllyel mérés indítása; háttérbe helyezés, a mikrofonengedély visszavonása, majd visszatérés. Automatikusan vezérelt engedélyforrással és valós eszközbeállítással is ellenőrizni kell; az Android által kezdeményezett folyamat-újraindítást külön kell kezelni.
- Ellenőrzés: engedély nélkül nincs rögzítés vagy késleltetett újraindítás; érthető engedélykérő állapot látszik; a referenciahang továbbra is engedélyezett; új engedély után újra használható a hangoló.
- Minősítés: ellenőrizendő életciklus-/engedélykezelési forgatókönyv, nem bizonyított engedélykezelési hiba.

### AR00-04 – A metronómszolgáltatás leállítása

- Feladat: T10.06–T10.09. Forrás: `MetronomeService`, `MetronomeRepositoryImpl`.
- Lépések: metronóm indítása; háttérbe helyezéskor a szándékos lejátszás fennmaradását ellenőrizni; Stop az értesítésből; külön service-megszüntetés, ismételt START és null intentes újraindulás; végül újraindítás és értesítésből navigáció.
- Ellenőrzés: Stop/megszűnés után nincs árva lejátszó, foglalt hangkimenet vagy indokolatlan értesítés; az alkalmazás visszatéréskor helyes állapotot mutat; a kompatibilitási navigációs azonosítók tovább működnek.
- Kockázat alapja: a repository önálló scope-ban indít munkát, a service nem tartalmaz `onDestroy` takarítást, a ToneGenerator felszabadítása és az értesítés eltávolítása külön figyelmet igényel. A tényleges szolgáltatás-/hangviselkedés ebben a fázisban nem lett reprodukálva.

## A v1 adatbázis kiindulási állapota

- Asset: `app/src/main/assets/alexiGuitarTuner_db.db`, méret **61 440 bájt**.
- SQLite `PRAGMA user_version`: **1**, a Room `AppDatabase` verziója is **1**.
- `PRAGMA integrity_check`: **ok**.
- SHA-256: `54a3c2bbeb290a933bcd6e00a3d9e56aa45a9139b237a60cd7741a3a9b81c9fd`.
- Room identity hash: `7ac2b80da346916dce14eb93d02793a7`.
- A sémában nincs húrsorszám a hangolás–hang kapcsolatban. A kapcsolótábla kulcsa `(tuningId, frequency)`. A kapcsolat-árvaságok külön lekérdezései mind 0-t adtak a mellékelt asseten; ez nem jelenti, hogy a séma idegen kulcsokkal már kikényszeríti az integritást.

| Tábla | Sor | Megőrzendő adatok |
| --- | --- | --- |
| Instrument | 6 | instrumentId, név, húrszám |
| Tuning | 9 | tuningId, név, instrumentId; később a felhasználó minden saját hangolása is |
| Pitch | 108 | frekvencia és aktuális hangnév/jelölés |
| PitchTuningCrossRef | 52 | minden még létező tuningId–frekvencia kapcsolat |
| Chord | 72 | chordId, név, tuningId |
| ChordTable | 108 | chordTableId, chordId, pozíció és fogáspozíció-lista |
| InstrumentString | 6 | stringNumber, frekvencia, menzúra, feszítés; később minden felhasználói kalkulátorsor |
| UserSettings | 1 | id, kiválasztott tuningId, useSharp, useEnglish |
| android_metadata | 1 | adatbázis-locale metadata |
| room_master_table | 1 | jelenlegi sémaazonosító dokumentálása; migrációnál az új séma szerint változhat |

A snapshot tartalmazza a teljes `sqlite_master` leltárt, az oszloptípusokat/elsődleges kulcsokat, a táblaadatok kanonikus SHA-256 értékét, az autoincrement-szekvenciák állapotát, a beépített hangszereket/hangolásokat, a kezdeti beállítást és a kalkulátorsorokat. A sorlenyomat képzése: oszlopneves JSON, rendezett kulcsok, UTF-8, rövid elválasztók; a sorok JSON-szövege lexikografikusan rendezve, sortörésekkel és záró sortöréssel összefűzve, majd SHA-256.

A kiinduló beállítás az 1-es Standard E hangolás, sharp és angol jelölés. A hangolásonkénti húrkapcsolatok száma a beépített hangszer húrszámával egyezik; a részletes lista a snapshotban található. A migrációs adatmegőrzés a mellékelt minta mellett az ettől eltérő saját hangolásokra, beállításokra és kalkulátorsorokra is vonatkozik.

**Nem visszaállítható régi információ:** a saját hangolások eredeti fizikai húrsorrendje és a már összevont azonos frekvenciájú húrok száma. Ezeket nem szabad biztosan visszanyert adatként feltüntetni vagy kitalált húrokkal pótolni; a későbbi migrációnak a megmaradt adatokat kell megőriznie és a korlátot dokumentálnia.

### Adatbiztonsági határ és változatlanság

A vizsgálat az assetet `mode=ro&immutable=1` SQLite URI-val nyitotta meg. Az RC00-04/RC00-05 módosításai kizárólag `:memory:` másolaton történtek. Felhasználói/eszközön tárolt adatbázist nem nyitottunk meg, nem töröltünk, nem migráltunk.

A snapshot **158 védett projektfájl** SHA-256 értékét rögzíti az `app/`, `gradle/`, wrapper és gyökér buildkonfiguráció köréből. A fázis lezárásakor mind a 158 ellenőrzőösszeget újraellenőriztük: mind változatlan; az asset ellenőrzőösszege a vizsgálat előtt/után is azonos.

A `Room exportSchema` továbbra is false; Room-sémaexport, migrációs fixture, v2 entitás, új asset és migrációs kód **nem készült**. A jelenlegi SQL-leltár nem helyettesíti a T04.01-ben kért Room-sémaexportot/fixture-t. A 01–11. fázis feladatai nyitva maradnak.
