package glavnipaket.baza;

 

import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import glavnipaket.EntitetZaBazu;
import glavnipaket.entiteti.Product;
import glavnipaket.entiteti.Buyer;



public class BazaPodataka {
        private String stringZaKonekciju;
        private String username = "assignment_user";
        private String lozinka = "password";
        private String imeBaze = "sales_assignment";
        private String adresaServera = "localhost";
        private int portServera = 3306;


        public static int NEISPRAVAN_ID=-1, SQL_EXCEPTION=-4;
        public static final ArrayList<String> NEMA_USLOVA = null;
        public static boolean bazaSeUpdejtuje;

        public BazaPodataka(){
            this.stringZaKonekciju = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", adresaServera, portServera, imeBaze, username, lozinka);
        }

        public BazaPodataka(String imeBaze, String adresaServera, int portServera, String username, String lozinka){
            this.imeBaze = imeBaze;
            this.adresaServera = adresaServera;
            this.portServera = portServera;
            this.username = username;
            this.lozinka = lozinka;
            this.stringZaKonekciju = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", adresaServera, portServera, imeBaze, username, lozinka);
        }



    public long ubaciteUBazu(EntitetZaBazu entitet, String imeTabele){
        NazivVrednostPolja[] naziviVrednostiPolja = entitet.getNaziveVrednostiPoljaBezId();
        String naziviPolja = spojiStringoveZapetom(NazivVrednostPolja.getNazivePolja(naziviVrednostiPolja));
        String vrednostiPolja = spojiStringoveZapetom(NazivVrednostPolja.getVrednostiPolja(naziviVrednostiPolja));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", imeTabele, naziviPolja, vrednostiPolja);
        try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            long idUbacenogEntiteta= rs.getLong(1);
            return idUbacenogEntiteta;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public void ubaciteUsales(int product_id, int buyer_id){
            String sql = String.format("INSERT INTO sales (product_id, buyer_id) VALUES (%s, %s);", product_id, buyer_id);
            executeUpdate(sql);
            String sqlProduct = String.format("UPDATE products SET products.broj_prodaja = (Select broj_prodaja WHERE product_id=%s) + 1 WHERE product_id=%s;", product_id, product_id);
            executeUpdate(sqlProduct);
            String sqlBuyer = String.format("UPDATE buyers SET broj_kupovina = (Select broj_kupovina WHERE buyer_id=%s) + 1 WHERE buyer_id=%s;", buyer_id, buyer_id);
            executeUpdate(sqlBuyer);
    }

    private void executeUpdate(String sql){
        try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }


    private ArrayList<EntitetZaBazu> izvuci2entitetaIzBaze(String sql, EntitetZaBazu instancaEntitetaZaPrikaz, EntitetZaBazu entitetDrugeTabele, NazivVrednostPolja[] nvpEntitetaZaPrikaz, NazivVrednostPolja[] nvpDrugeTabele){
       try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            ArrayList<EntitetZaBazu> sviEntitetiZaPrikaz = new ArrayList<>();
            int idPoslednjegEntiteta = NEISPRAVAN_ID;
            while(rs.next()){
                int id = rs.getInt(instancaEntitetaZaPrikaz.getNazivIdPolja());
                if(id != idPoslednjegEntiteta) {
                    EntitetZaBazu entitetZaPrikaz = instancaEntitetaZaPrikaz.getClass().newInstance();
                    for(NazivVrednostPolja nvp : nvpEntitetaZaPrikaz){
                        entitetZaPrikaz.inicijalizujPolje(new NazivVrednostPolja(nvp.nazivPolja, rs.getObject(nvp.nazivPolja, nvp.getKlasa()), nvp.imaNavodnike));
                    }
                    sviEntitetiZaPrikaz.add(entitetZaPrikaz);
                }
                idPoslednjegEntiteta = id;

                EntitetZaBazu entitetDobijenIzDrugeTabele = entitetDrugeTabele.getClass().newInstance();
                if(rs.getObject(entitetDrugeTabele.getNazivIdPolja()) != null) {
                    for (NazivVrednostPolja nvp : nvpDrugeTabele) {
                        entitetDobijenIzDrugeTabele.inicijalizujPolje(new NazivVrednostPolja(nvp.nazivPolja, rs.getObject(nvp.nazivPolja), nvp.imaNavodnike));
                    }
                    final int POSLEDNJI_INDEKS = sviEntitetiZaPrikaz.size() - 1;
                    EntitetZaBazu poslednjiEntitet = sviEntitetiZaPrikaz.get(POSLEDNJI_INDEKS);
                    poslednjiEntitet.dodajDrugiEntitetTabele(entitetDobijenIzDrugeTabele);
                }
            }
            return sviEntitetiZaPrikaz;
        } catch(SQLException | IllegalAccessException | InstantiationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public ArrayList<EntitetZaBazu> prikaziEntiteteIzManyToManyTabelePoUslovu(EntitetZaBazu instancaEntitetaZaPrikaz, EntitetZaBazu entitetDrugeKlase, String kljucPrveTabele, String kljucDrugeTabele, String prvaTabela, String drugaTabela, String manyToManyTabela, ArrayList<String> uslovi){
            String uslov = (uslovi==NEMA_USLOVA)? "(1=1)" : spojiUslove(uslovi);
            String sql = String.format("SELECT * FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s WHERE %s;", prvaTabela, manyToManyTabela, prvaTabela, kljucPrveTabele, manyToManyTabela, kljucPrveTabele, drugaTabela, drugaTabela, kljucDrugeTabele, manyToManyTabela, kljucDrugeTabele, uslov);


            NazivVrednostPolja[] nvpEntitetaZaPrikaz = instancaEntitetaZaPrikaz.getNaziveVrednostiPolja();
            NazivVrednostPolja[] nvpDrugogEntiteta = entitetDrugeKlase.getNaziveVrednostiPolja();

            return izvuci2entitetaIzBaze(sql, instancaEntitetaZaPrikaz, entitetDrugeKlase, nvpEntitetaZaPrikaz, nvpDrugogEntiteta);
    }


    public ArrayList prikaziKupovinePoUslovuPrekoListeProizvoda(ArrayList<String> uslovi){
        String uslov = (uslovi==NEMA_USLOVA)? "1=1" : spojiUslove(uslovi);

        Buyer buyer = new Buyer().dajInstancuSaDefaultVrednostima();
        Product product = new Product().dajInstancuSaDefaultVrednostima();

        String sql = String.format("SELECT * FROM sales LEFT JOIN buyers ON buyers.buyer_id=sales.buyer_id LEFT JOIN products ON products.product_id=sales.product_id WHERE " + uslov + ";");
        return izvuci2entitetaIzBaze(
                sql,
                product, buyer,
                product.getNaziveVrednostiPolja(), buyer.getNaziveVrednostiPolja());
    }


    public ArrayList prikaziEntiteteIzTabele(String imeTabele, String imeDrugeTabele, String imeManyToManyTabele, String kljucPrveTabele, String kljucDrugeTabele, EntitetZaBazu entitetPrveTabele, EntitetZaBazu entitetDrugeTabele, ArrayList<String> uslovi) {
            return prikaziEntiteteIzManyToManyTabelePoUslovu(
                    entitetPrveTabele.dajInstancuSaDefaultVrednostima(), entitetDrugeTabele.dajInstancuSaDefaultVrednostima(),
                    kljucPrveTabele, kljucDrugeTabele,
                    imeTabele, imeDrugeTabele, imeManyToManyTabele, uslovi);
    }


    public ArrayList<EntitetZaBazu> prikaziEntiteteIzJedneTabele(String imeTabele, EntitetZaBazu instancaEntiteta, ArrayList<String> uslovi){
            String uslov = (uslovi==NEMA_USLOVA)? "1=1" : spojiUslove(uslovi);
            String sql = "SELECT * FROM " + imeTabele + " WHERE " + uslov +";";
            NazivVrednostPolja[] nvpEntiteta = instancaEntiteta.getNaziveVrednostiPolja();

            try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                ArrayList<EntitetZaBazu> entitetiIzBaze = new ArrayList<>();
                while(rs.next()){
                    EntitetZaBazu entitetIzBaze = instancaEntiteta.getClass().newInstance();
                    for(NazivVrednostPolja nvp : nvpEntiteta){
                        entitetIzBaze.inicijalizujPolje(new NazivVrednostPolja(nvp.nazivPolja, rs.getObject(nvp.nazivPolja), nvp.imaNavodnike));
                    }
                    entitetiIzBaze.add(entitetIzBaze);
                }
                return entitetiIzBaze;
            } catch(SQLException | InstantiationException | IllegalAccessException exception){
                exception.printStackTrace();
                return null;
            }
    }



    private String spojiUslove(ArrayList<String> uslovi){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<uslovi.size()-1; i++){
            sb.append("(" + uslovi.get(i) + ")");
            sb.append(" AND ");
        }

        String poslednjiUslov = uslovi.get(uslovi.size() - 1);
        sb.append("(" + poslednjiUslov + ")");
        return sb.toString();
    }

