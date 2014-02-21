var st = new ShardingTest({ shards: 1, mongos: 1 });
run('java', '-jar', 'writetest.jar', 'localhost', '30999', '1', '2.6/2.6');
st.stop();

var options = { shardOptions: { binVersion: '2.4' }, separateConfig: true };
st = new ShardingTest({ shards: 1, mongos: 1, other: options });
run('java', '-jar', 'writetest.jar', 'localhost', '30999', '1', '2.6/2.4');
st.stop();

options = {
    mongosOptions: { binVersion: '2.4' },
    shardOptions: { binVersion: '2.4' },
    separateConfig: true
};

st = new ShardingTest({ shards: 1, mongos: 1, other: options });
run('java', '-jar', 'writetest.jar', 'localhost', '30999', '0', '2.4/2.4');
st.stop();

