package mapreduceassignment2;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * MovieMapper is a mapper class for Movies file 
 * MovieID as a Key and Mavie Name as Value
 */
public class MovieMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
	private IntWritable outkey = new IntWritable();
	private Text outvalue = new Text();

	/*
	 * (Set up method is called at the start of Map i.e. before the actual map
	 * function to do some tranformation before map)
	 * @see org.apache.hadoop.mapreduce.Mapper#setup(org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void setup(Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
	}

	/*
	 * (Method for reading the movie file and trasform in Key(movieId)
	 * value(movieName) Wrting to the Key value in the data block)
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN,
	 * org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		try {
			String line = value.toString();
			String[] strArrText = new String[5];
			strArrText[0] = line.substring(0, line.indexOf(','));
			strArrText[1] = line.substring(line.indexOf(',') + 1, line.lastIndexOf(','));
			outkey.set(Integer.parseInt(strArrText[0]));
			String strValue = "movies" + "%" + strArrText[1];
			outvalue = new Text(strValue);
			context.write(outkey, outvalue);
		} catch (Exception e) {
		}

	}

	/*
	 * (Cleanup is called once at the end to finish off anything)
	 * @see org.apache.hadoop.mapreduce.Mapper#cleanup(org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void cleanup(Mapper<LongWritable, Text, IntWritable, Text>.Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
	}
}