    private String spojiStringoveZapetom(String[] stringovi){
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<stringovi.length; i++){
                if(i>0){
                    sb.append(", ");
                }

                sb.append(stringovi[i]);
            }
            return sb.toString();
    }


    public int izbrisiteEntitet(int id, String imeTabele, String imeKljuca){
        String sql = String.format("DELETE FROM %s WHERE %s=%s;", imeTabele, imeKljuca, id);
        try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            return st.executeUpdate(sql);
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }


    public int update(NazivVrednostPolja[] poljaZaUpdejt, String imeTabele, String imeKljuca, int id){
        final String SQL = napraviSqlUpdate(poljaZaUpdejt, imeTabele, imeKljuca, id);
        try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            int ROWS_AFFECTED = st.executeUpdate(SQL);
            return ROWS_AFFECTED;
        } catch(SQLException exception){
            exception.printStackTrace();
            return SQL_EXCEPTION;
        }
    }

    private String napraviSqlUpdate(NazivVrednostPolja[] poljaZaUpdejt, String imeTabele, String imeKljuca, int id){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE " + imeTabele + " SET ");
        sql.append(napraviUpdejtSQLIzPolja(poljaZaUpdejt, imeTabele));
        sql.append(" WHERE " + imeTabele + "." + imeKljuca + "=" + id);
        sql.append(";");
        return sql.toString();
    }

    private String napraviUpdejtSQLIzPolja(NazivVrednostPolja[] svaPolja, String imeTabele){
        ArrayList<NazivVrednostPolja> poljaZaUpdejt = Arrays.stream(svaPolja).filter(polje -> polje.vrednostPolja != null).collect(Collectors.toCollection(ArrayList::new));
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<poljaZaUpdejt.size(); i++){
            if(i>0){
                sb.append(", ");
            }

            if(!poljaZaUpdejt.get(i).imaNavodnike){
                sb.append(imeTabele + "." + poljaZaUpdejt.get(i).nazivPolja + "=" + poljaZaUpdejt.get(i).vrednostPolja);
            } else {
                sb.append(imeTabele + "." + poljaZaUpdejt.get(i).nazivPolja + "=" + "'" + poljaZaUpdejt.get(i).vrednostPolja + "'");
            }
        }
        return sb.toString();
    }


}
