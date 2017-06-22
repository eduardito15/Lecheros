/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class DocumentoDeVenta extends ObjetoPersistente {
    
    private String tipoDocumento;
    
    private boolean suma;

    /**
     * @return the nombre
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the nombre to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the suma
     */
    public boolean isSuma() {
        return suma;
    }

    /**
     * @param suma the suma to set
     */
    public void setSuma(boolean suma) {
        this.suma = suma;
    }
    
    @Override
    public String toString(){
        return this.getTipoDocumento();
    }
}
