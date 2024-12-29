package edu.tamu.scholars.discovery.index.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.IndexConfig;
import edu.tamu.scholars.discovery.index.component.Harvester;
import edu.tamu.scholars.discovery.index.component.Indexer;
import edu.tamu.scholars.discovery.service.Triplestore;

@Service
public class IndexService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    private static final AtomicBoolean indexing = new AtomicBoolean(false);

    private final IndexConfig index;
    private final List<Harvester> harvesters;
    private final List<Indexer> indexers;
    private final Triplestore triplestore;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    IndexService(
        IndexConfig index,
        List<Harvester> harvesters,
        List<Indexer> indexers,
        Triplestore triplestore,
        ThreadPoolTaskScheduler threadPoolTaskScheduler
    ) {
        this.index = index;
        this.harvesters = harvesters;
        this.indexers = indexers;
        this.triplestore = triplestore;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    public Boolean isIndexing() {
        return indexing.get();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (index.isSchematize()) {
            logger.info("Initializing index fields...");
            indexers.stream().forEach(indexer -> {
                logger.info("Initializing {} fields.", indexer.type().getSimpleName());
                indexer.init();
            });
        }
        if (index.isOnStartup()) {
            Instant startTime = Instant.now().plusMillis(index.getOnStartupDelay());
            threadPoolTaskScheduler.schedule(this::index, startTime);
        }
    }

    @Scheduled(cron = "${discovery.index.cron}", zone = "${discovery.index.zone}")
    public void index() {
        if (indexing.compareAndSet(false, true)) {
            triplestore.init();
            final Instant start = Instant.now();
            logger.info("Indexing...");
            harvesters.parallelStream().forEach(harvester -> {
                logger.info("Indexing {} documents.", harvester.type().getSimpleName());
                if (indexers.stream().anyMatch(indexer -> indexer.type().equals(harvester.type()))) {
                    try {
                        harvester.harvest()
                            .buffer(index.getBatchSize())
                            .subscribe(batch -> indexers.parallelStream()
                                .filter(indexer -> indexer.type().equals(harvester.type()))
                                .forEach(indexer -> indexer.index(batch)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.warn("No indexer found for {} documents!", harvester.type().getSimpleName());
                }
                logger.info("Indexing {} documents finished.", harvester.type().getSimpleName());
            });
            indexers.stream().forEach(indexer -> {
                logger.info("Optimizing {} index.", indexer.type().getSimpleName());
                indexer.optimize();
            });
            logger.info("Indexing finished. {} seconds.", Duration.between(start, Instant.now()).toMillis() / 1000.0);
            triplestore.destroy();
            indexing.set(false);
        } else {
            logger.info("Already indexing. Waiting for next schedule.");
        }
    }

}
