package mongo.writeCmd.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class UpdateMulti implements TestFunc {

	@Override
	public void setup(DBCollection coll) {
		coll.drop();
		for (int x = 0; x < 1000; x++) {
			coll.insert(new BasicDBObject("x", x));
		}
	}

	@Override
	public void cleanup(DBCollection coll) {
		coll.drop();
	}

	@Override
	public void run(DBCollection coll) {
		for (int x = 0; x < 1000; x++) {
			coll.update(new BasicDBObject(),
						new BasicDBObject("$inc", new BasicDBObject("x", 1)),
						false, // upsert
						true); // multi
		}
	}

	@Override
	public String getName() {
		return "updateMulti";
	}

}
