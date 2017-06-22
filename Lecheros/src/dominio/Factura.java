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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edu
 */
@Entity
public class Factura extends ObjetoPersistente {
    
    @OneToOne(fetch = FetchType.EAGER)
    private DocumentoDeVenta tipoDocumento;
    
    @Column
    private long numero;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<FacturaRenglon> renglones;
    
    @Column
    private double totalMinimo;
    
    @Column
    private double totalBasico;
    
    @Column
    private double subtotal;
    
    @Column
    private double total;
    
    @Column
    private boolean estaPaga;
    
    @Column
    private boolean esManual;
    
    @Column
    private double descuento;

    public Factura() {
        renglones = new ArrayList<>();
        estaPaga = true;
        esManual = true;
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
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the renglones
     */
    public List<FacturaRenglon> getRenglones() {
        return renglones;
    }

    /**
     * @param renglones the renglones to set
     */
    public void setRenglones(List<FacturaRenglon> renglones) {
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
    
    @Override
    public String toString() {
       return "Factura Manual: " + this.getFecha().toString() + " " + this.getReparto();
    }

    /**
     * @return the estaPaga
     */
    public boolean isEstaPaga() {
        return estaPaga;
    }

    /**
     * @param estaPaga the estaPaga to set
     */
    public void setEstaPaga(boolean estaPaga) {
        this.estaPaga = estaPaga;
    }

    /**
     * @return the esManual
     */
    public boolean isEsManual() {
        return esManual;
    }

    /**
     * @param esManual the esManual to set
     */
    public void setEsManual(boolean esManual) {
        this.esManual = esManual;
    }

    /**
     * @return the tipoDocumento
     */
    public DocumentoDeVenta getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(DocumentoDeVenta tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the descuento
     */
    public double getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    
}
