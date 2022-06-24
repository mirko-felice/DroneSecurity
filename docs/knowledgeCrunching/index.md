## Knowledge Crunching

Data la relativa complessità del progetto, è stato opportuno effettuare un confronto con gli stakeholders
durante il quale sono stati definiti in modo più approfondito i requisiti richiesti.

Per avere una visione più estesa delle richieste riguardanti l'applicativo sono stati effettuati
diversi colloqui, durante i quali gli stakeholders sono stati interrogati allo scopo di comprendere meglio
il modello che essi idealizzano.

Di seguito vengono riportate le domande più rilevanti, suddivise per tematica.

1. Monitoraggio:

    - _Q_: **Nella sua idea, in cosa consistono le informazioni riguardanti i droni da monitorare?**  
   _A_: In questo contesto i droni vengono dotati di particolari sensori ritenuti necessari per il 
   corretto pilotaggio. Infatti abbiamo richiesto a un'altra azienda un particolare algoritmo 
   d'Intelligenza Artificiale per fare in modo che i droni svolgano le consegne in maniera automatizzata.
   Però, dato che il software non garantirà al 100% l'incolumità dei droni, si è pensato 
   d'incaricare diversi nostri impiegati a controllare lo stato dei dispositivi.
   Allo stato corrente, l'algoritmo a nostra disposizione riesce a lavorare in maniera corretta tramite
   una serie di sensori; per questo motivo vorremmo sfruttare i medesimi per dare la
   possibilità ai nostri dipendenti di avere un monitoraggio attendibile dei droni stessi.
   I sensori in questione comprendono un sensore di prossimità, un accelerometro e una camera.
   Perciò le informazioni dovranno riguardare la distanza dall'ostacolo più vicino al dispositivo, gli 
   angoli di rollio e di beccheggio e le immagini catturate dalla videocamera.  
   <br>  
    - _Q_: **Cosa rappresentano gli angoli di rollio e di beccheggio?**  
   _A_: In riferimento al velivolo, il rollio rappresenta la rotazione del drone intorno all'asse X, mentre
   il beccheggio rappresenta la rotazione intorno all'asse Y.
   Come sistema di riferimento, si considera l'asse X la retta lungo la quale il drone opera un movimento
   frontale, mentre l'asse Y è la retta lungo la quale il drone opera un movimento laterale.
   Infine, l'asse Z è la retta durante la quale il drone è capace di scendere o salire.  
   <br>

2. Sicurezza:

    - _Q_: **Cosa si intende per situazione pericolosa?**  
   _A_: La situazione pericolosa si presenta in due possibili casi: nel momento nel quale la distanza 
   dall'ostacolo più vicino diventi troppo ristretta o nel momento in cui o l'angolo
   di rollio o di beccheggio assuma un valore significativo. In tale situazione si deve avvertire il pilota
   al fine d'incoraggiarlo a prestare attenzione ai movimenti del drone e, nel caso fosse necessario, manovrare
   manualmente il dispositivo.  
   <br>

    - _Q_: **Quindi non è presente solo il pilotaggio automatico?**  
   _A_: No, il corriere infatti sarà dotato di un radiocomando da poter utilizzare in seguito del cambio di modalità 
   di pilotaggio.  
   <br>
   
    - _Q_: **La possibilità di manovrare manualmente il drone può avvenire soltanto nel caso in cui sia rilevata una 
    situazione pericolosa?**  
   _A_: No, il corriere ha assoluta libertà di scegliere di completare la consegna personalmente attraverso il
   radiocomando.  
   <br>
    - _Q_: **Perciò l'unica azione che può compiere il corriere durante la consegna è il cambio di modalità?**  
   _A_: Assolutamente no, il sistema deve poter permettere al corriere di arrestare il drone in qualsiasi momento, 
   mentre la ripartenza può essere conseguita soltanto nel caso in cui la situazione non sia critica.  
   <br>

    - _Q_: **La situazione critica è considerabile alla pari della situazione pericolosa?**  
   _A_: No, consideriamo che un drone non dovrebbe mai risultare in tale situazione. Infatti questa deve essere 
   considerata una situazione in cui la probabilità di recare danni a se stesso o a un eventuale ostacolo sia 
   estremamente elevata. Per questo motivo riteniamo che se durante una consegna il drone dovesse mai imbattersi in 
   una situazione critica, sarebbe necessario in seguito operare un'indagine sulle motivazioni di mancanza d'azione da 
   parte del corriere che la stava eseguendo.  
   <br>

    - _Q_: **In che modo deve essere prevenuto l'incidente?**  
    _A_: Analizzando i vari rischi collegati alle diverse opzioni, pensiamo che la soluzione migliore per tentare di 
    prevenire un incidente nei riguardi, sia del drone che dell'eventuale ostacolo, sia una manovra d'arresto.  
    <br>
   
    - _Q_: **Oltre a quella pericolosa e critica, devono essere presenti altre situazioni rilevanti nel sistema?**  
    _A_: No, a parte queste due appena descritte si potrebbe considerare quella del funzionamento regolare del 
    drone.  
    <br>
   
