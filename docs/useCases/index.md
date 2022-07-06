## Casi d'uso

Dopo aver effettuato una scoperta del dominio in maniera più viscerale e aver interpretato minuziosamente il 
relativo linguaggio, si è potuti giungere a una rappresentazione di tutti i possibili casi d'uso.
Il diagramma qui indicato riporta la rappresentazione visuale in forma di UML.

![Diagramma UML dei casi d'uso](https://app.genmymodel.com/api/projects/_Ea_J4ExfEeyzq6ra3GZCdA/diagrams/_Ea_J40xfEeyzq6ra3GZCdA/svg)

Sostanzialmente, i principali attori del dominio interagiscono con il sistema o mediante 
quest'ultimo tra di essi in differenti maniere al fine di costruire tutte le funzionalità
che devono essere presenti.

Innanzitutto, si può osservare come il **Drone** sia in grado di eseguire un analisi dei dati 
la quale permette di rielaborare i valori grezzi in informazioni preziose. 
Grazie all'accurata analisi effettuata, i dati possono essere continuamente monitorati dai
due utenti fisici principali del sistema: **Corrieri** e **Manutentori**.
Nel caso in cui il **Drone** rilevi, tramite i propri sensori, una situazione pericolosa, una
notifica giungerà al **Corriere** che sta monitorando quel **Drone** in modo tale da poter effettuare
manovre di correzione di rotta.
Inoltre, nel caso in cui il **Corriere** non sia riuscito per un qualche motivo a cambiare rotta,
il sistema posizionato sul **Drone** è capace di eseguire una manovra d'arresto per la quale
tentare di prevenire un incidente, data la situazione estremamente pericolosa per l'incolumità del
**Drone** stesso o dell'ostacolo.
A questo punto il **Drone** invia una segnalazione per negligenza del **Corriere** a un determinato 
**Manutentore**, il suo supervisore. 
In questo modo egli potrà revisionare l'accaduto e prendere provvedimenti verso il 
negligente. 

D'altro canto, il **Corriere** può invece segnalare un malfunzionamento del **Drone** proprio al 
**Manutentore**, il quale si occuperà di controllare fisicamente il **Drone** e i suoi componenti
e correggerà il problema compilando inoltre una scheda in cui viene descritto il problema
e la sua risoluzione.

Come punto di partenza, il **Cliente** è l'attore principale. Egli infatti potrà effettuare un
ordine cosicché il **Corriere** potrà eseguire la consegna sfruttando un determinato **Drone**.
Durante la consegna il **Cliente** potrà inoltre tracciare la posizione corrente del **Drone** per
verificarne la distanza dal punto di arrivo.
Nel momento in cui il **Drone** arriva a destinazione verrà inviata una 
notifica al**Cliente** per avvisarlo di procedere con il ritiro del pacco. 
Nel caso di mancato ritiro, il**Corriere** potrà riprogrammare la consegna 
per un giorno lavorativo successivo e il **Cliente** verrà notificato della data del nuovo tentativo di
consegna.

Per vincolare meglio il sistema, è necessario che gli utenti (**Corrieri** e **Manutentori**)
eseguano l'accesso per poter operare le proprie funzionalità. Per par condicio, è possibile 
effettuare il _log out_.
