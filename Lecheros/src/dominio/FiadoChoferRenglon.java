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
public class FiadoChoferRenglon extends ObjetoPersistente {
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private FiadoChofer fiadoChofer;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    
    @Column(nullable = false)
    private double total;
    
    @Column
    private int envases;

    /**
     * @return the fiadoChofer
     */
    public FiadoChofer getFiadoChofer() {
        return fiadoChofer;
    }

    /**
     * @param fiadoChofer the fiadoChofer to set
     */
    public void setFiadoChofer(FiadoChofer fiadoChofer) {
        this.fiadoChofer = fiadoChofer;
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
        return this.getFiadoChofer() + " " + this.getCliente().getNombre() + " " + this.getTotal();
    }

    /**
     * @return the envases
     */
    public int getEnvases() {
        return envases;
    }

    /**
     * @param envases the envases to set
     */
    public void setEnvases(int envases) {
        this.envases = envases;
    }
    
}
