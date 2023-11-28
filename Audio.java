public class Audio {
    String fileName;
    double audioLength;

    public Audio(String fileName, double audioLength) {
        this.fileName = fileName;
        this.audioLength = audioLength;
    }

    public String getFileName() {
        return fileName;
    }

    public double getLength() {
        return audioLength;
    }
}