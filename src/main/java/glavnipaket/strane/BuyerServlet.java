package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;
import glavnipaket.entiteti.Buyer;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = {"/buyers"})
public class BuyerServlet extends HttpServlet{

    public static final String UPDATE_URL = "updatebuyer";
    public static final String ID_ZA_UPDATE_URL = "updatebuyerid";
    public static final String UPDATE_IME = "updateimebuyera";
    public static final String UPDATE_PREZIME = "updateprezimebuyera";
    public static final String CHECKBOX = "checkbox";
    public static final String OBRISI_BUYER = "obrisibuyer";
    public static final String ID_ZA_BRISANJE_URL = "idbuyerazabrisanje";
    public static final String IME_TABELE = "buyers";
    public static final String IME_KLJUCA_TABELE = "buyer_id";

    private boolean prvaProba = true;
    private static final String FORMA_ZA_NOVOG_KUPCA =
            "  <form action=\"index.jsp\" method=\"POST\">\n" +
            "        <label for=\"buyerubazu\">Stiklijate ovde i popunite polja kako biste ubacili kupca (buyer) u bazu. </label>\n" +
            "        <input type=\"checkbox\" name=\"buyerubazu\"> <br>\n" +
            "\n" +
            "        <label for=\"ime\">ime: </label>\n" +
            "        <input type=\"text\" name=\"ime\"> <br>\n" +
            "\n" +
            "        <label for=\"prezime\">prezime: </label>\n" +
            "        <input type=\"text\" name=\"prezime\">\n" +
            "        <input type=\"submit\" value=\"Ubacite kupca (buyer) u bazu.\">\n" +
            "    </form>\n";

    private static final String UPDATE = "";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        obradiPodatke(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        obradiPodatke(request, response);
    }

    public void obradiPodatke(HttpServletRequest request, HttpServletResponse response){
            obrisiKupca(request);
            BazaPodataka baza = new BazaPodataka();
            ArrayList<String> uslovi = pronadjiUslove(request);

            try {
                ArrayList buyers = baza.prikaziEntiteteIzJedneTabele("buyers", new Buyer(), uslovi);
                String tabelaHtml = HtmlUtil.napraviHtmlTabelu(buyers);
                HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Buyers", HtmlUtil.odvojiRedom(new String[]{tabelaHtml, FORMA_ZA_NOVOG_KUPCA, getHtmlUpdate(), getHtmlZaBrisanje(), HtmlUtil.getHtmlLinkZaPocetnuStranicu()}));
            } catch (Exception exception) {
                if(prvaProba){
                    prvaProba = false;
                    obradiPodatke(request, response);
                } else {
                    exception.printStackTrace();
                    HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Sales", UrlUtil.EXCEPTION_PRILIKOM_PRIKAZIVANJA_TABELE);
                }
            }
    }

    
    public static ArrayList<String> pronadjiUslove(HttpServletRequest request){
        ArrayList<String> uslovi = new ArrayList<>();

        if(UrlUtil.uslovPostoji("buyeridcheckbox", request)){
            uslovi.add("buyers.buyer_id = " + request.getParameter("buyerid"));
        }
        if(UrlUtil.uslovPostoji("imecheckbox", request)){
            uslovi.add("buyers.ime = '" + request.getParameter("ime") + "'");
        }
        if(UrlUtil.uslovPostoji("prezimecheckbox", request)){
            uslovi.add("buyers.prezime = '" + request.getParameter("prezime") + "'");
        }

        if(uslovi.size() == 0){
            return BazaPodataka.NEMA_USLOVA;
        }
        return uslovi;
    }

    public static String getHtmlUpdate(){
        return "<form action=\"index.jsp\" method=\"POST\">" +
                HtmlUtil.napraviUpdejtForme(UPDATE_URL, ID_ZA_UPDATE_URL,
                new String[]{UPDATE_IME, UPDATE_PREZIME},
                new String[]{"ime", "prezime"},
                "checkbox", "buyer")+
                "<input type=\"submit\" value=\"updejtujte kupca\">"   +
                "</form>";
    }




    public static void obrisiKupca(HttpServletRequest request){
        UrlUtil.obrisiNaOsnovuUrl(request, OBRISI_BUYER, ID_ZA_BRISANJE_URL, IME_TABELE, IME_KLJUCA_TABELE);
    }

    public static String getHtmlZaBrisanje(){
        return HtmlUtil.getHtmlFormeZaBrisanjeEntiteta("buyers", "buyer", OBRISI_BUYER, ID_ZA_BRISANJE_URL);
    }

}
