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

  L'aggregato Client invece, provvede, tramite un servizio apposito, le seguenti funzionalità:
    * Creazione dell'ordine, a partire da un dato prodotto;
    * Tracciamento della posizione corrente del drone effettuante la consegna di uno specifico ordine;
    * Gestione della notifica di arrivo a destinazione;
    * Gestione della notifica di un nuovo tentativo di consegna.

  Le notifiche sono gestite come _Domain Event_ in quanto appunto eventi scatenati dalle corrispettive operazioni.


* _**Drone Context**_: anch'esso si compone di due aggregati: _Drone System_ e _User Monitoring_.

  L'aggregato _Drone System_ mostra le entità principali che gli sono necessarie per offrire le funzionalità
  richieste. Il servizio principale deve infatti poter avviare il drone a partire da un dato percorso.
  Per fare ciò è necessaria un'entità _Drone_ la quale deve poter leggere i dati ricevuti dai sensori annessi.
  Si potrebbe quindi risolvere mediante una collezione di entità _Sensor_, la quale fornisce i propri dati aggiornati.
  Quindi il _Drone Service_ pubblica costantemente i dati. Inoltre, mediante l'entità _Data Analyzer_ è in grado di 
  rilevare situazioni pericolose e nel caso si verificassero inviare un avvertimento all'aggregato _User Monitoring_. 
  Infine, esso è in grado di tracciare lo stato corrente del Drone.

  L'aggregato _User Monitoring_ deve invece poter gestire tutti gli eventi causati dall'analisi dei dati che sono stati
  già studiati in precedenza.
  Sono infatti presenti i seguenti _Domain Event_:
    * _Data Read_, per aggiornare i dati che un utente debba controllare;
    * _Warning Situation_, per avvisare di una situazione potenzialmente pericolosa;
    * _Critical Situation_, per avvertire di una situazione estremamente critica;
    * _Status Changed_, per informare del cambiamento di stato del Drone durante la consegna.


* _**Issue Reporting Context**_: questo contesto si occupa di gestire solo le segnalazioni
  riguardanti i malfunzionamenti. Esso è costituito da un unico aggregato.

  Sostanzialmente quest'ultimo fornisce due servizi.
  Uno dei quali riguarda il corriere ed è in grado di eseguire una segnalazione per
  malfunzionamento utilizzando l'opportuna entità, l'_Issue Report_. Quest'operazione genererà un _New Issue_, 
  ovvero un _Domain Event_ che avvisi della creazione di una nuova segnalazione.

  Il secondo invece riguarda il manutentore e permette di gestire l'arrivo di una nuova segnalazione.
  Inoltre, a scapito di come analizzato durante lo studio del dominio, esso offre la possibilità di visionare
  tali malfunzionamenti e in seguito dare l'opportunità di chiudere la segnalazione, compilando una scheda di 
  riparazione.


* _**Negligence Reporting Context**_: al contrario questo contesto permette di occuparsi solo delle
  segnalazioni per negligenza. Sono presenti due aggregati: _Drone Report_ e _Maintainer Negligence Report_.

  Il primo si occupa di effettuare la segnalazione, mediante l'entità appropriata (_Negligence Report_).
  Verrà quindi generato un evento che avvisi della nuova segnalazione.

  A questo punto, il secondo aggregato entra in gioco, in quanto è capace di gestire l'arrivo di una
  segnalazione per negligenza e inoltre, di occuparsene, compilando un'apposita scheda
  di provvedimento.
