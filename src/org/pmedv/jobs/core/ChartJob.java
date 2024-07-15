package org.pmedv.jobs.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.jobs.SchedulerControl;
import org.pmedv.pojos.Config;
import org.pmedv.services.ChartService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * The chart job is intended to create recruitment and center charts for each study in 
 * an 24h interval. The Job is triggered from the {@link SchedulerControl} at system startup.
 * 
 * @author Matthias Pueski
 * 
 */
public class ChartJob implements Job {

	private static final Log log = LogFactory.getLog(ChartJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1L);

		Properties gwProps = null;

		try {
			String propertiesLoc = config.getBasepath() + "WEB-INF/application.properties";
			FileInputStream is = new FileInputStream(new File(propertiesLoc));

			gwProps = new Properties();
			gwProps.load(is);
		}
		catch (FileNotFoundException e1) {
			log.error("Could not load application.properties");
			return;
		}
		catch (IOException e1) {
			log.error("Could not load application.properties");
			return;
		}

		String gwUser = gwProps.getProperty("gw.username");
		String gwPass = gwProps.getProperty("gw.pass");

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(gwUser, gwPass);
		SecurityContextHolder.getContext().setAuthentication(token);
		
		Long startTime = System.currentTimeMillis();
		
		ApplicationContext ctx = AppContext.getApplicationContext();		
		ChartService service = (ChartService)ctx.getBean("chartService");
		
		// clean up recent chart files.
		File chartFile = new File(config.getBasepath() + "charts/");		
		File[] recentChartFiles = chartFile.listFiles(); 		
		for (int i = 0; i < recentChartFiles.length;i++) {
			recentChartFiles[i].delete();
		}

		Map<Integer, String> studies =  service.getStudies();
		
		for (Integer identity : studies.keySet()) {

			log.info("Creating recruitment chart for study "+identity);
			service.createRecruitmentChart(String.valueOf(identity));
			
			log.info("Creating center chart for study "+identity);		
			service.createCenterChart(String.valueOf(identity));
			
		}
		
		log.info("Chart creation done in "+(System.currentTimeMillis()-startTime)+" ms");
	}

}
