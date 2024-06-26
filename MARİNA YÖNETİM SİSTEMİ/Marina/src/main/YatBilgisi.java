package main;

import java.text.SimpleDateFormat;
import java.util.Date;

class YatBilgisi {

    private String uzunluk;
    private String ad;
    private String telefon;
    private Date girisSaati;


    public YatBilgisi(String uzunluk, String ad, String telefon, Date girisSaati) {
        this.uzunluk = uzunluk;
        this.ad = ad;
        this.telefon = telefon;
        this.girisSaati = girisSaati;
    }

    public String getuzunluk() {
        return uzunluk;
    }

    public String getAd() {
        return ad;
    }

    public String getTelefon() {
        return telefon;
    }

    public Date getGirisSaati() {
        return girisSaati;
}

    public String getGirisSaatiStr() {

        SimpleDateFormat tarihformati2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return tarihformati2.format(girisSaati);
    }
}
