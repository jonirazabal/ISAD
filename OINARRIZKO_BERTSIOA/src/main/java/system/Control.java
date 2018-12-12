package system;

import java.nio.file.Path;
import java.util.Observable;

import client.TdClient;
import model.Model;
import persistence.Database;
import view.View;

public class Control {

	private final ViewControl mViewControl;
	private final Database mDB;
	private final Model mModel;
	private final TdClient mClient;
	private Path mAppDir;

	public Control(Path appDir) {
		mAppDir = appDir;
		mViewControl = new ViewControl();
		mDB = new Database(appDir);
		mModel = Model.setup(mDB, appDir);
		mClient = TdClient.create(mViewControl, appDir);

	}
	public void quit() {
		mClient.quit();
	}

	public void launch() {

		mModel.load();
		View view = View.create(mViewControl, mModel).orElse(null);
		view.init();
		

	}


	public class ViewControl extends Observable {

		public void gehituTxatIzenburua(long id, String title, int ordua) {
			ViewEvent newChat = new ViewEvent.NewChat(id, title, ordua);
			this.changed(newChat);
		}
		public void getChatList() {
			mClient.getChatList(20);
		}
		public String lortuTxatarenAzkenengoMezua(String id) {
			return mClient.getChat(id);
		}
		public void lortuTxatarenAzkenMezuak(String id, int zenbat) {
			  mClient.getChatMessages(Long.parseLong(id),0l,zenbat);
		}
		public void bistaratuMezua(String mezua) {
			ViewEvent newMessage = new ViewEvent.NewMessage(mezua);
			this.changed(newMessage);
		}
		public void bistaratuIrudia(int id, String path) {
			ViewEvent newPhoto = new ViewEvent.NewPhoto(id, path);
			this.changed(newPhoto);
		}
		

		void changed(ViewEvent event) {
			this.setChanged();
			this.notifyObservers(event);
		}

	}

}
