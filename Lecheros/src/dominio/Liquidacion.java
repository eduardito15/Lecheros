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
public class Liquidacion extends ObjetoPersistente {
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Chofer chofer;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Camion camion;
    
    @Column
    private double saldoAnterior;
    
    @Column
    private double compras;
    
    @Column
    private double utilidad;
    
    @Column
    private double remitoPinchadas;
    
    @Column
    private double gastos;
    
    @Column
    private double entregaEfectivo;
    
    @Column
    private double entregaCheques;
    
    @Column
    private double correcionDeDiferencia;
    
    @Column
    private double deuda;
    
    @Column
    private double inventario;
    
    @Column
    private double fiadoChofer;
    
    @Column
    private double fiadoEmpresa;
    
    @Column
    private double inau;
    
    @Column 
    private double anep;
    
    @Column
    private double retenciones;
    
    @Column
    private double diferencia;
    
    @Column
    private boolean cerrada;
    
    //Campo agregado para GIAMO para cobrarle al chofer un monto X que a veces les pasa.
    //@Column
    //private double correccionDeDiferenciaMenos;
    
    public Liquidacion() {
        //correccionDeDiferenciaMenos = 0;
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
     * @return the camion
     */
    public Camion getCamion() {
        return camion;
    }

    /**
     * @param camion the camion to set
     */
    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    /**
     * @return the saldoAnterior
     */
    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    /**
     * @param saldoAnterior the saldoAnterior to set
     */
    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    /**
     * @return the compras
     */
    public double getCompras() {
        return compras;
    }

    /**
     * @param compras the compras to set
     */
    public void setCompras(double compras) {
        this.compras = compras;
    }

    /**
     * @return the utilidad
     */
    public double getUtilidad() {
        return utilidad;
    }

    /**
     * @param utilidad the utilidad to set
     */
    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }

    /**
     * @return the remitoPinchadas
     */
    public double getRemitoPinchadas() {
        return remitoPinchadas;
    }

    /**
     * @param remitoPinchadas the remitoPinchadas to set
     */
    public void setRemitoPinchadas(double remitoPinchadas) {
        this.remitoPinchadas = remitoPinchadas;
    }

    /**
     * @return the gastos
     */
    public double getGastos() {
        return gastos;
    }

    /**
     * @param gastos the gastos to set
     */
    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    /**
     * @return the entregaEfectivo
     */
    public double getEntregaEfectivo() {
        return entregaEfectivo;
    }

    /**
     * @param entregaEfectivo the entregaEfectivo to set
     */
    public void setEntregaEfectivo(double entregaEfectivo) {
        this.entregaEfectivo = entregaEfectivo;
    }

    /**
     * @return the entregaCheques
     */
    public double getEntregaCheques() {
        return entregaCheques;
    }

    /**
     * @param entregaCheques the entregaCheques to set
     */
    public void setEntregaCheques(double entregaCheques) {
        this.entregaCheques = entregaCheques;
    }

    /**
     * @return the correcionDeDiferencia
     */
    public double getCorrecionDeDiferencia() {
        return correcionDeDiferencia;
    }

    /**
     * @param correcionDeDiferencia the correcionDeDiferencia to set
     */
    public void setCorrecionDeDiferencia(double correcionDeDiferencia) {
        this.correcionDeDiferencia = correcionDeDiferencia;
    }

    /**
     * @return the deuda
     */
    public double getDeuda() {
        return deuda;
    }

    /**
     * @param deuda the deuda to set
     */
    public void setDeuda(double deuda) {
        this.deuda = deuda;
    }

    /**
     * @return the inventario
     */
    public double getInventario() {
        return inventario;
    }

    /**
     * @param inventario the inventario to set
     */
    public void setInventario(double inventario) {
        this.inventario = inventario;
    }

    /**
     * @return the fiadoChofer
     */
    public double getFiadoChofer() {
        return fiadoChofer;
    }

    /**
     * @param fiadoChofer the fiadoChofer to set
     */
    public void setFiadoChofer(double fiadoChofer) {
        this.fiadoChofer = fiadoChofer;
    }

    /**
     * @return the fiadoEmpresa
     */
    public double getFiadoEmpresa() {
        return fiadoEmpresa;
    }

    /**
     * @param fiadoEmpresa the fiadoEmpresa to set
     */
    public void setFiadoEmpresa(double fiadoEmpresa) {
        this.fiadoEmpresa = fiadoEmpresa;
    }

    /**
     * @return the inau
     */
    public double getInau() {
        return inau;
    }

    /**
     * @param inau the inau to set
     */
    public void setInau(double inau) {
        this.inau = inau;
    }

    /**
     * @return the retenciones
     */
    public double getRetenciones() {
        return retenciones;
    }

    /**
     * @param retenciones the retenciones to set
     */
    public void setRetenciones(double retenciones) {
        this.retenciones = retenciones;
    }

    /**
     * @return the diferencia
     */
    public double getDiferencia() {
        return diferencia;
    }

    /**
     * @param diferencia the diferencia to set
     */
    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }
    
    @Override
    public String toString(){
        return "Liq: " + this.getFecha().toString() + " " + this.getReparto().toString() + " Diferencia: " + Double.toString(diferencia);
    }

    /**
     * @return the chofer
     */
    public Chofer getChofer() {
        return chofer;
    }

    /**
     * @param chofer the chofer to set
     */
    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    /**
     * @return the anep
     */
    public double getAnep() {
        return anep;
    }

    /**
     * @param anep the anep to set
     */
    public void setAnep(double anep) {
        this.anep = anep;
    }

    /**
     * @return the cerrada
     */
    public boolean getCerrada() {
        return cerrada;
    }

    /**
     * @param cerrada the cerrada to set
     */
    public void setCerrada(boolean cerrada) {
        this.cerrada = cerrada;
    }

    /**
     * @return the correccionDeDiferenciaMenos
     */
    /*public double getCorreccionDeDiferenciaMenos() {
        return correccionDeDiferenciaMenos;
    }*/

    /**
     * @param correccionDeDiferenciaMenos the correccionDeDiferenciaMenos to set
     */
    /*public void setCorreccionDeDiferenciaMenos(double correccionDeDiferenciaMenos) {
        this.correccionDeDiferenciaMenos = correccionDeDiferenciaMenos;
    }*/
    
    
}
