/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
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
public class Precio extends ObjetoPersistente {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Articulo articulo;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column
    private Double precioCompra;
    
    @Column
    private Double precioVenta;

    /**
     * @return the articulo
     */
    public Articulo getArticulo() {
        return articulo;
    }

    /**
     * @param articulo the articulo to set
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the precioCompra
     */
    public Double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * @param precioCompra the precioCompra to set
     */
    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    /**
     * @return the precioVenta
     */
    public Double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * @param precioVenta the precioVenta to set
     */
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public String toString(){
        return Integer.toString(this.getArticulo().getCodigo()) + "Fecha: " + this.getFecha().toString() + " Precio Compra: " + Double.toString(this.getPrecioCompra()) + " Precio Venta: " + Double.toString(this.getPrecioVenta());
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Precio)) return false;
        else return ((Precio)obj).getArticulo().equals(this.getArticulo()) && ((Precio)obj).getPrecioCompra().equals(this.getPrecioCompra()) && ((Precio)obj).getPrecioVenta().equals(this.getPrecioVenta());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.articulo);
        hash = 67 * hash + Objects.hashCode(this.fecha);
        hash = 67 * hash + Objects.hashCode(this.precioCompra);
        hash = 67 * hash + Objects.hashCode(this.precioVenta);
        return hash;
    }
    
}
