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
public class JornalRenglon extends ObjetoPersistente{
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Jornales jornales;
    
    @OneToOne(fetch = FetchType.EAGER)
    private RubroGasto rubro;
    
    @Column
    private double total;
    
    @Column
    private int cantidadDeJornales;
    
    @Column
    private double horas;
    
    @Column
    private double horasDeDescansoSemanal;

    /**
     * @return the jornales
     */
    public Jornales getJornales() {
        return jornales;
    }

    /**
     * @param jornales the jornales to set
     */
    public void setJornales(Jornales jornales) {
        this.jornales = jornales;
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

    /**
     * @return the cantidadDeJornales
     */
    public int getCantidadDeJornales() {
        return cantidadDeJornales;
    }

    /**
     * @param cantidadDeJornales the cantidadDeJornales to set
     */
    public void setCantidadDeJornales(int cantidadDeJornales) {
        this.cantidadDeJornales = cantidadDeJornales;
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
    
    @Override
    public String toString(){
        return this.getJornales() + " " + this.getRubro().getNombre() + " " + this.getTotal();
    }
}
