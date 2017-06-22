/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.usuarios;

import dominio.ObjetoPersistente;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Edu
 */
@Entity
public class Usuario extends ObjetoPersistente {
    
    @Column
    private String nombreDeUsuario;
    
    @Column
    private String contrasena;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Rol rol;
    
    @Column
    private boolean activo;

    /**
     * @return the nombreDeUsuario
     */
    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    /**
     * @param nombreDeUsuario the nombreDeUsuario to set
     */
    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    /**
     * @return the contrasena
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * @param contrasena the contrasena to set
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * @return the rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return this.getNombreDeUsuario(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Usuario)) return false;
        else return ((Usuario)obj).getNombreDeUsuario().equals(this.getNombreDeUsuario());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nombreDeUsuario);
        return hash;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
    
}
