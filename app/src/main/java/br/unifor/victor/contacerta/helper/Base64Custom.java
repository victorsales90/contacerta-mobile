package br.unifor.victor.contacerta.helper;

import android.util.Base64;

/**
 * Created by victor on 08/06/2017.
 */

public class Base64Custom {
    public static String codificarParaBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarBase64(String textoCodificado){
       return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
