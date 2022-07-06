## Strategic Design

Per evolvere il dominio a questo punto si è pensato di procedere tramite _Strategic Design_.
Questo tipo di studio individua, a partire dai [casi d'uso](../useCases), i diversi 
sotto-domini che compongono il dominio principale.

Sono stati quindi trascritti i casi d'uso descritti precedentemente nel file
[_domain.cml_](https://raw.githubusercontent.com/mirko-felice/DroneSecurity/master/user-application/src/main/cml/domain.cml)
usufruendo di [Context Mapper](https://contextmapper.org/) come framework.
Si è quindi pensato di suddividere il dominio in quattro sotto-domini:
* **Drone**: sotto-dominio in grado di analizzare i dati ricevuti dai sensori
e perciò in grado di prevenire incidenti e/o avvisare circa situazioni pericolose.
* **Reporting**: sotto-dominio il quale gestisce le segnalazioni riguardo i malfunzionamenti 
dei droni e le negligenze dei corrieri.
* **Shipping**: sotto-dominio supportante tutto ciò che riguarda il processo di spedizione.
* **User**: sotto-dominio supportante l'accesso al sistema e la disconnessione da quest'ultimo 
da parte dell'utente.

A questo punto, secondo la metodologia DDD, è necessario costruire i cosiddetti _Bounded Context_,
i quali debbano soddisfare i requisiti del sotto-dominio a cui si riferiscono.

Nel nostro dominio sono stati individuati quattro Bounded Context:
* **Shipping Context**
* **Drone Context**
* **Issue Reporting Context**
* **Negligence Reporting Context**
* **User Context**

Dopo aver determinato i Bounded Context è necessario capire le relazioni tra di essi.
Per mostrare tali relazioni viene costruita una _Context Map_.
In questo dominio le associazioni riconosciute sono state le seguenti:
* _NegligenceReportingContext [U] -> [D, CF] DroneContext_:

  questa relazione indica che il _supplier_ **NegligenceReportingContext** fornisce al _consumer_ **DroneContext** le 
funzionalità per permettere di eseguire una segnalazione per negligenza.
Per far ciò il _consumer_ deve adeguarsi ai cambiamenti del modello del dominio presente nel _supplier_.
* _DroneContext [U] -> [D, CF] ShippingContext_:

  in un altro scenario il **DroneContext** funziona da _supplier_ verso lo **ShippingContext** per mettere a 
disposizione le possibilità di avviare il viaggio di spedizione, di tracciare il drone durante il 
viaggio, di ricevere la notifica di arrivo a destinazione e di richiamare il drone al magazzino.
Anche in questo caso, il _consumer_ deve adattarsi alle richieste del _supplier_.

* _UserContext [U, OHS, PL] -> [D] ShippingContext_:
* _UserContext [U, OHS, PL] -> [D] NegligenceReportingContext_:
* _UserContext [U, OHS, PL] -> [D] IssueReportingContext_:

  queste tre relazioni stanno a indicare che lo **UserContext** opera da fornitore nei confronti
degli altri tre contesti elencati, i quali necessitano d'informazioni, spesso relative all'utente
acceduto. Data la molteplicità d'uso, al fine di evitare vincoli rispetto ai cambiamenti interni 
del dominio utente, è stato scelto di aggiungere un layer _OHS_ per permettere ai consumatori
di mantenere le informazioni necessarie senza impattare lo sviluppo dello **UserContext**.
 