3. Segnalazioni:

    - _Q_: **In quali circostanze avviene una negligenza?**  
   _A_: Il corriere compie una negligenza solo ed esclusivamente nel momento in cui il drone si ritrovi in 
   situazione critica.  
   <br>
   
    - _Q_: **In che modo vengono rilevati i malfunzionamenti dei droni?**  
   _A_: Abbiamo pensato a questa funzionalità aggiuntiva attraverso la quale il corriere
   posso comunicare in modo rapido e digitale dei malfunzionamenti riscontrati durante l'operatività del drone.
   Quindi il corriere dovrebbe poter segnalare i malfunzionamenti al proprio supervisore.  
   <br>
   
    - _Q_: **Riguardo quest'ultimi, quali livelli di criticità presuppone?**  
   _A_: Si suppone che i droni possano avere malfunzionamenti più o meno gravi, a seconda dei componenti fisici o 
   software che lo costituiscono.
   Si potrebbe pensare che esista un livello grave in cui il drone non è neanche più agibile, un livello intermedio in
   cui il drone riesce a operare ma a causa di qualche componente non fornisce sicurezza adeguata per cui dovrà 
   comunque risultare inutilizzabile e infine un livello base in cui il drone opera in maniera corretta ma sarebbero
   presenti alcuni problemi estetici o non compromettenti la sicurezza del drone e dell'ambiente circostante.

4. Ordini:
   
   - _Q_: **Quali sono le funzionalità che lei ritene siano inclusi nella gestione degli ordini?**  
   _A_: L'intero processo aziendale degli ordini parte dall'effettuazione dell'ordine da parte del cliente.
   Da questo momento in poi lo stato dell'ordine sarà visibile dai due utenti interessati: il cliente e il corriere che
   esegue la consegna.
   Infatti, dal momento in cui il corriere avvierà la spedizione, il cliente potrà costantemente tracciare la 
   posizione del pacco, fino a quando esso non arriverà a destinazione ove attenderà il ritiro da parte del cliente.
   Per agevolare quest'ultimo egli verrà notificato di tale evento.
   In caso il cliente non riesce per qualsiasi motivazione a ritirare il pacco, l'ordine dovrà ritenersi
   fallito. Sempre allo scopo di agevolare il più possibile il cliente finale, deve essere possibile effettuare 
   almeno un ulteriore tentativo di consegna in un giorno lavorativo successivo, informando il cliente della data.
   <br>

5. Altro:
    
    - _Q_: **Sono presenti dei vincoli riguardanti l'utilizzo di una piattaforma IoT?**  
   _A_: No, avete completa libertà sia nella scelta della piattaforma, sia dal punto di vista delle modalità 
   d'uso.  
   <br>

    - _Q_: **Sono stati nominati varie volte corrieri e supervisori. Quali utenti fanno parte del sistema?
   Che relazioni hanno e quali peculiarità?**  
   _A_: Per ora nel nostro sistema gli unici utenti che devono essere presenti sono i corrieri e i manutentori.
   Mentre i primi si occupano appunto della gestione degli ordini e del monitoraggio dei droni, i secondi si occupano
   principalmente di gestire le segnalazioni ricevute dai droni riguardanti le negligenze compiute dai corrieri e le
   segnalazioni relative ai malfunzionamenti che i corrieri osservano in corso d'opera.
   Inoltre, la gerarchia della nostra azienda prevede che i corrieri riferiscano le segnalazioni allo specifico
   supervisore a loro assegnato. Un supervisore non è altro che un manutentore, non vi è alcuna distinzione semantica.
   Infine, tengo a precisare che i corrieri per aumentare la propria efficienza, sono predisposti all'esecuzione
   contemporanea di molteplici consegne le quali quindi debbano essere compiute dai rispettivi droni assegnatogli.  
   <br>
