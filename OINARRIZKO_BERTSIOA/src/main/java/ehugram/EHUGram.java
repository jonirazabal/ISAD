package ehugram;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.lang3.SystemUtils;
import org.drinkless.tdlib.TdApi;

import client.TdClient;
import system.Control;
import system.Control.ViewControl;

public class EHUGram {

	private static final Logger LOGGER = Logger.getLogger(EHUGram.class.getName());
    public static final String VERSION = "0.0.1";
    private final Path mAppDir;
    
	public int start() {
		
		// TR.init();
		
		LOGGER.info("--START, version: "+VERSION+"--");
		Control control;
		control = new Control(mAppDir);
		
		control.launch();
		Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook") {
			public void run() {
				System.out.println("quit");
				control.quit();
				
			}
		});
		
		return 0;
	
	}
	
	
	public EHUGram() {
	
		// platform dependent configuration directory
        this(Paths.get(System.getProperty("user.home"),
                SystemUtils.IS_OS_WINDOWS ? "ehugram" : ".ehugram"));
	}
	
	public EHUGram(Path appDir) {
        mAppDir = appDir.toAbsolutePath();
	}

	public static void main(String[] args) {
		
		EHUGram app = new EHUGram();
		int returnCode = app.start();
		
	}
	
}
