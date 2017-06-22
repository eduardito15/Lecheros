/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 *
 * @author Edu
 */
@Entity
public class Chofer extends ObjetoPersistente{
    
    @Column(unique = true)
    private String nombre;
    
    @Column(unique = true)
    private int codigo;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Comision comision;

    /**
     * @return the nombre
     */
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
        return this.getCodigo() + " " +  this.getNombre();
    }

    /**
     * @return the comision
     */
    public Comision getComision() {
        return comision;
    }

    /**
     * @param comision the comision to set
     */
    public void setComision(Comision comision) {
        this.comision = comision;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
