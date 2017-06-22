/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.usuarios;

import dominio.ObjetoPersistente;
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
public class LogDeOperacion extends ObjetoPersistente {
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    
    @OneToOne
    private Actividad actividad;
    
    @Column
    private String descripcion;

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
     * @return the a
     */
    public Actividad getActividad() {
        return actividad;
    }

    /**
     * @param a the a to set
     */
    public void setAactividad(Actividad a) {
        this.actividad = a;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
