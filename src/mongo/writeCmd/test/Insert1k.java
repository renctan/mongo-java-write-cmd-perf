package mongo.writeCmd.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class Insert1k implements TestFunc {

	@Override
	public void run(DBCollection coll, WriteConcernMode mode) {
		StringBuilder strBuilder = new StringBuilder(1024);
		for (int x = 0; x < 1024; x++) {
			strBuilder.append('a');
		}
		
		final String str = strBuilder.toString();
		for (int x = 0; x < 1000; x++) {
			coll.insert(new BasicDBObject("x", str));
			
			if (mode == WriteConcernMode.GLEEveryWrite) {
				coll.getDB().command(new BasicDBObject("getLastError", 1));
			}
		}
		
		if (mode == WriteConcernMode.GLEAfterBatch) {
			coll.getDB().command(new BasicDBObject("getLastError", 1));
		}
	}

	@Override
	public String getName() {
		return "Insert1k";
	}

	@Override
	public void setup(DBCollection coll) {
		// no-op
	}

	@Override
	public void cleanup(DBCollection coll) {
		// no-op
	}
}
