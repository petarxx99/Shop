package glavnipaket.strane.util;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;
import glavnipaket.baza.NazivVrednostPolja;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

public class UrlUtil {

    public static ArrayList<String> pronadjiUslove(HttpServletRequest request, String USLOV){
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
        return uslovi;
    }

    public static ArrayList<String> pronadjiUslove(HttpServletRequest request, EntitetZaBazu entitet){
        ArrayList<String> uslovi = new ArrayList<>();
        String[] naziviPolja  = entitet.getNazivePolja();
        for(int i=0; i< naziviPolja.length; i++){
            naziviPolja[i] = naziviPolja[i].toLowerCase().replace("_", "");
        }

        for(String polje : naziviPolja){
            if(uslovPostoji(polje + "checkbox", request)){
                uslovi.add(polje + "="+request.getParameter(polje));
            }
        }

        if(uslovi.size() == 0){
            return BazaPodataka.NEMA_USLOVA;
        }
        return uslovi;
    }



    public static boolean uslovPostoji(String checkbox, HttpServletRequest request){
        if(request.getParameter(checkbox) == null) return false;
        return request.getParameter(checkbox).equals("on");
    }
}
