package glavnipaket.strane.util;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UrlUtil {

    public static final String EXCEPTION_PRILIKOM_PRIKAZIVANJA_TABELE = "Nema entiteta u tabeli. Molim Vas pokusajte ponovo.";

    public static boolean uslovPostoji(String checkbox, HttpServletRequest request){
        if(request.getParameter(checkbox) == null) return false;
        return request.getParameter(checkbox).equals("on");
    }



    public static void obrisiNaOsnovuUrl(HttpServletRequest request, String checkboxObrisi, String idUrl, String imeTabele, String imeKljuca){
       try {
           if (UrlUtil.uslovPostoji(checkboxObrisi, request)) {
               new BazaPodataka().izbrisiteEntitet(Integer.parseInt(request.getParameter(idUrl)), imeTabele, imeKljuca);
           }
       } catch(Exception e){
           e.printStackTrace();
       }
    }

}
