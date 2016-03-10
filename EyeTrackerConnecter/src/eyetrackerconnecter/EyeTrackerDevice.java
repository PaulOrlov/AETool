package eyetrackerconnecter;

/**
 *
 * @author 316-t
 */
public abstract class EyeTrackerDevice {
    public abstract String getSendIP();
    public abstract int getRecivePort();
    public abstract int getSendPort();
}
