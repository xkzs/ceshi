package cn.sibat.hibernate.spec;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class InSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<?> valueList;
	String column;

	
	public InSpecification( String column, List<?> valueList ) {
		this.column =column;
		this.valueList = valueList;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if( this.valueList.isEmpty() ) {
			return null;
		}
		
		Path<T> path = root.get(column);
		if( path == null ) {
			return null;
		}
		
		return path.in(valueList);
	}

}
