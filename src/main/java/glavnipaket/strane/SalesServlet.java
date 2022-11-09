package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;
import glavnipaket.entiteti.Buyer;
import glavnipaket.entiteti.Product;
import glavnipaket.strane.util.HtmlUtil;
import glavnipaket.strane.util.MojException;
import glavnipaket.strane.util.NemaEntitetaUBazi;
import glavnipaket.strane.util.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/Shop/sales"})
public class SalesServlet extends HttpServlet{

    public static final String USLOVI_POSTOJE = "uslovipostoje";
    public static final String USLOV = "uslov";
    private boolean prvaProba = true;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        obradiPodatke(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        obradiPodatke(request, response);
    }

    public void obradiPodatke(HttpServletRequest request, HttpServletResponse response){
        try {
            BazaPodataka baza = new BazaPodataka();
            String tabelaHtml = napraviHtmlTabeleProdajaUzPomocPodatakaIzBaze(request, response, baza);
            HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Sales", tabelaHtml);
        } catch(MojException exception){
            if(prvaProba){
                prvaProba = false;
                obradiPodatke(request, response);
            } else {
                exception.printStackTrace();
                HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Sales", UrlUtil.EXCEPTION_PRILIKOM_PRIKAZIVANJA_TABELE);
            }
        }
    }


    public static String napraviHtmlTabeleProdajaUzPomocPodatakaIzBaze(HttpServletRequest request, HttpServletResponse response, BazaPodataka baza) throws MojException{
        ArrayList<String> usloviProduct = ProductServlet.pronadjiUslove(request);
        ArrayList<String> usloviBuyer = BuyerServlet.pronadjiUslove(request);
        ArrayList<String> uslovi = spojiUrlUslove(usloviProduct, usloviBuyer);

        try {
            ArrayList<EntitetZaBazu> products = baza.prikaziEntiteteIzTabele("products", "buyers", "sales",
                    "product_id", "buyer_id",
                    new Product(), new Buyer(), uslovi);
            return napraviSalesTabelu(products);
        } catch(MojException exception){
            exception.printStackTrace();
            throw new MojException();
        }
    }

    public static String napraviSalesTabelu(ArrayList productsP) throws NemaEntitetaUBazi{
        ArrayList<Product> products = (ArrayList<Product>) productsP;
        if(products == null){
            throw new NemaEntitetaUBazi();
        }
        products = products.stream().filter(product -> product.getBuyers() != null).collect(Collectors.toCollection(ArrayList::new));

        if(products.size() <1){
            throw new NemaEntitetaUBazi();
        }

        int brojRedova= 0;
        for(Product p : products){
            brojRedova += p.getBuyers().size();
        }
        final int BROJ_KOLONA = new Product().getNaziveVrednostiPolja().length +
                new Buyer().getNaziveVrednostiPolja().length;

        NazivVrednostPolja[] nvpZaNazivKolonaTabeleProducts = new Product().getNaziveVrednostiPolja();
        NazivVrednostPolja[] nvpZaNazivKolonaTabeleBuyers = new Buyer().getNaziveVrednostiPolja();
        NazivVrednostPolja[] nvpZaNazivKolonaTabele = new NazivVrednostPolja[nvpZaNazivKolonaTabeleBuyers.length + nvpZaNazivKolonaTabeleProducts.length];
        System.arraycopy(nvpZaNazivKolonaTabeleProducts, 0, nvpZaNazivKolonaTabele, 0, nvpZaNazivKolonaTabeleProducts.length);
        System.arraycopy(nvpZaNazivKolonaTabeleBuyers, 0, nvpZaNazivKolonaTabele, nvpZaNazivKolonaTabeleProducts.length, nvpZaNazivKolonaTabeleBuyers.length);

        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"1\"> \n");
        sb.append("<h> \n");
        for(int j=0; j<BROJ_KOLONA; j++) {
            sb.append("<td> \n");
            sb.append(nvpZaNazivKolonaTabele[j].nazivPolja);
            sb.append("</td> \n");
            sb.append("</h> \n");
        }

        for(int i=0; i<products.size(); i++){
            NazivVrednostPolja[] nvpProducts = products.get(i).getNaziveVrednostiPolja();
            Set<Buyer> buyers = products.get(i).getBuyers();
            for(Buyer buyer : buyers) {
                NazivVrednostPolja[] nvpBuyers = buyer.getNaziveVrednostiPolja();
                NazivVrednostPolja[] nvpZaTabelu = new NazivVrednostPolja[nvpBuyers.length + nvpProducts.length];
                System.arraycopy(nvpProducts, 0, nvpZaTabelu, 0, nvpProducts.length);
                System.arraycopy(nvpBuyers, 0, nvpZaTabelu, nvpProducts.length, nvpBuyers.length);

                sb.append("<tr> \n");
                for (NazivVrednostPolja n : nvpZaTabelu) {
                    sb.append("<td> \n");
                    sb.append(n.vrednostPolja);
                    sb.append("</td> \n");
                }
                sb.append("</tr> \n");
            }
        }

        sb.append("</table>");
        return sb.toString();
    }

    public static ArrayList<String> spojiUrlUslove(ArrayList<String> uslovi1, ArrayList<String> uslovi2){
        if(uslovi1 == null && uslovi2==null) return null;
        if(uslovi1==null) return uslovi2;
        if(uslovi2==null) return uslovi1;

        ArrayList<String> spojeniUslovi = new ArrayList<>();
        spojeniUslovi.addAll(uslovi1);
        spojeniUslovi.addAll(uslovi2);
        return spojeniUslovi;
    }

}
