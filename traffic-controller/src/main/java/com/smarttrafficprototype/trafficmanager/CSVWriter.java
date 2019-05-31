package com.smarttrafficprototype.trafficmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CSVWriter {
	public Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ExecutionCyclesRepository cyclesRepository;
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSSS");
	
	public void writeCSV() {
		try (PrintWriter writer = new PrintWriter(new File("result.csv"))) {

			StringBuilder sb = new StringBuilder();
			sb.append("id,");
			sb.append("start,");
			sb.append("end,");
			sb.append("executedAt,");
			sb.append("duration,");
			sb.append("result,");
			sb.append('\n');
			
			for (ExecutionCycle cycle : cyclesRepository.getAll()) {
				sb.append(cycle.getId()).append(',');
				sb.append(cycle.getStart().getTime()).append(',');
				sb.append(cycle.getEnd().getTime()).append(',');
				sb.append(formatter.format(cycle.getEnd())).append(',');
				sb.append(cycle.getDuration()).append(',');
				sb.append(cycle.getClassification().toString()).append('\n');
			}

			writer.write(sb.toString());
			
			logger.info(sb.toString());
			logger.info("DONE!");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}
