## Strategic Design

Per evolvere il dominio a questo punto si è pensato di procedere tramite _Strategic Design_.
Questo tipo di studio individua, a partire dai [casi d'uso](../useCases), i diversi 
sotto-domini che compongono il dominio principale.

Sono stati quindi trascritti i casi d'uso descritti precedentemente nel file
[_domain.cml_](Application/src/main/cml/domain.cml) usufruendo di [Context Mapper](https://contextmapper.org/)
come framework.
Si è quindi pensato di suddividere il dominio in tre sotto-domini:
* **Drone**: sotto-dominio in grado di analizzare i dati ricevuti dai sensori
e perciò in grado di prevenire incidenti e/o avvisare circa situazioni pericolose.
* **Reporting**: sotto-dominio il quale gestisce le segnalazioni riguardo i malfunzionamenti 
dei droni e le negligenze dei corrieri.
* **Shipping**: sotto-dominio supportante tutto ciò che riguarda il processo di spedizione.

A questo punto, secondo la metodologia DDD, è necessario costruire i cosiddetti _Bounded Context_,
i quali debbano soddisfare i requisiti del sotto-dominio a cui si riferiscono.

Nel nostro dominio sono stati individuati quattro Bounded Context:
* **Shipping Context**
* **Drone Context**
* **Issue Reporting Context**
* **Negligence Reporting Context**

Dopo aver determinato i Bounded Context è necessario capire le relazioni tra di essi.
Per mostrare tali relazioni viene costruita una _Context Map_.
In questo dominio le associazioni riconosciute sono state le seguenti:
* _DroneContext [U] -> [D, ACL] NegligenceReportingContext_:

  questa relazione indica che il _supplier_ **DroneContext** fornisce al _consumer_ **NegligenceReportingContext** le 
funzionalità per permettere d'individuare le situazioni critiche. Dato che il modello potrebbe cambiare,
il consumer necessita di un _AntiCorruptionLayer (ACL)_ per rendere i dati provenienti dal _supplier_ conformi alle sue
necessità. In questa maniera il consumer non deve cambiare il proprio modello.
* _DroneContext [U] -> [D, ACL] ShippingContext_:

  analogamente il **DroneContext** funziona da _supplier_ verso lo **ShippingContext** per mettere a disposizione
le possibilità di avviare il viaggio di spedizione, di tracciare il drone durante il 
viaggio, di ricevere la notifica di avvenuta consegna e di richiamare il drone al magazzino.
