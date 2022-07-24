## Tactical Design

Successivamente, il DDD richiede uno studio più approfondito dei Bounded Context chiamato _Tactical Design_.

Sono stati quindi analizzati nel dettaglio ciascuno di essi:
* _**Shipping Context**_: esso mette a disposizione due aggregati: _Shipping_ e _Client_.

  Il primo di questi fonda le proprie basi sull'entità _Order_, la cui particolarità è il ciclo di vita collegato alla
  consegna del medesimo. Viene chiaramente supportato da _Value Object_ indispensabili a rappresentare le informazioni 
  che esso contiene.
  Esso inoltre agisce da _aggregate root_, perciò viene supportato da un _repository_ apposito per fare in modo di 
  assicurarsi che le informazioni rimangano persistenti e consistenti.
  Come raccomandato, per evitare lo _State Pattern_ lo stato dell'ordine è stato realizzato come una collezione di 
  differenti entità, ciascuna delle quali rappresenta lo specifico stato in quel momento.
  Per separare il più possibile la logica relativa agli ordini e alle consegne, sono stati modellati tre _domain 
  services_:
   * _Order Manager_: esso si occupa soltanto della mera gestione degli ordini;
   * _Delivery Service_: esso offre le funzionalità dedicate alla consegna, ad esempio permette di avviare la spedizione
  o di pianificare un ulteriore tentativo di consegna.
   * _Drone Controller_: questo servizio permette di controllare il drone durante la spedizione.  
   <br/>
  
  NOTE: a causa del suo ruolo nel dominio e avendo evitato lo _State Pattern_, non sono presenti
  ulteriori invarianti dell'entità _Order_ oltre alla semplice validazione che la data di consegna
  debba essere successiva alla data di ordinazione e che il nuovo tentativo di consegna non debba essere
  precedente al vecchio.  
  <br/>

  L'aggregato Client invece, provvede, tramite un servizio apposito, le seguenti funzionalità:
    * Creazione dell'ordine, a partire da un dato prodotto;
    * Tracciamento della posizione corrente del drone effettuante la consegna di uno specifico ordine;
    * Gestione della notifica di arrivo a destinazione;
    * Gestione della notifica di un nuovo tentativo di consegna.

  Le notifiche sono gestite come _Domain Event_ in quanto appunto eventi scatenati dalle corrispettive operazioni.


* _**Drone Context**_: questo aggregato si compone di otto aggregati, di cui sette operano nel sistema del drone e 
  uno nell'applicativo dell'utente. Gli aggregati del sistema del drone sono: _Drone_, _Sensor_, _Proximity_,
  _Accelerometer_, _Camera_, _Alert_ e _Order_. L'applicativo dell'utente invece contiene il solo aggregato
  _UserMonitoring_.
  
  L'aggregato _Drone_ possiede le entità necessarie per poter gestire il drone intero, permettendone, ad esempio,
  l'attivazione e l'arresto oppure di operare sui suoi sensori. Esso contiene anche il *service* apposito per informare 
  se il drone è stato arrestato, oppure se è ripartito per la consegna. Esso infatti a sua volta comunica con
  l'aggregato _Sensor_ attraverso il suo *aggregate root*, ovvero _SensorSet_, che permette di maneggiare
  contemporaneamente tutti i sensori del drone, permettendo di gestirne l'attivazione e fornendo il necessario per
  leggere e pubblicare i loro dati. I servizi a disposizione dell'aggregato, gli permettono di analizzare tutti gli
  stati di allerta dei sensori per ottenere lo stato attuale del drone e di pubblicare il corrente stato d'allerta.

  I sensori perciò sono suddivisi in tre aggregati: _Proximity_, _Accelerometer_ e _Camera_. Ognuno di essi fornisce
  quindi le stesse operazioni del _SensorSet_ ma, al contrario di quest'ultimo, essi operano ognuno sul rispettivo
  sensore, nelle modalità appropriate. Infatti contengono anche dei *Service* per poter processare, analizzare e,
  infine, pubblicare i dati dei loro sensori.

  Infine sono presenti altri due aggregati per agevolare il funzionamento del contesto, fornendo i *Value Object*
  relativi agli ordini e allerte rispettivamente negli aggregati _Order_ e _Alert_. Essi mettono a disposizione
  esclusivamente i *value object* necessari per il funzionamento del contesto, perciò non sono presenti dei *service*
  addizionali.

  L'aggregato _UserMonitoring_ invece è mirato al tracciamento dei messaggi inviati dal sistema del drone. Infatti
  possiede i *value object* per mantenere i dati ricevuti e due servizi, uno dei quali riceverà tutti i messaggi del
  drone relativi ai sensori, mentre il secondo permetterà di osservare lo stato della consegna e controllare il drone
  da remoto. Essi avranno inoltre il compito d'informare il resto del sistema attraverso l'uso dei rispettivi
  *Domain Events*. I messaggi in questione riguardano i dati di ogni sensore, i messaggi di allerta del drone e 
  quelli relativi allo stato del movimento del drone (arrestato oppure in movimento).


