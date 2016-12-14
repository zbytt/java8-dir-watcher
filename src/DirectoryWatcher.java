import com.sun.istack.internal.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by zbyszekt on 2016-12-14.
 */
public final class DirectoryWatcher {

    private final WatchService watcher;
    private WatchKey key ;
    private final Path  path;

    public DirectoryWatcher(@NotNull String dirPath) throws IOException {
        if (dirPath == null && dirPath.isEmpty())
            throw new IOException("Path can't be empty");

        this.path = Paths.get(dirPath);

        if (Files.notExists(this.path))
            throw new FileNotFoundException("Path not accessible");
        if (!Files.isDirectory(this.path))
            throw new IllegalArgumentException("Path should return a directory");

        this.watcher = FileSystems.getDefault().newWatchService();
        this.path.register(this.watcher,StandardWatchEventKinds.ENTRY_CREATE);
    }

    public void pullDirectory(@NotNull FileProcessor filePrcessor) throws InterruptedException {

        for(;;){
            key = watcher.take();

            for (WatchEvent<?> event : key.pollEvents()){

                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW)
                    continue;

                WatchEvent<Path> eventPath = (WatchEvent<Path>) event;

                Path processedFilePath = eventPath.context();

                filePrcessor.proccess(processedFilePath);
            }

            if(key!=null){
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }

    }
}
