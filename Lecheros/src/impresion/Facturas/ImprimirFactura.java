/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impresion.Facturas;

import dominio.Factura;
import java.util.List;


/**
 *
 * @author Edu
 */
public interface ImprimirFactura {
    
    public void imprimirFactura(Factura f) throws Exception;
    public void imprimirListaDeFacturas(List<Factura> facturas) throws Exception;
    
}
