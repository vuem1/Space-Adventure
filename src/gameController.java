import java.util.LinkedList;

/**
 * Controller initializes view and model and links user input from the view to the model.
 *
 * Created by Ethan Cassel-Mace, Hannah Barnstone, Quinn Mayville, && Michael Vue
 */
public class gameController {
    private GameModel model;
    private gameView view;

    public gameController(GameModel model) {
        this.model = model;
        view = new gameView(this, model);
        try {
            view.start(gameView.gameStage);

        } catch (Exception error) {
            error.printStackTrace();
        }
        model.initialize();
        model.setView(view);
        model.startGame();
    }


    /*
     * methods below pass on alerts from the view to the model, where they are handled appropriately.
     */

    public void rightArrowKey() {
        model.startAccelerationPositive();
    }

    public void leftArrowKey() {
        model.startAccelerationNegative();
    }

    public void rightArrowKeyReleased() { model.stopAccelerationPositive(); }

    public void leftArrowKeyReleased() { model.stopAccelerationNegative(); }

    public void pKey() {model.pause();}

}
