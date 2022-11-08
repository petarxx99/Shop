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
        ArrayList<String> uslovi = UrlUtil.pronadjiUslove(request, new Buyer());
        ArrayList buyers = baza.prikaziEntiteteIzJedneTabele("buyers", new Buyer(), uslovi);
        try {
            String tabelaHtml = HtmlUtil.napraviHtmlTabelu(buyers);
            HtmlUtil.dajHtmlStranuKrajnjemKorisniku(response, "Buyers", tabelaHtml);
        } catch(MojException exception){
            exception.printStackTrace();
        }
    }



}
