package com.val.ebtm.dto;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.val.ebtm.InfoUsuario;

/**
 * Created by V on 19/03/16.
 *
 * Clase que crea objetos con al información de un usuario, al modo de factoría.
 * Muy necesaria esta clase para no arrastrar las dependecias necesarias con las clases
 * que gestionan la información del usuario y aislarla de la propia información del usuario
 * neta; para no arrastrar esas dependecias a la parte servidora en el proceso de serialización
 * deserialización
 *
 * @see InfoUsuario
 *
 * @author  Val
 * @version 1.1.3
 */
public class FactoryUsuario
{


    private static String[] componListaCorreos  (String lexpr)
    {
        String[] lista_correos = null;

        lista_correos = lexpr.split(",");

        return lista_correos;
    }

    private static String[] getCuentasMail (AccountManager accountManager)
    {
        String[] cuentas_mail = null;
        String str_aux = null;

        Account[] list = accountManager.getAccounts();

        str_aux = new String();
        for (Account cuenta : list)
        {

            if (cuenta.type.equals(InfoUsuario.ID_CUENTA_GOOGLE))
            {
                str_aux = str_aux + cuenta.name+",";
            }

        }

        cuentas_mail = componListaCorreos(str_aux.substring(0, str_aux.length() - 1));

        return cuentas_mail;
    }


    public static InfoUsuario crearInfoUsuario (AccountManager accountManager)
    {
        InfoUsuario infoUsuario = null;

            infoUsuario = new InfoUsuario();

            infoUsuario.setLista_emails(getCuentasMail (accountManager));

        return infoUsuario;


    }
}
