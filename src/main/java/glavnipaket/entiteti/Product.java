package glavnipaket.entiteti;


import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.NazivVrednostPolja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Product implements EntitetZaBazu {

    public final BigDecimal NEINICIJALIZOVANA_CENA = new BigDecimal(-1);
    private int product_id = NEISPRAVAN_ID;
    private String naziv = NEINICIJALIZOVAN_STRING;
    private String proizvodjac = NEINICIJALIZOVAN_STRING;
    private String drzava_proizvodnje = NEINICIJALIZOVAN_STRING;
    private BigDecimal cena = NEINICIJALIZOVANA_CENA;
    private int broj_prodaja = 0;
    private Set<Buyer> buyers;

    public Product(){}
    public Product(int product_id, String naziv, String proizvodjac, String drzava_proizvodnje, BigDecimal cena, int broj_prodaja){
        this.product_id = product_id;
        this.naziv = naziv;
        this.proizvodjac = proizvodjac;
        this.drzava_proizvodnje = drzava_proizvodnje;
        this.cena = cena;
        this.broj_prodaja = broj_prodaja;
    }

    public Product(String naziv, String proizvodjac, String drzava_proizvodnje, BigDecimal cena, int broj_prodaja) {
        this.naziv = naziv;
        this.proizvodjac = proizvodjac;
        this.drzava_proizvodnje = drzava_proizvodnje;
        this.cena = cena;
        this.broj_prodaja = broj_prodaja;
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
                new NazivVrednostPolja("cena", cena, false),
                new NazivVrednostPolja("broj_prodaja", broj_prodaja, false)};
    }

    @Override
    public NazivVrednostPolja[] getNaziveVrednostiPoljaBezId(){
        return new NazivVrednostPolja[]{
                new NazivVrednostPolja("naziv", naziv, true),
                new NazivVrednostPolja("proizvodjac", proizvodjac, true),
                new NazivVrednostPolja("drzava_proizvodnje", drzava_proizvodnje, true),
                new NazivVrednostPolja("cena", cena, false),
                new NazivVrednostPolja("broj_prodaja", broj_prodaja, false)};
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
        return new Product(NEISPRAVAN_ID, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVAN_STRING, NEINICIJALIZOVANA_CENA, 0);
    }

    @Override
    public int getId() {
        return product_id;
    }

    @Override
    public String getNazivIdPolja(){
        return "product_id";
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
            case "broj_prodaja":
                broj_prodaja = (int) nvp.vrednostPolja;
            default:
        }
    }

    public Set<Buyer> getBuyers(){
        return buyers;
    }


}

