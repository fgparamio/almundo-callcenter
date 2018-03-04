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
import org.springframework.beans.factory.annotation.Value;
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
 * Almundo CallCenter Application Test
 * @author fgparamio
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
final public class AlmundoCallCenterControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(AlmundoCallCenterControllerTest.class);
	
	// Maximum threads in Thread Pool to attend by priority queue
	@Value("${callcenter.numThreads}")
	private Integer maxNumThreads;
	
	@Value("${callcenter.minCallTime}")
	private Integer minCallTime;
	
	@Value("${callcenter.maxCallTime}")
	private Integer maxCallTime;

	// Main Controller
	@InjectMocks
	AlmundoCallCenterController controller;

	// Spring Boot Application Context
	@Autowired
	WebApplicationContext context;
	
	// ThreadPool in BeanContext
	@Autowired
	private ThreadPoolTaskExecutor threadPool;

	// Mocking Spring MVC
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
	 *  Check every operator attend every call
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldAllOperatorReceiveCallsOK() throws Exception {

		LOG.info(" ****************** shouldAllOperatorReceiveCallsOK  *******************");

		IntStream.rangeClosed(1, maxNumThreads).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, maxNumThreads).forEach(i -> createCall(i, "true"));
		
		Thread.sleep(maxNumThreads*1000+(maxCallTime-minCallTime)*1000);

	}

	/**
	 * Check every operator attend every call before the supervisors and directors
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldOperatorsFirstOK() throws Exception {

		LOG.info(" ********************** shouldOperatorsFirstOK  ************************");

		// First calls dispatch operators by priority
		IntStream.rangeClosed(1, (int)(0.30*maxNumThreads)).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, (int)(0.20*maxNumThreads)).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, maxNumThreads/2).forEach(i -> createOperator(i));
		IntStream.rangeClosed(1, maxNumThreads).forEach(i -> createCall(i, "true"));

		Thread.sleep(maxNumThreads*1000+(maxCallTime-minCallTime)*1000);

	}

	/**
	 * Check waiting calls throwing double calls than employees
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldWaitingFreeThreads() throws Exception {

		LOG.info(" *********************** shouldWaitingFreeThreads  **********************");

		// Last ten calls waiting free threads
		IntStream.rangeClosed(1, (int)(0.30*maxNumThreads)).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, (int)(0.20*maxNumThreads)).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, maxNumThreads/2).forEach(i -> createOperator(i));
		// Throws Double Calls To check waiting
		IntStream.rangeClosed(1, maxNumThreads*2).forEach(i -> createCall(i, "true"));

		Thread.sleep(maxNumThreads*1000+(maxCallTime-minCallTime)*1000*2);

	}

	/**
	 *  Check Not waiting throwing double calls with isWait parameter to false 
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldNotWaitingFreeThreads() throws Exception {

		LOG.info(" *********************** shouldNotWaitingFreeThreads  ********************");

		// Last ten calls not waiting and finish
		IntStream.rangeClosed(1, (int)(0.30*maxNumThreads)).forEach(i -> createSupervisor(i));
		IntStream.rangeClosed(1, (int)(0.20*maxNumThreads)).forEach(i -> createDirector(i));
		IntStream.rangeClosed(1, maxNumThreads/2).forEach(i -> createOperator(i));
		
		// Launch maxNumThredas calls
		IntStream.rangeClosed(1, maxNumThreads).forEach(i -> createCall(i, "false"));

		// Launchs maxNumThreads calls requests. Some request should not wait 
		// Some Request with Cause => BusyConcurrentException 
		IntStream.rangeClosed(1, maxNumThreads).forEach(i -> {
			try {
				mvc.perform(post("/almundo/v1/callcenter/call").param("message", "CALLING-" + (10 + i))
						.param("isWait", "false").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				fail("API-REST not Working");
			}
		});

		Thread.sleep(maxNumThreads*1000+(maxCallTime-minCallTime)*1000*2);
	}

	/**
	 * Check if not employees then BadRequest Response
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
	 * @param serializable
	 * @return
	 * @throws Exception
	 */
	private byte[] toJson(Object serializable) throws Exception {
		ObjectMapper map = new ObjectMapper();
		byte[] byteArray = map.writeValueAsString(serializable).getBytes();
		LOG.debug("JSON: " + new String(byteArray));
		return byteArray;
	}
}
