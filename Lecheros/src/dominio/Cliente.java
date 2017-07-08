/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class Cliente extends ObjetoPersistente {
    
    @Column
    private boolean prorrateo;
    
    @Column
    private boolean cobraChofer;
    
    @OneToOne(fetch = FetchType.EAGER)
    private GrupoCliente grupoCliente;
    
    @Column
    private String codigo;
    
    @Column(unique = true)
    private long codigoPS;
    
    @Column
    private int sucursalPS;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Reparto reparto;
    
    @Column
    private String nombre;
    
    @Column 
    private String razonSocial;
    
    @Column
    private String rut;
    
    @Column
    private String direccion;
    
    @Column
    private String telefono;
    
    @Column
    private String correoElectronico;
    
    @Column
    private double deuda;
    
    @Column
    private int litrosComun;
    
    @Column
    private int litrosUltra;
    
    @Column
    private int litrosDeslactosada;
    
    @Column
    private String frecuenciaFacturacionLeche;
    
    @Column
    private double productos;
    
    @Column
    private String frecuenciaFacturacionProductos;
    
    @Column
    private boolean activo;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<ProductoClienteProrrateo> productosClienteProrrateo;
    
    @Column
    private String frecuenciaFacturacionLecheUltra;
    
    @Column
    private String frecuenciaFacturacionLecheDeslactosada;
    
    public Cliente(){
        productosClienteProrrateo = new ArrayList<>();
    }

    /**
     * @return the prorrateo
     */
    public boolean isProrrateo() {
        return prorrateo;
    }

    /**
     * @param prorrateo the prorrateo to set
     */
    public void setProrrateo(boolean prorrateo) {
        this.prorrateo = prorrateo;
    }

    /**
     * @return the Codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param Codigo the Codigo to set
     */
    public void setCodigo(String Codigo) {
        this.codigo = Codigo;
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
     * @return the rut
     */
    public String getRut() {
        return rut;
    }

    /**
     * @param rut the rut to set
     */
    public void setRut(String rut) {
        this.rut = rut;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the correoElectronico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * @param correoElectronico the correoElectronico to set
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * @return the litrosComun
     */
    public int getLitrosComun() {
        return litrosComun;
    }

    /**
     * @param litrosComun the litrosComun to set
     */
    public void setLitrosComun(int litrosComun) {
        this.litrosComun = litrosComun;
    }

    /**
     * @return the litrosUltra
     */
    public int getLitrosUltra() {
        return litrosUltra;
    }

    /**
     * @param litrosUltra the litrosUltra to set
     */
    public void setLitrosUltra(int litrosUltra) {
        this.litrosUltra = litrosUltra;
    }

    /**
     * @return the productos
     */
    public double getProductos() {
        return productos;
    }

    /**
     * @param productos the productos to set
     */
    public void setProductos(double productos) {
        this.productos = productos;
    }

    /**
     * @return the deuda
     */
    public double getDeuda() {
        return deuda;
    }

    /**
     * @param deuda the deuda to set
     */
    public void setDeuda(double deuda) {
        this.deuda = deuda;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the cobraChofer
     */
    public boolean isCobraChofer() {
        return cobraChofer;
    }

    /**
     * @param cobraChofer the cobraChofer to set
     */
    public void setCobraChofer(boolean cobraChofer) {
        this.cobraChofer = cobraChofer;
    }

    /**
     * @return the grupoCliente
     */
    public GrupoCliente getGrupoCliente() {
        return grupoCliente;
    }

    /**
     * @param grupoCliente the grupoCliente to set
     */
    public void setGrupoCliente(GrupoCliente grupoCliente) {
        this.grupoCliente = grupoCliente;
    }
    
    @Override
    public String toString(){
        return this.getNombre() + "-" + this.getRazonSocial();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof Cliente)) return false;
        else return ((Cliente)obj).getNombre().equals(this.getNombre()) && ((Cliente)obj).getCodigo().equals(this.getCodigo()) && ((Cliente)obj).getReparto().equals(this.getReparto());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.codigo);
        hash = 83 * hash + Objects.hashCode(this.reparto);
        hash = 83 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    /**
     * @return the productosClienteProrrateo
     */
    public List<ProductoClienteProrrateo> getProductosClienteProrrateo() {
        return productosClienteProrrateo;
    }

    /**
     * @param productosClienteProrrateo the productosClienteProrrateo to set
     */
    public void setProductosClienteProrrateo(List<ProductoClienteProrrateo> productosClienteProrrateo) {
        this.productosClienteProrrateo = productosClienteProrrateo;
    }

    /**
     * @return the frecuenciaFacturacionLeche
     */
    public String getFrecuenciaFacturacionLeche() {
        return frecuenciaFacturacionLeche;
    }

    /**
     * @param frecuenciaFacturacionLeche the frecuenciaFacturacionLeche to set
     */
    public void setFrecuenciaFacturacionLeche(String frecuenciaFacturacionLeche) {
        this.frecuenciaFacturacionLeche = frecuenciaFacturacionLeche;
    }

    /**
     * @return the frecuenciaFacturacionProductos
     */
    public String getFrecuenciaFacturacionProductos() {
        return frecuenciaFacturacionProductos;
    }

    /**
     * @param frecuenciaFacturacionProductos the frecuenciaFacturacionProductos to set
     */
    public void setFrecuenciaFacturacionProductos(String frecuenciaFacturacionProductos) {
        this.frecuenciaFacturacionProductos = frecuenciaFacturacionProductos;
    }

    /**
     * @return the codigoPS
     */
    public long getCodigoPS() {
        return codigoPS;
    }

    /**
     * @param codigoPS the codigoPS to set
     */
    public void setCodigoPS(long codigoPS) {
        this.codigoPS = codigoPS;
    }

    /**
     * @return the litrosDeslactosada
     */
    public int getLitrosDeslactosada() {
        return litrosDeslactosada;
    }

    /**
     * @param litrosDeslactosada the litrosDeslactosada to set
     */
    public void setLitrosDeslactosada(int litrosDeslactosada) {
        this.litrosDeslactosada = litrosDeslactosada;
    }

    /**
     * @return the razonSocial
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * @param razonSocial the razonSocial to set
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * @return the frecuenciaFacturacionLecheUltra
     */
    public String getFrecuenciaFacturacionLecheUltra() {
        return frecuenciaFacturacionLecheUltra;
    }

    /**
     * @param frecuenciaFacturacionLecheUltra the frecuenciaFacturacionLecheUltra to set
     */
    public void setFrecuenciaFacturacionLecheUltra(String frecuenciaFacturacionLecheUltra) {
        this.frecuenciaFacturacionLecheUltra = frecuenciaFacturacionLecheUltra;
    }

    /**
     * @return the frecuenciaFacturacionLecheDeslactosada
     */
    public String getFrecuenciaFacturacionLecheDeslactosada() {
        return frecuenciaFacturacionLecheDeslactosada;
    }

    /**
     * @param frecuenciaFacturacionLecheDeslactosada the frecuenciaFacturacionLecheDeslactosada to set
     */
    public void setFrecuenciaFacturacionLecheDeslactosada(String frecuenciaFacturacionLecheDeslactosada) {
        this.frecuenciaFacturacionLecheDeslactosada = frecuenciaFacturacionLecheDeslactosada;
    }

    /**
     * @return the sucursalPS
     */
    public int getSucursalPS() {
        return sucursalPS;
    }

    /**
     * @param sucursalPS the sucursalPS to set
     */
    public void setSucursalPS(int sucursalPS) {
        this.sucursalPS = sucursalPS;
    }
    
    
}
