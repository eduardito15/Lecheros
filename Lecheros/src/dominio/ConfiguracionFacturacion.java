/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class ConfiguracionFacturacion extends ObjetoPersistente {
 
    @Column
    private boolean detalladaPorArticulo;
    
    @Column
    private boolean soloLeche;

    @Column
    private long ultimoNumeroFactura;
    
    @Column
    private double porcentajeFacturacion;
    
    @Column
    private double maximoBoletaContadoFinal;
    
    @Column
    private boolean detalladoPorGrupoDeArticulo;
    /**
     * @return the detalladaPorArticulo
     */
    public boolean isDetalladaPorArticulo() {
        return detalladaPorArticulo;
    }

    /**
     * @param detalladaPorArticulo the detalladaPorArticulo to set
     */
    public void setDetalladaPorArticulo(boolean detalladaPorArticulo) {
        this.detalladaPorArticulo = detalladaPorArticulo;
    }

    /**
     * @return the ultimoNumeroFactura
     */
    public long getUltimoNumeroFactura() {
        return ultimoNumeroFactura;
    }

    /**
     * @param ultimoNumeroFactura the ultimoNumeroFactura to set
     */
    public void setUltimoNumeroFactura(long ultimoNumeroFactura) {
        this.ultimoNumeroFactura = ultimoNumeroFactura;
    }

    /**
     * @return the soloLeche
     */
    public boolean isSoloLeche() {
        return soloLeche;
    }

    /**
     * @param soloLeche the soloLeche to set
     */
    public void setSoloLeche(boolean soloLeche) {
        this.soloLeche = soloLeche;
    }

    /**
     * @return the porcentajeFacturacion
     */
    public double getPorcentajeFacturacion() {
        return porcentajeFacturacion;
    }

    /**
     * @param porcentajeFacturacion the porcentajeFacturacion to set
     */
    public void setPorcentajeFacturacion(double porcentajeFacturacion) {
        this.porcentajeFacturacion = porcentajeFacturacion;
    }

    /**
     * @return the maximoBoletaContadoFinal
     */
    public double getMaximoBoletaContadoFinal() {
        return maximoBoletaContadoFinal;
    }

    /**
     * @param maximoBoletaContadoFinal the maximoBoletaContadoFinal to set
     */
    public void setMaximoBoletaContadoFinal(double maximoBoletaContadoFinal) {
        this.maximoBoletaContadoFinal = maximoBoletaContadoFinal;
    }

    /**
     * @return the detalladoPorGrupoDeArticulo
     */
    public boolean isDetalladoPorGrupoDeArticulo() {
        return detalladoPorGrupoDeArticulo;
    }

    /**
     * @param detalladoPorGrupoDeArticulo the detalladoPorGrupoDeArticulo to set
     */
    public void setDetalladoPorGrupoDeArticulo(boolean detalladoPorGrupoDeArticulo) {
        this.detalladoPorGrupoDeArticulo = detalladoPorGrupoDeArticulo;
    }

    
    
    
}
