//localhost:8080/Shop ukucati u pretrazivac

package glavnipaket;

import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;
import glavnipaket.entiteti.Buyer;
import glavnipaket.entiteti.Product;
import glavnipaket.strane.SalesServlet;
import glavnipaket.strane.util.HtmlUtil;
import glavnipaket.strane.util.MojException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class ProbniMain {

    public static void printArrayList(ArrayList list){
        for(Object o : list){
            System.out.println(o);
        }
    }

    public static void bazaProba(){
        String stringZaKonekciju = "jdbc:mysql://localhost:3306/sales_assignment?user=assignment_user&password=password";
        try(Connection connection = DriverManager.getConnection(stringZaKonekciju)){
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM buyers;");
            ArrayList imena = new ArrayList();
            while(rs.next()){
                imena.add(rs.getString("ime"));
            }
            printArrayList(imena);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        System.out.println("Hello world");
        BazaPodataka baza = new BazaPodataka("sales_assignment", "localhost", 3306, "assignment_user", "password");

        Product pProba1 = new Product("kiselo mleko", "imlek", "Srbija", new BigDecimal(100));
       // System.out.println(baza.ubaciteUBazu(pProba1, "products"));

        ArrayList<String> uslovi1 = new ArrayList<>();
        uslovi1.add("products.cena>50");
        ArrayList<Product> proizvodi = baza.prikaziKupovinePoUslovuPrekoListeProizvoda(uslovi1);
        System.out.println(proizvodi==null);
        System.out.println(proizvodi.size());
        for(Product p : proizvodi){
            System.out.println(p);
        }
        System.out.println("Ovo su bile kupovine po uslovu\n\n");
        proveraHtmlUtilTabela(proizvodi);

        System.out.println("\n Sada sledi sales proba\n");
        salesProba(proizvodi, baza);

        //proba(baza);

    }

    public static <T extends EntitetZaBazu> void proveraHtmlUtilTabela(ArrayList<T> entiteti){
        try {
            String strana = HtmlUtil.napraviHtmlTabelu(entiteti);
            System.out.println(strana);
        } catch(MojException exception){
            exception.printStackTrace();
        }
    }


    public static void salesProba(ArrayList productsP, BazaPodataka baza){
        ArrayList<String> uslovi = new ArrayList<>();
        uslovi.add("products.cena > 50");
        uslovi.add("products.drzava_proizvodnje = 'Srbija'");
        try {
            ArrayList<Product> products = (ArrayList<Product>)productsP;
            products = baza.prikaziEntiteteIzTabele("products", "buyers", "sales",
                    "product_id", "buyer_id",
                    new Product(), new Buyer(), uslovi);
            String tabelaHtml = SalesServlet.napraviSalesTabelu(products);
            System.out.println(tabelaHtml);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void proba(BazaPodataka baza){
        NazivVrednostPolja nvp1 = new NazivVrednostPolja("cena", 130, false);
        System.out.println("rows affected = " + baza.updejt(new NazivVrednostPolja[]{nvp1}, "products", "product_id", 3));

        Set<Product> kupovineJedneOsobe = baza.prikaziSveKupovineJedneOsobe(2);
        for(Product p : kupovineJedneOsobe){
            System.out.println(p);
        }


        Set<Buyer> kupci = baza.prikaziSveKupceJednogProizvoda(3);
        for(Buyer b : kupci){
            System.out.println(b);
        }

        Product p1 = new Product("pavlaka", "imlek", "Srbija", new BigDecimal(80));
        baza.ubaciteUBazu(p1, "products");
        Buyer b1 = new Buyer("Matija", "Mitic");
        baza.ubaciteUBazu(b1, "buyers");
        System.exit(0);

        System.out.println("\nSADA SLEDI buyers\n");


        ArrayList buyers = baza.prikaziEntiteteIzTabele("buyers", "products", "sales", "buyer_id", "product_id", new Buyer(), new Product(), BazaPodataka.NEMA_USLOVA);
        for(Object buyerO : buyers){
            Buyer buyer = (Buyer) buyerO;
            System.out.println(buyer);
            Set<Product> setProducts = buyer.getProducts();
            if(setProducts != null) {
                for (Product p : setProducts) {
                    System.out.println(p);
                }
                System.out.println("setProducts nije null");
            }
            System.out.println("\n\n");
        }

    }


    public static String napraviRedoveTeksta(ArrayList kupovine){
        StringBuilder sb = new StringBuilder();
        for(Object jednaKupovina : kupovine){
            for(Object stavka : (ArrayList)jednaKupovina){
                sb.append(stavka);
                sb.append("\n");
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }


    public static JTextArea staviTextFieldNaProzor(JFrame prozor){
        JTextArea tArea = new JTextArea(100, 50);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        tArea.setEditable(false);


        JScrollPane scrollPane = new JScrollPane(tArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        prozor.getContentPane().add(scrollPane);
        prozor.validate();
        return tArea;
    }

    public static JFrame napraviFrejm(int duzina, int visina){
        JFrame prozor = new JFrame("titl");
        prozor.setSize(duzina, visina);
        prozor.setLocationRelativeTo(null);
        prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prozor.setVisible(true);
        return prozor;
    }

    public static void ispisiTekstNaJFrejm(String tekst){
        JFrame frejm = napraviFrejm(500, 500);
        JTextArea ta = staviTextFieldNaProzor(frejm);
        ta.setText(tekst);
        frejm.validate();
    }
}
