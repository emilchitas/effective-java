package at.chitas.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Cleaner;

public class Room implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Room.class);
    private static final Cleaner cleaneer = Cleaner.create();


    //Resource that requires cleaning
    private static class State implements Runnable {
        int numJunkPiles;

        public State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        @Override
        public void run() {
            LOGGER.info("Cleaning the room.");
            numJunkPiles = 0;
        }
    }

    //The state of this room, shared with our cleanable
    private final State state;
    //Our cleanable cleans the room when it's eligible for GC
    private final Cleaner.Cleanable cleanable;

    public Room(State state) {
        this.state = state;
        this.cleanable = cleaneer.register(this, state);
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }

}
