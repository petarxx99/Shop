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
    private int broj_kupovina = 0;
    private Set<Product> products;


    public Buyer(){}
    public Buyer(int buyer_id, String ime, String prezime, int broj_kupovina){
        this.buyer_id = buyer_id;
        this.ime = ime;
        this.prezime = prezime;
        this.broj_kupovina = broj_kupovina;
    }

    public Buyer(String ime, String prezime, int broj_kupovina) {
        this.ime = ime;
        this.prezime = prezime;
        this.broj_kupovina = broj_kupovina;
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
                new NazivVrednostPolja("prezime", prezime, true),
                new NazivVrednostPolja("broj_kupovina", broj_kupovina, false)};
    }

    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPoljaBezId(){
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("ime", ime, true),
                new NazivVrednostPolja("prezime", prezime, true),
                new NazivVrednostPolja("broj_kupovina", broj_kupovina, false)};
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
        return new Buyer(NEISPRAVAN_ID, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING, 0);
    }

    @Override
    public int getId() {
        return buyer_id;
    }

    @Override
    public String getNazivIdPolja(){
        return "buyer_id";
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
            case "broj_kupovina":
                broj_kupovina = (int) nvp.vrednostPolja;
            default:
        }
    }

    public Set<Product> getProducts(){
        return products;
    }
}
