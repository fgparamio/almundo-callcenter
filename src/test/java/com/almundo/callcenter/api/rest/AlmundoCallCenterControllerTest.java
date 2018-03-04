package com.almundo.callcenter.api.rest;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.almundo.callcenter.Application;
import com.almundo.callcenter.api.rest.AlmundoCallCenterController;
import com.almundo.callcenter.domain.Director;
import com.almundo.callcenter.domain.Operator;
import com.almundo.callcenter.domain.Supervisor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Almundo CallCenter Test
 * @author fgparamio
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
final public class AlmundoCallCenterControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(AlmundoCallCenterControllerTest.class);

	@InjectMocks
	AlmundoCallCenterController controller;

	@Autowired
	WebApplicationContext context;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;

	private MockMvc mvc;

	/**
	 * This method is executed before every @Test
	 */
	@Before
	public void initTests() {

		LOG.info("Init new Test. Cleaning Employes");

		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		threadPool.initialize();
		// Delete employees by EndPoint
		deleteEmployees();
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldAllOperatorReceiveCallsOK() throws Exception {

		LOG.info(" ****************** shouldAllOperatorReceiveCallsOK  *******************");

		IntStream.rangeClosed(1, 10).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "true"));
		Thread.sleep(15000);

	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldOperatorsFirstOK() throws Exception {

		LOG.info(" ********************** shouldOperatorsFirstOK  ************************");

		// First calls dispatch operators by priority
		IntStream.rangeClosed(1, 3).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 2).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 5).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "true"));

		Thread.sleep(15000);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldWaitingFreeThreads() throws Exception {

		LOG.info(" *********************** shouldWaitingFreeThreads  **********************");

		// Last ten calls waiting free threads
		IntStream.rangeClosed(1, 3).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 2).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 8).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 20).forEach(i -> createCall(i, "true"));

		Thread.sleep(30000);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldNotWaitingFreeThreads() throws Exception {

		LOG.info(" *********************** shouldNotWaitingFreeThreads  ********************");

		// Last ten calls not waiting and finish
		IntStream.rangeClosed(1, 1).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, 1).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, 8).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, 10).forEach(i -> createCall(i, "false"));

		// Launchs 30 calls requests. Some request should not wait 
		// Some Request with Cause => BusyConcurrentException 
		IntStream.rangeClosed(1, 10).forEach(i -> {
			try {
				mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-" + (10 + i))
						.param("isWait", "false").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				fail("API-REST not Working");
			}
		});

		Thread.sleep(20000);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldBadRequestBeacauseNotEmployees() throws Exception {

		LOG.info(" ****************** shouldBadRequestBeacauseNotEmployees  *******************");

		// There isn't employes => Bad Request , Cause => BusyConcurrentException 
		mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-WITH-EMPLOYEES")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

	}

	/**********************************   PRIVATE METHODS   ********************************* /
	 * 
	 * 
	 * @param number of Operator ID
	 */
	private void createOperator(final int number) {
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
	 * @param number of Supervisor ID
	 */
	private void createSupervisor(final int number) {

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
	 * @param number of director ID
	 */
	private void createDirector(final int number) {

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
	 * @param number of call ID
	 */
	private void createCall(final int number, final String wait) {
		try {
			mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-" + number).param("isWait", wait)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
		} catch (Exception e) {
			fail("API-REST not Working");
		}
	}
	
	/**
	 * Delete all employees in Queue Manager
	 * 
	 */
	private void deleteEmployees() {
		try {
			mvc.perform(delete("/almundo/v1/callcenter/employees")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
		} catch (Exception e) {
			fail("API-REST not Working");
		}
	}


	/**
	 *   Get byte array Json serializable Object
	 * 
	 * @param r
	 * @return
	 * @throws Exception
	 */
	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		byte[] byteArray = map.writeValueAsString(r).getBytes();
		LOG.debug("JSON: " + new String(byteArray));
		return byteArray;
	}
}
