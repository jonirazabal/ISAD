package system;

public class ViewEvent {

	private ViewEvent() {};
	
	public static class StatusChange extends ViewEvent {
		
	}
	public static class NewPhoto extends ViewEvent {
		public final String path;
		public final int id;
		public NewPhoto(int pId,String pPath) {
			this.id = pId;
			this.path = pPath;
		}
	}
	
	public static class NewMessage extends ViewEvent {
		public final String mezua;
		public NewMessage(String pMezua) {
			this.mezua = pMezua;
		}
	}
	
	public static class NewChat extends ViewEvent {
		public final long id;
		public final String title;
		public final int ordua;
		public NewChat(long pId, String pTitle, int pOrdua) {
			this.id = pId;
			this.title = pTitle;
			this.ordua = pOrdua;
		}
		
		
	}
	
	

}
