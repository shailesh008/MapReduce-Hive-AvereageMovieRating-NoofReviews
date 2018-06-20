package mapreduceassignment2;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * ReviewMapper is a mapper class for Reviews file 
 * MovieID as a Key and ratings as Value
 */
public class ReviewMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private IntWritable outkey = new IntWritable();
	private Text outvalue = new Text();

	/*
	 * (Set up method is called at the start of Map i.e. before the actual map function to do some tranformation before map) 
	 * org.apache.hadoop.mapreduce.Mapper#setup(org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void setup(Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {}

	/*
	 * (Method for reading the review file and trasform in Key(movieId)
	 * value(ratings) Wrting to the Key value in the data block)
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN,
	 * org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		try {
			String[] strArrText = value.toString().split(",");
			outkey.set(Integer.parseInt(strArrText[1]));
			String strValue = "reviews" + "%" + strArrText[2];
			outvalue = new Text(strValue);
			context.write(outkey, outvalue);
		} catch (Exception e) {
		}
	}

	/*
	 * (Cleanup is called once at the end to finish off anything) 
	 * @seeorg.apache.hadoop.mapreduce.Mapper#cleanup(org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void cleanup(Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
	}
}
