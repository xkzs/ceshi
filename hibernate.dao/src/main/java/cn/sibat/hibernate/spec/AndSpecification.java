package cn.sibat.hibernate.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class AndSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Specification<T>> specList;
	
	public AndSpecification() {
		this.specList = new ArrayList<Specification<T>>();
	}
	
	public AndSpecification( String column , MatchType type, Object value) {
		this.specList = new ArrayList<Specification<T>>();
		this.specList.add( new BaseSpecification<T>( column, type, value ) );
	}
	
	public AndSpecification<T> and( Specification<T> sp ){
		
		this.specList.add( sp );
		return this;
	}
	
	public AndSpecification<T> and( String column , MatchType type, Object value ){
		
		this.specList.add( new BaseSpecification<T>( column, type, value ) );
		return this;
	}
	
	public int getSpecListSize() {
		if( this.specList == null || this.specList.isEmpty() ) {
			return 0;
		}
		return this.specList.size();
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if( this.specList.isEmpty() ) {
			return null;
		}
		
		Predicate tmp = null;
		List<Predicate> pList = new ArrayList<Predicate>();
		for( Specification<T> sp : specList ) {
			tmp = sp.toPredicate(root, query, cb);
			if( tmp == null ) {
				continue;
			}
			pList.add( tmp );
			
		} // for
		
		if( pList.isEmpty() ) {
			return null;
		}
		
		return cb.and( pList.toArray(new Predicate[pList.size()]));
	} // toPredicate

}
