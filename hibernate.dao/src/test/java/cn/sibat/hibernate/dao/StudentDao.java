package cn.sibat.hibernate.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import cn.sibat.hibernate.builder.SortBuilder;
import cn.sibat.hibernate.builder.SpecBuilder;
import cn.sibat.hibernate.dao.model.TestStudent;
import cn.sibat.hibernate.spec.MatchType;

public class StudentDao<T> extends HibernateDao<TestStudent>{

	Logger log = Logger.getLogger( UserDao.class );
	
	/*public Student createUser( Student user ) {
		user.setId( UUID.randomUUID().toString() );
		
		return this.dao.insert(user);
	}*/
	
	public StudentDao(Class<TestStudent> clazz) {
		super(TestStudent.class);
		log.debug("class=" +this.clazz.getName());
		// TODO Auto-generated constructor stub
	}

	public TestStudent findById( String id ) {
		return this.findById(id);
	}
	
	public List<TestStudent> list() {
		Sort sort= new SortBuilder(Sort.Direction.DESC, "age").add(Sort.Direction.ASC, "id").getSort();
		return this.list(new SpecBuilder<TestStudent>().getBase("age", MatchType.le, 22), sort);
	}
	
	public Long count1(Specification<TestStudent> spec) {
		spec = new SpecBuilder<TestStudent>().getBase("age", MatchType.equal, 22);
		Long total = count(spec);

		return total;
	}
	public PageImpl<TestStudent> listPage(PageRequest pr) {
		
		return (PageImpl<TestStudent>) this.list(pr);
	}
	
	
	
}
