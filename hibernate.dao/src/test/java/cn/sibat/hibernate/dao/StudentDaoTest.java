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
import org.springframework.data.domain.PageRequest;

import cn.sibat.hibernate.dao.model.TestStudent;
import cn.sibat.hibernate.dao.model.TestUser;



public class StudentDaoTest {
	Logger log = Logger.getLogger( StudentDaoTest.class );
	StudentDao userDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.userDao = new StudentDao(TestStudent.class);
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void test3() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		StudentDao studentDao = new StudentDao(TestStudent.class);
		System.out.println(studentDao.count1(null));
		
	}
	//@Test
	public void test2() {
		StudentDao studentDao = new StudentDao(TestStudent.class);
		PageRequest pr = new PageRequest(1,3);
		System.out.println(studentDao.listPage(pr));
		
	}
	@Test
	public void test1() {
		StudentDao studentDao = new StudentDao(TestStudent.class);
		System.out.println(studentDao.list());
		
	}
	

}
