import java.io.Serializable;

public class Samochody implements Serializable {
    private static final long serialVersionUID = 1L;
    private String Marka;
    private String Wlasciciel;
    private int kwota_naprawy;
    private String status_naprawy;
    private String opis_usterki;
    private String data_przyjecia;
    private String Mechanik;

    public Samochody(String Marka, String Wlaściciel, int kwota, String status_naprawy, String opis_usterki, String data_przyjecia, String Mechanik) {
        this.Marka = Marka;
        this.Wlasciciel = Wlaściciel;
        this.kwota_naprawy = kwota;
        this.status_naprawy = status_naprawy;
        this.opis_usterki=opis_usterki;
        this.data_przyjecia = data_przyjecia;
        this.Mechanik = Mechanik;

    }

    public String getMarka() {
        return Marka;
    }

    public void setMarka(String Marka) {
        this.Marka = Marka;
    }

    public String getWlasciciel() {
        return Wlasciciel;
    }

    public int getKwota_naprawy() {
        return kwota_naprawy;
    }

    public String getStatus_naprawy() {
        return status_naprawy;
    }
    public String getOpis_usterki() {
        return opis_usterki;
    }

    public String getData_przyjecia() {
        return data_przyjecia;
    }
    public String getMechanik(){
        return Mechanik;
    }

    public void setWlasciciel(String Wlasciciel) {
        this.Wlasciciel = Wlasciciel;
    }

    public void setKwota_naprawy(int kwota_naprawy) {
        this.kwota_naprawy = kwota_naprawy;
    }

    public void setStatus_naprawy(String status) {
        this.status_naprawy = status;
    }
    public void setOpis_usterki(String opis){
        this.opis_usterki=opis;
    }
    public void setData_przyjecia(String data_przyjecia) {
        this.data_przyjecia = data_przyjecia;
    }
    public void setMechanik(String Mechanik){
        this.Mechanik=Mechanik;
    }

    public String toString() {
        return "Marka: " + Marka + " Właściciel: " + Wlasciciel + " kwota naprawy: " + kwota_naprawy + " status naprawy: " + status_naprawy + " Opis Usterki: "+opis_usterki+" data przyjęcia: " + data_przyjecia + " Mechanik: " + Mechanik;
    }
}
