package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardSpace extends AbstractSpace implements Serializable {

		private final Integer level;
		private final Resource reward;
		private FamilyMember member;
		
		/*
		 * ATTENZIONE con i costruttori perchè reward va caricato da file
		 * */
		public CardSpace (Integer requirement, Integer level, Resource reward){
			super(requirement);
			this.level = level;
			this.reward = reward;
		}
				
		public Integer getLevel(){
			return level;
		}
		
		public FamilyMember getMember() {
			return member;
		}
		
		public void setMember(FamilyMember member) {
			this.member = member;
		}
		
		public Resource getReward() {
			return reward;
		}
		
	
		
}
