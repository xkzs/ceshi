package com.blb.action;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class Splout extends BaseRichSpout{
	private SpoutOutputCollector collector;
	private int count = 0;
	@Override
	public void nextTuple() {
		  if(count<=2){
	            System.out.println("第"+count+"次开始发送数据...");
	            this.collector.emit(new Values(count,0));
	        }
	        count++;
		
	}

	@Override
	public void open(Map map, TopologyContext arg1, SpoutOutputCollector collector) {
		 System.out.println("open:"+map.get("test"));
	     this.collector = collector;
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-genedeclarer.declare(new Fields(field));rated method stub
		declarer.declare(new Fields("0"));
	}

}
