package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.entiteti.Product;
import glavnipaket.strane.util.HtmlUtil;
import glavnipaket.strane.util.MojException;
import glavnipaket.strane.util.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = {"/Shop/products"})
public class ProductServlet extends HttpServlet{

    public final String USLOVI_POSTOJE = "uslovipostoje";
    public final String USLOV = "uslov";

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
        BazaPodataka baza = new BazaPodataka();
        ArrayList<String> uslovi = pronadjiUslove(request);

        try {
            ArrayList<EntitetZaBazu> products = baza.prikaziEntiteteIzJedneTabele("products", new Product(), uslovi);
            String tabelaHtml = HtmlUtil.napraviHtmlTabelu(products);
            HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Products", tabelaHtml);
        } catch(Exception exception){
            if(prvaProba){
                prvaProba = false;
                obradiPodatke(request, response);
            } else {
                exception.printStackTrace();
                HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Product", UrlUtil.EXCEPTION_PRILIKOM_PRIKAZIVANJA_TABELE);
            }
        }
    }

    public static ArrayList<String> pronadjiUslove(HttpServletRequest request){
        ArrayList<String> uslovi = new ArrayList<>();

        if(UrlUtil.uslovPostoji("productidcheckbox", request)) {
            uslovi.add("products.product_id = " + request.getParameter("productid"));
        }
        if(UrlUtil.uslovPostoji("nazivcheckbox", request)) {
            uslovi.add("products.naziv = '" + request.getParameter("naziv") + "'");
        }
        if(UrlUtil.uslovPostoji("proizvodjaccheckbox", request)){
            uslovi.add("products.proizvodjac = '" + request.getParameter("proizvodjac") + "'");
        }
        if(UrlUtil.uslovPostoji("drzavaproizvodnjecheckbox", request)){
            uslovi.add("products.drzava_proizvodnje = '" + request.getParameter("drzavaproizvodnje") + "'");
        }

        if(UrlUtil.uslovPostoji("cenacheckbox", request)){
            uslovi.add("products.cena >= " + request.getParameter("minimalnacena"));
            uslovi.add("products.cena <= " + request.getParameter("maksimalnacena"));
        }

        if(uslovi.size() == 0){
            return BazaPodataka.NEMA_USLOVA;
        }
        return uslovi;
    }

}
