package cn.sibat.hibernate.builder;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SortBuilder {
	Sort sort;
	
	public SortBuilder() {//凯哥
		
	}
	
	public SortBuilder(Direction direction, List<String> properties) {
		super();
		sort = new Sort(direction,properties);
	}
	public SortBuilder(Direction direction, String properties) {
		super();
		sort = new Sort(direction,properties);
	}
	
	public SortBuilder addAsc( String... properties ) {
		this.add( Sort.Direction.ASC, properties );
		return this;
	}
	
	public SortBuilder addDesc( String... properties ) {
		this.add( Sort.Direction.DESC, properties );
		return this;
	}
	
	public SortBuilder addAsc( List<String> properties ) {
		this.add( Sort.Direction.ASC, properties );
		return this;
	}
	
	public SortBuilder addDesc( List<String> properties ) {
		this.add( Sort.Direction.DESC, properties );
		return this;
	}

	public SortBuilder add(Direction direction, List<String> properties){
		if( this.sort == null ) {
			this.sort = new Sort(direction, properties);
		}else {
			sort = sort.and( new Sort(direction, properties) );
		}
		return this;
	}
	
	public SortBuilder add(Direction direction, String... properties){ 
		if( this.sort == null ) {
			this.sort = new Sort(direction, properties);
		}else {
			this.sort = sort.and( new Sort(direction, properties) );
		}
		return this;
	}
	
	public Sort getSort() {
		return this.sort;
	}
}
