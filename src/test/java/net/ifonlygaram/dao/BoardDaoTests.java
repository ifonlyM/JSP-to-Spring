package net.ifonlygaram.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.ifonlygaram.domain.Criteria;
import net.ifonlygaram.service.BoardService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardDaoTests {
	@Setter @Autowired
	BoardService service;
	
	
	@Test
	public void testGetList(){
		log.info(service.list(new Criteria(1, 10, 1)));
	}
}
