package com.example.script.cli;

import com.example.domain.customer.dto.CustomerRecord;
import com.example.domain.customer.model.Customer;
import com.example.domain.customer.service.CustomerService;
import com.example.script.io.FileParser;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.script.functional.Predicates.safeTest;

/**
 *
 */
@Log4j2
@Singleton
public class DefaultCLIArgsProcessor implements CLIArgsProcessor {

    private final FileParser fileParser;
    private final MapperFacade mapper;
    private final CustomerService customerService;

    /**
     *
     * @param fileParser
     * @param mapper
     * @param customerService
     */
    @Inject
    public DefaultCLIArgsProcessor(FileParser fileParser, MapperFacade mapper, CustomerService customerService) {
        this.fileParser = fileParser;
        this.mapper = mapper;
        this.customerService = customerService;
    }

    /**
     *
     * @param args
     */
    @Override
    public void process(@Valid @NotNull final CLIArgs args) {
        if (args.isVerbose()) {
            increaseLogLevel();
        }

        List<CustomerRecord> customersRecords =
                fileParser.parse(args.getInputFile(), CustomerRecord.class);

        List<Customer> customers = customersRecords.stream()
                .map(r -> mapper.map(r, Customer.class))
                .collect(Collectors.toList());

        List<Customer> customersWithinRange = customers.stream()
                        .filter(safeTest(c -> customerService.isCustomerWithinRange(
                                c, args.getTargetLocation(), args.getRadius().doubleValue() * 1_000.0 /* 1km */), false))
                        .collect(Collectors.toList());

        customersWithinRange.stream()
                .sorted(Comparator.comparing(Customer::getUserId))
                .forEach(r -> log.info(r.toString()));
    }

    private void increaseLogLevel() {
        Configurator.setLevel("com.example", Level.DEBUG);
        log.debug("Verbose mode enabled");
    }
}
