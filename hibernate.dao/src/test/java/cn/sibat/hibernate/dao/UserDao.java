package cn.sibat.hibernate.dao;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import cn.sibat.hibernate.builder.SortBuilder;
import cn.sibat.hibernate.builder.SpecBuilder;
import cn.sibat.hibernate.dao.model.TestUser;
import cn.sibat.hibernate.spec.MatchType;


public class UserDao extends HibernateDao<TestUser>{

	Logger log = Logger.getLogger( UserDao.class );
	public UserDao() {
		super(TestUser.class);
		log.debug("class=" +this.clazz.getName());
	
	}
	
	public List<TestUser> testIn(){
		log.debug("start test in....");
		SpecBuilder<TestUser> sb = new SpecBuilder<TestUser>();
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add( "test11");
		nameList.add( "test99");
		
		Specification<TestUser> spec = sb.getIn("name", nameList );
		
		SortBuilder sortBuilder = new SortBuilder();
		sortBuilder.addAsc("age");
		
		return this.list(spec, sortBuilder.getSort() );
		
		
	}
	
	public List<TestUser> listByProp( TestUser u ){
		return this.listByProperties(u);
		
	}
	
	public List<TestUser> testLoad(){
		log.debug("start test load....");
		SpecBuilder<TestUser> sb = new SpecBuilder<TestUser>();
		
		
		Specification<TestUser> spec = 
				sb.getOr( sb.getAnd( "age", MatchType.gt, 25 ).and("name", MatchType.equal, "test")
						,sb.getBase("age", MatchType.le, 10)
					);
//		
//		Specification<User> spec1 = sb.getAnd( "name", MatchType.equal, "test").and( "age", MatchType.gt, 25 );
//		
//		Specification<User> spec2 = sb.getBase( "age", MatchType.gt, 25);
//		
//		
//		Specification<User> spec = sb.getOr(spec1, spec2);

		
				
		
		log.debug("spec=" + spec.toString());
		List<TestUser> list;
		Sort sort = null;
		list = this.list(spec, sort);
		return list;
	}
	
	public List<TestUser> listBySpec( Specification<TestUser> spec, Sort sort){
		return super.list(spec, sort);
	}
	
	
	
	
	
}
