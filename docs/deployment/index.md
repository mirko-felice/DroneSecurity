## Installazione

Ciascuno degli eseguibili JAR forniti contengono tutto ciò che è necessario per la propria esecuzione.

I requisiti necessari per avviare e far funzionare correttamente i due applicativi sono differenti a seconda che sia 
l'applicativo da eseguire sul drone o sul sistema dell'utente.

I requisiti comuni sono:

* Aver installato Java versione 11+.
* Possedere una cartella contenente tutti i certificati necessari, creati a partire dalla registrazione in remoto del 
drone, ovvero il certificato che rappresenta l'autorità (in questo caso Amazon),
il certificato del dispositivo (drone) e la sua chiave privata.

Un requisito aggiuntivo necessario per l'applicativo dell'utente è possedere un server MongoDB.

Un requisito aggiuntivo necessario per l'applicativo del drone è aver installato Python versione 3.7 o maggiore.

Entrambi gli applicativi devono essere lanciati da linea di comando, a meno che sulla macchina sia stata impostata 
correttamente la variabile d'ambiente che sia indirizzata al percorso dell'eseguibile della Java Virtual Machine.

Per eseguire correttamente l'applicativo sul drone è necessario dapprima configurare le proprietà utili alla 
connessione del servizio cloud.
Per questo motivo è necessario eseguire il comando `java -jar drone-system-fat.jar` da linea di comando per permettere
all'applicativo di chiedere all'utente alcune informazioni indispensabili.
Dall'esecuzione successiva non sarà più necessario richiedere altre informazioni in quanto le informazioni vengono 
salvate su un file nella cartella corrente in cui viene lanciato l'applicativo, a meno che l'utente non voglia 
modificare la configurazione. In tal caso è necessario riavviare l'applicativo da riga di comando, aggiungendo come 
parametro `generate-properties`.

Per quanto riguarda l'applicativo utente è predisposta un'interfaccia grafica tramite la quale memorizzare la 
configurazione della connessione. Se si vorrà modificare alcuni parametri, l'applicativo permetterà di modificarli 
ponendo la domanda all'avvio del programma.

Poiché non è possibile controllare che il _Client Identifier_ sia univoco, è opportuno prestare attenzione a
non inserire il medesimo nei due applicativi, altrimenti il comportamento del sistema in toto diventa imprevedibile.