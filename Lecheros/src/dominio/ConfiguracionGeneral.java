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
public class ConfiguracionGeneral extends ObjetoPersistente {
    
    @Column
    private String rutaInforme;
    
    @Column
    private String rutaLog;
    
    @Column
    private String directorioInstalacion;
    
    @Column
    private String nombreImpresora;
    
    @Column 
    private int numeroEmpresaPS;
    
    @Column 
    private int numeroEmpresa2PS;

    /**
     * @return the rutaInforme
     */
    public String getRutaInforme() {
        return rutaInforme;
    }

    /**
     * @param rutaInforme the rutaInforme to set
     */
    public void setRutaInforme(String rutaInforme) {
        this.rutaInforme = rutaInforme;
    }

    /**
     * @return the rutaLog
     */
    public String getRutaLog() {
        return rutaLog;
    }

    /**
     * @param rutaLog the rutaLog to set
     */
    public void setRutaLog(String rutaLog) {
        this.rutaLog = rutaLog;
    }

    /**
     * @return the directorioInstalacion
     */
    public String getDirectorioInstalacion() {
        return directorioInstalacion;
    }

    /**
     * @param directorioInstalacion the directorioInstalacion to set
     */
    public void setDirectorioInstalacion(String directorioInstalacion) {
        this.directorioInstalacion = directorioInstalacion;
    }

    /**
     * @return the nombreImpresora
     */
    public String getNombreImpresora() {
        return nombreImpresora;
    }

    /**
     * @param nombreImpresora the nombreImpresora to set
     */
    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }

    /**
     * @return the numeroEmpresaPS
     */
    public int getNumeroEmpresaPS() {
        return numeroEmpresaPS;
    }

    /**
     * @param numeroEmpresaPS the numeroEmpresaPS to set
     */
    public void setNumeroEmpresaPS(int numeroEmpresaPS) {
        this.numeroEmpresaPS = numeroEmpresaPS;
    }

    /**
     * @return the numeroEmpresa2PS
     */
    public int getNumeroEmpresa2PS() {
        return numeroEmpresa2PS;
    }

    /**
     * @param numeroEmpresa2PS the numeroEmpresa2PS to set
     */
    public void setNumeroEmpresa2PS(int numeroEmpresa2PS) {
        this.numeroEmpresa2PS = numeroEmpresa2PS;
    }

    
}
