
package com.blb.action;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class Test {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
    	 
        //1：准备一个TopologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //2：这里，定义了Spout的使用，并行度为2，也就是说有两个task用于处理输入；前面的名称指定了Spout的专属ID，用于后续处理能够指定
        topologyBuilder.setSpout("Splout",new Splout(),2);
        //3：这里，定义了接下来的SplitBolt，定义了承接关系
        //其承接来自于Spout的输入，并且定义了并行度为2，而且定义了StreamingGroup，这样可以完全随机地接收来自于前面的spout的数据
        //并且交由两个Task来进行处理。
        topologyBuilder.setBolt("mybolt",new Sbolt(),4).shuffleGrouping("Splout");
        //4：这里，定义接下来的承接关系，用MyCountBolt来承接mybole1发送过来的Tuple，
//        topologyBuilder.setBolt("mybolt2",new MyCountBolt(),4).shuffleGrouping("mybolt1");
        //  config.setNumWorkers(2);
 
 
        //2、创建一个configuration，用来指定当前topology 需要的worker的数量
        Config config =  new Config();
        config.setNumWorkers(2);
 
        //3、提交任务  -----两种模式 本地模式和集群模式
//        StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }

}
