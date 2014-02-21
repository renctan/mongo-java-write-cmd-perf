var WRITE_CMD_CMD = ['java', '-jar', 'writetest.jar', 'localhost', '30999', '1'];
var LEGACY_CMD = ['java', '-jar', 'writetest.jar', 'localhost', '30999', '0'];

var st = new ShardingTest({ shards: 1, mongos: 1 });
run.apply(null, WRITE_CMD_CMD);
st.stop();

var options = { shardOptions: { binVersion: '2.4' }, separateConfig: true };
st = new ShardingTest({ shards: 1, mongos: 1, other: options });
run.apply(null, WRITE_CMD_CMD);
st.stop();

options = {
    mongosOptions: { binVersion: '2.4' },
    shardOptions: { binVersion: '2.4' },
    separateConfig: true
};

st = new ShardingTest({ shards: 1, mongos: 1, other: options });
run.apply(null, LEGACY_CMD);
st.stop();

