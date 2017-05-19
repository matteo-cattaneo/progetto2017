package it.polimi.ingsw.LM22.model;

public class CardSpace extends AbstractSpace{
	//costante da inizializzare con il costruttore
		//che andrò a richiamare il costruttore super();
		private final Resource reward;
		private FamilyMember member;
		
		
		
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
