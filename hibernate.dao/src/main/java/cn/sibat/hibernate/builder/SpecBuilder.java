package cn.sibat.hibernate.builder;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import cn.sibat.hibernate.spec.AndSpecification;
import cn.sibat.hibernate.spec.BaseSpecification;
import cn.sibat.hibernate.spec.BetweenSpecification;
import cn.sibat.hibernate.spec.InSpecification;
import cn.sibat.hibernate.spec.MatchType;
import cn.sibat.hibernate.spec.OrSpecification;

public class SpecBuilder<T> {
 
	public BaseSpecification<T> getBase() {
		return new BaseSpecification<T>();
	
	}
	
	public BaseSpecification<T> getBase( String column , MatchType type, Object value ) {
		return new BaseSpecification<T>(  column ,  type,  value);
	
	}
	
	public AndSpecification<T> getAnd(){
		return new AndSpecification<T>() ;
		
	}
	
	public AndSpecification<T> getAnd( String column , MatchType type, Object value ){
		return new AndSpecification<T>( column, type, value ) ;
		
	}
	
	public OrSpecification<T> getOr( Specification<T> arg0, Specification<T> arg1 ){
		return new OrSpecification<T>( arg0, arg1 );
	}
	
	public InSpecification<T> getIn( String column, List<?> valueList ){
		return new InSpecification<T>( column, valueList ); 
	}
	
	public BetweenSpecification<T, Integer> getBetweenInteger(String column, Integer num0, Integer num1 ){
		return new BetweenSpecification<T, Integer>( column, num0, num1);
	}
	
	public BetweenSpecification<T, Float> getBetweenFloat(String column, Float num0, Float num1 ){
		return new BetweenSpecification<T, Float>( column, num0, num1);
	}
	
	public BetweenSpecification<T, Double> getBetweenDouble(String column, Double num0, Double num1 ){
		return new BetweenSpecification<T, Double>( column, num0, num1);
	}
	
	public BetweenSpecification<T, Date> getBetweenDate(String column, Date num0, Date num1 ){
		return new BetweenSpecification<T, Date>( column, num0, num1);
	}
	
	public BaseSpecification<T> getIsNull( String column ) {
		return new BaseSpecification<T>(  column ,  MatchType.isNull, "" );
	}
	
	public BaseSpecification<T> getIsNotNull( String column ) {
		return new BaseSpecification<T>(  column ,  MatchType.isNotNull, "" );
	}
	
}
