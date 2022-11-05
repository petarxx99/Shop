package glavnipaket.baza;

public class NazivVrednostPolja {

    final public String nazivPolja;
    final public Object vrednostPolja;
    final public boolean imaNavodnike;

    public Class getKlasa(){
        if(vrednostPolja != null){
            return vrednostPolja.getClass();
        }
        return null;
    }


    public NazivVrednostPolja(String nazivPolja, Object vrednostPolja, boolean imaNavodnike){
        this.nazivPolja = nazivPolja;
        this.vrednostPolja = vrednostPolja;
        this.imaNavodnike = imaNavodnike;
    }
    


    public String getVrednostPoljaZaBazu(){
        if(imaNavodnike){
            return "'" + vrednostPolja + "'";
        } else {
            return vrednostPolja.toString();
        }
    }


    public static String[] getNazivePolja(NazivVrednostPolja[] naziviVrednostiPolja){
        String[] zaReturn = new String[naziviVrednostiPolja.length];
        for(int i=0; i< naziviVrednostiPolja.length; i++){
            zaReturn[i] = naziviVrednostiPolja[i].nazivPolja;
        }
        return zaReturn;
    }

    public static String[] getVrednostiPolja(NazivVrednostPolja[] naziviVrednostiPolja){
        String[] zaReturn = new String[naziviVrednostiPolja.length];
        for(int i=0; i< naziviVrednostiPolja.length; i++){
            if(naziviVrednostiPolja[i].imaNavodnike){
                zaReturn[i] = "'" + naziviVrednostiPolja[i].vrednostPolja + "'";
            } else {
                zaReturn[i] = naziviVrednostiPolja[i].vrednostPolja.toString();
            }
        }
        return zaReturn;
    }

    public Class getKlasaPolja(){
        return this.vrednostPolja.getClass();
    }
}
