package glavnipaket.strane.util;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.NazivVrednostPolja;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HtmlUtil {

    public static void dajHtmlStranuKrajnjemKorisniku(HttpServletResponse response, String naslov, String body){
        String html = napraviHtmlStranu(naslov, body);
        dajHtmlStranuKrajnjemKorisniku(response, html);
    }


    public static void dajHtmlStranuKrajnjemKorisniku(HttpServletResponse response, final String HTML){
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.println(HTML);
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static String napraviHtmlStranu(String titl, String body){
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>"+
                        "<head>"+
                        "<title> %s </title>" +
                        "<meta charset=\"UTF-8\">" +
                        "</head>"+
                        "<body> %s </body>"+
                        "</html>"
                ,titl, body);
    }

    public static String napraviBodySaObicnomPorukom(String naslov, String poruka){
        return String.format("<h1> %s </h1> \n%s", naslov, poruka);
    }

    public static <T extends EntitetZaBazu> String napraviHtmlTabelu(ArrayList<T> entiteti) throws MojException{
        if(entiteti == null){
                throw new NemaEntitetaUBazi();
        }
        if(entiteti.size() < 1){
            throw new NemaEntitetaUBazi();
        }

        final int BROJ_REDOVA = entiteti.size();
        final int BROJ_KOLONA = entiteti.get(0).getNaziveVrednostiPolja().length;
        if(BROJ_REDOVA < 1){
            throw new NemaEntitetaUBazi();
        }

        NazivVrednostPolja[] nvpZaNazivKolonaTabele = entiteti.get(0).getNaziveVrednostiPolja();

        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"1\"> \n");
        sb.append("<h> \n");
        for(int j=0; j<BROJ_KOLONA; j++) {
            sb.append("<td> \n");
            sb.append(nvpZaNazivKolonaTabele[j].nazivPolja);
            sb.append("</td> \n");
            sb.append("</h> \n");
        }

        for(int i=0; i<BROJ_REDOVA; i++){
            sb.append("<tr> \n");
            NazivVrednostPolja[] nvpEntiteta = entiteti.get(i).getNaziveVrednostiPolja();
            for(NazivVrednostPolja n : nvpEntiteta){
                    sb.append("<td> \n");
                    sb.append(n.vrednostPolja);
                    sb.append("</td> \n");
            }
            sb.append("</tr> \n");
        }

        sb.append("</table>");
        return sb.toString();
    }

    public static String odvojiRedom(String[] html){
        StringBuilder sb = new StringBuilder();
        for(String s : html){
            sb.append(s + "<br>");
        }
        return sb.toString();
    }

    public static String getHtmlLinkZaPocetnuStranicu(){
        return  "<a href=\"index.jsp\">Kliknite ovde da biste se vratili na pocetnu stranicu </a>";
    }

    public static String napraviUpdejtFormu(String urlPolja, String nazivPolja, String checkbox){
       return "            <label for=\"" + urlPolja + "\">" + nazivPolja + ": </label>\n" +
                "            <input type=\"text\" name=\"" + urlPolja + "\">\n" +
                "            <input type=\"checkbox\" name=\"" + checkbox + urlPolja +"\">\n";
    }

    public static String napraviUpdejtForme(String updateNazivUrl, String idUrl, String[] urlPolja, String[] naziviPolja, String checkbox, String imeKlase){
        StringBuilder sb = new StringBuilder();
        sb.append( "            <label for=\"" + updateNazivUrl + "\"> Stiklirajte ovde da biste updejtovali vrednosti " +imeKlase +".  </label>\n" +
                "            <input type=\"checkbox\" name=\""+ updateNazivUrl+ "\">\n" +
                "            <br>\n" +
                "            Stiklirajte polja koja zelite da updejtujete.<br>" );

        sb.append( "<br>\n" +
        "            <label for=\"" + idUrl +"\"> Upisite id koji updejtujete </label>\n" +
                "            <input type=\"text\" name=\"" + idUrl + "\">\n");

        for(int i=0; i<urlPolja.length; i++){
            sb.append(napraviUpdejtFormu(urlPolja[i], naziviPolja[i], checkbox));
            sb.append("<br>");
        }

        return sb.toString();
    }
    public static final String CHECKBOX = "checkbox";

    public static void update(HttpServletRequest request, String UPDATE_URL, String idUrl, String[]urlPolja, String checkbox, EntitetZaBazu entitet){
        if(!UrlUtil.uslovPostoji(UPDATE_URL, request)) return;

        try{
            int id = Integer.parseInt(request.getParameter(idUrl));

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String izvuciUpdejt(HttpServletRequest request, String UPDATE_URL_POLJA, String checkbox){
        if(UrlUtil.uslovPostoji(checkbox + UPDATE_URL_POLJA, request)){
            return request.getParameter(UPDATE_URL_POLJA);
        } else {
            return null;
        }
    }

    public static String getHtmlFormeZaBrisanjeEntiteta(String urlServleta, String imeEntiteta, String checkboxZaBrisanje, String idUrl){
        return "<form action=\"" + urlServleta + "\" method=POST>" + "\n" +
                "<label for=\"" + checkboxZaBrisanje + "\" > Stiklirajte ovde ako hocete da brisete iz baze i upisite id koji zelite da obrisete." + "\n"
                +"<input type=\"checkbox\" name=\"" + checkboxZaBrisanje + "\"> \n"
                +"<input type=\"text\" name=\"" + idUrl + "\"> \n" +
                "<input type=\"submit\" value=\" obrisite " + imeEntiteta + "\">" +
                "</form>";
    }
}
