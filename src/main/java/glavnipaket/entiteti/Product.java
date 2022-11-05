package glavnipaket.entiteti;


import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.NazivVrednostPolja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Product implements EntitetZaBazu {

    public final BigDecimal NEINICIJALIZOVANA_CENA = new BigDecimal(-1);
    private int product_id = NEISPRAVAN_ID;
    private String naziv = NEINICIJALIZOVAN_STRING;
    private String proizvodjac = NEINICIJALIZOVAN_STRING;
    private String drzava_proizvodnje = NEINICIJALIZOVAN_STRING;
    private BigDecimal cena = NEINICIJALIZOVANA_CENA;
    private Set<Buyer> buyers;

    public Product(){}
    public Product(int product_id, String naziv, String proizvodjac, String drzava_proizvodnje, BigDecimal cena){
        this.product_id = product_id;
        this.naziv = naziv;
        this.proizvodjac = proizvodjac;
        this.drzava_proizvodnje = drzava_proizvodnje;
        this.cena = cena;
    }

    public Product(String naziv, String proizvodjac, String drzava_proizvodnje, BigDecimal cena) {
        this.naziv = naziv;
        this.proizvodjac = proizvodjac;
        this.drzava_proizvodnje = drzava_proizvodnje;
        this.cena = cena;
    }

    @Override
    public String[] getNazivePolja() {
        return new String[]{"product_id", "naziv", "proizvodjac", "drzava_proizvodnje", "cena"};
    }

    @Override
    public void inicijalizujObjekatPodacimaIzBaze(ArrayList podaciIzBaze) {
        product_id = (int)podaciIzBaze.get(0);
        naziv = (String)podaciIzBaze.get(1);
        proizvodjac = (String)podaciIzBaze.get(2);
        drzava_proizvodnje = (String)podaciIzBaze.get(3);
        cena = (BigDecimal)podaciIzBaze.get(4);
    }

    @Override
    public String toString(){
        return "product_id: " + product_id +"\n" +
                "naziv: " + naziv + "\n" +
                "proizvodjac: " + proizvodjac + "\n"+
                "drzava proizvodnje: " + drzava_proizvodnje + "\n"+
                "cena: " + cena;
    }


    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPolja() {
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("product_id", product_id, false),
                new NazivVrednostPolja("naziv", naziv, true),
                new NazivVrednostPolja("proizvodjac", proizvodjac, true),
                new NazivVrednostPolja("drzava_proizvodnje", drzava_proizvodnje, true),
                new NazivVrednostPolja("cena", cena, false)};
    }

    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPoljaBezId(){
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("naziv", naziv, true),
                new NazivVrednostPolja("proizvodjac", proizvodjac, true),
                new NazivVrednostPolja("drzava_proizvodnje", drzava_proizvodnje, true),
                new NazivVrednostPolja("cena", cena, false)};
    }

    @Override
    public void dodajDrugiEntitetTabele(EntitetZaBazu entitet) {
        if(buyers==null){
            buyers = new HashSet<Buyer>();
        }
        buyers.add((Buyer) entitet);
    }

    @Override
    public Product dajInstancuSaDefaultVrednostima() {
        return new Product(NEISPRAVAN_ID, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVANA_CENA);
    }

    @Override
    public int getId() {
        return product_id;
    }

    @Override
    public String getNazivIdPolja(){
        return "product_id";
    }

    @Override
    public void inicijalizujPodacimaIzBaze(NazivVrednostPolja[] nvpIzBaze){
        product_id = (Integer)pronadjiPolje("product_id", nvpIzBaze);
        naziv = (String) pronadjiPolje("naziv", nvpIzBaze);
        proizvodjac = (String) pronadjiPolje("proizvodjac", nvpIzBaze);
        drzava_proizvodnje = (String) pronadjiPolje("drzava_proizvodnje", nvpIzBaze);
        cena = (BigDecimal) pronadjiPolje("cena", nvpIzBaze);
    }

    public void inicijalizujPolje(NazivVrednostPolja nvp){
        switch(nvp.nazivPolja){
            case "product_id":
                product_id = (Integer) nvp.vrednostPolja;
                break;
            case "naziv":
                naziv = (String) nvp.vrednostPolja;
                break;
            case "proizvodjac":
                proizvodjac = (String) nvp.vrednostPolja;
                break;
            case "drzava_proizvodnje":
                drzava_proizvodnje = (String) nvp.vrednostPolja;
                break;
            case "cena":
                cena = (BigDecimal) nvp.vrednostPolja;
                break;
            default:
        }
    }

    public Set<Buyer> getBuyers(){
        return buyers;
    }

}

