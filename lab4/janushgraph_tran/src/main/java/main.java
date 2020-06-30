
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;


import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;


import java.util.Scanner;


public class main {
    public static void dodaj(GraphTraversalSource g){
        System.out.println("Podaj nazwisko kierowcy");
        Scanner scan=new Scanner(System.in);
        String nazwisko=scan.next();
        System.out.println("Podaj imię kierowcy");
        String imie=scan.next();
        System.out.println("Podaj wiek kierowcy");
        int wiek=scan.nextInt();
        System.out.println("Podaj wynagrodzenie");
        int wynagrodzenie=scan.nextInt();
        g.addV("Kierowcy").property("Nazwisko",nazwisko).property("Imie",imie).property("wiek",wiek).property("wynagrodzenie",wynagrodzenie).next();
    }
    public static void wyświetl(GraphTraversalSource g){
        System.out.println(g.V().hasLabel("Kierowcy").valueMap().toList());
    }
    public static void modyfikuj(GraphTraversalSource g){
        System.out.println("Podaj nazwisko");
        Scanner scan=new Scanner(System.in);
        String nazwisko=scan.next();
        System.out.println("Podaj imie");
        String imie=scan.next();
        System.out.println("1- zmiana nazwiska");
        System.out.println("2- zmiana imienia");
        System.out.println("3- zmiana wieku");
        System.out.println("4- zmiana wynagrodzenia");
        int wybor=scan.nextInt();
        switch (wybor){
            case 1:
                System.out.println("Podaj nowe nazwisko");
                String n_nazwisko=scan.next();
                g.V().has("Nazwisko",nazwisko).has("Imie",imie).property("Nazwisko",n_nazwisko).iterate();
                break;
            case 2:
                System.out.println("Podaj nowe imie");
                String n_imie=scan.next();
                g.V().has("Nazwisko",nazwisko).has("Imie",imie).property("Imie",n_imie).iterate();
                break;
            case 3:
                System.out.println("Podaj nowy wiek");
                int n_wiek=scan.nextInt();
                g.V().has("Nazwisko",nazwisko).has("Imie",imie).property("wiek",n_wiek).iterate();
                break;
            case 4:
                System.out.println("Podaj nowe wynagrodzenie");
                int n_wynagrodzenie=scan.nextInt();
                g.V().has("Nazwisko",nazwisko).has("Imie",imie).property("wynagrodzenie",n_wynagrodzenie).iterate();
                break;
        }
    }
    public static void usun(GraphTraversalSource g){
        wyświetl(g);
        System.out.println("Podaj nazwisko");
        Scanner scan=new Scanner(System.in);
        String nazwisko=scan.next();
        System.out.println("Podaj imie");
        String imie=scan.next();
        g.V().has("Nazwisko",nazwisko).has("Imie",imie).drop().iterate();
    }
    public static void wyszukaj(GraphTraversalSource g){
        Scanner scan=new Scanner(System.in);
        System.out.println("1-po nazwisku");
        System.out.println("2- po imieniu");
        System.out.println("3 - po wieku");
        System.out.println("4 - po wynagrodzeniu");
        int wybor =scan.nextInt();
        switch (wybor){
            case 1:
                System.out.println("Podaj nazwisko");
                String naz=scan.next();
                System.out.println("wynik wyszukiwania");
                System.out.println(g.V().has("Nazwisko",naz).valueMap().toList());
                break;
            case 2:
                System.out.println("Podaj imie");
                String imie=scan.next();
                System.out.println("wynik wyszukiwania");
                System.out.println(g.V().has("Imie",imie).valueMap().toList());
                break;
            case 3:
                System.out.println("Podaj wiek minimalny");
                int wiek_min=scan.nextInt();
                System.out.println("Podaj wiek maksymalny");
                int wiek_maks=scan.nextInt();
                System.out.println("wynik wyszukiwania");
                System.out.println(g.V().has("wiek", P.gt(wiek_min)).has("wiek",P.lt(wiek_maks)).valueMap().toList());
                break;
            case 4:
                System.out.println("Podaj wypłate minimalną");
                int wyp_min=scan.nextInt();
                System.out.println("Podaj wynagrodzenie maksymalne");
                int wyp_maks=scan.nextInt();
                System.out.println("wynik wyszukiwania");
                System.out.println(g.V().has("wynagrodzenie", P.gt(wyp_min)).has("wynagrodzenie",P.lt(wyp_maks)).valueMap().toList());
                break;
        }
    }
    public  static void drop(GraphTraversalSource g){
        g.V().hasLabel("Kierowcy").drop().iterate();
    }
    public static void main (String[]args) throws Exception {
        Graph graph = EmptyGraph.instance();
        GraphTraversalSource g = graph.traversal().withRemote("conf/remote-graph.properties");
        boolean test=true;
        Scanner scan=new Scanner(System.in);
        while (test){
            System.out.println("1- dodawanie kierowców");
            System.out.println("2- wyświetlanie kierowców");
            System.out.println("3- modyfikacja");
            System.out.println("4- usuwanie");
            System.out.println("5- Wyszukiwanie");
            System.out.println("6- Czyszczenie bazy");
            int wybor=scan.nextInt();
            switch (wybor) {
                case 1:
                    dodaj(g);
                    break;
                case 2:
                    wyświetl(g);
                    break;
                case 3:
                    modyfikuj(g);
                    break;
                case 4:
                    usun(g);
                    break;
                case 5:
                    wyszukaj(g);
                   break;
                case 6:
                    drop(g);
                    break;
            }
        }
        }
}