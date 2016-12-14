import java.io.IOException;
import java.nio.file.*;

import java.nio.file.Paths.*;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        String separator = FileSystems.getDefault().getSeparator();

        Consumer<Path> function= (path) -> System.out.println("Found->" + path.normalize().toString());
        try {
            DirectoryWatcher watcher = new DirectoryWatcher("C:\\testDir");
            watcher.pullDirectory(function::accept);
        } catch (IOException e) {
            System.out.println("Init problems occured  : "+ e.getMessage());
        } catch (InterruptedException e ){
            System.out.println("Interrupted : "+ e.getMessage());
        }

    }
}
