package glavnipaket.entiteti;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.NazivVrednostPolja;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Buyer implements EntitetZaBazu {
    public int buyer_id = NEISPRAVAN_ID;
    private String ime = NEINICIJALIZOVAN_STRING;
    private String prezime = NEINICIJALIZOVAN_STRING;
    private Set<Product> products;


    public Buyer(){}
    public Buyer(int buyer_id, String ime, String prezime){
        this.buyer_id = buyer_id;
        this.ime = ime;
        this.prezime = prezime;
    }
    public Buyer(String ime, String prezime){
        this.ime = ime;
        this.prezime = prezime;
    }


    @Override
    public String[] getNazivePolja() {
        return new String[]{"buyer_id", "ime", "prezime"};
    }

    @Override
    public void inicijalizujObjekatPodacimaIzBaze(ArrayList podaciIzBaze) {
        buyer_id = (int)podaciIzBaze.get(0);
        ime = (String)podaciIzBaze.get(1);
        prezime = (String)podaciIzBaze.get(2);
    }

    @Override
    public String toString(){
        return "buyer_id: " + buyer_id + "\n" +
                "ime: " + ime + "\n" +
                "prezime: " + prezime;
    }

    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPolja() {
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("buyer_id", buyer_id, false),
                new NazivVrednostPolja("ime", ime, true),
                new NazivVrednostPolja("prezime", prezime, true)};
    }

    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPoljaBezId(){
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("ime", ime, true),
                new NazivVrednostPolja("prezime", prezime, true)};
    }

    @Override
    public void dodajDrugiEntitetTabele(EntitetZaBazu entitet) {
        if(products==null){
            products = new HashSet<Product>();
        }
        products.add((Product)entitet);
    }

    @Override
    public Buyer dajInstancuSaDefaultVrednostima() {
        return new Buyer(NEISPRAVAN_ID, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING);
    }

    @Override
    public int getId() {
        return buyer_id;
    }

    @Override
    public String getNazivIdPolja(){
        return "buyer_id";
    }

    @Override
    public void inicijalizujPodacimaIzBaze(NazivVrednostPolja[] nvpIzBaze){
        buyer_id = (Integer)pronadjiPolje("buyer_id", nvpIzBaze);
        ime = (String)pronadjiPolje("ime", nvpIzBaze);
        prezime = (String)pronadjiPolje("prezime", nvpIzBaze);
    }


    public void inicijalizujPolje(NazivVrednostPolja nvp){
        switch(nvp.nazivPolja){
            case "buyer_id":
                buyer_id = (Integer) nvp.vrednostPolja;
                break;
            case "ime":
                ime = (String) nvp.vrednostPolja;
                break;
            case "prezime":
                prezime = (String) nvp.vrednostPolja;
                break;
            default:
        }
    }

    public Set<Product> getProducts(){
        return products;
    }
}
