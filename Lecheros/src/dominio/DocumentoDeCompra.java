/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.Objects;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class DocumentoDeCompra extends ObjetoPersistente {
    
    private String tipoDocumento;
    
    private boolean suma;
    
    private boolean esDocumentoInterno;

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
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof DocumentoDeCompra)) {
            return false;
        } else {
            return ((DocumentoDeCompra) obj).getId() == this.getId() && ((DocumentoDeCompra) obj).getTipoDocumento().equals(this.getTipoDocumento());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.tipoDocumento);
        return hash;
    }

    /**
     * @return the esDocumentoInterno
     */
    public boolean isEsDocumentoInterno() {
        return esDocumentoInterno;
    }

    /**
     * @param esDocumentoInterno the esDocumentoInterno to set
     */
    public void setEsDocumentoInterno(boolean esDocumentoInterno) {
        this.esDocumentoInterno = esDocumentoInterno;
    }
}
