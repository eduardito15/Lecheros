/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Date;
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
public class LiquidacionClafer extends ObjetoPersistente {
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @Column
    private int cantPichadasPagadas;
    
    @Column
    private int cantPinchadasCobradas;
    
    @Column
    private double totalPinchadasPagadas;
    
    @Column 
    private double totalPinchadasCobradas;
    
    @Column
    private int kilometros;

    /**
     * @return the cantPichadasPagadas
     */
    public int getCantPichadasPagadas() {
        return cantPichadasPagadas;
    }

    /**
     * @param cantPichadasPagadas the cantPichadasPagadas to set
     */
    public void setCantPichadasPagadas(int cantPichadasPagadas) {
        this.cantPichadasPagadas = cantPichadasPagadas;
    }

    /**
     * @return the cantPinchadasCobradas
     */
    public int getCantPinchadasCobradas() {
        return cantPinchadasCobradas;
    }

    /**
     * @param cantPinchadasCobradas the cantPinchadasCobradas to set
     */
    public void setCantPinchadasCobradas(int cantPinchadasCobradas) {
        this.cantPinchadasCobradas = cantPinchadasCobradas;
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
     * @return the totalPinchadasPagadas
     */
    public double getTotalPinchadasPagadas() {
        return totalPinchadasPagadas;
    }

    /**
     * @param totalPinchadasPagadas the totalPinchadasPagadas to set
     */
    public void setTotalPinchadasPagadas(double totalPinchadasPagadas) {
        this.totalPinchadasPagadas = totalPinchadasPagadas;
    }

    /**
     * @return the totalPinchadasCobradas
     */
    public double getTotalPinchadasCobradas() {
        return totalPinchadasCobradas;
    }

    /**
     * @param totalPinchadasCobradas the totalPinchadasCobradas to set
     */
    public void setTotalPinchadasCobradas(double totalPinchadasCobradas) {
        this.totalPinchadasCobradas = totalPinchadasCobradas;
    }

    /**
     * @return the kilometros
     */
    public int getKilometros() {
        return kilometros;
    }

    /**
     * @param kilometros the kilometros to set
     */
    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }
    
    
}
