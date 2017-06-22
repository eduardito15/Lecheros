/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

import dominio.ObjetoPersistente;
import dominio.usuarios.Usuario;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edu
 */
@Entity
public class LogDeExcepciones extends ObjetoPersistente {
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    
    @Column
    private String mensajeDeError;

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
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the mensajeDeError
     */
    public String getMensajeDeError() {
        return mensajeDeError;
    }

    /**
     * @param mensajeDeError the mensajeDeError to set
     */
    public void setMensajeDeError(String mensajeDeError) {
        this.mensajeDeError = mensajeDeError;
    }
    
    
}
