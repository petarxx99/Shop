package glavnipaket.strane.util;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.NazivVrednostPolja;

import javax.servlet.http.HttpServletResponse;
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



}
