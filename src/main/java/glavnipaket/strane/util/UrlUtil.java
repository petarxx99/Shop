package glavnipaket.strane.util;

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
}
