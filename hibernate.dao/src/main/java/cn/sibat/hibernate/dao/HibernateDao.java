package cn.sibat.hibernate.dao;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import cn.sibat.hibernate.builder.SortBuilder;
import cn.sibat.hibernate.builder.SpecBuilder;
import cn.sibat.hibernate.spec.AndSpecification;
import cn.sibat.hibernate.spec.MatchType;
import cn.sibat.model.Pojo;


public class HibernateDao<T extends Pojo> {
	private static Logger log = Logger.getLogger(HibernateDao.class);
	private HibernateUtil util;
	protected Class<T> clazz;
	private SpecBuilder<T> defSpecBuilder;

	public HibernateDao(Class<T> clazz) {
//		log.debug("Hibernate Dao init begin");
		this.util = HibernateUtil.getInstance();
		this.clazz = clazz;
		this.defSpecBuilder = new SpecBuilder<T>();
//		log.debug("Hibernate Dao init end!");
	}

	private Session getSession() {
		return this.util.getSession();
	}
	
	public SpecBuilder<T> getDefaultSpecBuilder(){
		return this.defSpecBuilder;
	}

	public void update(T pojo) {
		pojo.setModifyDate(new Date());
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		session.update(pojo);
		tran.commit();
		session.close();
	}

	public void batchDelete(List<T> list) {
		log.debug("batch delete");
		if (list == null || list.isEmpty()) {
			log.debug("list is null");
			return;

		} else {
			log.debug("size = " + list.size());
		}

		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();

		Iterator<T> iter = list.iterator();
		while (iter.hasNext()) {
			session.delete(iter.next());
		}

		tran.commit();

		session.close();
		log.debug("batch delete end");
	}

	public void batchUpdate(List<T> list) {
		log.debug("batch update");
		if (list == null || list.isEmpty()) {
			log.debug("list is null");
			return;
		} else {
			log.debug("size = " + list.size());
		}
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();

		Iterator<T> iter = list.iterator();
		while (iter.hasNext()) {
			session.merge(iter.next());
		}

		tran.commit();

		session.close();
		log.debug("batch update end");
	} // batchUpdate

	/**
	 * 分多次批量更新，每次固定更新splitSize指定的数量
	 * 
	 * @param list 更新的列表
	 * @param splitSize 每次更新的数量
	 */
	public void batchUpdate(List<T> list, int splitSize) {
		log.debug("batch update");
		if (splitSize < 10) {
			splitSize = 10;
		}

		int size = list.size();
		int end = splitSize;

		for (int begin = 0; begin < size;) {
			if (size < end) {
				end = size;
			}

			batchUpdate(list.subList(begin, end));

			begin = begin + splitSize;
			end = end + splitSize;
		} // for
	} // batchUpdate

	protected T insert(T pojo) {
		pojo.setCreateDate(new Date());
		pojo.setModifyDate(new Date());

		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		session.save(pojo);
		tran.commit();
		session.close();
		return pojo;
	}
	
	protected T insertOrUpdate( T pojo ) {
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		session.saveOrUpdate(pojo);
		tran.commit();
		session.close();
		return pojo;
	}
	
	/**
	 * 分多次批量插入，每次固定插入splitSize指定的数量
	 * 
	 * @param list
	 * @param splitSize
	 */
	protected void batchInsert(List<T> list, int splitSize) {
		log.debug("batch Insert, split size = " + splitSize);
		if (splitSize < 10) {
			splitSize = 10;
		}

		int size = list.size();
		int end = splitSize;

		for (int begin = 0; begin < size;) {
			if (size < end) {
				end = size;
			}

			batchInsert(list.subList(begin, end));

			begin = begin + splitSize;
			end = end + splitSize;
		} // for
	}

	/**
	 * 批量同步插入，如果有一条插入失败便回滚之前的插入数据
	 * 
	 * @param list
	 */
	protected void syncBatchInsert(List<T> list) throws Exception {
		if (list == null || list.isEmpty()) {
			log.debug("sync batch insert error, list is null");
			return;

		} else {
			log.debug("sync batch insert, size = " + list.size());
		}

		Session session = this.getSession();
		Transaction tran = session.beginTransaction();

		Iterator<T> iter = list.iterator();
		Pojo pojo;
		try {
			while (iter.hasNext()) {
				pojo = iter.next();
				pojo.setCreateDate(new Date());
				pojo.setModifyDate(new Date());
				session.save(pojo);
			}
			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			log.error("sync batch error !");
			throw e;

		} finally {
			session.close();
		}

		log.debug("batch insert done!");
	} // syncBatchInsert

	/**
	 * 批量插入
	 * 
	 * @param list
	 */
	protected void batchInsert(List<T> list) {

		if (list == null || list.isEmpty()) {
			log.debug("batch insert error, list is null");
			return;

		} else {
			log.debug("batch insert, size = " + list.size());
		}

		Session session = this.getSession();
		Transaction tran = session.beginTransaction();

		Iterator<T> iter = list.iterator();
		Pojo pojo;
		while (iter.hasNext()) {
			pojo = iter.next();
			pojo.setCreateDate(new Date());
			pojo.setModifyDate(new Date());
			session.save(pojo);
		}

		tran.commit();

		session.close();
		log.debug("batch insert done!");
	}
	
	/**
	 * 批量插入或者更新
	 * 
	 * @param list
	 * @return void
	 */
	protected void batchInsertOrUpdate(List<T> list) {
		// log.debug("batch insert new obj");
		if (list == null || list.isEmpty()) {
			log.debug("list is null");
			return;
		} else {
			log.debug("batch insert, size = " + list.size());
		}
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();

		Iterator<T> iter = list.iterator();
		while (iter.hasNext()) {
			session.saveOrUpdate(iter.next());
		}

		tran.commit();

		session.close();
		log.debug("batch insert done!");
	}

