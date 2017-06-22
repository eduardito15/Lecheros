/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Edu
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"tipoDocumento_id", "numero"})})
public class Compra extends ObjetoPersistente {
    
    @OneToOne(fetch = FetchType.EAGER)
    private DocumentoDeCompra tipoDocumento;
    
    @Column
    private long numero;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Temporal(TemporalType.DATE)
    private Date fechaLiquidacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CompraRenglon> renglones;
    
    @Column
    private double totalMinimo;
    
    @Column
    private double totalBasico;
    
    @Column
    private double subtotal;
    
    @Column
    private double total;
    
    @Column
    private double totalAPrecioDeVentaSinIva;
    
    @Column
    private double totalAPrecioDeVentaConIva;
    
    public Compra(){
        renglones = new ArrayList<>();
    }

    /**
     * @return the tipoDocumento
     */
    public DocumentoDeCompra getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(DocumentoDeCompra tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the numero
     */
    public long getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(long numero) {
        this.numero = numero;
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
     * @return the camion
     */
    public Reparto getReparto() {
        return reparto;
    }

    /**
     * @param camion the camion to set
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    /**
     * @return the renglones
     */
    public List<CompraRenglon> getRenglones() {
        return renglones;
    }

    /**
     * @param renglones the renglones to set
     */
    public void setRenglones(List<CompraRenglon> renglones) {
        this.renglones = renglones;
    }

    /**
     * @return the totalMinimo
     */
    public double getTotalMinimo() {
        return totalMinimo;
    }

    /**
     * @param totalMinimo the totalMinimo to set
     */
    public void setTotalMinimo(double totalMinimo) {
        this.totalMinimo = totalMinimo;
    }

    /**
     * @return the totalBasico
     */
    public double getTotalBasico() {
        return totalBasico;
    }

    /**
     * @param totalBasico the totalBasico to set
     */
    public void setTotalBasico(double totalBasico) {
        this.totalBasico = totalBasico;
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
     * @return the totalAPrecioDeVenta
     */
    public double getTotalAPrecioDeVentaSinIva() {
        return totalAPrecioDeVentaSinIva;
    }

    /**
     * @param totalAPrecioDeVentaSinIva the totalAPrecioDeVenta to set
     */
    public void setTotalAPrecioDeVentaSinIva(double totalAPrecioDeVentaSinIva) {
        this.totalAPrecioDeVentaSinIva = totalAPrecioDeVentaSinIva;
    }

    /**
     * @return the totalAPrecioDeVentaConIva
     */
    public double getTotalAPrecioDeVentaConIva() {
        return totalAPrecioDeVentaConIva;
    }

    /**
     * @param totalAPrecioDeVentaConIva the totalAPrecioDeVentaConIva to set
     */
    public void setTotalAPrecioDeVentaConIva(double totalAPrecioDeVentaConIva) {
        this.totalAPrecioDeVentaConIva = totalAPrecioDeVentaConIva;
    }
    
    @Override
    public String toString(){
        return this.tipoDocumento.getTipoDocumento() + " " + this.getNumero();
    }

    /**
     * @return the fechaLiquidacion
     */
    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * @param fechaLiquidacion the fechaLiquidacion to set
     */
    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }
    
}
