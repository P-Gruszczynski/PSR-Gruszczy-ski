import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.inc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
public class zoo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String user = "user";
        String password = "user";
        String host = "localhost";
        int port = 27017;
        String database = "zoo";

        String clientURI = "mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database;
        MongoClientURI uri = new MongoClientURI(clientURI);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase db = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = db.getCollection("zoo");
        int i = 0;
        int j;
        boolean test = true;
        while (test) {
            System.out.println("1- Dodaj wpis");
            System.out.println("2- Wypisz");
            System.out.println("3- Wyszukaj");
            System.out.println("4- Usun");
            System.out.println("5- Wyczysć bazę");
            System.out.println("6- Modyfikuj");
            System.out.println("9-koniec");
            i = scan.nextInt();
            switch (i) {
                case 1:
                    System.out.println("Podaj id");
                    j = scan.nextInt();
                    scan.nextLine();
                    System.out.println("Podaj nazwe");
                    String strefaz = scan.nextLine();
                    System.out.println("Podaj godzine otwarcia ");
                    String godz_otwarcia = scan.nextLine();
                    System.out.println("Podaj godzine otwarcia ");
                    String godz_zamkniecia = scan.nextLine();
                    System.out.println("Podaj cene biletu");
                    int cena_biletu = scan.nextInt();
                    System.out.println("Podaj liczbę odwiedzajacych");
                    int liczba_odwiedzajacych = scan.nextInt();
                    int licznik = 0;
                    List<String> zwierzeta = new ArrayList<String>();
                    scan.nextLine();
                    while (true) {
                        System.out.println("Podaj nazwę lub q żeby zakończyć wspisywanie");
                        String zwierze = scan.nextLine();
                        if (zwierze.equals("q")) break;
                        else zwierzeta.add(zwierze);
                    }
                    Document strefa = new Document("_id", j)
                            .append("Strefa", strefaz)
                            .append("godziny otwarcia", Arrays.asList(new Document("godzina otwarcia",godz_otwarcia),new Document("godzina zamkniecia",godz_zamkniecia)))
                            .append("cena biletu", cena_biletu)
                            .append("liczba odwiedzajacych", liczba_odwiedzajacych)
                            .append("zwierzeta", (zwierzeta));
                    collection.insertOne(strefa);
                    break;
                case 2:
                    for (Document doc : collection.find())
                        System.out.println("wyniki:" + doc.toJson());
                    break;
                case 3:
                    System.out.println("1- wyszukiwanie po id");
                    System.out.println("2- wyszukiwanie po nazwie strefy");
                    int k = 0;
                    k = scan.nextInt();
                    switch (k) {

                        case 1:
                            System.out.println("Podaj id strefy którą chcesz wyszukać");
                            k = scan.nextInt();

                            Document Doc = collection.find(eq("_id", k)).first();
                            System.out.println("wynik wyszukiwania " + Doc.toJson());
                            break;
                        case 2:
                            System.out.println("Podaj nazwę strefy");
                            scan.nextLine();
                            String nazwa = scan.nextLine();
                            for (Document Doc2 : collection.find(eq("Strefa", nazwa)))
                                System.out.println("wynik wyszukiwania " + Doc2.toJson());
                            break;
                    }
                    break;
                        case 4:
                            System.out.println("Podaj id strefy którą chcesz usunać");
                            int st = scan.nextInt();
                            collection.deleteOne(eq("_id", st));
                            break;

                        case 5:
                            db.getCollection("zoo").drop();
                            break;
                        case 6:
                            System.out.println("1 - zmiana godzin otwarcia");
                            System.out.println("2 - zmiana zwierząt w strefie ");
                           int wybor=scan.nextInt();
                            System.out.println("Wprowadz id");
                           int id = scan.nextInt();

                            if(wybor==1) {
                                scan.nextLine();
                                System.out.println("Podaj godzine otwarcia ");
                                godz_otwarcia = scan.nextLine();
                                System.out.println("Podaj godzine zamknięcia");
                                godz_zamkniecia = scan.nextLine();
                                collection.updateOne(eq("_id", id), new Document("$set",new Document("godziny otwarcia",Arrays.asList(new Document("godzina otwarcia",godz_otwarcia),new Document("godzina zamkniecia",godz_zamkniecia)))));
                            }
                            else if(wybor==2) {
                                scan.nextLine();
                                List<String> zwierzet = new ArrayList<String>();
                                while (true) {
                                    System.out.println("Podaj nazwę lub q żeby zakończyć wspisywanie");
                                    String zwierze = scan.nextLine();
                                    if (zwierze.equals("q")) break;
                                    else zwierzet.add(zwierze);
                                }
                                collection.updateOne(eq("_id", id), new Document("$set", new Document("zwierzeta", zwierzet)));
                            }
                            break;
                case 7:
                    System.out.println("Podaj id strefy którą chcesz wyszukać");
                    k = scan.nextInt();

                    Document Doc = collection.find(eq("_id", k)).first();
                    int suma=0;
                    suma=Doc.getInteger("cena biletu")*Doc.getInteger("liczba odwiedzajacych");
                    System.out.println("Dochod wynosi "+ suma);
                        break;
                        case 9:
                            test = false;
                            break;


                    }

            }
        }
}

