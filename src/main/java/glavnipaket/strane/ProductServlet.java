package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;
import glavnipaket.entiteti.Product;
import glavnipaket.strane.util.HtmlUtil;
import glavnipaket.strane.util.UrlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet{

    public final static String UPDATE_URL = "updateproductcheckbox";
    public final static String ID_ZA_UPDATE_URL = "idupdateproduct";
    public final static String UPDATE_NAZIV_URL = "nazivupdateproduct";
    public final static String UPDATE_PROIZVODJAC_URL = "proizvodjacupdateproduct";
    public final static String UPDATE_DRZAVA_URL = "drzavaproizvodnjeupdateproduct";
    public final static String UPDATE_CENA_URL = "cenaupdateproduct";
    public final static String CHECKBOX = "checkbox";
    public final static String ID_ZA_BRISANJE_URL = "idproductazabrisanje";
    public final static String OBRISI_PRODUCT_URL = "obrisiproduct";
    public final static String IME_TABELE = "products";
    public final static String IME_KLJUCA_TABELE = "product_id";

    public final String USLOVI_POSTOJE = "uslovipostoje";
    public final String USLOV = "uslov";
    private boolean prvaProba = true;
    private static final String FORMA_ZA_NOVI_PROIZVOD =
                    "    <form action=\"index.jsp\" method=\"POST\">\n" +
                    "        <label for=\"productubazu\"> Stiklirajte ovde i popunite polja kako biste ubacili proizvod (product) u bazu. </label>\n" +
                    "        <input type=\"checkbox\" name=\"productubazu\"> <br>\n" +
                    "\n" +
                    "\n" +
                    "        <label for=\"naziv\">naziv: </label>\n" +
                    "        <input type=\"text\" name=\"naziv\"> <br>\n" +
                    "\n" +
                    "        <label for=\"drzavaproizvodnje\">drzava proizvodnje: </label>\n" +
                    "        <input type=\"text\" name=\"drzavaproizvodnje\"> <br>\n" +
                    "\n" +
                    "        <label for=\"proizvodjac\">proizvodjac: </label>\n" +
                    "        <input type=\"text\" name=\"proizvodjac\"> <br>\n" +
                    "\n" +
                    "        <label for=\"cena\">cena: </label>\n" +
                    "        <input type=\"text\" name=\"cena\"> <br>\n" +
                    "\n" +
                    "        <input type=\"submit\" value=\"Ubacite proizvod (product) u bazu\">\n" +
                    "    </form>\n";




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        obradiPodatke(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        obradiPodatke(request, response);
    }

    public void obradiPodatke(HttpServletRequest request, HttpServletResponse response){
        update(request);
        obrisiProduct(request);
        BazaPodataka baza = new BazaPodataka();
        ArrayList<String> uslovi = pronadjiUslove(request);

        try {
            ArrayList<EntitetZaBazu> products = baza.prikaziEntiteteIzJedneTabele("products", new Product(), uslovi);
            String tabelaHtml = HtmlUtil.napraviHtmlTabelu(products);
            HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Products", HtmlUtil.odvojiRedom(new String[]{tabelaHtml, FORMA_ZA_NOVI_PROIZVOD, getUpdateHtml(), getHtmlFormeZaBrisanje(), HtmlUtil.getHtmlLinkZaPocetnuStranicu()}));
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

    public static void update(HttpServletRequest request){
        if(!UrlUtil.uslovPostoji(UPDATE_URL, request)) return;
        try {
            int id = Integer.parseInt(request.getParameter(ID_ZA_UPDATE_URL));
            String novNaziv = HtmlUtil.izvuciUpdejt(request, UPDATE_NAZIV_URL, CHECKBOX);
            NazivVrednostPolja nvpNaziv = new NazivVrednostPolja("naziv", novNaziv, true);

            String novProizvodjac = HtmlUtil.izvuciUpdejt(request, UPDATE_PROIZVODJAC_URL, CHECKBOX);
            NazivVrednostPolja nvpProizvodjac = new NazivVrednostPolja("proizvodjac", novProizvodjac, true);

            String novaDrzava = HtmlUtil.izvuciUpdejt(request, UPDATE_DRZAVA_URL, CHECKBOX);
            NazivVrednostPolja nvpDrzava = new NazivVrednostPolja("drzava_proizvodnje", novaDrzava, true);

            BigDecimal novaCena = new BigDecimal(HtmlUtil.izvuciUpdejt(request, UPDATE_CENA_URL, CHECKBOX));
            NazivVrednostPolja nvpCena = new NazivVrednostPolja("cena", novaCena, false);

            new BazaPodataka().update(new NazivVrednostPolja[]{nvpNaziv, nvpProizvodjac, nvpDrzava, nvpCena}, "products", "product_id", id);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getUpdateHtml(){
        return "<form action=\"index.jsp\" method=\"POST\">" +
                HtmlUtil.napraviUpdejtForme(UPDATE_URL, ID_ZA_UPDATE_URL,
                new String[]{UPDATE_NAZIV_URL, UPDATE_PROIZVODJAC_URL, UPDATE_DRZAVA_URL, UPDATE_CENA_URL},
                new String[]{"naziv", "proizvodjac", "drzava proizvodnje", "cena"},
                "checkbox", "proizvod") +
                "<input type=\"submit\" value=\"updejtujte product\">"   +
                "</form>";
    }


    public static String getHtmlFormeZaBrisanje(){
        return HtmlUtil.getHtmlFormeZaBrisanjeEntiteta("products", "product", OBRISI_PRODUCT_URL, ID_ZA_BRISANJE_URL);
    }

    public static void obrisiProduct(HttpServletRequest request){
        UrlUtil.obrisiNaOsnovuUrl(request, OBRISI_PRODUCT_URL, ID_ZA_BRISANJE_URL, IME_TABELE, IME_KLJUCA_TABELE);
    }
}
