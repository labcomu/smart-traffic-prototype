package com.smarttrafficprototype.trafficmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CSVWriter {
	
	@Autowired
	private ExecutionCyclesRepository cyclesRepository;
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSSS");
	
	public void writeCSV() {
		try (PrintWriter writer = new PrintWriter(new File("result.csv"))) {

			StringBuilder sb = new StringBuilder();
			sb.append("id,");
			sb.append("timeStamp,");
			sb.append("executedAt,");
			sb.append("duration,");
			sb.append("result,");
			sb.append('\n');
			
			for (ExecutionCycle cycle : cyclesRepository.getAll()) {
				sb.append(cycle.getId()).append(',');
				sb.append(cycle.getMoment().getTime()).append(',');
				sb.append(formatter.format(cycle.getMoment())).append(',');
				sb.append(cycle.getDuration()).append(',');
				sb.append(cycle.getClassification().toString()).append('\n');
			}

			writer.write(sb.toString());
			
			System.out.println("done!");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

}
