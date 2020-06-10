package Policja;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateType;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Policja_table extends SimpleManager {

    public Policja_table(CqlSession session) {
        super(session);

    }
   Scanner scan = new Scanner(System.in);
    public void createTable() {
        CreateType createType = SchemaBuilder.createType("adres").withField("miejscowosc", DataTypes.TEXT)
                .withField("ulica",DataTypes.TEXT).withField("numer_domu", DataTypes.INT);
        session.execute(createType.build());
        CreateTable createTable = SchemaBuilder.createTable("Policja")
                .withPartitionKey("id", DataTypes.INT)
                .withColumn("Imie", DataTypes.TEXT)
                .withColumn("Nazwisko", DataTypes.TEXT)
                .withColumn("Wiek", DataTypes.INT)
                .withColumn("Adres", QueryBuilder.udt("adres"))
                .withColumn("Stopien", DataTypes.TEXT)
                .withColumn("Region",DataTypes.listOf(DataTypes.TEXT))
                .withColumn("Staz",DataTypes.INT)
                .withColumn("Wynagrodzenie", DataTypes.INT);
        session.execute(createTable.build());
    }

    public void insertIntoTable() {
        System.out.println("Podaj id");
        int id=scan.nextInt();
        System.out.println("Podaj imię");
        String imie=scan.next();
        System.out.println("Podaj nazwisko");
        String nazwisko=scan.next();
        System.out.println("Podaj wiek");
        int wiek=scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj Miejscowośc");
        String Miejscowosc=scan.next();
        System.out.println("Podaj ulicę");
        String ulica=scan.next();
        System.out.println("Podaj numer domu");
        int numer=scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj Stopień");
        String stopien=scan.nextLine();
        List regiony=new ArrayList();
        while (true){
            System.out.println("Podaj region patrolu lub q aby zakończyć wpisywanie");
            String region=scan.nextLine();
            if(region.equals("q")){
                break;
            }
            region="'"+region+"'";
            regiony.add((region));

        }
        System.out.println("Podaj staż w latach");
        int staz=scan.nextInt();
        System.out.println("Podaj Wysokosc wypłaty");
        int wyplata=scan.nextInt();
        Insert insert = QueryBuilder.insertInto("Policja", "Policja")
                .value("id", QueryBuilder.raw(String.valueOf(id)))
                .value("Imie",QueryBuilder.raw("'"+imie+"'"))
                .value("Nazwisko", QueryBuilder.raw("'"+nazwisko+"'"))
                .value("Wiek", QueryBuilder.raw(String.valueOf(wiek)))
                .value("Adres", QueryBuilder.raw("{miejscowosc : '"+Miejscowosc+"' , ulica : '"+ulica+"' , numer_domu : "+String.valueOf(numer)+"}"))
                .value("Stopien", QueryBuilder.raw("'"+stopien+"'"))
                .value("Region", QueryBuilder.raw(String.valueOf(regiony)))
                .value("Staz", QueryBuilder.raw(String.valueOf(staz)))
                .value("Wynagrodzenie", QueryBuilder.raw(String.valueOf(wyplata)));

        session.execute(insert.build());
    }

    public void deleteFromTable() {
        selectFromTable();
        System.out.println("Podaj id do usunięcia");
        int id=scan.nextInt();
        Delete delete = QueryBuilder.deleteFrom("Policja").whereColumn("id").isEqualTo(QueryBuilder.literal(id));
        session.execute(delete.build());
    }

    public void selectFromTable() {
        Select query = QueryBuilder.selectFrom("Policja").all();
        SimpleStatement statement = query.build();
        ResultSet resultSet = session.execute(statement);
        System.out.print("Policjanci: \n ");
        for (Row row : resultSet) {
            System.out.print(row.getInt("id") + ",");
            System.out.print(row.getString("Imie") + ", ");
            System.out.print(row.getString("Nazwisko") + ", ");
            System.out.print(row.getInt("Wiek") + ", ");
            UdtValue address = row.getUdtValue("adres");
            System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                    + address.getInt("numer_domu") + "}" + ", ");
            System.out.print(row.getString("Stopien") + ", ");
            System.out.print(row.getList("Region", String.class) + ", ");
            System.out.print(row.getInt("Staz") + ", ");
            System.out.print(row.getInt("Wynagrodzenie"));
            System.out.println();
        }
    }
    public void select() {
        System.out.println("1- wyszukiwanie po id");
        System.out.println("2- wyszukiwanie po Imieniu");
        System.out.println("3-Wyszukiwanie po Nazwisku");
        System.out.println("4- Wyszukiwanie po stopniu");
        System.out.println("5- Wyszukiwanie po stażu");
        int wybor = scan.nextInt();
        switch (wybor) {
            case 1:
                System.out.println("Podaj id");
                String id = scan.next();
                String statement = "SELECT * FROM Policja Where id = " + id + ";";
                ResultSet resultSet = session.execute(statement);
                for (Row row : resultSet) {
                    System.out.print("Policjanci: \n ");
                    System.out.print(row.getInt("id") + ",");
                    System.out.print(row.getString("Imie") + ", ");
                    System.out.print(row.getString("Nazwisko") + ", ");
                    System.out.print(row.getInt("Wiek") + ", ");
                    UdtValue address = row.getUdtValue("adres");
                    System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                            + address.getInt("numer_domu") + "}" + ", ");
                    System.out.print(row.getString("Stopien") + ", ");
                    System.out.print(row.getList("Region", String.class) + ", ");
                    System.out.print(row.getInt("Staz") + ", ");
                    System.out.print(row.getInt("Wynagrodzenie"));
                    System.out.println();
                }
                System.out.println();
                break;
            case 2:
                System.out.println("Podaj imie");
                String imie=scan.next();
                 statement = "SELECT * FROM Policja Where Imie = '" + imie + "' ALLOW FILTERING ;";
                 resultSet = session.execute(statement);
                for (Row row : resultSet) {
                    System.out.print("Policjanci: \n ");
                    System.out.print(row.getInt("id") + ",");
                    System.out.print(row.getString("Imie") + ", ");
                    System.out.print(row.getString("Nazwisko") + ", ");
                    System.out.print(row.getInt("Wiek") + ", ");
                    UdtValue address = row.getUdtValue("adres");
                    System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                            + address.getInt("numer_domu") + "}" + ", ");
                    System.out.print(row.getString("Stopien") + ", ");
                    System.out.print(row.getList("Region", String.class) + ", ");
                    System.out.print(row.getInt("Staz") + ", ");
                    System.out.print(row.getInt("Wynagrodzenie"));
                    System.out.println();
                }
                System.out.println();
                break;
            case 3:
                System.out.println("Podaj nazwisko");
                String nazwisko=scan.next();
                statement = "SELECT * FROM Policja Where Nazwisko = '" + nazwisko + "'ALLOW FILTERING;";
                resultSet = session.execute(statement);
                for (Row row : resultSet) {
                    System.out.print("Policjanci: \n ");
                    System.out.print(row.getInt("id") + ",");
                    System.out.print(row.getString("Imie") + ", ");
                    System.out.print(row.getString("Nazwisko") + ", ");
                    System.out.print(row.getInt("Wiek") + ", ");
                    UdtValue address = row.getUdtValue("adres");
                    System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                            + address.getInt("numer_domu") + "}" + ", ");
                    System.out.print(row.getString("Stopien") + ", ");
                    System.out.print(row.getList("Region", String.class) + ", ");
                    System.out.print(row.getInt("Staz") + ", ");
                    System.out.print(row.getInt("Wynagrodzenie"));
                    System.out.println();
                }
                System.out.println();
                break;
            case 4:
                System.out.println("Podaj stopien");
                String stopien=scan.next();
                statement = "SELECT * FROM Policja Where Stopien = '" + stopien + "'ALLOW FILTERING;";
                resultSet = session.execute(statement);
                for (Row row : resultSet) {
                    System.out.print("Policjanci: \n ");
                    System.out.print(row.getInt("id") + ",");
                    System.out.print(row.getString("Imie") + ", ");
                    System.out.print(row.getString("Nazwisko") + ", ");
                    System.out.print(row.getInt("Wiek") + ", ");
                    UdtValue address = row.getUdtValue("adres");
                    System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                            + address.getInt("numer_domu") + "}" + ", ");
                    System.out.print(row.getString("Stopien") + ", ");
                    System.out.print(row.getList("Region", String.class) + ", ");
                    System.out.print(row.getInt("Staz") + ", ");
                    System.out.print(row.getInt("Wynagrodzenie"));
                    System.out.println();
                }
                System.out.println();
                break;
            case 5:
                System.out.println("1 - równy");
                System.out.println(("2 - mniejszy"));
                System.out.println("3-Większy");
                int wybor2=scan.nextInt();
                System.out.println("Podaj staż");
                switch (wybor2){
                    case 1:
                        int staz=scan.nextInt();
                        statement = "SELECT * FROM Policja Where Staz = " +staz + " ALLOW FILTERING;";
                        resultSet = session.execute(statement);
                        for (Row row : resultSet) {
                            System.out.print("Policjanci: \n ");
                            System.out.print(row.getInt("id") + ",");
                            System.out.print(row.getString("Imie") + ", ");
                            System.out.print(row.getString("Nazwisko") + ", ");
                            System.out.print(row.getInt("Wiek") + ", ");
                            UdtValue address = row.getUdtValue("adres");
                            System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                                    + address.getInt("numer_domu") + "}" + ", ");
                            System.out.print(row.getString("Stopien") + ", ");
                            System.out.print(row.getList("Region", String.class) + ", ");
                            System.out.print(row.getInt("Staz") + ", ");
                            System.out.print(row.getInt("Wynagrodzenie"));
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    case 2:
                        staz=scan.nextInt();
                        statement = "SELECT * FROM Policja Where Staz < " +staz + " ALLOW FILTERING;";
                        resultSet = session.execute(statement);
                        for (Row row : resultSet) {
                            System.out.print("Policjanci: \n ");
                            System.out.print(row.getInt("id") + ",");
                            System.out.print(row.getString("Imie") + ", ");
                            System.out.print(row.getString("Nazwisko") + ", ");
                            System.out.print(row.getInt("Wiek") + ", ");
                            UdtValue address = row.getUdtValue("adres");
                            System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                                    + address.getInt("numer_domu") + "}" + ", ");
                            System.out.print(row.getString("Stopien") + ", ");
                            System.out.print(row.getList("Region", String.class) + ", ");
                            System.out.print(row.getInt("Staz") + ", ");
                            System.out.print(row.getInt("Wynagrodzenie"));
                            System.out.println();
                        }
                        System.out.println();
                        break;
                    case 3:
                    staz=scan.nextInt();
                    statement = "SELECT * FROM Policja Where Staz > " +staz + " ALLOW FILTERING;";
                    resultSet = session.execute(statement);
                    for (Row row : resultSet) {
                        System.out.print("Policjanci: \n ");
                        System.out.print(row.getInt("id") + ",");
                        System.out.print(row.getString("Imie") + ", ");
                        System.out.print(row.getString("Nazwisko") + ", ");
                        System.out.print(row.getInt("Wiek") + ", ");
                        UdtValue address = row.getUdtValue("adres");
                        System.out.print("{" + address.getString("miejscowosc") + ", " + address.getString("ulica") + ", "
                                + address.getInt("numer_domu") + "}" + ", ");
                        System.out.print(row.getString("Stopien") + ", ");
                        System.out.print(row.getList("Region", String.class) + ", ");
                        System.out.print(row.getInt("Staz") + ", ");
                        System.out.print(row.getInt("Wynagrodzenie"));
                        System.out.println();
                    }
                    System.out.println();
                    break;
                }
                break;
        }
    }
    public void update(){
        System.out.println("1- zmiana imienia");
        System.out.println("2- zmiana nazwiska");
        System.out.println("3- zmiana Wieku");
        System.out.println("4- Zmiana adresu");
        System.out.println("5- Zmiana Stopnia");
        System.out.println("6- Zmiana regionu");
        System.out.println("7- Zmiana Stazu");
        System.out.println("8- Zmiana Wynagrodzenia");
        int wybor=scan.nextInt();
        selectFromTable();
        switch (wybor) {
            case 1:
                System.out.println("Podaj id");
                int id = scan.nextInt();
                System.out.println("Podaj nowe imie");
                String imie = scan.next();
                Update update = QueryBuilder.update("Policja").setColumn("Imie", QueryBuilder.literal(imie)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 2:
                System.out.println("Podaj id");
                id = scan.nextInt();
                System.out.println("Podaj nowe nazwisko");
                String nazwisko = scan.next();
                update = QueryBuilder.update("Policja").setColumn("Nazwisko", QueryBuilder.literal(nazwisko)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 3:
                System.out.println("Podaj id");
                id = scan.nextInt();
                System.out.println("Podaj nowy wiek");
               int Wiek = scan.nextInt();
                update = QueryBuilder.update("Policja").setColumn("Wiek", QueryBuilder.literal(Wiek)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 4:
                System.out.println("Podaj id");
                id=scan.nextInt();
                System.out.println("Podaj nowa miejscowosc");
                String miejscowosc  =scan.next();
                System.out.println("Podaj nowa ulice");
                String ulica  =scan.next();
                System.out.println("Podaj nowa numer domu");
                int numer  =scan.nextInt();
               String query="UPDATE Policja SET adres.miejscowosc = '"+miejscowosc+"' , adres.ulica = '"+ulica+"' , adres.numer_domu = "+numer+" WHERE id = "+id+";";
                ResultSet resultSet = session.execute(query);
                break;
            case 5:
                System.out.println("Podaj id");
                id = scan.nextInt();
                System.out.println("Podaj nowy stopien");
                String stopien = scan.next();
                update = QueryBuilder.update("Policja").setColumn("Stopien", QueryBuilder.literal(stopien)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 6:
                System.out.println("Podaj id");
                id = scan.nextInt();
                List regiony = new ArrayList();
                while (true) {
                    System.out.println("Podaj region patrolu lub q aby zakończyć wpisywanie");
                    String region = scan.next();
                    if (region.equals("q")) {
                        break;
                    }
                    region = region;
                    regiony.add((region));

                }
                update = QueryBuilder.update("Policja").setColumn("Region", QueryBuilder.literal(regiony)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 7:
                System.out.println("Podaj id");
                id = scan.nextInt();
                System.out.println("Podaj nowy staz");
                int staz = scan.nextInt();
                update = QueryBuilder.update("Policja").setColumn("Staz", QueryBuilder.literal(staz)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
            case 8:
                System.out.println("Podaj id");
                id = scan.nextInt();
                System.out.println("Podaj nowe wynagrodzenie");
                int wynagrodzenie = scan.nextInt();
                update = QueryBuilder.update("Policja").setColumn("Wynagrodzenie", QueryBuilder.literal(wynagrodzenie)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
                session.execute(update.build());
                break;
        }
        }
    public void zlicz(){
        System.out.println("Podaj stopien dla którego chcesz zliczyć wynagrodzenie");
        String stopien=scan.next();
        int wynagrodzenia=0;
        String statement = "SELECT Wynagrodzenie FROM Policja Where Stopien = '" + stopien + "' ALLOW FILTERING;";
        ResultSet resultSet = session.execute(statement);
        for (Row row : resultSet) {
            wynagrodzenia=wynagrodzenia+(row.getInt("Wynagrodzenie"));

        }
        System.out.println("Wynagrodzenia: "+wynagrodzenia);
    }


    public void dropTable() {
        Drop drop = SchemaBuilder.dropTable("Policja");
        executeSimpleStatement(drop.build());
    }
}
