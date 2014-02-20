package mongo.writeCmd.test;
import java.net.UnknownHostException;

import java.util.*;

import com.mongodb.*;

public class WriteTest {

	static public class Stats {
		public final double sd;
		public final double mean;
		
		public Stats(double sd, double mean) {
			this.sd = sd;
			this.mean = mean;
		}
	}
	
	static public class Aggregator {
		private List<Long> valList = new ArrayList<Long>();
		
		public void add(long newVal) {
			valList.add(newVal);
		}
		
		public Stats computeStats() {
			long total = 0;
			for (long val: valList) {
				total += val;
			}
			
			double mean = total / valList.size();
			float variance = 0;
			
			for (long val: valList) {
				double diff = val - mean;
				variance += diff * diff;
			}
			
			variance /= valList.size();
			double sd = Math.sqrt(variance);
			
			return new Stats(sd, mean);
		}
	}
	
	static void runTest(TestFunc test, DBCollection coll, int loops) {
		Aggregator agg = new Aggregator();
		
		for (int i = 0; i < loops; i++) {
			test.setup(coll);
			
			final long start = System.nanoTime();
			test.run(coll);
			final long end = System.nanoTime();
			agg.add((end - start) / 1000 / 1000);
			
			test.cleanup(coll);
			System.out.println("REN_raw: " + ((end - start) / 1000 /1000));
		}
		
		Stats stats = agg.computeStats();
		System.out.println("REN: avg|sd, " + test.getName() + ", " + stats.mean + ", " + stats.sd);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		
		MongoClient conn;
		try {
			conn = new MongoClient(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		DB db = conn.getDB("test");
		db.command(new BasicDBObject("dropDatabase", 1));
		
		DBCollection coll = db.getCollection("user");
		runTest(new InsertEmpty(), coll, 100);
		runTest(new Insert1k(), coll, 100);
		runTest(new UpdateOne(), coll, 100);
		runTest(new UpdateMulti(), coll, 100);
		runTest(new RemoveOne(), coll, 100);
	}

}
