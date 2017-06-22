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
public class RubroGasto extends ObjetoPersistente{
    
    private int codigo;
    private String nombre;

    /**
     * @return the codigo
     */
    @Column
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
    @Column
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
        return this.getCodigo() + " " + this.getNombre();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof RubroGasto)) {
            return false;
        } else {
            return ((RubroGasto) obj).getCodigo() == this.getCodigo() && ((RubroGasto) obj).getNombre().equals(this.getNombre());
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.codigo;
        hash = 41 * hash + Objects.hashCode(this.nombre);
        return hash;
    }
}
