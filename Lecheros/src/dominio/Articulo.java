/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Edu
 */
@Entity
public class Articulo extends ObjetoPersistente {
    
    @Column(unique = true)
    private int codigo;
    
    @Column
    private String descripcion;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Iva iva;

    @OneToOne(fetch = FetchType.EAGER)
    private FamiliaDeProducto familia;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "articulo")
    private List<Precio> precios;
    
    public Articulo(){
        precios = new ArrayList<>();
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

    /**
     * @return the iva
     */
    public Iva getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(Iva iva) {
        this.iva = iva;
    }

    /**
     * @return the familia
     */
    public FamiliaDeProducto getFamilia() {
        return familia;
    }

    /**
     * @param familia the familia to set
     */
    public void setFamilia(FamiliaDeProducto familia) {
        this.familia = familia;
    }
    
    
    /**
     * @return the precios
     */
    public List<Precio> getPrecios() {
        return precios;
    }

    /**
     * @param precios the precios to set
     */
    public void setPrecios(List<Precio> precios) {
        this.precios = precios;
    }
    
    @Override
    public String toString(){
        return Integer.toString(codigo) + " " + this.getDescripcion();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Articulo)) return false;
        else return ((Articulo)obj).getCodigo() == this.getCodigo();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.codigo;
        return hash;
    }
}
