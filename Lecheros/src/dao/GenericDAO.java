/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dominio.ObjetoPersistente;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

/**
 *
 * @author Edu
 */
public class GenericDAO {

    private static GenericDAO genericDAO;

    /**
     * @return the genericDAO
     */
    public static GenericDAO getGenericDAO() {
        if (genericDAO == null) {
            genericDAO = new GenericDAO();
        }
        return genericDAO;
    }

    private final SessionFactory sessionFactory;

    private final Session session;

    private GenericDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    public boolean guardar(ObjetoPersistente obj) {
        boolean retorno = false;
        Session s = getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(obj);
            s.getTransaction().commit();
            s.close();
            retorno = true;
            
        } catch (HibernateException e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            s.close();
            throw e;
        }

        return retorno;
    }

    public boolean actualizar(ObjetoPersistente obj) {
        boolean retorno = false;
        Session s = getSessionFactory().openSession();
        try {

            s.beginTransaction();
            s.saveOrUpdate(obj);
            s.getTransaction().commit();
            s.close();
            retorno = true;
        } catch (HibernateException e) {
            e.printStackTrace();
            getSession().getTransaction().rollback();
            s.close();
            throw e;
        }
        return retorno;
    }

    public boolean borrar(ObjetoPersistente obj) throws HibernateException {
        boolean retorno = false;
        Session s = getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.delete(obj);
            s.getTransaction().commit();
            s.close();
            retorno = true;
        } catch (HibernateException e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            s.close();
            throw e;
        }

        return retorno;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
