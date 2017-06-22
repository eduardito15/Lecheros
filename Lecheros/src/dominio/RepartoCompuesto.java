/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 *
 * @author Edu
 */
@Entity
public class RepartoCompuesto extends ObjetoPersistente {
    
    @Column(unique = true)
    private int codigo;
    
    @Column
    private String nombre;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Reparto reparto1;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Reparto reparto2;

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

    /**
     * @return the reparto1
     */
    public Reparto getReparto1() {
        return reparto1;
    }

    /**
     * @param reparto1 the reparto1 to set
     */
    public void setReparto1(Reparto reparto1) {
        this.reparto1 = reparto1;
    }

    /**
     * @return the reparto2
     */
    public Reparto getReparto2() {
        return reparto2;
    }

    /**
     * @param reparto2 the reparto2 to set
     */
    public void setReparto2(Reparto reparto2) {
        this.reparto2 = reparto2;
    }
    
     @Override
    public String toString(){
        return Integer.toString(codigo) + " " + this.getNombre();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof RepartoCompuesto)) return false;
        else return ((RepartoCompuesto)obj).getNombre().equals(this.getNombre()) && ((RepartoCompuesto)obj).getReparto1().equals(this.getReparto1()) && ((RepartoCompuesto)obj).getReparto2().equals(this.getReparto2());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.nombre);
        return hash;
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
