/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author Edu
 */
@Entity
public class GrupoDeArticulos extends ObjetoPersistente {
    
    @Column
    private String nombre;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Articulo> articulos;
    
    @Column
    private int facturarDeA;

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
     * @return the articulos
     */
    public List<Articulo> getArticulos() {
        return articulos;
    }

    /**
     * @param articulos the articulos to set
     */
    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrupoDeArticulos other = (GrupoDeArticulos) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +  nombre ;
    }

    /**
     * @return the facturarDeA
     */
    public int getFacturarDeA() {
        return facturarDeA;
    }

    /**
     * @param facturarDeA the facturarDeA to set
     */
    public void setFacturarDeA(int facturarDeA) {
        this.facturarDeA = facturarDeA;
    }
    
    
}
