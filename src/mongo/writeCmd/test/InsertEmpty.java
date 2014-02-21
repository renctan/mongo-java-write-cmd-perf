package mongo.writeCmd.test;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;

public class InsertEmpty implements TestFunc {

	@Override
	public void run(DBCollection coll, WriteConcernMode mode) {
		for (int x = 0; x < 1000; x++) {
			coll.insert(new BasicDBObject());
			
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
		return "InsertEmpty";
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
