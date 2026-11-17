
import mobility.rail.RailBooking;
import mobility.rail.RailBookingHelper;
import mobility.rail.Trip;

import org.omg.CORBA.ORB;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class CorbaTestClient {

    public static void main(String[] args) throws Exception {

        String iorPath = args.length > 0
                ? args[0]
                : "C:/dev/Projet-IAE/ior/RailBooking.ior";
        // 1. Initialisation de l'ORB JacORB
        Properties properties = new Properties();

        properties.setProperty(
                "org.omg.CORBA.ORBClass",
                "org.jacorb.orb.ORB"
        );

        properties.setProperty(
                "org.omg.CORBA.ORBSingletonClass",
                "org.jacorb.orb.ORBSingleton"
        );

        ORB orb = ORB.init(new String[0], properties);

        try {
            // 2. Lecture du fichier IOR
            String ior = new String(
                    Files.readAllBytes(Paths.get(iorPath)),
                    "UTF-8"
            ).trim();

            // 3. Récupération de l'objet distant
            org.omg.CORBA.Object object =
                    orb.string_to_object(ior);

            RailBooking booking =
                    RailBookingHelper.narrow(object);

            if (booking == null) {
                throw new IllegalStateException(
                        "Impossible de récupérer RailBooking"
                );
            }

            // 4. Appel CORBA : recherche de trajets
            System.out.println("Recherche des trajets...");
            System.out.println("Casablanca -> Rabat");
            System.out.println("Date : 2026-11-15");

            Trip[] trips = booking.search(
                    "Casablanca",
                    "Rabat",
                    "2026-11-15"
            );

            // 5. Affichage du résultat
            System.out.println("\nNombre de trajets : "
                    + trips.length);

            for (Trip trip : trips) {
                System.out.println("-------------------------");
                System.out.println("ID : " + trip.tripId);
                System.out.println("Origine : " + trip.origin);
                System.out.println("Destination : " + trip.destination);
                System.out.println("Départ : " + trip.departure);
                System.out.println("Arrivée : " + trip.arrival);
                System.out.println("Prix : "
                        + (trip.priceMinor / 100.0) + " MAD");
                System.out.println("Places : " + trip.seatsAvailable);
            }

            System.out.println("\nAppel CORBA réussi !");

        } finally {
            orb.destroy();
        }
    }
}
