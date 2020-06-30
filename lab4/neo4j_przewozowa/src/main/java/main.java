import static org.neo4j.driver.internal.types.InternalTypeSystem.TYPE_SYSTEM;

import java.lang.invoke.SwitchPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

public class main {

    public static Result createKierowca(Transaction transaction) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwisko");
        String kierowcaName = scan.next();
        System.out.println("Podaj imie");
        String kierowcaImie = scan.next();
        System.out.println("Podaj wiek");
        int kierowcaWiek = scan.nextInt();
        System.out.println("Podaj wynagrodzenie");
        int kierowcaWynagrodzenie = scan.nextInt();
        String command = "CREATE (:Kierowca {nazwisko:$kierowcaName , imie:$kierowcaImie , wiek:$kierowcaWiek, wynagrodzenie:$kierowcaWynagrodzenie })";
        System.out.println("Executing: " + command);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("kierowcaName", kierowcaName);
        parameters.put("kierowcaImie", kierowcaImie);
        parameters.put("kierowcaWiek", kierowcaWiek);
        parameters.put("kierowcaWynagrodzenie", kierowcaWynagrodzenie);
        return transaction.run(command, parameters);
    }
    public static Result createTrasa(Transaction transaction) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazw");
        String nazwa= scan.next();
        System.out.println("Podaj przystanek początkowy");
        String pPocz=scan.next();
        System.out.println("Podaj przystanek końcowy");
        String pKon=scan.next();
        String command = "CREATE (:Trasa {nazwa:$nazwa , przystanekPoczatkowy:$pPocz , przystanekKoncowy:$pKon })";
        System.out.println("Executing: " + command);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nazwa", nazwa);
        parameters.put("pPocz", pPocz);
        parameters.put("pKon", pKon);
        return transaction.run(command, parameters);
    }
    public static Result createRelationship(Transaction transaction, String kierowcaName, String trasaName) {
        String command =
                "MATCH (k:Kierowca),(t:Trasa) " +
                        "WHERE k.nazwisko = $kierowcaName AND t.nazwa = $nazwa "
                        + "CREATE (k)−[r:Wykonuje]−>(t)" +
                        "RETURN type(r)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("kierowcaName", kierowcaName);
        parameters.put("nazwa", trasaName);
        //System.out.println("Executing: " + command);
        return transaction.run(command, parameters);
    }
    public static void printField(Pair<String, Value> field) {
        Value value = field.value();
        if (TYPE_SYSTEM.NODE().isTypeOf(value))
            printNode(field.value().asNode());
        else if (TYPE_SYSTEM.RELATIONSHIP().isTypeOf(value))
            printRelationship(field.value().asRelationship());
        else
            throw new RuntimeException();
    }
    public static void printField2(Pair<String, Value> field) {
        Value value = field.value();
        if (TYPE_SYSTEM.NODE().isTypeOf(value))
            printNode2(field.value().asNode());
        else if (TYPE_SYSTEM.RELATIONSHIP().isTypeOf(value))
            printRelationship(field.value().asRelationship());
        else
            throw new RuntimeException();
    }

    public static void printNode(Node node) {
        System.out.println("id = " + node.id());
        System.out.println("Tabela = " + " : " + node.labels());
        System.out.println("Wartosc = "+ node.asMap());
    }
    public static void printNode2(Node node) {
        System.out.println("id = "+ node.id()+" Wartosc = "+ node.asMap());
    }

    public static void printRelationship(Relationship relationship) {
        System.out.println("id = " + relationship.id());
        System.out.println("type = " + relationship.type());
        System.out.println("Kierowca Id = " + relationship.startNodeId());
        System.out.println("Trasa Id = " + relationship.endNodeId());
        System.out.println("asMap = " + relationship.asMap());
    }

    public static Result deleteEverything(Transaction transaction) {
        String command = "MATCH (n) DETACH DELETE n";
        System.out.println("Executing: " + command);
        return transaction.run(command);
    }
    public static Result readAllNodes(Transaction transaction) {
        String command =
                "MATCH (n)" +
                        "RETURN n";
        Result result = transaction.run(command);
        System.out.println("Wyniki: ");
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            for (Pair<String, Value> field : fields)
                printField(field);
        }
        return result;

    }
    public static Result readAllKierowca(Transaction transaction) {
        String command =
                "MATCH (k:Kierowca)" +
                        "RETURN k";
        Result result = transaction.run(command);
        System.out.println("Wyniki: ");
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            for (Pair<String, Value> field : fields)
                printField2(field);
        }
        return result;
    }
    public static Result readAllTrasy(Transaction transaction) {
        String command =
                "MATCH (t:Trasa)" +
                        "RETURN t";
        Result result = transaction.run(command);
        System.out.println("Wyniki: ");
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            for (Pair<String, Value> field : fields)
                printField2(field);
        }
        return result;
    }
    public static Result readAllRealtionships(Transaction transaction) {
        String command =
                "MATCH ()−[r]−>()" +
                        "RETURN r;";
        System.out.println("Executing: " + command);
        Result result = transaction.run(command);
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            for (Pair<String, Value> field : fields)
                printField(field);
        }
        return result;
    }
    public static Result wyszukaj(Transaction transaction) {
        String command;
        System.out.println("1- Wyszukaj kierowce po nazwisku");
        System.out.println("2- Wyszukaj kierowce po Imieniu");
        System.out.println("3- Wyszukaj kierowce po wieku");
        System.out.println("4- Wyszukaj kierowce po wynagrodzeniu");
        System.out.println("5- Wyszukaj trase po nazwie");
        System.out.println("6- Wyszukaj trase po przystanku początkowym");
        System.out.println("7- Wyszukaj trase po przystanku koncowym");
        System.out.println("8- Wyszukaj kierowców po trasie");
        Scanner scan=new Scanner(System.in);
        int wybor=scan.nextInt();

        switch (wybor) {

            case 1:
                System.out.println("Podaj nazwisko");
                String nazwisko=scan.next();
                command = "MATCH (k:Kierowca {nazwisko : '"+nazwisko+"' } )" +
                        "RETURN k";
                break;
            case 2:
            System.out.println("Podaj imie");
            String imie=scan.next();
            command = "MATCH (k:Kierowca {imie : '"+imie+"' } )" +
                    "RETURN k";
            break;
            case 3:
                System.out.println("Podaj wiek");
                String wiek=scan.next();
                command = "MATCH (k:Kierowca {wiek : "+wiek+" } )" +
                        "RETURN k";
                break;
            case 4:
                System.out.println("Podaj wynagrodzenie");
                String wynagrodzenie=scan.next();
                command = "MATCH (k:Kierowca {wynagrodzenie : "+wynagrodzenie+" } )" +
                        "RETURN k";
                break;
            case 5:
                System.out.println("Podaj nazwe");
                String nazwa=scan.next();
                command = "MATCH (t:Trasa {nazwa : '"+nazwa+"' } )" +
                        "RETURN t";
                break;
            case 6:
                System.out.println("Podaj przystanek poczatkowy");
                String pPocz=scan.next();
                command = "MATCH (t:Trasa {pPocz : '"+pPocz+"' } )" +
                        "RETURN t";
                break;
            case 7:
                System.out.println("Podaj przystanek koncowy");
                String pKon=scan.next();
                command = "MATCH (t:Trasa {pKon : '"+pKon+"' } )" +
                        "RETURN t";
                break;
            case 8:
                System.out.println("Podaj nazwe trasy");
                String nazwa_t=scan.next();
                command = "MATCH (t:Trasa {nazwa : '"+nazwa_t+ "' })<--(k:Kierowca)"+
                        "Return k";
                break;
            default:
                command =
                        "MATCH (n)" +
                                "RETURN n";
        }
        System.out.println(command);
        Result result = transaction.run(command);
        System.out.println("Wyniki: ");
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> fields = record.fields();
            for (Pair<String, Value> field : fields)
                printField2(field);
        }
        return result;
    }
    public static Result Modyfikacja(Transaction transaction) {
        System.out.println("1- zmiana imienia kierowcy");
        System.out.println("2- zmiana wieku kierowcy");
        System.out.println("3- zmiana wynagorodzenia kierowcy");
        System.out.println("4- Zmiana przystanku początkowego trasy");
        System.out.println("5- Zmiana przystanku końcowego trasy");
        Scanner scan = new Scanner(System.in);
        String command;
        String kierowcaName;
        String nazwa;
        int wybor=scan.nextInt();
        switch (wybor){
            case 1:
                System.out.println("Podaj nazwisko kierowcy");
                kierowcaName=scan.next();
                System.out.println("Podaj nowe imie");
                String nkierowcaImie=scan.next();
        command=
                "MATCH (k:Kierowca {nazwisko: '"+kierowcaName+"' } ) " +
                        "SET k.imie = '"+nkierowcaImie+"' ";
        System.out.println("Executing: " + command);
        return transaction.run(command);
            case 2:
                System.out.println("Podaj nazwisko kierowcy");
                kierowcaName=scan.next();
                System.out.println("Podaj nowe wiek");
                int nkierowcaWiek=scan.nextInt();
                command=
                        "MATCH (k:Kierowca {nazwisko: '"+kierowcaName+"' } ) " +
                                "SET k.wiek = "+nkierowcaWiek+" ";
                System.out.println("Executing: " + command);
                return transaction.run(command);
            case 3:
                System.out.println("Podaj nazwisko kierowcy");
                kierowcaName=scan.next();
                System.out.println("Podaj nowe wynagrodzenie");
                int nkierowcaWynagrodzenie=scan.nextInt();
                command=
                        "MATCH (k:Kierowca {nazwisko: '"+kierowcaName+"' } ) " +
                                "SET k.wynagrodzenie = "+nkierowcaWynagrodzenie+" ";
                System.out.println("Executing: " + command);
                return transaction.run(command);
            case 4:
                System.out.println("Podaj nazwe trasy");
               nazwa=scan.next();
                System.out.println("Podaj nowy przystanek początkowy");
                String p_pocz=scan.next();
                command=
                        "MATCH (t:Trasa {nazwa: '"+nazwa+"' } ) " +
                                "SET t.pPocz = '"+p_pocz+"' ";
                System.out.println("Executing: " + command);
                return transaction.run(command);
            case 5:
                System.out.println("Podaj nazwe trasy");
                nazwa=scan.next();
                System.out.println("Podaj nowy przystanek koncowy");
                String p_kon=scan.next();
                command=
                        "MATCH (t:Trasa {nazwa: '"+nazwa+"' } ) " +
                                "SET t.pKon = '"+p_kon+"' ";
                System.out.println("Executing: " + command);
                return transaction.run(command);
        }


        return null;
    }
    public static Result usun_kierowce(Transaction transaction) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwisko");
        String nazwisko=scan.next();
        System.out.println("Podaj imie");
        String imie=scan.next();
        String command = "MATCH (k:Kierowca {nazwisko : '"+nazwisko+"' , imie : '"+imie+"' }) DETACH DELETE k";
        System.out.println("Executing: " + command);
        return transaction.run(command);
    }
    public static Result usun_trase(Transaction transaction) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwe");
        String nazwa=scan.next();
        String command ="MATCH (t:Kierowca {nazwa : '"+nazwa+"' }) DETACH DELETE t";

        return transaction.run(command);
    }
    public static Result k_n_t(Transaction transaction) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj nazwe");
        String nazwa=scan.next();
        String command = "MATCH (t:Trasa {nazwa : '"+nazwa+ "' })<--(k:Kierowca)"+
                "Return count(*)";
        Result result = transaction.run(command);
        System.out.println("Trase obsługuje "+result.next().fields().get(0));
        return transaction.run(command);
    }





        public static void main (String[]args) throws Exception {
            try (Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jpassword"));
                 Session session = driver.session()) {
                Scanner scan=new Scanner(System.in);
                session.writeTransaction(tx -> deleteEverything(tx));
                boolean test=true;
                while(test) {

                    System.out.println("1-Dodanie Kierowcy");
                    System.out.println("2-Dodanie Trasy");
                    System.out.println("3- Utworzenie Relacji");
                    System.out.println("4- Wypisanie Tabel");
                    System.out.println("5- modyfikacja");
                    System.out.println("6- wyszukanie");
                    System.out.println("7- Ilośc kierowców na danej trasie");
                    System.out.println("8- usuwanie");


                    System.out.println("0- exit");
                    int wybor = scan.nextInt();
                    switch (wybor) {
                        case 1:
                            session.writeTransaction(tx -> createKierowca(tx));
                            break;
                        case 2:
                            session.writeTransaction(tx -> createTrasa(tx));
                            break;
                        case 3:
                            System.out.println("Wprowadz nazwisko kierowcy");
                            session.writeTransaction(tx -> readAllKierowca(tx));
                            String kierowca=scan.next();
                            System.out.println("Wprowdz nazwe trasy");
                            session.writeTransaction(tx -> readAllTrasy(tx));
                            String trasa=scan.next();
                            session.writeTransaction(tx -> createRelationship(tx, kierowca, trasa));
                            session.writeTransaction(tx -> readAllRealtionships(tx));

                            break;
                        case 4:
                            session.writeTransaction(tx -> readAllNodes(tx));
                            break;
                        case 5:
                            session.writeTransaction(tx -> Modyfikacja(tx));
                            break;
                        case 6:
                            session.writeTransaction(tx -> wyszukaj(tx));
                            break;
                        case 7:
                            session.writeTransaction(tx -> k_n_t(tx));
                            break;
                        case 8:
                            System.out.println("1 - usuwanie kierowcy");
                            System.out.println("2- usuwanie trasy");
                            int wybor2= scan.nextInt();
                            switch (wybor2) {
                                case 1:
                                    session.writeTransaction(tx -> usun_kierowce(tx));
                                    break;
                                case 2:
                                    session.writeTransaction(tx -> usun_trase(tx));
                                    break;
                            }
                            break;
                        case 0:
                            test=false;
                            break;
                    }
                }
            }
        }
    }
