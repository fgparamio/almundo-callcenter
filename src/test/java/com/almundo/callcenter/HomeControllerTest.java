package com.almundo.callcenter;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.almundo.callcenter.api.rest.HomeController;
import com.almundo.callcenter.domain.Director;
import com.almundo.callcenter.domain.Operator;
import com.almundo.callcenter.domain.Supervisor;
import com.almundo.callcenter.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author fgparamio
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class HomeControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(HomeControllerTest.class);

	@InjectMocks
	HomeController controller;

	@Autowired
	WebApplicationContext context;

	@Autowired
	EmployeeService employeeService;

	private MockMvc mvc;

	@Before
	public void initTests() {

		LOG.info("Init new Test. Cleaning Employes");

		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		employeeService.cleanEmployes();
	}

	@Test
	public void shouldAllOperatorReceiveCallsOK() throws Exception {

		LOG.info(" *************** shouldAllOperatorReceiveCallsOK  ****************");

		IntStream.rangeClosed(1, 10).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "true"));
		Thread.sleep(15000);

	}

	@Test
	public void shouldOperatorsFirstOK() throws Exception {

		LOG.info(" ******************* shouldOperatorsFirstOK  *********************");

		// First calls dispatch operators by priority
		IntStream.rangeClosed(1, 3).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 2).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 5).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "true"));

		Thread.sleep(15000);
	}

	@Test
	public void shouldWaitingFreeThreads() throws Exception {

		LOG.info(" ******************** shouldWaitingFreeThreads  *******************");

		// Last ten calls waiting free threads
		IntStream.rangeClosed(1, 3).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 2).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createOperator(i));		
		IntStream.rangeClosed(1, 20).forEach(i -> createCall(i, "true"));

		Thread.sleep(30000);
	}

	@Test
	public void shouldNotWaitingFreeThreads() throws Exception {

		LOG.info(" ******************** shouldNotWaitingFreeThreads  *****************");

		// Last ten calls not waiting and finish
		IntStream.rangeClosed(1, 3).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 2).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 8).forEach(i -> createOperator(i));		
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "false"));

		// Some request should not wait
		for (int i = 0; i < 30; i++) {			
			
			mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-"+(11+i)).param("isWait", "false")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		}

		Thread.sleep(30000);
	}

	@Test
	public void shouldBadRequestBeacauseNotEmployees() throws Exception {

		LOG.info(" *************** shouldBadRequestBeacauseNotEmployees  ****************");

		mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-WITH-EMPLOYEES")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	/***********************************    PRIVATE METHODS    ********************************* /

	/**
	 * 
	 * @param number
	 */
	private void createOperator(int number) {
		try {
			mvc.perform(post("/almundo/v1/callcenter/operator").content(toJson(new Operator("OPERATOR-" + number)))
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
		} catch (final Exception e) {
			fail("API-REST not Working");
		}
	}

	/**
	 * 
	 * @param number
	 */
	private void createSupervisor(int number) {

		try {
			mvc.perform(
					post("/almundo/v1/callcenter/supervisor").content(toJson(new Supervisor("SUPERVISOR-" + number)))
							.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			fail("API-REST not Working");
		}
	}

	/**
	 * 
	 * @param number
	 */
	private void createDirector(int number) {

		try {
			mvc.perform(post("/almundo/v1/callcenter/director").content(toJson(new Director("DIRECTOR-" + number)))
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			fail("API-REST not Working");
		}
	}

	/**
	 * 
	 * @param number
	 */
	private void createCall(int number, String wait) {
		try {
			mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-" + number).param("isWait", wait)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			fail("API-REST not Working");
		}
	}

	/**
	 * 
	 * @param r serializable object
	 * @return
	 * @throws Exception
	 */
	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r).getBytes();
	}
}
