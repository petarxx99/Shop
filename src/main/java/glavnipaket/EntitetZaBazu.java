package glavnipaket;

import glavnipaket.baza.NazivVrednostPolja;

import java.util.ArrayList;
import java.util.Set;

public interface EntitetZaBazu {

    public final int NEISPRAVAN_ID = -1;
    public final String NEINICIJALIZOVAN_STRING = ".";

    public String[] getNazivePolja();
    public void inicijalizujObjekatPodacimaIzBaze(ArrayList<String> podaciIzBaze);
    public void inicijalizujPodacimaIzBaze(NazivVrednostPolja[] nvpIzBaze);
    public void inicijalizujPolje(NazivVrednostPolja nvp);
    public NazivVrednostPolja[] getNaziveVrednostiPolja();
    public NazivVrednostPolja[] getNaziveVrednostiPoljaBezId();
    public void dodajDrugiEntitetTabele(EntitetZaBazu entitet);
    public EntitetZaBazu dajInstancuSaDefaultVrednostima();
    public int getId();
    public String getNazivIdPolja();

    default public Object pronadjiPolje(String imePolja, NazivVrednostPolja[] svaPolja){
        for(NazivVrednostPolja nvp : svaPolja){
            if(nvp.nazivPolja.equals(imePolja)){
                return nvp;
            }
        }
        return null;
    }


}
