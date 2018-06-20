package mapreduceassignment2;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

/*
 * FinalDriver class is a driver class 
 * Designed to set configuration for job
 * Mapper and reducer class defined
 * No. of reducer also defined
 * ToolRunner is used to accept the commandline arguments
 */
public class FinalDriver extends Configured implements Tool {
	private Configuration conf1;
	private static final Logger log = Logger.getLogger(FinalDriver.class);

	public int run(String[] args) throws Exception {

		String inputLoc1 = args[0];
		String inputLoc2 = args[1];
		String outputLoc = args[2];
		conf1 = getConf();
		conf1.set("mapred.job.queue.name", "Shai");

		Job job1 = new Job(conf1, "MapReduce:");
		job1.setJarByClass(FinalDriver.class);

		MultipleInputs.addInputPath(job1, new Path(inputLoc1), TextInputFormat.class, MovieMapper.class);
		MultipleInputs.addInputPath(job1, new Path(inputLoc2), TextInputFormat.class, ReviewMapper.class);

		job1.setMapOutputKeyClass(IntWritable.class);
		job1.setMapOutputValueClass(Text.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job1, new Path(outputLoc));
		job1.setReducerClass(FinalReducer.class);
		
		int status = (job1.waitForCompletion(true) == true) ? 0 : 1;
		return status;

	}
/*
 * Main Method to run Job
 */
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int res = ToolRunner.run(new Configuration(), new FinalDriver(), args);
		long end = System.currentTimeMillis();
		log.info("Total time taken to execute Map-Reduce Job is : " + (end - start) + " millisecs");
		if (res != 0) {
			System.exit(res);
		}

	}

}
