package cn.sibat.hibernate.dao;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.sibat.hibernate.dao.model.TestUser;



public class UserDaoTest {
	Logger log = Logger.getLogger( UserDaoTest.class );
	UserDao userDao;

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
	}

	
	@Test
	public void test() {

		// test insert
		TestUser user = new TestUser();
		
		log.debug( "test user dao ");
		
		user.setId( UUID.randomUUID().toString() );
		user.setAge( 9);
		user.setName( "test" );
		
		this.userDao.insert( user );
		assertNotNull( user.getId() );
		
		TestUser user2 = this.userDao.findById( user.getId());
		assertNotNull( user2 );
		
		log.debug("test load");
		List<TestUser> uList;
		uList = this.userDao.testLoad();
		this.outputUserList(uList);
		assertTrue( ! uList.isEmpty() );
		
		uList.clear();
		uList = this.userDao.testIn();
		this.outputUserList(uList);
//		assertTrue( ! uList.isEmpty() );
		
		log.debug("test list by prop");
		user2 = new TestUser();
		user2.setId( user.getId() );
		uList = this.userDao.listByProp( user2 );
		assertTrue( user.getId().equals( uList.get( 0 ).getId() ) );
		
		
		

	} // test
	
	private void outputUserList( List<TestUser> uList ) {
		log.debug( "size=" + uList.size());
		uList.forEach((TestUser u) -> {
			log.debug("name:" + u.getName() + ", age:" + u.getAge() );
		});
	}

}
