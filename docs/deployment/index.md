## Installazione

Ciascuno degli eseguibili JAR forniti contengono tutto ciò che è necessario per la propria esecuzione.

Come requisiti, è necessario possedere Java versione 11+ e una cartella
contenente tutti i certificati necessari, creati a partire dalla registrazione in remoto del drone,
ovvero il certificato che rappresenta l'autorità (in questo caso Amazon),
il certificato del dispositivo (drone) e la sua chiave privata.

Per eseguire correttamente gli applicativi sono possibili due opzioni:
- Cliccare due volte l'applicativo;
- Oppure, si può comunque eseguire da riga di comando digitando il comando `java -jar `
seguito dal nome del JAR.

Inizialmente dovranno essere salvate le impostazioni della connessione, per cui verrà aperta un'interfaccia
grafica la quale creerà un file per memorizzare le impostazioni. Se il dispositivo su cui
eseguire l'applicativo `drone-system` non offre un'interfaccia grafica è necessario generare il file a priori
e in seguito copiarlo nella stessa cartella nella quale verrà posto l'eseguibile.

Solo se si vuole modificare il file, nel caso di `drone-system` è necessario aggiungere il parametro 
`generate-properties` a seguito del comando per lanciare l'applicativo, come riportato di seguito ->

`java -jar drone-system-fat.jar generate-properties`.

Poiché non è possibile controllare che il _Client Identifier_ sia univoco, è opportuno prestare attenzione a
non inserire il medesimo nei due applicativi, altrimenti il comportamento del sistema diventa imprevedibile.