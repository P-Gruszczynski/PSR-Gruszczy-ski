package Policja;

import com.datastax.oss.driver.api.core.CqlSession;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {
            KeyspaceManager keyspaceManager = new KeyspaceManager(session, "Policja");
            Scanner scan=new Scanner(System.in);
            keyspaceManager.dropKeyspace();
            keyspaceManager.selectKeyspaces();
            keyspaceManager.createKeyspace();
            keyspaceManager.useKeyspace();

           Policja_table tableManager = new Policja_table(session);
            tableManager.createTable();
            boolean test=true;
            while (test){
                System.out.println("1- dodanie");
                System.out.println("2- wypisanie wszystkiego");
                System.out.println("3- Wypisanie z warunkami");
                System.out.println("4- Modyfikacja");
                System.out.println("5- Usuwanie");
                System.out.println("6- zliczenie");
                System.out.println("7- drop");
                System.out.println("9- wysjcie");
                int wybor=scan.nextInt();
                switch (wybor){
                    case 1:
                        tableManager.insertIntoTable();
                        break;
                    case 2:
                        tableManager.selectFromTable();
                        break;
                    case 3:
                        tableManager.select();
                        break;
                    case 4:
                        tableManager.update();
                        break;
                    case 5:
                        tableManager.deleteFromTable();
                        break;
                    case 6:
                        tableManager.zlicz();
                        break;
                    case 7:
                        tableManager.dropTable();
                        break;
                    case 9:
                        test=false;
                        break;
                }

            }


        }
    }
}
