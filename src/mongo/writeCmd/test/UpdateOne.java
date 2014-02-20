package mongo.writeCmd.test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class UpdateOne implements TestFunc {

	@Override
	public void setup(DBCollection coll) {
		coll.drop();
		coll.insert(new BasicDBObject("x", 1));
	}

	@Override
	public void cleanup(DBCollection coll) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(DBCollection coll) {
		for (int x = 0; x < 1000; x++) {
			coll.update(new BasicDBObject("x", 1),
					new BasicDBObject("$inc", new BasicDBObject("x", 1)));
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "updateOne";
	}

}
