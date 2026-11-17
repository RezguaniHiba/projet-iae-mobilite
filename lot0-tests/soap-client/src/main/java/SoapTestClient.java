import ma.mobility.air.ws.Flight;
import ma.mobility.air.ws.SearchFlightsRequest;
import ma.mobility.air.ws.SearchFlightsResponse;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SoapTestClient {

    public static void main(String[] args) throws Exception {

        // 1. Configuration JAXB
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("ma.mobility.air.ws");
        marshaller.afterPropertiesSet();

        // 2. Création du client SOAP
        WebServiceTemplate template =
                new WebServiceTemplate(marshaller);

        // 3. Préparation de la requête
        SearchFlightsRequest request =
                new SearchFlightsRequest();

        request.setOrigin("Rabat");
        request.setDestination("Paris");
        request.setDate("2026-11-15");

        System.out.println("Recherche des vols...");
        System.out.println("Rabat -> Paris");

        // 4. Appel du service SOAP
        Object response = template.marshalSendAndReceive(
                "http://localhost:8081/ws",
                request
        );

        SearchFlightsResponse result =
                (SearchFlightsResponse) response;

        // 5. Affichage des résultats
        System.out.println("\nNombre de vols : "
                + result.getFlight().size());

        for (Flight flight : result.getFlight()) {

            System.out.println("-------------------------");
            System.out.println("ID : " + flight.getFlightId());
            System.out.println("Origine : " + flight.getOrigin());
            System.out.println("Destination : "
                    + flight.getDestination());
            System.out.println("Départ : " + flight.getDeparture());
            System.out.println("Arrivée : " + flight.getArrival());
            System.out.println("Prix : "
                    + (flight.getPriceMinor() / 100.0)
                    + " MAD");
            System.out.println("Places : "
                    + flight.getSeatsAvailable());
        }

        System.out.println("\nAppel SOAP réussi !");
    }
}
