import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
public class main {
    static void put(HazelcastInstance instance) throws UnknownHostException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj Marke Samochodu");
        String Marka = scan.nextLine();
        System.out.println("Podaj Właściciela Samochodu");
        String Wlasciciel = scan.nextLine();
        System.out.println("Podaj kwote naprawy");
        int kwota = scan.nextInt();
        scan.nextLine();
        System.out.println("Podaj etap naprawy");
        String status = scan.nextLine();
        System.out.println("Podaj ustarkę");
        String usterka = scan.nextLine();
        System.out.println("Podaj date przyjęcia");
        String data = scan.nextLine();
        System.out.println("Podaj mechanika zajmującego się samochodem");
        String Mechanik = scan.nextLine();
        final Random r = new Random(System.currentTimeMillis());
        Map<Long, Samochody> samochody = instance.getMap("samochody");
        Long key = (long) Math.abs(r.nextInt());
        Samochody samochod = new Samochody(Marka, Wlasciciel, kwota, status, usterka, data, Mechanik);
        samochody.put(key, samochod);
        System.out.println("Dodano samochód o kluczu" + key + "o wartościach" + samochod);


    }

    static void get(HazelcastInstance instance) throws UnknownHostException {
        IMap<Long, Samochody> samochody = instance.getMap("samochody");
        System.out.println("Wszystkie serwisy: ");
        for (Entry<Long, Samochody> e : samochody.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }

    }
    static  void modyfikacja(HazelcastInstance instance, long key) {
        System.out.println("1- Zmiana marki \n 2 - Zmiana właściciela \n 3- Zmiana kwoty naprawy \n 4- Zmiana etapu naprawy\n 5- Zmiana Opisu usterki\n 6- Zmiana daty przyjęcia\n 7- zmiana mechanika \n ");
        Scanner scan = new Scanner(System.in);
        String wybor = scan.nextLine();
        System.out.println("Wprowadz nowe dane");
        String status = scan.nextLine();
        IMap<Long, Samochody> samochody = instance.getMap("samochody");
        Samochody nowy=samochody.get(key);
        if(wybor.equals("1")){
            nowy.setMarka(status);
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("2")){
            nowy.setWlasciciel(status);
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("3")){
            nowy.setKwota_naprawy(Integer.parseInt(status));
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("4")) {
            nowy.setStatus_naprawy(status);
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("5")){
            nowy.setOpis_usterki(status);
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("6")){
            nowy.setData_przyjecia(status);
            samochody.remove(key);
            samochody.set(key,nowy);
        }
        if(wybor.equals("7")){
            nowy.setMechanik("Status");
            samochody.remove(key);
            samochody.set(key,nowy);
        }

    }
    static void delete(String Marka, String Wlasciciel, HazelcastInstance instance) throws UnknownHostException {
        Long key;
        IMap<Long, Samochody> samochody = instance.getMap("samochody");
        for (Entry<Long, Samochody> e : samochody.entrySet()) {
            if (e.getValue().getWlasciciel().equals(Wlasciciel) && e.getValue().getMarka().equals(Marka)) {
                key = e.getKey();
                samochody.remove(key);
                System.out.println("Usunięto");
            }
        }

    }
    static void zlicz(HazelcastInstance instance) throws UnknownHostException{
        IMap<Long, Samochody> samochody = instance.getMap("samochody");
        int suma=0;
        System.out.println("Podaj dane Klienta");
        Scanner scan = new Scanner(System.in);
        String Klient= scan.nextLine();
        for (Entry<Long, Samochody> e : samochody.entrySet()) {
            if (e.getValue().getWlasciciel().equals(Klient)) {
                suma = suma + e.getValue().getKwota_naprawy();
            }
        }
        System.out.println("W sumie "+ suma);
    }
    public static void main(String[] args  ) throws UnknownHostException{
        Config config = HConfig.getConfig();
        Scanner scann=new Scanner(System.in);
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        while(true){
            System.out.print("1 - Dodadnie do bazy \n 2- wyświetlenie bazy \n 3-usunięcie z bazy \n 4- modyfikacja \n 5- zliczenie kosztów naprawy  \n q- koniec programu \n");
            String wybor = scann.nextLine();
            if(wybor.equals("1")){
             put(instance);
            }
            if(wybor.equals("2")){
                get(instance);
            }
            if(wybor.equals("3")){
                System.out.println("Podaj własciciela pojazdu");
                String wlasciciel=scann.nextLine();
                System.out.println("Podaj marke pojazdu");
                String marka=scann.nextLine();
                delete(marka,wlasciciel,instance);
            }
            if (wybor.equals("4")){
                System.out.println("Podaj klucz");
                Long key=scann.nextLong();
                scann.nextLine();
                modyfikacja(instance,key);
            }
            if (wybor.equals("5")){
                zlicz(instance);
            }
            if (wybor.equals("q")){
                instance.getLifecycleService().shutdown();
                break;
            }
        }
    }

}