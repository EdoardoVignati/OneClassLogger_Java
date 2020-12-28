public class Main {
    public static void main(String[] args) {
        // Without options the console is selected by default
        OneClassLogger logger = new OneClassLogger(Main.class);
        logger.debug("This is a debug message");
        logger.warn("This is a warning message");

        // Load options from a specific file
        String path = "./log.options";
        OneClassLogger loggerWithOptions = new OneClassLogger(path, Main.class);
        loggerWithOptions.debug("This is a debug message");
        loggerWithOptions.warn("This is a warning message");
    }
}
