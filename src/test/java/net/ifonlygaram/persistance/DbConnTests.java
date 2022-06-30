package net.ifonlygaram.persistance;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class DbConnTests {
	private Connection connection;
	@Setter	@Autowired
	private DataSource ds;
	
	@Before
	public void before() throws SQLException {
		connection = ds.getConnection();
	}
	
	@Test
	public void testExist() {
		assertNotNull(connection);
		log.info(connection);
	}
}
