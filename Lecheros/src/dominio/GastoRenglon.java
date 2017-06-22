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
public class GastoRenglon extends ObjetoPersistente {
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Gasto gasto;
    
    @OneToOne(fetch = FetchType.EAGER)
    private RubroGasto rubro;
    
    @Column
    private double total;
    
    @Column
    private double horas;
    
    @Column
    private double horasDeDescansoSemanal;

    /**
     * @return the gasto
     */
    public Gasto getGasto() {
        return gasto;
    }

    /**
     * @param gasto the gasto to set
     */
    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    /**
     * @return the rubro
     */
    public RubroGasto getRubro() {
        return rubro;
    }

    /**
     * @param rubro the rubro to set
     */
    public void setRubro(RubroGasto rubro) {
        this.rubro = rubro;
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
        return this.getGasto() + " " + this.getRubro().getNombre() + " " + this.getTotal();
    }

    /**
     * @return the horas
     */
    public double getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(double horas) {
        this.horas = horas;
    }

    /**
     * @return the horasDeDescansoSemanal
     */
    public double getHorasDeDescansoSemanal() {
        return horasDeDescansoSemanal;
    }

    /**
     * @param horasDeDescansoSemanal the horasDeDescansoSemanal to set
     */
    public void setHorasDeDescansoSemanal(double horasDeDescansoSemanal) {
        this.horasDeDescansoSemanal = horasDeDescansoSemanal;
    }
    
    
}
