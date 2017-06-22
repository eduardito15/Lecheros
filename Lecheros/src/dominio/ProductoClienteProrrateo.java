/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 *
 * @author Edu
 */
@Entity
public class ProductoClienteProrrateo extends ObjetoPersistente {
    
    @OneToOne(fetch = FetchType.EAGER)
    private Articulo articulo;
    
    @Column
    private int cantidad;
    
    @Column
    private String frecuencia;

    /**
     * @return the a
     */
    public Articulo getArticulo() {
        return articulo;
    }

    /**
     * @param a the a to set
     */
    public void setArticulo(Articulo a) {
        this.articulo = a;
    }

    /**
     * @return the frecuencia
     */
    public String getFrecuencia() {
        return frecuencia;
    }

    /**
     * @param frecuencia the frecuencia to set
     */
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    @Override
    public String toString(){
        return this.getArticulo().getCodigo() + " " + this.getArticulo().getDescripcion() + " " + this.getCantidad() + " " + this.getFrecuencia();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof ProductoClienteProrrateo)) return false;
        else return ((ProductoClienteProrrateo)obj).getArticulo().getCodigo() == this.getArticulo().getCodigo();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.articulo);
        hash = 79 * hash + Objects.hashCode(this.frecuencia);
        return hash;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
