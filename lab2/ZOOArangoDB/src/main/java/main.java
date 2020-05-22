import java.lang.reflect.Array;
import java.util.*;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.model.AqlQueryOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import com.arangodb.velocypack.exception.VPackException;

import javax.swing.text.Document;

public class main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        final ArangoDB arangoDB = new ArangoDB.Builder().build();
        final String dbName = "zoo";
        arangoDB.db(dbName).drop();
        try {
            arangoDB.createDatabase(dbName);
            System.out.println("Utworzono bazę danych: " + dbName);
        } catch (ArangoDBException e) {
            System.err.println("Tworzenie bazy danych nie udane " + dbName + "; " + e.getMessage());
        }
        String collectionName = "zoo";
        try {
            CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(collectionName);
        } catch (ArangoDBException e) {
            System.err.println("Nie udało się utworzyć: " + collectionName + "; " + e.getMessage());
        }
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
            System.out.println("7- Dochód z strefy przy maksymalnej liczbie osób");
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
                    System.out.println("Podaj godzine zamknięcia");
                    String godz_zamknięcia = scan.nextLine();
                    String[] godziny = {godz_otwarcia, godz_zamknięcia};
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
                    BaseDocument dokument = new BaseDocument();
                    dokument.setKey(String.valueOf(j));
                    dokument.addAttribute("strefa", strefaz);
                    dokument.addAttribute("godziny", Arrays.asList(godziny));
                    dokument.addAttribute("cena biletu", cena_biletu);
                    dokument.addAttribute("liczba odwiedzajacych", liczba_odwiedzajacych);
                    dokument.addAttribute("Zwierzeta", zwierzeta);
                    try {
                        arangoDB.db(dbName).collection(collectionName).insertDocument(dokument);
                        System.out.println("Utworzono wpis");
                    } catch (ArangoDBException e) {
                        System.err.println("Nie udało się dodać wpisu. " + e.getMessage());
                    }
                    break;

                case 2:
                    String query = "FOR t IN zoo RETURN t";
                    Map<String, Object> bindVars = new MapBuilder().get();
                    ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query,bindVars,null,BaseDocument.class);
                    cursor.forEachRemaining(aDocument -> {
                        System.out.println("id: " + aDocument.getKey()+" Strefa: "+ aDocument.getAttribute("strefa")+" godziny otwarcia "+ aDocument.getAttribute("godziny")+" cena biletu: "+ aDocument.getAttribute("cena biletu")
                        + "liczba odwiedzających: " + aDocument.getAttribute("liczba odwiedzajacych")+" Zwierzeta: "+ aDocument.getAttribute("Zwierzeta"));
                    });
                   break;
                case 3:
                    System.out.println("1- wyszukiwanie po id ");
                    System.out.println("2- wyszukiwanie po nazwie strefy ");
                    int wybor=scan.nextInt();
                    scan.nextLine();
                            if(wybor==1) {
                                System.out.println("Wprowadz id");
                                String id = scan.nextLine();
                                try {
                                    dokument = arangoDB.db(dbName).collection(collectionName).getDocument(id,
                                            BaseDocument.class);
                                    System.out.println("id: " + dokument.getKey()+" Strefa: "+ dokument.getAttribute("strefa")+" godziny otwarcia "+ dokument.getAttribute("godziny")+" cena biletu: "+ dokument.getAttribute("cena biletu")
                                            + "liczba odwiedzających: " + dokument.getAttribute("liczba odwiedzajacych")+" Zwierzeta: "+ dokument.getAttribute("Zwierzeta"));
                                } catch (ArangoDBException e) {
                                    System.err.println("Nie znaleziono dokumentu; " + e.getMessage());
                                }
                            }
                            else if(wybor==2){
                                System.out.println("Wprowadz nazwe");
                                String nazwa = scan.nextLine();
                                query = "FOR t IN zoo FILTER t.strefa == @strefa RETURN t";
                                Map<String, Object> binvarss = new MapBuilder().put("strefa",nazwa).get();
                                ArangoCursor<BaseDocument> cursor2 = arangoDB.db(dbName).query(query,binvarss,null,BaseDocument.class);
                                cursor2.forEachRemaining(aDocument -> {
                                    System.out.println("id: " + aDocument.getKey()+" Strefa: "+ aDocument.getAttribute("strefa")+" godziny otwarcia "+ aDocument.getAttribute("godziny")+" cena biletu: "+ aDocument.getAttribute("cena biletu")
                                            + "liczba odwiedzających: " + aDocument.getAttribute("liczba odwiedzajacych")+" Zwierzeta: "+ aDocument.getAttribute("Zwierzeta"));
                                });

                            }
                            break;
                case 4:
                    scan.nextLine();
                    System.out.println("Podaj id które chcesz usunąć");
                    String id = scan.nextLine();
                    arangoDB.db(dbName).collection(collectionName).deleteDocument(id);
                break;
                case 5:
                    arangoDB.db(dbName).drop();
                break;

                case 6:
                    System.out.println("1 - zmiana godzin otwarcia");
                    System.out.println("2 - zmiana zwierząt w strefie ");
                    wybor=scan.nextInt();
                    scan.nextLine();
                    System.out.println("Wprowadz id");
                     id = scan.nextLine();
                    dokument = arangoDB.db(dbName).collection(collectionName).getDocument(id,
                            BaseDocument.class);
                    if(wybor==1){
                        System.out.println("Podaj godzine otwarcia ");
                   godz_otwarcia = scan.nextLine();
                        System.out.println("Podaj godzine zamknięcia");
               godz_zamknięcia = scan.nextLine();
                    String [] godzinyy = {godz_otwarcia, godz_zamknięcia};
                        dokument.updateAttribute("godziny",godzinyy);
                        arangoDB.db(dbName).collection(collectionName).updateDocument(id,dokument);
                    }
                    else if (wybor==2) {
                        List<String> zwierzetaa = new ArrayList<String>();
                        while (true) {
                            System.out.println("Podaj nazwę lub q żeby zakończyć wspisywanie");
                            String zwierze = scan.nextLine();
                            if (zwierze.equals("q")) break;
                            else zwierzetaa.add(zwierze);
                        }
                        dokument.updateAttribute("Zwierzeta",zwierzetaa);
                        arangoDB.db(dbName).collection(collectionName).updateDocument(id,dokument);
                    }

                    break;
                case 7:
                    scan.nextLine();
                    System.out.println("Podaj id strefy dla której chcesz policzyć");
                    long suma;
                    id=scan.nextLine();
                    dokument = arangoDB.db(dbName).collection(collectionName).getDocument(id,
                            BaseDocument.class);
                    suma= (long) dokument.getAttribute("cena biletu")*(long)dokument.getAttribute("liczba odwiedzajacych");
                    System.out.println("Zarobek ze strefy może wynies "+ suma);
                    break;
                case 9:
                    System.exit(0);
            };


        }
    }
}

