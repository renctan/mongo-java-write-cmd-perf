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
	public void run(DBCollection coll) {
		for (int x = 0; x < 1000; x++) {
			coll.remove(new BasicDBObject("x", x));
		}
	}

	@Override
	public String getName() {
		return "RemoveOne";
	}

}
