package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
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
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = {"/Shop/buyers"})
public class BuyerServlet extends HttpServlet{

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
                ArrayList buyers = baza.prikaziEntiteteIzJedneTabele("buyers", new Buyer(), uslovi);
                String tabelaHtml = HtmlUtil.napraviHtmlTabelu(buyers);
                HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Buyers", tabelaHtml);
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


}
