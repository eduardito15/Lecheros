/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edu
 */
@Entity
public class Retencion extends ObjetoPersistente {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @Column
    private String  nombre;
    
    @Column 
    private String aQueCorresponde;
    
    @Column
    private double importe;
    
    @Column
    private boolean activa;
    
    @Temporal(TemporalType.DATE)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Date> fechasCanceladas;

    /**
     * @return the reparto
     */
    public Reparto getReparto() {
        return reparto;
    }

    /**
     * @param reparto the reparto to set
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the aQueCorresponde
     */
    public String getaQueCorresponde() {
        return aQueCorresponde;
    }

    /**
     * @param aQueCorresponde the aQueCorresponde to set
     */
    public void setaQueCorresponde(String aQueCorresponde) {
        this.aQueCorresponde = aQueCorresponde;
    }

    /**
     * @return the importe
     */
    public double getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }
    
    
    @Override
    public String toString(){
        return this.nombre + " " + this.getaQueCorresponde() + " " + this.getImporte();
    }

    /**
     * @return the activa
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * @param activa the activa to set
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * @return the fechasCanceladas
     */
    public List<Date> getFechasCanceladas() {
        return fechasCanceladas;
    }

    /**
     * @param fechasCanceladas the fechasCanceladas to set
     */
    public void setFechasCanceladas(List<Date> fechasCanceladas) {
        this.fechasCanceladas = fechasCanceladas;
    }
    
    
    
}
