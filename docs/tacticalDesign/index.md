## Tactical Design

Successivamente, il DDD richiede uno studio più approfondito dei Bounded Context chiamato _Tactical Design_.

Sono stati quindi analizzati nel dettaglio ciascuno di essi:
* _**Shipping Context**_: esso mette a disposizione due aggregati: _Courier Shipping_ e _Client_.

  Il primo di questi sostanzialmente fornisce un servizio il quale, mediante le proprie entità _Order_
  e _Drone Selection_, le quali rispettivamente rappresentano l'ordine effettuato dal cliente e
  la selezione del drone, permette di svolgere la spedizione a partire da tali entità.
  Inoltre, permette di richiamare il drone al magazzino e di poter programmare un nuovo tentativo di consegna di uno specifico ordine.

  L'aggregato Client invece, provvede, tramite un servizio apposito, le seguenti funzionalità:
    * Creazione dell'ordine, a partire da un dato prodotto;
    * Tracciamento della posizione corrente del drone effettuante la consegna di uno specifico ordine;
    * Gestione della notifica di avvenuta consegna;
    * Gestione della notifica di un nuovo tentativo di consegna.

  Le notifiche sono gestite come _Domain Event_ in quanto appunto eventi scatenati dalle corrispettive operazioni.


* _**Drone Context**_: anch'esso si compone di due aggregati: _Drone System_ e _User Monitoring_.

  L'aggregato _Drone System_ mostra le entità principali che gli sono necessarie per offrire le funzionalità
  richieste. Il servizio principale deve infatti poter avviare il drone a partire da un dato percorso.
  Per fare ciò è necessaria un'entità _Drone_ la quale deve poter analizzare i dati ricevuti dai sensori annessi.
  Si potrebbe quindi risolvere mediante una collezione di entità _Sensor_, la quale fornisce i propri dati aggiornati.

  L'aggregato _User Monitoring_ deve invece poter gestire tutti gli eventi causati dall'analisi dei dati che sono stati
  già studiati in precedenza.
  Sono infatti presenti i seguenti _Domain Event_:
    * _Updated Data_, per aggiornare i dati che un utente debba controllare;
    * _Warning Situation_, per avvisare di una situazione potenzialmente pericolosa;
    * _Critical Situation_, per avvertire di una situazione estremamente critica.


* _**Issue Reporting Context**_: questo contesto si occupa di gestire solo le segnalazioni
  riguardanti i malfunzionamenti. I due aggregati facenti parte sono nominati _Courier Report_ e
  _Maintainer Issue Report_.

  Sostanzialmente il primo fornisce un servizio che è in grado di eseguire una segnalazione per
  malfunzionamento a partire dall'entità che la rappresenta. Esso genererà un _New Issue_, ovvero un
  _Domain Event_ che avvisi della creazione di una nuova segnalazione.

  L'aggregato riguardante il manutentore, invece, permette di gestire l'arrivo di una nuova segnalazione
  e di poter chiudere una segnalazione, compilando una scheda di riparazione.


* _**Negligence Reporting Context**_: al contrario questo contesto permette di occuparsi solo delle
  segnalazioni per negligenza. Sono presenti due aggregati: _Drone Report_ e _Maintainer Negligence Report_.

  Il primo si occupa di effettuare la segnalazione, mediante l'entità appropriata (_Negligence Report_).
  Verrà quindi generato un evento che avvisi della nuova segnalazione.

  A questo punto, il secondo aggregato entra in gioco, in quanto è capace di gestire l'arrivo di una
  segnalazione per negligenza e inoltre, di occuparsene, compilando un'apposita scheda
  di provvedimento.
