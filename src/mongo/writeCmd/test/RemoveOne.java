package mongo.writeCmd.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class RemoveOne implements TestFunc {

	@Override
	public void setup(DBCollection coll) {
		coll.drop();
		for (int x = 0; x < 1000; x++) {
			coll.insert(new BasicDBObject("x", x));
		}
	}

	@Override
	public void cleanup(DBCollection coll) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(DBCollection coll, WriteConcernMode mode) {
		for (int x = 0; x < 1000; x++) {
			coll.remove(new BasicDBObject("x", x));
			
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
		return "RemoveOne";
	}

}
