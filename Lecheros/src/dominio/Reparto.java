/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class Reparto extends ObjetoPersistente{
    
    private int codigo;
    private String nombre;
    
    private int numeroVendedorPS;

    
    /**
     * @return the codigo
     */
    @Column(unique = true)
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    /**
     * @return the nombre
     */
    @Column(unique = true)
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString(){
        return Integer.toString(codigo) + " " + this.getNombre();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Reparto)) return false;
        else return ((Reparto)obj).getCodigo() == this.getCodigo() && ((Reparto)obj).getNombre().equals(this.getNombre());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.codigo;
        hash = 19 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    /**
     * @return the numeroVendedorPS
     */
    @Column
    public int getNumeroVendedorPS() {
        return numeroVendedorPS;
    }

    /**
     * @param numeroVendedorPS the numeroVendedorPS to set
     */
    public void setNumeroVendedorPS(int numeroVendedorPS) {
        this.numeroVendedorPS = numeroVendedorPS;
    }
    
    
}
