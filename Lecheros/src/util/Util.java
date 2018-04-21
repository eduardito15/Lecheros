/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dominio.Articulo;
import dominio.GrupoDeArticulos;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import sistema.SistemaMantenimientoArticulos;

/**
 *
 * @author Edu
 */
public class Util {
    
    private static DecimalFormat df = new DecimalFormat(".##");

    /**
     * @return the df
     */
    public static DecimalFormat getDf() {
        return df;
    }
    
    public static String obtenerStackTraceEnString(Exception e){
        StackTraceElement[] stack = e.getStackTrace();
        String theTrace = "";
        for (StackTraceElement line : stack) {
            theTrace += line.toString();
        }
        return theTrace;
    }
    
    public static boolean esLeche(Articulo a) {
        boolean retorno = false;
        //GrupoDeArticulos grupoLeche = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche");
        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");
        GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");
        GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");
        //if(grupoLeche.getArticulos().contains(a) || grupoLecheComun.getArticulos().contains(a) || grupoLecheDeslactosada.getArticulos().contains(a) || grupoLecheUltraDiferenciada.getArticulos().contains(a) || grupoLecheUtra.getArticulos().contains(a)) {
        if(grupoLecheComun.getArticulos().contains(a) || grupoLecheDeslactosada.getArticulos().contains(a) || grupoLecheUltraDiferenciada.getArticulos().contains(a) || grupoLecheUtra.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }
    
    public static boolean esLecheParaFacturas(Articulo a) {
        boolean retorno = false;
        //GrupoDeArticulos grupoLeche = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche");
        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");
        GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");
        GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");
        GrupoDeArticulos grupoLecheBlancaNube = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Blanca Nube");
        GrupoDeArticulos grupoLecheOmega = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Omega");
        //if(grupoLeche.getArticulos().contains(a) || grupoLecheComun.getArticulos().contains(a) || grupoLecheDeslactosada.getArticulos().contains(a) || grupoLecheUltraDiferenciada.getArticulos().contains(a) || grupoLecheUtra.getArticulos().contains(a)) {
        if(grupoLecheComun.getArticulos().contains(a) || grupoLecheDeslactosada.getArticulos().contains(a) || grupoLecheUltraDiferenciada.getArticulos().contains(a) || grupoLecheUtra.getArticulos().contains(a) || grupoLecheBlancaNube.getArticulos().contains(a) || (grupoLecheOmega != null ? grupoLecheOmega.getArticulos().contains(a):false)) {
            retorno = true;
        }
        return retorno;
    }
    
    public static boolean esEnvase(Articulo a) {
        boolean retorno = false;
        GrupoDeArticulos grupoEnvases = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Envases");
        if(grupoEnvases.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }
    
    public static boolean esCodigoEspecial(Articulo a) {
        boolean retorno = false;
        GrupoDeArticulos grupoCodigosEspeciales = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Codigos Especiales");
        if(grupoCodigosEspeciales.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }
    
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}
