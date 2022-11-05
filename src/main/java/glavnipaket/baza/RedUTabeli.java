package glavnipaket.baza;

import glavnipaket.EntitetZaBazu;

import java.util.ArrayList;

public class RedUTabeli {
    
    public final EntitetZaBazu[] ENTITETI_IZ_BAZE;
    
    public RedUTabeli(EntitetZaBazu[] ENTITETI_IZ_BAZE){
        this.ENTITETI_IZ_BAZE = ENTITETI_IZ_BAZE;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(EntitetZaBazu entitetIzBaze : ENTITETI_IZ_BAZE){
            NazivVrednostPolja[] nvpJednogEntiteta = entitetIzBaze.getNaziveVrednostiPolja();
            for(NazivVrednostPolja nvp : nvpJednogEntiteta){
                sb.append(nvp.toString());
                sb.append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
