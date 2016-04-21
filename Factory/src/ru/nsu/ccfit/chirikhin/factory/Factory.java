package ru.nsu.ccfit.chirikhin.factory;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Observable;

public class Factory {
    public static final long DEFAULT_PRODUCING_SPEED = 1000;

    private final Logger logger = Logger.getLogger(Factory.class.getName());
    private final String pathToConfig;
    private final IDRegisterer idRegisterer = new IDRegisterer();

    JavaFXView view;

    public Factory(String pathToConfig, JavaFXView view) {
        this.pathToConfig = pathToConfig;
        this.view = view;
    }

    public void start() throws DeveloperBugException, InvalidConfigException, InterruptedException, IOException {
        ConfigParser configParser;

        try {
            configParser = new ConfigParser(pathToConfig);

            Storage<Accessory> accessoryStorage = new Storage<>(configParser.getAccessoryStorageSize());
            accessoryStorage.addObserver(view);

            Storage<CarBody> carBodyStorage = new Storage<>(configParser.getCarBodyStorageSize());
            carBodyStorage.addObserver(view);

            Storage<Engine> engineStorage = new Storage<>(configParser.getEngineStorageSize());
            engineStorage.addObserver(view);

            Storage<Car> carStorage = new Storage<>(configParser.getCarStorageSize());
            carStorage.addObserver(view);


            Thread accessoryProducers[] = new Thread[configParser.getAccessorySupplCount()];
            for (int k = 0; k < configParser.getAccessorySupplCount(); ++k) {
                accessoryProducers[k] = new Thread(new AccessoryProducer(accessoryStorage, "Accessory Producer num " + k, DEFAULT_PRODUCING_SPEED, idRegisterer));
                accessoryProducers[k].start();
            }

            Thread engineProducer = new Thread (new EngineProducer(engineStorage, DEFAULT_PRODUCING_SPEED, idRegisterer));
            engineProducer.start();

            Thread carBodyProducer = new Thread(new CarBodyProducer(carBodyStorage, DEFAULT_PRODUCING_SPEED, idRegisterer));
            carBodyProducer.start();

            CarCollectors carCollectors = new CarCollectors(configParser.getWorkersCount(), engineStorage, carBodyStorage, accessoryStorage, carStorage, idRegisterer);
            CarStorageController carStorageController = new CarStorageController(carStorage, carCollectors);

            Thread carStorageCollectorThread = new Thread(carStorageController);
            carStorageCollectorThread.start();


            Thread dealerThread[] = new Thread[configParser.getDealersCount()];
            for (int k = 0; k < configParser.getDealersCount(); ++k) {
                dealerThread[k] = new Thread(new Dealer(carStorage, "Dealer " + k));
                dealerThread[k].start();
            }
        } catch(Exception e) {
            logger.error(e.toString());
            throw e;
        }


    }

    public void stop() {

    }
}
