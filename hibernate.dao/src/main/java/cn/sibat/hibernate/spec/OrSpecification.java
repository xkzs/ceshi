package cn.sibat.hibernate.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class OrSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List< Specification<T> > spList;

	
	public OrSpecification( Specification<T> arg0, Specification<T> arg1) {
		this.spList = new ArrayList<Specification<T>>();
		spList.add( arg0 );
		spList.add( arg1 );
	}
	
	public OrSpecification<T> or( Specification<T> arg0) {
		this.spList.add( arg0 );
		return this;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if( this.spList.isEmpty() ) {
			return null;
		}
		
		List<Predicate> preList = new ArrayList<Predicate>();
		for( Specification<T> sp : spList ) {
			preList.add( sp.toPredicate(root, query, cb) );
		}
		return cb.or( preList.toArray( new Predicate[preList.size()] ));
	}

}