* _**Issue Reporting Context**_: questo contesto si occupa di gestire solo le segnalazioni
  riguardanti i malfunzionamenti. Esso è costituito da sei aggregati.

  L'aggregato _CreationIssue_ permette di creare una nuova segnalazione e successivamente spedirla per essere aggiunta
  al sistema. Infatti una segnalazione, prima di essere aggiunta al sistema, non possiede un identificativo, non essendo
  ancora parte del sistema. Per questo motivo sarà utilizzato l'aggregato _CreatedIssue_, che definisce le segnalazioni
  che sono già state aggiunte al sistema e sono quindi in possesso di un identificativo.

  Dopo essere creata, una segnalazione diventa una segnalazione attiva che, a differenza di una chiusa, deve essere
  processata o è in fase. Questo tipo di segnalazioni sono rappresentate dall'aggregato _ActiveIssue_, che a sua volta
  può essere rappresentato da due ulteriori aggregati: _OpenIssue_ e _VisionedIssue_. Questo aggregato come
  funzionalità, permette di ritrovare nel sistema tutte le segnalazioni attive, d'interesse all'utente che le sta
  richiedendo.

  L'aggregato _OpenIssue_ rappresenta la situazione in cui una segnalazione è stata appena creata. In questo stato
  nessun manutentore è ancora intervenuto in alcun modo per risolverla. Infatti permette al manutentore di eseguire
  l'operazione che la trasformerà in una visionata. Quest'ultima è definita dall'aggregato _VisionedIssue_, per
  rappresentare lo stato in cui un manutentore sta attualmente prendendo visione della segnalazione e sta lavorando a
  una possibile soluzione per risolverla. L'aggregato ha lo scopo di fornire al corriere l'informazione che la sua
  segnalazione è stata presa in carica e potrebbe essere risolta a breve. A questo scopo l'agregato mette a disposizione
  del manutentore una possibilità che gli permetta, alla fine dell'elaborazione della segnalazione, di chiudere la
  segnalazione, specificando la soluzione adottata.

  Infine perciò è presente l'aggregato _ClosedIssue_, il quale rappresenta una segnalazione che è stata definitivamente
  chiusa. Quindi l'unica funzionalità che tale aggregato fornisce, è quella che permette a un utente di ritrovare tutte
  le segnalazioni chiuse che sono di suo interesse.


* _**Negligence Reporting Context**_: al contrario questo contesto permette di occuparsi solo delle
  segnalazioni per negligenza. È presente un unico aggregato: _Negligence Reporting_.

  Esso si occupa di effettuare la segnalazione, mediante il servizio appropriato (_Drone Reporter_).
  Verrà quindi generato un evento che avvisi della nuova segnalazione (_New Negligence_).

  A questo punto, il modello è avvisato di una nuova segnalazione e perciò, da questo momento in poi,
  il supervisore assegnato a questa negligenza dovrà occuparsene, compilando un'apposita scheda
  di provvedimento.


* _**User Context**_: questo contesto, relativamente di minor dominio, offre un singolo aggregato _User_,
il quale offre le funzionalità base per ciò che concerne tutti gli utenti, come la possibilità di effettuare
il login e il logout. Inoltre, per differenziare i tipi di utenti del sistema sono presenti estensioni
dell'utente generico a seconda dei ruoli ricoperti da essi: attualmente sono presenti _Courier_ e
_Maintainer_. Infine, sono due i principali servizi legati a questo sotto-dominio: _Authentication Service_
e _User Manager_. Il primo di questi permette di effettuare il login e il logout dal sistema.
Il secondo invece, fungendo da gestore degli utenti, concede la possibilità di ricavare le informazioni
relative a specifici utenti, così come all'utente che ha acceduto al sistema.
