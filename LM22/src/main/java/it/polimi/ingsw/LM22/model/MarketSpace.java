package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class MarketSpace extends AbstractSpace implements Serializable {
	
		/*
		 * i bonus del marketSpace devono essere instanziati da File
		 */
		private final Resource reward;
		private final Integer councilPrivilege;
		private FamilyMember member;
		
		
		/*
		 * reward deve essere caricato da file 
		 * ATTENZIONE al costruttore
		 * */
		public MarketSpace (Integer requirement, Resource reward, Integer councilPrivilege){
			super(requirement);
			this.reward = reward;
			this.councilPrivilege = councilPrivilege;
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

		public Integer getCouncilPrivilege() {
			return councilPrivilege;
		}
		
		
		
		/* forse da mettere nel controller 
		 * 
		 * public boolean isOccupied(){ 
		 * 		if (member != null){
		 * 			return false;
		 * 		}
		 * 		return true;
		 * }
		*/
		
}
