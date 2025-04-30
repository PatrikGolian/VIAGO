package startup;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RunClient extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        //Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        stage.setMinHeight(400);
        stage.setMinWidth(600);
       // stage.setFullScreen(true);
        ViewHandler viewHandler = new ViewHandler(stage);
        viewHandler.start();
    }

    public static void main(String[] args)
    {
        RunClient.launch(args);
    }
}
