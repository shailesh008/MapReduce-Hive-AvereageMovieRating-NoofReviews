package mapreduceassignment2;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

/*
 * FinalReducer is a reducer class for reducing and combining 
 * the movie and reviews data together.
 * In this NoOf Reviews & Average rating is calculated for each movies.
 * Output is sorted on the basis of Average Rating
 */
public class FinalReducer extends Reducer<IntWritable, Text, Text, Text> {
	public Map<String, Double> movieAvgReviewCountMap = new HashMap<String, Double>();

	/*
	 * (Set up method is called at the start of Reduce i.e. before the actual Reduce
	 * function to do some tranformation before map)
	 */
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
	}

	/*
	 * Output of Movie Mapper and Review Mapper are merged Same Movie Name Handled
	 * Putting Movie Name and No Of Review for each movie Having review atleast one
	 * in Map NOo of reviews and Average Rating is calculated for each movie in
	 * dataset
	 */

	protected void reduce(IntWritable key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		double sum = 0;
		double avgRating = 0.0;
		int noOfReview = 0;
		String tmp = "";
		String movieName = "";
		Iterator<Text> val = values.iterator();

		while (val.hasNext()) {
			tmp = val.next().toString();
			String record[] = tmp.split("%");

			if (record[0].equalsIgnoreCase("movies")) {
				movieName = record[1];

			} else if (record[0].equalsIgnoreCase("reviews")) {
				sum += Double.parseDouble(record[1]);
				noOfReview++;
			}
		}
		avgRating = sum / noOfReview;

		if (avgRating > 4 && noOfReview > 10) {
			String newString = movieName + "%" + Integer.toString(noOfReview);
			System.out.println("newString" + newString);
			movieAvgReviewCountMap.put(newString, avgRating);

		}
	}

	/*
	 * Method for returning the sorted map. Comparator is used to compare the value
	 * of the map sort the output according to value and put it in map And return
	 * it.
	 */
	public static Map<String, Double> returnSortedMap(Map<String, Double> input) {
		LinkedList<Map.Entry<String, Double>> entries = new LinkedList<Map.Entry<String, Double>>(input.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue().compareTo(o1.getValue()));
			}
		});

		Map<String, Double> returnMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : entries) {
			returnMap.put(entry.getKey(), entry.getValue());
		}
		return returnMap;
	}

	/*
	 * (Cleanup is called once at the end to finish off anything for reducer Here
	 * final out is written to file in HDFS)
	 * 
	 * @see org.apache.hadoop.mapreduce.Reducer#cleanup(org.apache.hadoop.mapreduce.
	 * Reducer.Context)
	 */
	public void cleanup(Context context) throws IOException, InterruptedException {
		Map<String, Double> sortedMovieReviewCountMap = new HashMap<String, Double>();

		sortedMovieReviewCountMap = returnSortedMap(movieAvgReviewCountMap);

		for (Map.Entry<String, Double> entry : sortedMovieReviewCountMap.entrySet()) {
			String record[] = entry.getKey().split("%");
			context.write(new Text(record[0]), new Text("	" + entry.getValue() + "		" + record[1]));

		}
	}
}
