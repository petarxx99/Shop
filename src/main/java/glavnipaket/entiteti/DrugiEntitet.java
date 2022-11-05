package glavnipaket.entiteti;

import glavnipaket.EntitetZaBazu;

public class DrugiEntitet {

    private int brojKupovina;
    public final EntitetZaBazu entitetIzDrugeTabele;

    public DrugiEntitet(EntitetZaBazu entitetIzDrugeTabele){
        this.entitetIzDrugeTabele = entitetIzDrugeTabele;
    }

    public int getBrojKupovina(){return brojKupovina;}

    public void dodajKupovinu(){
        brojKupovina++;
    }

    public boolean izbrisiKupovinuDaLiJeOstaloJosKupovina(){
        brojKupovina--;
        return brojKupovina > 0;
    }

    public boolean daLiJeOstaloJosKupovina(){
        return brojKupovina > 0;
    }
}
