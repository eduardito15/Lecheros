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
public class Comision extends ObjetoPersistente {
    
    @Column
    private String nombre;
    
    @Column
    private double porcentaje;
    
    @Column
    private double porcentajeLeche;

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
     * @return the porcentaje
     */
    public double getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    @Override
    public String toString(){
        return this.getNombre() + " " + Double.toString(this.getPorcentaje()) + "%" + " - " + Double.toString(this.getPorcentajeLeche()) + "%";
    }

    /**
     * @return the porcentajeLeche
     */
    public double getPorcentajeLeche() {
        return porcentajeLeche;
    }

    /**
     * @param porcentajeLeche the porcentajeLeche to set
     */
    public void setPorcentajeLeche(double porcentajeLeche) {
        this.porcentajeLeche = porcentajeLeche;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Comision)) {
            return false;
        } else {
            return ((Comision) obj).getNombre().equals(this.getNombre()) && ((Comision) obj).getPorcentaje() == this.getPorcentaje() && ((Comision) obj).getPorcentajeLeche() == this.getPorcentajeLeche();
        }
    }
    
}
