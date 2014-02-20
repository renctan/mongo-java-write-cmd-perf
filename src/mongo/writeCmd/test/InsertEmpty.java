package mongo.writeCmd.test;

import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;

public class InsertEmpty implements TestFunc {

	@Override
	public void run(DBCollection coll) {
		for (int x = 0; x < 1000; x++) {
			coll.insert(new BasicDBObject());
		}
	}

	@Override
	public String getName() {
		return "LegacyInsertEmpty";
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
