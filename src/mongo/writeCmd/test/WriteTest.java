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
	
	static void runTest(TestFunc test, DBCollection coll, WriteConcernMode mode, int loops) {
		Aggregator agg = new Aggregator();
		
		for (int i = 0; i < loops; i++) {
			test.setup(coll);
			
			final long start = System.nanoTime();
			test.run(coll, mode);
			final long end = System.nanoTime();
			agg.add((end - start) / 1000 / 1000);

			test.cleanup(coll);
			System.out.println("REN_raw: " + ((end - start) / 1000 /1000));
		}
		
		Stats stats = agg.computeStats();
		System.out.println("REN: avg|sd " + test.getName() + ", " + stats.mean + ", " + stats.sd);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
		final Boolean useWriteCmd = Integer.parseInt(args[2]) == 1;
		
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
		List<TestFunc> tests = new ArrayList<TestFunc>();
		tests.add(new InsertEmpty());
		tests.add(new Insert1k());
		tests.add(new Insert1MB());
		tests.add(new UpdateOne());
		tests.add(new UpdateMulti());
		tests.add(new RemoveOne());

		for (TestFunc test: tests) {
			if (useWriteCmd) {
				System.out.println("REN: WRITE CMD");
				coll.setWriteConcern(WriteConcern.ACKNOWLEDGED);
				runTest(test, coll, WriteConcernMode.None, 100);
			}
			
			System.out.println("REN: LEGACY GLE EVERY WRITE"); 
			coll.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
			runTest(test, coll, WriteConcernMode.GLEEveryWrite, 100);
			
			System.out.println("REN: LEGACY GLE AFTER ALL WRITES");
			runTest(test, coll, WriteConcernMode.GLEAfterBatch, 100);
		}
	}

}
