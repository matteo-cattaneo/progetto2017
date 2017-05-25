package it.polimi.ingsw.LM22.model;

public class MarketSpace extends AbstractSpace{
	
		private final Resource reward;
		private FamilyMember member;
		
		
		/*
		 * reward deve essere caricato da file 
		 * ATTENZIONE al costruttore
		 * */
		public MarketSpace (Integer requirement, Resource reward){
			super(requirement);
			this.reward = reward;
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
