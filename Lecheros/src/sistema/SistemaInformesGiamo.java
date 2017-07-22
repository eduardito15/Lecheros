/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.Factura;
import dominio.FacturaRenglon;
import dominio.Precio;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;
import util.Util;

/**
 *
 * @author Edu
 */
public class SistemaInformesGiamo {
    
    private static SistemaInformesGiamo instance;
    SimpleDateFormat formatter;
    DecimalFormat df;
    /**
     * @return the instance
     */
    public static SistemaInformesGiamo getInstance() {
        if (instance == null) {
            instance = new SistemaInformesGiamo();
        }
        return instance;
    }

    private SistemaInformesGiamo() {
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        df = new DecimalFormat("0.00");
    }
    
    public void importarFacturasManuales(String rutaArchivo, int desdeFila, int hastaFila) throws FileNotFoundException, IOException, Exception {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(rutaArchivo));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet firstSheet = (XSSFSheet) workbook.getSheetAt(0);
            
            //Iterator<Row> iterator = firstSheet.iterator();
            for(int i = desdeFila; i<=hastaFila;i++){
                Row fila = firstSheet.getRow(i-1);
                
                //String codigoArticulo = fila.getCell(0).getStringCellValue();
                int numero = (int)fila.getCell(1).getNumericCellValue();
                Date fecha = fila.getCell(2).getDateCellValue();
                
                try{
                   
                    
                }
                catch(NumberFormatException e){
                    
                } 
            } 
            
            workbook.close();
            inputStream.close();
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarPreciosAutomaticamente, "Ingreo precios desde archivo " );
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }  finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    public void sacarInformeContadoraGiamo(Date fechaDesde, Date fechaHasta) throws Exception {
        Writer writer = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = devolverMesString(mes);
            String nombreCapeta = "InformeContador" + mesS + year;
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + "InformeFacturasDesde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".txt")), "utf-8"));
            
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f WHERE f.fecha BETWEEN :stDate AND :edDate");
            consulta.setDate("stDate", fechaDesde);
            consulta.setDate("edDate", fechaHasta);
            List<Factura> facturas = consulta.list();
            session.getTransaction().commit();
            session.close();
            
            writer.write(";BorraExistentes = Si");
            
            String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
            writer.write(str);
            
            for (Factura f : facturas) {
                boolean esDeEnvases = false;
                //double netoExcento = 0;
                //double netoBasico = 0;
                //double netoMinimo = 0;
                double ventaExcenta = 0;
                double ventaMinimo = 0;
                double ventaBasico = 0;
                double ventaIvaMinimo = 0;
                double ventaIvaBasico = 0;
                for (FacturaRenglon fr : f.getRenglones()) {
                    Articulo a = fr.getArticulo();
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, f.getFecha());
                    if (this.esEnvase(a)) {
                        esDeEnvases = true;
                    }
                    if ("Excento".equals(a.getIva().getNombre())) {
                        //netoExcento = netoExcento + fr.getTotal();
                        ventaExcenta = ventaExcenta + fr.getTotal();
                    }
                    if ("Minimo".equals(a.getIva().getNombre())) {
                        //netoMinimo = netoMinimo + cr.getTotal();
                        ventaMinimo = ventaMinimo + fr.getTotal();
                        ventaIvaMinimo = ventaIvaMinimo + fr.getIva();
                    }
                    if ("Basico".equals(a.getIva().getNombre())) {
                        //netoBasico = netoBasico + cr.getTotal();
                        ventaBasico = ventaBasico + fr.getTotal();
                        ventaIvaBasico = ventaIvaBasico + fr.getIva();
                    }
                }
                    if(!esDeEnvases) {
                        
                        Calendar calParaDia = Calendar.getInstance();
                        calParaDia.setTime(f.getFecha());
                        int dia = calParaDia.get(Calendar.DAY_OF_MONTH);
                       
                        if (ventaExcenta != 0) {
                            writer.write(dia + "," + "1111" + "," + "5111" + "," + "B Contado A " + f.getNumero() + "," + f.getCliente().getRut() + "," + "0" + "," + Double.toString(ventaExcenta).replace(",", ".") + "," + "0" + "," + "0" + "," + "0.0000000" + "," + "I");
                            //String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                            writer.write(str);
                        }
                        if (ventaMinimo != 0) {
                            writer.write(dia + "," + "1111" + "," + "5113" + "," + "B Contado A " + f.getNumero() + "," + f.getCliente().getRut() + "," + "0" + "," + Double.toString(ventaMinimo).replace(",", ".") + "," + "16" + "," + df.format(ventaIvaMinimo).replace(",", ".") + "," + "0.0000000" + "," + "I");
                            //String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                            writer.write(str);
                        }
                        if (ventaBasico != 0) {
                            writer.write(dia + "," + "1111" + "," + "5112" + "," + "B Contado A " + f.getNumero() + "," + f.getCliente().getRut() + "," + "0" + "," + df.format(ventaBasico).replace(",", ".") + "," + "17" + "," + df.format(ventaIvaBasico).replace(",", ".") + "," + "0.0000000" + "," + "I");   
                            //String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                            writer.write(str);
                        }
                    }
                //}
            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }
        }
    }
    
    public String devolverMesString(int mes) {
        switch (mes){
            case 0: return "Enero"; 
            case 1: return "Febrero";
            case 2: return "Marzo";
            case 3: return "Abril";
            case 4: return "Mayo";
            case 5: return "Junio";
            case 6: return "Julio";
            case 7: return "Agosto";
            case 8: return "Setiembre";
            case 9: return "Octubre";
            case 10: return "Noviembre";
            case 11: return "Diciembre";
            default: return "";
        }
    }
    
    public boolean esEnvase(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 320009 || a.getCodigo() == 320017 || a.getCodigo() == 320069 || a.getCodigo() == 32101) {
            retorno = true;
        }*/
        retorno = Util.esEnvase(a);
        return retorno;
    }
}
