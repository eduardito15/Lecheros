/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class Iva extends ObjetoPersistente{
    
    private String nombre;
    private int porcentaje;

    /**
     * @return the nombre
     */
    @Column
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
    @Column
    public int getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    @Override
    public String toString(){
        return this.getNombre() + " " + Integer.toString(this.getPorcentaje()) + "%";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Iva)) return false;
        else return ((Iva)obj).getPorcentaje() == this.getPorcentaje() && ((Iva)obj).getNombre().equals(this.getNombre());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + this.porcentaje;
        return hash;
    }

}
