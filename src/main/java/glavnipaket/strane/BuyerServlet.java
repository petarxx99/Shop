package glavnipaket.strane;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.entiteti.Buyer;
import glavnipaket.entiteti.Product;
import glavnipaket.strane.util.HtmlUtil;
import glavnipaket.strane.util.MojException;

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
        try {
            ArrayList<EntitetZaBazu> buyers = new ArrayList<>();
            ArrayList<String> uslovi;  // uslov1, uslov2, uslov3...

            if(request.getParameter(USLOV + 1) == null){
                uslovi = BazaPodataka.NEMA_USLOVA;
            } else {
                uslovi = new ArrayList();
                int i=1;
                while(request.getParameter(USLOV + i) != null){
                    uslovi.add(request.getParameter(USLOV + i));
                    i++;
                }
            }

            buyers = baza.prikaziEntiteteIzJedneTabele("buyers", new Buyer(), uslovi);
            String tabelaHtml = HtmlUtil.napraviHtmlTabelu(buyers);
            HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Buyers", tabelaHtml);
        } catch(MojException exception){
            exception.printStackTrace();
        }

    }



}
