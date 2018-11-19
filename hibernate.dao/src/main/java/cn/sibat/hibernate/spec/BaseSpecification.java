package cn.sibat.hibernate.spec;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification<T> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(BaseSpecification.class);
	
	MatchType matchType;
	Object value;
	String column;
	
	public BaseSpecification() {
		super();
	}

	public BaseSpecification(String column, MatchType type, Object value) {
		super();
		this.matchType = type;
		this.value = value;
		this.column = column;
	}
	
	public void setValue(String column, MatchType type, Object value) {
		this.matchType = type;
		this.value = value;
		this.column = column;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		Path<T> path = root.get(column);

		EntityType<T> model = root.getModel();
		SingularAttribute<? super T, ?> att = model.getSingularAttribute(column);

		// check column and value
		if (column == null || value == null || att == null) {
			log.error("error: column or value is null !");
			return null;
		};
		
		switch (this.matchType) {
		case equal:
			return cb.equal(path, value);

		case notEqual:
			return cb.notEqual(path, value);

		case like:
			if (!this.isStringType(att)) {
				log.error("like type error:" + value + ", String is expected!");
				return null;
			}
			return cb.like(root.get((SingularAttribute<? super T, String>) att), "%" + value + "%");
		
		case likeAfter:
			if (!this.isStringType(att)) {
				log.error("likeAfter type error:" + value + ", String is expected!");
				return null;
			}
			return cb.like(root.get((SingularAttribute<? super T, String>) att), value + "%" );

		case likeBefore:
			if (!this.isStringType(att)) {
				log.error("likeBefore type error:" + value + ", String is expected!");
				return null;
			}
			return cb.like(root.get((SingularAttribute<? super T, String>) att), "%" + value );

		case notLike:
			if (!this.isStringType(att)) {
				log.error("notlike type error:" + value + ", String is expected!");
				return null;
			}
			return cb.notLike(root.get((SingularAttribute<? super T, String>) att), "%" + value + "%");

		case gt:
			if ( this.isNumberType(att, value)) {
				return cb.gt(root.get(column), (Number) value);
				
			}else if( this.isDateType(att, value) ) {
				return cb.greaterThan(root.get(column), (Date) value);
				
			}else {
				log.error("gt type error:" + value + ", Number or Date is expected!");
				return null;
			}
			
		case lt:
			if ( this.isNumberType(att, value)) {
				return cb.lt(root.get(column), (Number) value);
				
			}else if( this.isDateType(att, value) ) {
				return cb.lessThan(root.get(column), (Date) value);
				
			}else {
				log.error("lt type error:" + value + ", Number or Date is expected!");
				return null;
			}
			
		case ge:
			if ( this.isNumberType(att, value)) {
				return cb.ge(root.get(column), (Number) value);
				
			}else if( this.isDateType(att, value) ) {
				return cb.greaterThanOrEqualTo(root.get(column), (Date) value);
				
			}else {
				log.error("ge type error:" + value + ", Number or Date is expected!");
				return null;
			}
			
		case le:
			if ( this.isNumberType(att, value)) {
				return cb.le(root.get(column), (Number) value);
				
			}else if( this.isDateType(att, value) ) {
				return cb.lessThanOrEqualTo(root.get(column), (Date) value);
				
			}else {
				log.error("le type error:" + value + ", Number or Date is expected!");
				return null;
			}
			
		case isNotNull:
			return cb.isNotNull( root.get(column) );
			
		case isNull:
			return cb.isNull( root.get( column ) );
			
		default:
			log.error("match type error:" + this.matchType );
			return null;
		} // switch

	} // toPredicate
	
	private boolean isDateType(SingularAttribute<? super T, ?> att, Object value) {
		if( ! Date.class.isAssignableFrom( att.getJavaType() ) ) {
			return false;
		}
		
		if( !(value instanceof Date )  ) {
			return false;
		}
		return true;
	}

	private boolean isStringType(SingularAttribute<? super T, ?> att) {
		if (String.class.isAssignableFrom(att.getJavaType())) {
			return true;
		}
		return false;
	}

	private boolean isNumberType(SingularAttribute<? super T, ?> att, Object value) {
		if (!Number.class.isAssignableFrom(att.getJavaType())) {
			return false;
		}

		if (!(value instanceof Number)) {
			return false;
		}

		return true;
	}

}
