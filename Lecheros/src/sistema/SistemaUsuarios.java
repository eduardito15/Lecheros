/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.usuarios.Actividad;
import dominio.usuarios.LogDeOperacion;
import dominio.usuarios.Rol;
import dominio.usuarios.Usuario;
import excepciones.LogDeExcepciones;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lecheros.Lecheros;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class SistemaUsuarios {
    
    private static SistemaUsuarios instance;
    
    private static Usuario usuario;
    
    /**
     * @return the instance
     */
    public static SistemaUsuarios getInstance() {
        if(instance == null){
            return new SistemaUsuarios();
        }
        return instance;
    }
    
    private SistemaUsuarios(){
        try {
            //cargarNombreEmpresa();
        } catch (Exception ex) {
            //Logger.getLogger(SistemaUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static MessageDigest md;

    public static String cryptWithMD5(String pass) {
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;

    }
    
    public String devolverNombrePorDefecto() throws IOException, Exception {
        String retorno = "";
        String directorioDeInstalacion = SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getDirectorioInstalacion();
        try (BufferedReader br = new BufferedReader(new FileReader(directorioDeInstalacion + "/usu.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                
                line = br.readLine();
            }
            retorno = sb.toString();
            return retorno;
        } 
    }
    
    public final void cargarNombreEmpresa() throws IOException, Exception {
        Properties prop = new Properties();
        InputStream input = null;

	try {
            String sSistemaOperativo = System.getProperty("os.name");
            //System.out.println(sSistemaOperativo);
            if(sSistemaOperativo.equals("Mac OS X")) {
                input = new FileInputStream("src/config.properties");
            } else {
                String dir = System.getProperty("user.dir");
                input = new FileInputStream(dir + "\\src\\config.properties");
            }

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            Lecheros.nombreEmpresa = prop.getProperty("empresa");
            //System.out.println(prop.getProperty("dbuser"));
            //System.out.println(prop.getProperty("dbpassword"));

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
            if (input != null) {
		try {
                    input.close();
		} catch (IOException e) {
                    e.printStackTrace();
		}
            }
        }
    }
    
    public List<Actividad> devolverActividades() throws Exception {
        List<Actividad> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Actividad");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Actividad devolverActividadPorNombre(String nombre) throws Exception {
        Actividad retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT a FROM Actividad a WHERE a.nombre = :nom");
        consulta.setString("nom", nombre);
        retorno = (Actividad)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    //Metodos Choferes
    public boolean agregarRol(String nombre, List<Actividad> actividades) throws Exception {

        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setActividades(actividades);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRoles, "Creo el rol  :  " +  nombre);
        return GenericDAO.getGenericDAO().guardar(rol);
    }

    public List<Rol> devolverRoles() {
        List<Rol> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Rol");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarRol(Rol rol) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRoles, "Actualizo el rol  :  " + rol.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(rol);
    }

    public boolean eliminarRol(Rol rol) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRoles, "Elimino el rol  :  " + rol.getNombre() );
        return GenericDAO.getGenericDAO().borrar(rol);
    }

    public boolean nombreDeRolValido(String nombre) throws Exception {
        boolean retorno = true;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Rol r where r.nombre = :nom");
        consulta.setString("nom", nombre);
        Rol r = (Rol)consulta.uniqueResult();
        if(r != null){
            retorno = false;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean nombreDeUsuarioValido(String nombre) throws Exception {
        boolean retorno = true;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Usuario u where u.nombreDeUsuario = :nom and u.activo = :act");
        consulta.setString("nom", nombre);
        consulta.setBoolean("act", true);
        Rol r = (Rol)consulta.uniqueResult();
        if(r != null){
            retorno = false;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean agregarUsuario(String nombre, String contrasena, Rol rol) throws Exception {
        
        Usuario u = new Usuario();
        u.setActivo(true);
        u.setNombreDeUsuario(nombre);
        String contrasenaEncriptada = SistemaUsuarios.cryptWithMD5(contrasena);
        u.setContrasena(contrasenaEncriptada);
        u.setRol(rol);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoUsuarios, "Ingreso el usuario  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(u);
        
    }
    
    public boolean eliminarUsuario(Usuario u) throws Exception {
        u.setActivo(false);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoUsuarios, "Elimino el usuario  :  " + u.getNombreDeUsuario() );
        return GenericDAO.getGenericDAO().actualizar(u);
    }
    
    public boolean actualizarUsuario(Usuario u) throws Exception {
        String contrasenaEncriptada = SistemaUsuarios.cryptWithMD5(u.getContrasena());
        u.setContrasena(contrasenaEncriptada);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoUsuarios, "Actualizo el usuario  :  " + u.getNombreDeUsuario() );
        return GenericDAO.getGenericDAO().actualizar(u);
    }
    
    public List<Usuario> devolverUsuarios() {
        List<Usuario> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Usuario u where u.activo = :act");
        consulta.setBoolean("act", true);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean existeUsuario(String nombre, String contrasena) throws Exception {
        boolean retorno = false;
        String conEncriptada = SistemaUsuarios.cryptWithMD5(contrasena);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Usuario u where u.nombreDeUsuario = :nom and u.activo = :act and u.contrasena = :conEncriptada");
        consulta.setString("nom", nombre);
        consulta.setString("conEncriptada", conEncriptada);
        consulta.setBoolean("act", true);
        this.usuario = (Usuario)consulta.uniqueResult();
        if(this.usuario != null) {
            registrarOperacion(Constantes.Login, "Inicio aplicacion. Usuario: " + usuario.getNombreDeUsuario());
            retorno = true;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean tienePermisos(String nombreActividad) throws Exception{
        boolean retorno = false;
        Actividad a = devolverActividadPorNombre(nombreActividad);
        if(a != null) {
            if(this.usuario.getRol().getActividades().contains(a)){
                retorno = true;
            }
        }
        return retorno;
    }
    
    public void registrarOperacion(String nombreActividad, String descripcionActividad) throws Exception{
        LogDeOperacion log = new LogDeOperacion();
        Actividad a =  this.devolverActividadPorNombre(nombreActividad);
        log.setAactividad(a);
        log.setDescripcion(descripcionActividad);
        log.setUsuario(usuario);
        Date fecha = new Date();
        log.setFecha(fecha);
        GenericDAO.getGenericDAO().guardar(log);
    }
    
    public void registrarExcepcion(String mensajeDeError, String stackTrace){
        LogDeExcepciones log = new LogDeExcepciones();
        log.setUsuario(usuario);
        Date fecha = new Date();
        log.setFecha(fecha);
        log.setMensajeDeError(mensajeDeError);
        //log.setStackTrace(stackTrace);
        GenericDAO.getGenericDAO().guardar(log);
    }
    
    public boolean verificarContrasenia(String contrasenia){
        boolean retorno = false;
        String conEncriptada = cryptWithMD5(contrasenia);
        if(usuario.getContrasena().equals(conEncriptada)){
            retorno = true;
        }
        return retorno;
    }
    
    public void cambiarContrasenia(String contraseniaNueva) throws Exception{
        String nuevaConEncriptada = SistemaUsuarios.cryptWithMD5(contraseniaNueva);
        usuario.setContrasena(nuevaConEncriptada);
        try {
            actualizarUsuario(usuario);
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoUsuarios, "Cambio la contraseña  :  " );
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public List<LogDeOperacion> devolverLogDeOperacionesEntreFechas(Date desdeFecha, Date hastaFecha) {
        List<LogDeOperacion> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM LogDeOperacion WHERE (fecha BETWEEN :stDate AND :edDate )" );
        consulta.setDate("stDate", desdeFecha);
        consulta.setDate("edDate", hastaFecha);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
}
