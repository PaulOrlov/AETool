package eyetrackerconnecter;

/**
 *
 * @author paulorlov
 */
public class UpdateDeviceEvent {
    private int gazeX, gazeY;

    public UpdateDeviceEvent(int gazeX, int gazeY) {
        this.gazeX = gazeX;
        this.gazeY = gazeY;
    }

    public void setGazeX(int gazeX) {
        this.gazeX = gazeX;
    }

    public void setGazeY(int gazeY) {
        this.gazeY = gazeY;
    }

    public int getGazeX() {
        return gazeX;
    }

    public int getGazeY() {
        return gazeY;
    }
    
}
