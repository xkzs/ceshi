package com.blb.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class Sbolt extends BaseRichBolt{
	OutputCollector collector;
    Map<String, Integer> map = new HashMap<String, Integer>();
    
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =  collector;
	}

	@Override
	public void execute(Tuple input) {
	      String word = input.getString(0);
	        Integer num = input.getInteger(1);
	        System.out.println(Thread.currentThread().getId() + "    word:"+word);
	        if (map.containsKey(word)){
	            Integer count = map.get(word);
	            map.put(word,count + num);
	        }else {
	            map.put(word,num);
	        }

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
