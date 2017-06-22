/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author Edu
 */
@Entity
public class CoeficienteUtilidadCompras extends ObjetoPersistente {
    
    @Column
    private String nombre;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Articulo> articulos;
    
    @Column
    private double coeficiente;

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

    /**
     * @return the coeficiente
     */
    public double getCoeficiente() {
        return coeficiente;
    }

    /**
     * @param coeficiente the coeficiente to set
     */
    public void setCoeficiente(double coeficiente) {
        this.coeficiente = coeficiente;
    }

    @Override
    public String toString() {
        return this.getNombre() + " Coeficiente: " + this.getCoeficiente(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
