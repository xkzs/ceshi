package cn.sibat.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.Assert.*;

import cn.sibat.hibernate.builder.SpecBuilder;
import cn.sibat.hibernate.dao.model.TestUser;
import cn.sibat.hibernate.spec.MatchType;

public class NotNullTest {
	private Logger log = Logger.getLogger(NotNullTest.class);
	private UserDao userDao;
	private String userName = "not_null_test_user";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.userDao = new UserDao();
		
	}

	@After
	public void tearDown() throws Exception {
		
		List<TestUser> list = this.listTestUser();
		this.userDao.batchDelete(list);
	}

	
	@Test
	public void test() {
		log.info("start not null test");
		TestUser u = new TestUser();
		u.setId(UUID.randomUUID().toString());
		u.setName( this.userName );
		u.setPassword("not null");
		u.setNickName( null );
		
		this.userDao.insert( u );
		
		TestUser u2 = new TestUser();
		u2.setId(UUID.randomUUID().toString());
		u2.setName( this.userName );
		u2.setPassword( null );
		u2.setNickName( "not null" );
		
		this.userDao.insert(u2);
		
		SpecBuilder<TestUser> sb = new SpecBuilder<TestUser>();
		Specification<TestUser> spec = sb.getIsNotNull("password");
		Sort sort = null;
		List<TestUser> list = this.userDao.list( spec, sort);
		
		boolean isTestPassed = false;
		for( TestUser user: list ) {
			if( user.getId().equals( u.getId() ) ) {
				// 找到password不为null的u，通过测试
				isTestPassed = true;
			}
			if( user.getId().equals( u2.getId() ) ) {
				// u2 password 为 null，不应该在结果内
				isTestPassed = false;
			}
		} //
		
		assertTrue( isTestPassed );
		
		list.clear();
		spec = sb.getIsNull("password");
		list = this.userDao.list( spec, sort);
		
		isTestPassed = false;
		for( TestUser user: list ) {
			
			if( user.getId().equals( u2.getId() ) ) {
				// u2 password 为 null，测试pass
				isTestPassed = true;
			}
			if( user.getId().equals( u.getId() ) ) {
				// 找到password不为null的u，测试失败
				isTestPassed = false;
			}
		} //
		
		assertTrue( isTestPassed );
		
		log.info("--------------not null test finish---------------");
	} // test
	
	
	
	private List<TestUser> listTestUser(){
		TestUser user = new TestUser();
		user.setName( this.userName );
		return this.userDao.listByProperties( user );
	}
	
}
