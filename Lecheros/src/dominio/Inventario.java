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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Edu
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"reparto_id", "fecha"})})
public class Inventario extends ObjetoPersistente {
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InventarioRenglon> renglones;
    
    @Column
    private double totalMinimo;
    
    @Column
    private double totalBasico;
    
    @Column
    private double subtotal;
    
    @Column
    private double total;
    
    public Inventario(){
        renglones = new ArrayList<>();
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
     * @return the renglones
     */
    public List<InventarioRenglon> getRenglones() {
        return renglones;
    }

    /**
     * @param renglones the renglones to set
     */
    public void setRenglones(List<InventarioRenglon> renglones) {
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
    
    @Override
    public String toString(){
       return "INV: " + this.getFecha().toString() + " " + this.getReparto();
    }
}
