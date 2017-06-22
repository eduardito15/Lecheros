/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.CoeficienteUtilidadCompras;
import dominio.FamiliaDeProducto;
import dominio.GrupoDeArticulos;
import dominio.Precio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class SistemaMantenimientoArticulos {
    
    private static SistemaMantenimientoArticulos instance;

    /**
     * @return the instance
     */
    public static SistemaMantenimientoArticulos getInstance() {
        if(instance == null){
            instance = new SistemaMantenimientoArticulos();
        }
        return instance;
    }
    
    private SistemaMantenimientoArticulos(){
        
    }
    
    public Articulo devolverArticuloPorCodigo(int codigo) throws Exception {
        Articulo a = new Articulo();
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Articulo where codigo="+codigo);
        a = (Articulo)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return a;
    }
    
    public Precio devolverPrecioPorArticuloyFecha(Articulo articulo, Date fecha) throws Exception {
        Precio precio = null;
        for(Precio p : articulo.getPrecios()){
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            if(formatter.format(p.getFecha()).equals(formatter.format(fecha))){
                precio = p;
            }
        }
        return precio;
    }
    
    public Precio devolverPrecioParaFechaPorArticulo(Articulo a, Date fecha) throws Exception {
        Precio precio = null;
        Date ultimaFecha = null;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (Precio p : a.getPrecios()) {
            if (p.getFecha().before(fecha) || p.getFecha().equals(fecha)) {
                ultimaFecha = p.getFecha();
                precio = p;
            }
            if((p.getFecha().before(fecha) || p.getFecha().equals(fecha)) && p.getFecha().after(ultimaFecha)){
                ultimaFecha = p.getFecha();
                precio = p;
            }
        }
        return precio;
    }
    
    public boolean guardarPrecio(Articulo a,Date fecha, double precioCompra, double precioVenta) throws Exception {
        Precio p = new Precio();
        p.setArticulo(a);
        a.getPrecios().add(p);
        
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("yyyy-MM-dd");
        //String f = formatter.format(fecha.getTime());
        
            //Date fechaPrecio = formatter.parse(f);
        p.setFecha(fecha);
        
        
        p.setPrecioCompra(precioCompra);
        p.setPrecioVenta(precioVenta);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Ingreo el precio  :  " + p.toString() );
        return GenericDAO.getGenericDAO().guardar(p);
    }
    
    public boolean actualizarPrecio(Precio p) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio  :  " + p );
        return GenericDAO.getGenericDAO().actualizar(p);
    }
    
    public void cargarPrecios(String rutaArchivo, Date fecha, int desdeFila, int hastaFila) throws FileNotFoundException, IOException, FileNotFoundException, Exception{
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(rutaArchivo));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet firstSheet = (XSSFSheet) workbook.getSheetAt(0);
            XSSFSheet segundaHoja = (XSSFSheet) workbook.getSheetAt(1);
            //Iterator<Row> iterator = firstSheet.iterator();
            for(int i = desdeFila; i<=hastaFila;i++){
                Row fila = firstSheet.getRow(i-1);
                Row filaCompra = segundaHoja.getRow(i-1);
                //String codigoArticulo = fila.getCell(0).getStringCellValue();
                int codArt = (int)fila.getCell(0).getNumericCellValue();
                try{
                    Double precio = fila.getCell(4).getNumericCellValue();
                    //Articulo a = this.devolverArticuloPorCodigo(Integer.parseInt(codigoArticulo));
                   
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String f = formatter.format(fecha.getTime());
                    
                    Articulo a = this.devolverArticuloPorCodigo(codArt);
                    if(a!=null){
                        //Precio precioArtVta = this.devolverPrecioPorArticuloyFecha(a, new Date());
                        Precio precioArtVta = this.devolverPrecioPorArticuloyFecha(a, formatter.parse(f));
                        
                        if(precioArtVta==null){
                            Precio p = new Precio();
                            p.setArticulo(a);
                            
                            //p.setFecha(new Date());
                            p.setFecha(formatter.parse(f));
                            
                            p.setPrecioVenta(precio);
                            p.setPrecioCompra(0.0);
                            a.getPrecios().add(p);
                            GenericDAO.getGenericDAO().guardar(p);
                        } else {
                            precioArtVta.setPrecioVenta(precio);
                            GenericDAO.getGenericDAO().actualizar(precioArtVta);
                        }
                    }
                    
                    //String codigoArticuloCompra = filaCompra.getCell(0).getStringCellValue();
                    int codArtCompra = (int)filaCompra.getCell(0).getNumericCellValue();
                    
                    Double precioCompra = filaCompra.getCell(4).getNumericCellValue();
                    
                    //Articulo aux = this.devolverArticuloPorCodigo(Integer.parseInt(codigoArticuloCompra));
                    Articulo aux = this.devolverArticuloPorCodigo(codArtCompra);
                    if(aux != null){
                        
                        //Precio precioArt = this.devolverPrecioPorArticuloyFecha(aux, new Date());
                        Precio precioArt = this.devolverPrecioPorArticuloyFecha(aux, formatter.parse(f));
                        
                        if(precioArt == null){
                            Precio precioCom = new Precio();
                            precioCom.setArticulo(aux);
                            
                            //precioCom.setFecha(new Date());
                            precioCom.setFecha(formatter.parse(f));
                            
                            precioCom.setPrecioCompra(precioCompra);
                            precioCom.setPrecioVenta(0.0);
                            aux.getPrecios().add(precioCom);
                            GenericDAO.getGenericDAO().guardar(precioCom);
                        } else {
                            precioArt.setPrecioCompra(precioCompra);
                            GenericDAO.getGenericDAO().actualizar(precioArt);
                        }
                    }
                    
                }
                catch(NumberFormatException e){
                    
                } catch (ParseException ex) {
                    Logger.getLogger(SistemaMantenimientoArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            
            workbook.close();
            inputStream.close();
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarPreciosAutomaticamente, "Ingreo precios desde archivo " );
        }  finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    public void cargarPreciosPromociones(String rutaArchivo, Date fecha, int desdeFila, int hastaFila) throws FileNotFoundException, IOException, FileNotFoundException, Exception{
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(rutaArchivo));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet firstSheet = (XSSFSheet) workbook.getSheetAt(0);
            //Iterator<Row> iterator = firstSheet.iterator();
            for(int i = desdeFila; i<=hastaFila;i++){
                Row fila = firstSheet.getRow(i-1);
                //String codigoArticulo = fila.getCell(0).getStringCellValue();
                int codArt = (int)fila.getCell(0).getNumericCellValue();
                try{
                    Double precio = fila.getCell(1).getNumericCellValue();
                    //Articulo a = this.devolverArticuloPorCodigo(Integer.parseInt(codigoArticulo));
                   
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String f = formatter.format(fecha.getTime());
                    
                    Articulo a = this.devolverArticuloPorCodigo(codArt);
                    if(a!=null){
                        //Precio precioArtVta = this.devolverPrecioPorArticuloyFecha(a, new Date());
                        Precio precioArtVta = this.devolverPrecioPorArticuloyFecha(a, formatter.parse(f));
                        
                        if(precioArtVta==null){
                            Precio p = new Precio();
                            p.setArticulo(a);
                            
                            //p.setFecha(new Date());
                            p.setFecha(formatter.parse(f));
                            
                            p.setPrecioVenta(precio);
                            p.setPrecioCompra(0.0);
                            a.getPrecios().add(p);
                            GenericDAO.getGenericDAO().guardar(p);
                        } else {
                            precioArtVta.setPrecioVenta(precio);
                            GenericDAO.getGenericDAO().actualizar(precioArtVta);
                        }
                    }
                }
                catch(NumberFormatException e){
                    
                } catch (ParseException ex) {
                    Logger.getLogger(SistemaMantenimientoArticulos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            
            workbook.close();
            inputStream.close();
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarPreciosAutomaticamente, "Ingreo precios desde archivo " );
        }  finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    public boolean actualizarArticulo(Articulo a) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el articulo  :  " + a );
        return GenericDAO.getGenericDAO().actualizar(a);
    }
    
    public boolean eliminarArticulo(Articulo a) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Elimino el articulo  :  " + a );
        return GenericDAO.getGenericDAO().borrar(a);
    }
    
    public List<Articulo> devolverArticulosPorFamilia(FamiliaDeProducto fp) throws Exception {
        List<Articulo> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Articulo where familia_id="+fp.getId());
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public int devolverCodigoMenor() throws Exception {
        int retorno = 0;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT min(codigo) FROM Articulo");
        retorno = (int)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public int devolverCodigoMayor() throws Exception {
        int retorno = 0;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT max(codigo) FROM Articulo WHERE codigo != 320101");
        retorno = (int) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Articulo> devolverArticulosEntreCodigos(int codIni, int codFin) throws Exception {
        List<Articulo> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Articulo WHERE codigo >= " + codIni + " and codigo <= " + codFin);
        retorno = (List<Articulo>)consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Articulo> devolverArticulos() throws Exception {
        List<Articulo> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Articulo");
        retorno = (List<Articulo>)consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    //Metodos Grupos de Articulos
    public boolean agregarGrupoDeArticulos(String nombre, List<Articulo> articulos, int facturarDeA) throws Exception {

        GrupoDeArticulos gda = new GrupoDeArticulos();
        gda.setNombre(nombre);
        gda.setArticulos(articulos);
        gda.setFacturarDeA(facturarDeA);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGruposDeArticulos, "Ingreo el grupo de articulos  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(gda);
    }

    public List<GrupoDeArticulos> devolverGruposDeArticulos() {
        List<GrupoDeArticulos> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM GrupoDeArticulos");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarGrupoDeArticulos(GrupoDeArticulos gda) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGruposDeArticulos, "Actualizo el grupo de articulos   :  " + gda );
        return GenericDAO.getGenericDAO().actualizar(gda);
    }

    public boolean eliminarGrupoDeArticulos(GrupoDeArticulos gda) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGruposDeArticulos, "Elimino el grupo de articulos  :  " + gda );
        return GenericDAO.getGenericDAO().borrar(gda);
    }
    
    public GrupoDeArticulos devolverGrupoDeArticuloPorArticulo(Articulo a) {
        GrupoDeArticulos retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM GrupoDeArticulos");
        List<GrupoDeArticulos> grupos = consulta.list();
        session.getTransaction().commit();
        session.close();
        for(GrupoDeArticulos ga : grupos) {
            for(Articulo art : ga.getArticulos()) {
                if(art.equals(a)) {
                    retorno = ga;
                    break;
                }
            }
            if(retorno !=null) {
                break;
            }
        }
        
        return retorno;
    }
    
    public GrupoDeArticulos devolverGrupoDeArticuloPorNombre(String nombre) {
        GrupoDeArticulos retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM GrupoDeArticulos WHERE nombre = :nom");
        consulta.setString("nom", nombre);
        retorno = (GrupoDeArticulos) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    //Metodos Coeficientes
    public boolean agregarCoeficienteUtilidadCompras(String nombre, double coeficiente, List<Articulo> articulos) throws Exception {

        CoeficienteUtilidadCompras cuc = new CoeficienteUtilidadCompras();
        cuc.setNombre(nombre);
        cuc.setCoeficiente(coeficiente);
        cuc.setArticulos(articulos);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCoeficientesUtilidadCompras, "Ingreo el coeficiente de utilidades  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(cuc);
    }

    public List<CoeficienteUtilidadCompras> devolverCoeficientesUtilidadesCompras() {
        List<CoeficienteUtilidadCompras> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM CoeficienteUtilidadCompras");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarCoeficienteUtilidadCompras(CoeficienteUtilidadCompras cuc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCoeficientesUtilidadCompras, "Actualizo el coeficiente   :  " + cuc );
        return GenericDAO.getGenericDAO().actualizar(cuc);
    }

    public boolean eliminarCoeficienteUtilidadCompras(CoeficienteUtilidadCompras cuc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCoeficientesUtilidadCompras, "Elimino el coeficiente  :  " + cuc );
        return GenericDAO.getGenericDAO().borrar(cuc);
    }
    
    public CoeficienteUtilidadCompras devolverCoeficienteUtilidadPorArticulo(Articulo a) {
        CoeficienteUtilidadCompras retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM CoeficienteUtilidadCompras");
        List<CoeficienteUtilidadCompras> coeficientes = consulta.list();
        session.getTransaction().commit();
        session.close();
        for(CoeficienteUtilidadCompras cuc : coeficientes) {
            for(Articulo art : cuc.getArticulos()) {
                if(art.equals(a)) {
                    retorno = cuc;
                    break;
                }
            }
            if(retorno !=null) {
                break;
            }
        }
        
        return retorno;
    }
}
