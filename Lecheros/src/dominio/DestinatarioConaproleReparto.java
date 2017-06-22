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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Edu
 */
@Entity
@Table(name="DestinatarioConaproleReparto",
       uniqueConstraints = {@UniqueConstraint(columnNames={"numeroDestinatario", "reparto_id", "tipoDocumento_id"})})
public class DestinatarioConaproleReparto extends ObjetoPersistente {
    
    @Column
    private long numeroDestinatario;

    @OneToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @OneToOne(fetch = FetchType.EAGER)
    private DocumentoDeCompra tipoDocumento;

    /**
     * @return the numeroDestinatario
     */
    public long getNumeroDestinatario() {
        return numeroDestinatario;
    }

    /**
     * @param numeroDestinatario the numeroDestinatario to set
     */
    public void setNumeroDestinatario(long numeroDestinatario) {
        this.numeroDestinatario = numeroDestinatario;
    }

    /**
     * @return the reparto
     */
    public Reparto getReparto() {
        return reparto;
    }

    /**
     * @param reparto the reparto to set
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public String toString() {
        return Long.toString(numeroDestinatario) + " " + reparto.toString() + " " + tipoDocumento;
    }

    /**
     * @return the tipoDocumento
     */
    public DocumentoDeCompra getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(DocumentoDeCompra tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
