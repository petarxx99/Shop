package glavnipaket.strane.util;

import glavnipaket.EntitetZaBazu;
import glavnipaket.baza.BazaPodataka;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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

        for(String polje : naziviPolja){
            if(uslovPostoji("checkbox" + polje, request)){
                uslovi.add(request.getParameter(polje));
            }
        }

        if(uslovi.size() == 0){
            return BazaPodataka.NEMA_USLOVA;
        }
        return uslovi;
    }

    public static boolean uslovPostoji(String checkbox, HttpServletRequest request){
        return request.getParameter(checkbox) == null;
    }
}
