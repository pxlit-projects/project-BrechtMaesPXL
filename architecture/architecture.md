# Architecture

![Architecture](https://github.com/pxlit-projects/project-BrechtMaesPXL/tree/main/architecture/FullstackJavaDiagram.drawio.png)

### Synchronous communications
Synchrone communicatie wordt gebruikt voor directe interacties die een snelle en consistente uitwisseling van gegevens vereist.

Het ophalen van een post met relevante informatie:

- Als een post wordt aangevraagd via de Post Service, ontvangt deze:

    - De Review Service kan beoordelingen plaatsen via het post-ID.
    - eageer op de Comment Service met behulp van het post-ID.  
    - ...

-  Het verifiëren van de status van een post
    - Voordat een post goedgekeurd of afgewezen wordt, verifieert de Review Service onmiddellijk bij de Post Service of de status van de post "ingediend" is. Enkel ingediende berichten kunnen worden beoordeeld.
    - ...

- Controleer de status van een post:

    - Als een post wordt goedgekeurd of afgewezen, stuurt de Review Service een directe API-oproep naar de Post Service om de status te actualiseren (bijvoorbeeld).
    - ...


### Asynchronous communications
Asynchrone communicatie wordt ingezet voor processen waarin directe feedback niet vereist is of voor langdurige taken. Dit gebeurt met behulp van een message broker zoals RabbitMQ.

- Beoordelingen verwerken:

    - Als een post wordt goedgekeurd of afgewezen, verzendt de Review Service een bericht naar een Queue.

        - De Post Service bekijkt dit bericht en neemt extra maatregelen, zoals het verzenden van notificaties naar de auteur of de betrokken redacteuren.
        - Bijwerken van statusinformatie in andere systemen, zoals een analytische module.
        - ...


- Reacties verwerken:

    - Bij het toevoegen, aanpassen of verwijderen van reacties verzendt de Comment Service een bericht naar de queue.
        - De Post Service bekijkt dit bericht en voegt de relevante postinformatie toe.
            - Stuurt meldingen naar de relevante gebruikers.
            - ...

- Auditregistratie:
    - Voor elke belangrijke aanpassing, zoals statusupdates of beoordelingen, stuurt de service een asynchroon bericht dat door een Audit Service wordt verwerkt. Deze service verzamelt loggegevens voor compliance of debugging.
    - ...


