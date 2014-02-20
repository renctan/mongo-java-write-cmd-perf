var WRITE_CMD_CMD = ['java', '-jar', 'writetest.jar', 'localhost', '30999'];

var options = {

    mongosOptions : { binVersion : "2.6" },
    configOptions : { binVersion : "2.6" },
    shardOptions : { binVersion : "2.6" },

    separateConfig : true,
    sync : false
};

var st = new ShardingTest({ shards: 1, mongos: 1, other: options });
run.apply(null, WRITE_CMD_CMD);
st.stop();

