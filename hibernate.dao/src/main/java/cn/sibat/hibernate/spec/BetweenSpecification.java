package cn.sibat.hibernate.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

public class BetweenSpecification<T, Y extends Comparable<? super Y>> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger( BetweenSpecification.class );
	
	
	String column;
	Y val0;
	Y val1;
	
	public BetweenSpecification( String column, Y num0, Y num1 ) {
		this.column = column;
		this.val0 = num0;
		this.val1 = num1;
		
	}
	
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		
		
		return cb.between( root.get(column), val0, val1 );
	}
	
	
	
}
