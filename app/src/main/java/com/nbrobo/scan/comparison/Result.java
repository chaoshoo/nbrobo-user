package com.nbrobo.scan.comparison;

public class Result {
	
	private String id;
	
	private Integer distance;

	public Result(String id) {
		super();
		this.id = id;
		this.distance = 0;
	}

	public String getId() {
		return id;
	}

	public Integer getDistance() {
		return distance;
	}

	public Result distance(Integer distance) {
		if (this.distance == 0) {
			this.distance = distance;
		} 
		if (distance < this.distance){
			this.distance = distance;
		}
		return this;
	}
	
}
