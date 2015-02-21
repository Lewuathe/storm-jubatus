storm-jubatus
=========

# How to install

:TODO

# Algorithms supported by storm-jubatus

|Solver Type|Jubatus algorithm client|Use case|
|:------|:------|:------|
|REGRESSION|RegressionClient|For regression problems|
|RECOMMENDER|RecommenderClient|For recommendation|

# How to use

```java

public class JubatusTrainTopology {
  public static void main(String[] args) throws Exception {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("word", new TestWordSpout(), 5);
    JubatusBolt bolt = null;
    try {
	  // All configurations you have to write are
	  // * Algorithm Type
	  // * IP Address of Jubatus Server
	  // * Port of Jubatus Server
	  // * Model name which is used by Jubatus
      bolt = new JubatusBolt(Solver.Type.RECOMMENDER, "172.30.0.16", 9199, "bolt");
    } catch (NoSuchSolverException e) {
      e.printStackTrace();
    }
    builder.setBolt("format", new TupleFormatterBolt(), 2).shuffleGrouping("word");
    builder.setBolt("juba1", bolt, 2).shuffleGrouping("format");

    Config conf = new Config();
    conf.setDebug(true);
    if (args != null && args.length > 0) {
      conf.setNumWorkers(3);
      StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    }
  }
}

```

# LICENSE

[Apache v2](http://www.apache.org/licenses/LICENSE-2.0)

# Author

* Kai Sasaki([@Lewuathe](https://github.com/Lewuathe))
