package com.letsrace.game.map;

public class FRAngleMonitor{
	private float changeRate;
	private float turnAngle;
	public float getTurnAngle(){
		if(turnAngle>=0){
			if(turnAngle - changeRate >=0){
				turnAngle -= changeRate;
				return changeRate;
			}
		}else{
			if(turnAngle + changeRate <=0){
				turnAngle += changeRate;
				return -changeRate;
			}
		}
		float temp = turnAngle;
		turnAngle = 0;
		return temp;
	}
	public void setTurnAngle(float a){
		turnAngle+= a;
	}
	public FRAngleMonitor(float cr){
		changeRate =cr;
	}
}