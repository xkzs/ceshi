package cn.sibat.hibernate.dao;

import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import cn.sibat.util.config.ConfigUtil;


public class HibernateUtil{
	private static Logger log = Logger.getLogger(HibernateUtil.class);
	private ConfigUtil config = ConfigUtil.getInstance();
	private SessionFactory sessionFactory;
	private volatile static HibernateUtil DAO = null;
	
	private HibernateUtil() {
		log.info("Hibernate Dao init begin");
		String hiberPath = config.getStringValue("ext.config.hibernate.path", "../hibernate.cfg.xml");
		File extConfig = new File( hiberPath );
		
		StandardServiceRegistry  serviceRegistry;
		if( extConfig.canRead() ){
			log.info("use external file:" + extConfig.getAbsolutePath() );
			serviceRegistry = new StandardServiceRegistryBuilder().configure( extConfig ).build(); 
			
		}else{
		    log.info("use internal config file");
		    serviceRegistry = new StandardServiceRegistryBuilder().configure().build(); 
		} // if
		
        try {
    		sessionFactory = new MetadataSources( serviceRegistry ).buildMetadata().buildSessionFactory();
    	
        }catch (Exception e) {
    		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
    		// so destroy it manually.
        	log.error( e );
    		StandardServiceRegistryBuilder.destroy( serviceRegistry );
    	}// try
        
        log.info("Hibernate Util init end!");
	}
	
	public static HibernateUtil getInstance() {
		if (DAO == null) {
			synchronized (HibernateUtil.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (DAO == null) {
					DAO = new HibernateUtil();
				}
			}
		}
		return DAO;
	}
	
	
	
	public Session getSession() {
		
		return this.sessionFactory.openSession();
	}
	

}
