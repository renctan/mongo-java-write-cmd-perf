package mongo.writeCmd.test;

import com.mongodb.DBCollection;

public interface TestFunc {
	void setup(DBCollection coll);
	void cleanup(DBCollection coll);
	
	void run(DBCollection coll);
	String getName();
}
