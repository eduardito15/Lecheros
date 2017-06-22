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
public class Anep extends ObjetoPersistente {
    
    @Temporal(TemporalType.DATE)
    private Date desdeFecha;
    
    @Temporal(TemporalType.DATE)
    private Date hastaFecha;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @Column
    private int litrosLunes;
    
    @Column
    private int litrosMartes;
    
    @Column
    private int litrosMiercoles;
    
    @Column
    private int litrosJueves;
    
    @Column
    private int litrosViernes;
    
    @Column
    private int litrosSabado;
    
    @Column
    private int litrosDomingo;
    
    @Column
    private int litrosTotal;

    /**
     * @return the desdeFecha
     */
    public Date getDesdeFecha() {
        return desdeFecha;
    }

    /**
     * @param desdeFecha the desdeFecha to set
     */
    public void setDesdeFecha(Date desdeFecha) {
        this.desdeFecha = desdeFecha;
    }

    /**
     * @return the hastaFecha
     */
    public Date getHastaFecha() {
        return hastaFecha;
    }

    /**
     * @param hastaFecha the hastaFecha to set
     */
    public void setHastaFecha(Date hastaFecha) {
        this.hastaFecha = hastaFecha;
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
     * @return the litrosLunes
     */
    public int getLitrosLunes() {
        return litrosLunes;
    }

    /**
     * @param litrosLunes the litrosLunes to set
     */
    public void setLitrosLunes(int litrosLunes) {
        this.litrosLunes = litrosLunes;
    }

    /**
     * @return the litrosMartes
     */
    public int getLitrosMartes() {
        return litrosMartes;
    }

    /**
     * @param litrosMartes the litrosMartes to set
     */
    public void setLitrosMartes(int litrosMartes) {
        this.litrosMartes = litrosMartes;
    }

    /**
     * @return the litrosMiercoles
     */
    public int getLitrosMiercoles() {
        return litrosMiercoles;
    }

    /**
     * @param litrosMiercoles the litrosMiercoles to set
     */
    public void setLitrosMiercoles(int litrosMiercoles) {
        this.litrosMiercoles = litrosMiercoles;
    }

    /**
     * @return the litrosJueves
     */
    public int getLitrosJueves() {
        return litrosJueves;
    }

    /**
     * @param litrosJueves the litrosJueves to set
     */
    public void setLitrosJueves(int litrosJueves) {
        this.litrosJueves = litrosJueves;
    }

    /**
     * @return the litrosViernes
     */
    public int getLitrosViernes() {
        return litrosViernes;
    }

    /**
     * @param litrosViernes the litrosViernes to set
     */
    public void setLitrosViernes(int litrosViernes) {
        this.litrosViernes = litrosViernes;
    }

    /**
     * @return the litrosSabado
     */
    public int getLitrosSabado() {
        return litrosSabado;
    }

    /**
     * @param litrosSabado the litrosSabado to set
     */
    public void setLitrosSabado(int litrosSabado) {
        this.litrosSabado = litrosSabado;
    }

    /**
     * @return the litrosDomingo
     */
    public int getLitrosDomingo() {
        return litrosDomingo;
    }

    /**
     * @param litrosDomingo the litrosDomingo to set
     */
    public void setLitrosDomingo(int litrosDomingo) {
        this.litrosDomingo = litrosDomingo;
    }

    /**
     * @return the litrosTotal
     */
    public int getLitrosTotal() {
        return litrosTotal;
    }

    /**
     * @param litrosTotal the litrosTotal to set
     */
    public void setLitrosTotal(int litrosTotal) {
        this.litrosTotal = litrosTotal;
    }
    
    @Override
    public String toString(){
        return "Anep. Desde: " + this.getDesdeFecha() + " hasta: " + 
                this.getHastaFecha() + "Total de Litros: " + this.getLitrosTotal();    
    }
}