	public void delete(Object obj) {
		log.debug("delete obj");
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		session.delete(obj);
		tran.commit();

		session.close();
		// sessionFactory.close();
	}

	public T findById(String id) {
		return this.getSession().byId(this.clazz).load(id);
	}
	
	/**
	 * 查询id包含于idList内的对象
	 * 
	 * @param idList
	 * @return list<T>
	 */
	protected List<T> listByIds( List<String> idList ){
		if( idList == null || idList.isEmpty() ) {
			return Collections.emptyList();
		}
		
		SpecBuilder<T> sb = new SpecBuilder<T>();
		Sort sort = null;
		return this.list( sb.getIn("id", idList), sort );
	}
	
	protected List<T> listByProperties( T t ){
		return this.listByProperties(t, null );
	}
	
	/**
	 * 按照pojo对象的字段进行筛选
	 * 会查找pojo对象的上一层父对象
	 * 只读取public\private\protected 修饰的字段
	 * 
	 * @param t
	 * @param sort
	 * @return list<T>
	 */
	protected List<T> listByProperties( T t, Sort sort ){
		return this.listByProperties(t, sort, 0, 0);
		
	}
	
	protected List<T> listByProperties( T t, Sort sort, int index, int size ){

		if( t == null ) {
			return Collections.emptyList();
		}
		
		if( sort == null ) {
			sort = new SortBuilder()
					.addDesc("modifyDate")
					.addDesc("createDate")
					.getSort();
		}
		
		SpecBuilder<T> sb = new SpecBuilder<T>();
		AndSpecification<T> spec = sb.getAnd();
		
		
		
		Object o;
		Field[] fields = this.clazz.getDeclaredFields();
		
		for( Field field : fields ) {
			field.setAccessible( true );
			
			//read public\private\protected fields only
			if( field.getModifiers() > 4 ) {
				continue;
			}
			
			try {
				o = field.get( t );
				if( o != null ) {
					spec.and( field.getName(), MatchType.equal, field.get( t ) );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		} // for
		
		
		if( this.clazz.getSuperclass() == null ) {
			return this.list(spec, sort );
		}
		
		fields = this.clazz.getSuperclass().getDeclaredFields();
		for( Field field : fields ) {
			field.setAccessible( true );
			
			//read public\private\protected fields only
			if( field.getModifiers() > 4 ) {
				continue;
			}
			
			try {
				o = field.get( t );
				if( o != null ) {
					spec.and( field.getName(), MatchType.equal, field.get( t ) );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		} // for
		
		TypedQuery<T> query = getQuery(spec, sort);
		
		if( index > 0 ) {
			query.setFirstResult( index );
		}
		
		if( size > 0 ) {
			query.setMaxResults( size );
		}
		
		
		return query.getResultList();
		
	} //listByProperties

	/**
	 * 通过spec和sort查询数据库
	 * 
	 * @param spec
	 * @param org.springframework.data.domain.Sort sort
	 * @return list<T>
	 */
	protected List<T> list(Specification<T> spec, Sort sort) {
		TypedQuery<T> query = getQuery(spec, sort);
		if(query==null){
			return Collections.emptyList();
		}
		return query.getResultList();
	}

	/**
	 * 分页查询
	 * 
	 * @param pageable
	 * @return
	 */
	protected Page<T> list(Pageable pageable) {

		if (null == pageable) {
			return new PageImpl<T>( this.listAll() );
		}

		return list((Specification<T>) null, pageable);
	}

	protected List<T> listAll() {
		TypedQuery<T> query = getQuery(null, (Sort) null);
		if(query==null){
			return Collections.emptyList();
		}
		return query.getResultList();
	}

	protected Page<T> list(Specification<T> spec, Pageable pageable) {
		Sort sort = pageable == null ? null : pageable.getSort();
		TypedQuery<T> query = getQuery(spec, sort);
		if(query==null){
			return null;
		}
		if (pageable == null) {
			return new PageImpl<T>(query.getResultList());
		} else {
			query.setFirstResult((int) pageable.getOffset());// 版本不同，最新版的getOffset的返回值是long值，但是query里面是int
			query.setMaxResults(pageable.getPageSize());

			Long total = count(spec);
			List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

			return new PageImpl<T>(content, pageable, total);
		}
	}

	protected Long count(Specification<T> spec) {
		TypedQuery<Long> query = this.getCountQuery(spec);
		if(query==null){
			return 0L;
		}
		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}


	private TypedQuery<Long> getCountQuery(Specification<T> spec) {
		Session session = this.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = applySpecificationToCriteria(spec, query);
		if (root == null) {
			return null;
		}
		if (query.isDistinct()) {
			query.select(builder.countDistinct(root));
		} else {
			query.select(builder.count(root));
		}

		// Remove all Orders the Specifications might have applied
		// query.orderBy(Collections.<Order> emptyList());

		return session.createQuery(query);
	}

	private TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
		Session session = this.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.clazz);

		Root<T> root = applySpecificationToCriteria(spec, query, builder);
		if (root == null) {
			return null;
		}

		query.select(root);

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}
		return session.createQuery(query);
	}

	private Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<T> query,
			CriteriaBuilder builder) {

		Root<T> root = query.from(this.clazz);

		if (spec == null) {
			return root;
		}

		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		} else {
			return null;
		}

		return root;
	}

	private Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<Long> query) {

		Root<T> root = query.from(this.clazz);

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		} else {
			return null;
		}

		return root;
	}
}
