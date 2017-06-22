/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Edu
 */
@Entity
public class CompraRenglon extends ObjetoPersistente implements Comparable<CompraRenglon>{
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Compra compra;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Articulo articulo;
    
    @Column
    private double cantidad;
    
    @Column
    private double precio;
    
    @Column
    private double subtotal;
    
    @Column
    private double iva;
    
    @Column
    private double total;
    
    @Column
    private double totalPrecioVentaSinIva;
    
    @Column
    private double totalPrecioVentaConIva;

    /**
     * @return the compra
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

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
     * @return the cantidad
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * @return the subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the iva
     */
    public double getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(double iva) {
        this.iva = iva;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the totalPrecioVenta
     */
    public double getTotalPrecioVentaSinIva() {
        return totalPrecioVentaSinIva;
    }

    /**
     * @param totalPrecioVentaSinIva the totalPrecioVenta to set
     */
    public void setTotalPrecioVentaSinIva(double totalPrecioVentaSinIva) {
        this.totalPrecioVentaSinIva = totalPrecioVentaSinIva;
    }

    /**
     * @return the totalPrecioVentaConIva
     */
    public double getTotalPrecioVentaConIva() {
        return totalPrecioVentaConIva;
    }

    /**
     * @param totalPrecioVentaConIva the totalPrecioVentaConIva to set
     */
    public void setTotalPrecioVentaConIva(double totalPrecioVentaConIva) {
        this.totalPrecioVentaConIva = totalPrecioVentaConIva;
    }
    
    @Override
    public String toString(){
        return this.getCompra().getTipoDocumento().toString() + " " +this.getCompra().getNumero() + " " + this.getArticulo().getCodigo() + " " + this.getCantidad();
    }

    @Override
    public int compareTo(CompraRenglon o) {
        return Long.compare(this.getId(), o.getId());
    }
    
}
