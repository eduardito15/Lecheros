/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class ConfiguracionLiquidacion extends ObjetoPersistente {
    
    @Column
    private boolean modoAcumulativo;
    
    @Column
    private boolean mostrarCamion;
    
    @Column
    private boolean mostrarCorrecionDeDiferencia;
    
    @Column
    private boolean mostrarEntregaCheques;
    
    @Column
    private boolean mostrarFiadoClientesCobraChofer;
    
    @Column
    private boolean mostrarFiadoClientesCobraEmpresa;
    
    @Column
    private boolean mostrarInau;
    
    @Column
    private boolean mostrarAnep;
    
    @Column
    private boolean retenciones;

    /**
     * @return the modoAcumulativo
     */
    public boolean isModoAcumulativo() {
        return modoAcumulativo;
    }

    /**
     * @param modoAcumulativo the modoAcumulativo to set
     */
    public void setModoAcumulativo(boolean modoAcumulativo) {
        this.modoAcumulativo = modoAcumulativo;
    }

    /**
     * @return the mostrarCorrecionDeDiferencia
     */
    public boolean isMostrarCorrecionDeDiferencia() {
        return mostrarCorrecionDeDiferencia;
    }

    /**
     * @param mostrarCorrecionDeDiferencia the mostrarCorrecionDeDiferencia to set
     */
    public void setMostrarCorrecionDeDiferencia(boolean mostrarCorrecionDeDiferencia) {
        this.mostrarCorrecionDeDiferencia = mostrarCorrecionDeDiferencia;
    }

    /**
     * @return the mostrarEntregaCheques
     */
    public boolean isMostrarEntregaCheques() {
        return mostrarEntregaCheques;
    }

    /**
     * @param mostrarEntregaCheques the mostrarEntregaCheques to set
     */
    public void setMostrarEntregaCheques(boolean mostrarEntregaCheques) {
        this.mostrarEntregaCheques = mostrarEntregaCheques;
    }

    /**
     * @return the mostrarFiadoClientesCobraChofer
     */
    public boolean isMostrarFiadoClientesCobraChofer() {
        return mostrarFiadoClientesCobraChofer;
    }

    /**
     * @param mostrarFiadoClientesCobraChofer the mostrarFiadoClientesCobraChofer to set
     */
    public void setMostrarFiadoClientesCobraChofer(boolean mostrarFiadoClientesCobraChofer) {
        this.mostrarFiadoClientesCobraChofer = mostrarFiadoClientesCobraChofer;
    }

    /**
     * @return the mostrarFiadoClientesCobraEmpresa
     */
    public boolean isMostrarFiadoClientesCobraEmpresa() {
        return mostrarFiadoClientesCobraEmpresa;
    }

    /**
     * @param mostrarFiadoClientesCobraEmpresa the mostrarFiadoClientesCobraEmpresa to set
     */
    public void setMostrarFiadoClientesCobraEmpresa(boolean mostrarFiadoClientesCobraEmpresa) {
        this.mostrarFiadoClientesCobraEmpresa = mostrarFiadoClientesCobraEmpresa;
    }

    /**
     * @return the mostrarInau
     */
    public boolean isMostrarInau() {
        return mostrarInau;
    }

    /**
     * @param mostrarInau the mostrarInau to set
     */
    public void setMostrarInau(boolean mostrarInau) {
        this.mostrarInau = mostrarInau;
    }

    /**
     * @return the retenciones
     */
    public boolean isRetenciones() {
        return retenciones;
    }

    /**
     * @param retenciones the retenciones to set
     */
    public void setRetenciones(boolean retenciones) {
        this.retenciones = retenciones;
    }

    /**
     * @return the mostrarCamion
     */
    public boolean isMostrarCamion() {
        return mostrarCamion;
    }

    /**
     * @param mostrarCamion the mostrarCamion to set
     */
    public void setMostrarCamion(boolean mostrarCamion) {
        this.mostrarCamion = mostrarCamion;
    }

    /**
     * @return the mostrarAnep
     */
    public boolean isMostrarAnep() {
        return mostrarAnep;
    }

    /**
     * @param mostrarAnep the mostrarAnep to set
     */
    public void setMostrarAnep(boolean mostrarAnep) {
        this.mostrarAnep = mostrarAnep;
    }
    
}
