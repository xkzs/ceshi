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

import static org.junit.Assert.*;

import cn.sibat.hibernate.dao.model.TestUser;

public class BatchTest {
	private Logger log = Logger.getLogger(BatchTest.class);
	private UserDao userDao;
	private String userName = "batch_test_user";
	
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
		log.info("start test");
		List<TestUser> list = this.getNewUserList();
		int initSize = list.size();
		this.userDao.batchInsert(list);
		
		list.clear();
		list = this.listTestUser();
		assertTrue( initSize == list.size() );
		log.info("-------------batch insert test passed!------------");
		
		for( TestUser u: list ) {
			u.setEmail("first_insert");
		}
		list.addAll( this.getNewUserList() );
		this.userDao.batchInsertOrUpdate(list);
		
		list.clear();
		list = this.listTestUser();
		assertTrue( list.size() == ( initSize * 2 ) );
		
		int count = 0;
		for( TestUser u: list ) {
			if( "first_insert".equals( u.getEmail() )) {
				count++;
			}
		}
		assertTrue( count == initSize );
		log.info("-------------batch Insert Or Update test passed!------------");
		
		this.userDao.batchDelete(list);
		list.clear();
		list = this.listTestUser();
		assertTrue( list.size() == 0 );
		log.info("batch delete test passed!");
		
		list.clear();
		list.addAll( this.getNewUserList() );
		list.addAll( this.getNewUserList() );
		list.remove(0);
		initSize = list.size();
		log.info( "init size = " + initSize );
		this.userDao.batchInsert(list, 10);
		
		list.clear();
		list = this.listTestUser();
		log.info( "list size = " + list.size() );
		assertTrue( initSize == list.size() );
		log.info("-------------batch insert split test passed!------------");	
		
	} // test
	
	private List<TestUser> getNewUserList(){
		List<TestUser> list = new ArrayList<TestUser>();
		for( int i = 1; i <=20; i++ ) {
			TestUser u = new TestUser();
			u.setId(UUID.randomUUID().toString());
			u.setName( this.userName );
			list.add( u );
		}
		return list;
	}
	
	private List<TestUser> listTestUser(){
		TestUser user = new TestUser();
		user.setName( this.userName );
		return this.userDao.listByProperties( user );
	}
	
}
